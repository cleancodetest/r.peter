package paymentmachine;

import static org.testng.Assert.*;

import java.util.Map;

import org.testng.annotations.Test;

import paymentmachine.CoinStorage.State;
import paymentmachine.exceptions.InvalidStorageStateException;
import paymentmachine.exceptions.NotEnoughCoinInsertedException;

public class CoinStorageTest {

	@Test
	public void testStartTransaction() {
		CoinStorage cs = new CoinStorage();

		cs.startTransaction(1000);

		assertEquals(cs.getStorageState(), State.IN_TRANSACTION,
				"Storage must be in transaction");
	}

	@Test(expectedExceptions = InvalidStorageStateException.class)
	public void testStartTransactionWhenIntransaction() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);
		cs.startTransaction(1000);
	}

	@Test(expectedExceptions = InvalidStorageStateException.class)
	public void testInsertCoinWhenNotInTransaction() {
		CoinStorage cs = new CoinStorage();

		cs.insertCoin(Coin.C10);
	}

	@Test
	public void testInsertCoinWithC100() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);

		cs.insertCoin(Coin.C100);

		assertEquals(cs.getCurrentTransactionCoins().size(), 1,
				"Inserted Coin type must be 1");
	}

	@Test
	public void testInsertCoinWithTwoC100() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);

		cs.insertCoin(Coin.C100);
		cs.insertCoin(Coin.C100);

		assertEquals(cs.getCurrentTransactionCoins().size(), 1,
				"Inserted Coin type must be 1");
	}

	@Test(expectedExceptions = InvalidStorageStateException.class)
	public void testCancelTransactionWhenNotInTransaction() {
		CoinStorage cs = new CoinStorage();

		cs.cancelTransaction();
	}

	@Test
	public void testCancelTransactionWhenNoCoinInsertedCheckState() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);

		Map<Coin, Integer> transactionCoins = cs.cancelTransaction();

		assertEquals(cs.getStorageState(), State.WAITING,
				"The expected status of coinstorage is waiting");
	}

	@Test
	public void testCancelTransactionWhenNoCoinInsertedCheckReturnedValueIsNotNull() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);

		Map<Coin, Integer> transactionCoins = cs.cancelTransaction();

		assertNotEquals(transactionCoins, null,
				"The returned transaction coins can't be null");
	}

	@Test
	public void testCancelTransactionWhenNoCoinInsertedCheckReturnedMapSize() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);

		Map<Coin, Integer> transactionCoins = cs.cancelTransaction();

		assertEquals(transactionCoins.size(), 0,
				"The returned transaction coins must be 0.");
	}

	@Test
	public void testCancelTransactionWhenCoinInserted() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(1000);
		cs.insertCoin(Coin.C100);

		Map<Coin, Integer> transactionCoins = cs.cancelTransaction();

		assertEquals(transactionCoins.size(), 1,
				"The number of coin types must be 1.");
	}
	
	@Test(expectedExceptions = InvalidStorageStateException.class)
	public void testFinishTransactionWhenNotInTransaction() {
		CoinStorage cs = new CoinStorage();

		cs.finishTransaction();
	}

	@Test(expectedExceptions = NotEnoughCoinInsertedException.class)
	public void testFinishTransactionWhenNoCoinInserted() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(500);

		cs.finishTransaction();
	}

	@Test(expectedExceptions = NotEnoughCoinInsertedException.class)
	public void testFinishTransactionWhenLessCoinInserted() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(500);

		cs.insertCoin(Coin.C5);

		cs.finishTransaction();
	}

	@Test
	public void testFinishTransactionWhenExactCoinInsertedWithOneCoin() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(500);

		cs.insertCoin(Coin.C500);

		Map<Coin, Integer> handBack = cs.finishTransaction();

		assertEquals(handBack.size(), 0, "There can't be any hand back");
	}
	
	@Test
	public void testFinishTransactionWhenExactCoinInsertedWithMoreThanOneCoin() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(500);

		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C100);

		Map<Coin, Integer> handBack = cs.finishTransaction();

		assertEquals(handBack.size(), 0, "There can't be any hand back");
	}
	
	@Test
	public void testFinishTransactionWhenExactCoinInsertedWithMoreThanOneCoinCheckStorageCoins() {
		CoinStorage cs = new CoinStorage();
		int C200StartAmount = cs.getCurrentCoinsInStorage().get(Coin.C200);
		int C100StartAmount = cs.getCurrentCoinsInStorage().get(Coin.C100);
		cs.startTransaction(500);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C100);

		cs.finishTransaction();
		int C200CurrentAmount = cs.getCurrentCoinsInStorage().get(Coin.C200);
		int C100CurrentAmount = cs.getCurrentCoinsInStorage().get(Coin.C100);

		assertEquals(C200CurrentAmount,C200StartAmount + 2);
		assertEquals(C100CurrentAmount,C100StartAmount + 1);
	}
	
	@Test
	public void testFinishTransactionWhenMoreCoinInserted() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(500);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);

		Map<Coin, Integer> handBack = cs.finishTransaction();

		assertEquals(handBack.size(), 1, "There must on type of coin as hand back");
	}
	
	@Test
	public void testFinishTransactionWhenMoreCoinInsertedAndRequiredHandBackCoinIsMissing() {
		CoinStorage cs = new CoinStorage();
		cs.startTransaction(500);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		Map<Coin, Integer> handBack = cs.finishTransaction();
		
		cs.startTransaction(500);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		handBack = cs.finishTransaction();
		
		cs.startTransaction(500);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		cs.insertCoin(Coin.C200);
		handBack = cs.finishTransaction();

		assertEquals(handBack.size(), 1, "There must on type of coin as hand back");
		assertEquals((int)handBack.get(Coin.C50), 2, "There has to be two 50 coin value");
	}
}
