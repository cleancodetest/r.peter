package paymentmachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paymentmachine.exceptions.*;

public class CoinStorage {
	public static enum State {
		WAITING, IN_TRANSACTION
	}

	public class CoinCalculator {
		public Integer getCurrentTransactionValue() {
			ensureStorageState(State.IN_TRANSACTION);

			Integer totalValue = 0;
			for (Coin currentCoin : transactionCoins.keySet()) {
				totalValue += transactionCoins.get(currentCoin)
						* currentCoin.getValue();
			}

			return totalValue;
		}

		public Boolean isEnoughCoin() {
			ensureStorageState(State.IN_TRANSACTION);

			return getCurrentTransactionValue() >= transactionRequiredAmount;
		}

		public Map<Coin, Integer> getHandBackCoins() {
			ensureStorageState(State.IN_TRANSACTION);

			if (isEnoughCoin()) {
				List<Coin> reversedCoinListForGreedyMatch = Coin
						.getReversedCoinList();

				Integer currentRemainingAmount = getMissingAmount();
				Integer requiredCoinNumber;
				Integer actualUsableCoinNumber;
				Map<Coin, Integer> result = new HashMap<>();
				for (Coin coin : reversedCoinListForGreedyMatch) {
					requiredCoinNumber = currentRemainingAmount / coin.getValue();
					actualUsableCoinNumber = Integer.min(requiredCoinNumber,
							getCoinAmount(coin));
					if (actualUsableCoinNumber > 0) {
						result.put(coin, actualUsableCoinNumber);
						currentRemainingAmount -= coin.getValue()
								* actualUsableCoinNumber;
					}
				}
				if (currentRemainingAmount > 0) {
					throw new NotEnoughCoinException();
				}

				return result;
			} else {
				throw new NotEnoughCoinInsertedException();
			}
		}

		public Integer getMissingAmount() {
			return getCurrentTransactionValue() - transactionRequiredAmount;
		}

		private Integer getCoinAmount(Coin coin) {
			Integer amount = coinStore.get(coin);
			if (storageState == State.IN_TRANSACTION
					&& transactionCoins.containsKey(coin)) {
				amount += transactionCoins.get(coin);
			}
			return amount;
		}
	}

	private Map<Coin, Integer> coinStore = new HashMap<>();
	private State storageState;
	private CoinCalculator coinCalculator;

	private Map<Coin, Integer> transactionCoins;
	private Integer transactionRequiredAmount;

	public CoinStorage() {
		initializeCoinStorage();
	}

	private void initializeCoinStorage() {
		Coin[] coins = Coin.values();
		for (int i = 0; i < coins.length; i++) {
			coinStore.put(coins[i], 2);
		}

		storageState = State.WAITING;
		coinCalculator = new CoinCalculator();
	}

	public void startTransaction(Integer requiredAmount) {
		ensureStorageState(State.WAITING);

		storageState = State.IN_TRANSACTION;
		transactionCoins = new HashMap<>();
		transactionRequiredAmount = requiredAmount;
	}

	public void insertCoin(Coin c) {
		ensureStorageState(State.IN_TRANSACTION);

		if (transactionCoins.keySet().contains(c)) {
			transactionCoins.put(c, transactionCoins.get(c) + 1);
		} else {
			transactionCoins.put(c, 1);
		}
	}

	public Map<Coin, Integer> finishTransaction() {
		ensureStorageState(State.IN_TRANSACTION);

		Map<Coin, Integer> handBack = coinCalculator.getHandBackCoins();

		for (Coin coin : transactionCoins.keySet()) {
			coinStore.put(coin,
					coinStore.get(coin) + transactionCoins.get(coin));
		}

		for (Coin coin : handBack.keySet()) {
			coinStore.put(coin, coinStore.get(coin) - handBack.get(coin));
		}

		storageState = State.WAITING;
		transactionCoins = null;
		transactionRequiredAmount = 0;

		return handBack;
	}

	public Map<Coin, Integer> cancelTransaction() {
		ensureStorageState(State.IN_TRANSACTION);

		storageState = State.WAITING;
		Map<Coin, Integer> ret = transactionCoins;
		transactionCoins = null;
		return ret;
	}

	public Map<Coin, Integer> getCurrentTransactionCoins() {
		ensureStorageState(State.IN_TRANSACTION);

		return Collections.unmodifiableMap(transactionCoins);
	}

	public Map<Coin, Integer> getCurrentCoinsInStorage() {
		return Collections.unmodifiableMap(coinStore);
	}

	public State getStorageState() {
		return storageState;
	}

	public CoinCalculator getCoinCalculator() {
		return coinCalculator;
	}

	private void ensureStorageState(State... requiredState) {
		if (requiredState == null || requiredState.length == 0) {
			throw new IllegalArgumentException("requiredState");
		}

		List<State> requiredStateList = Arrays.asList(requiredState);
		if (!requiredStateList.contains(storageState)) {
			throw new InvalidStorageStateException(storageState,
					requiredStateList);
		}
	}
}
