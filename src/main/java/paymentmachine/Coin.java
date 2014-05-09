package paymentmachine;

import java.util.Arrays;
import java.util.List;

import paymentmachine.exceptions.InvalidCoinTypeException;

public enum Coin {
	C5(5), C10(10), C20(20), C50(50), C100(100), C200(200), C500(500), C1000(
			1000), C2000(2000), C5000(5000), C10000(10000), C20000(20000);

	private Integer value;

	Coin(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Coin fromInteger(Integer value) {
		try {
			Coin coin = Coin.valueOf(Coin.class, "C" + value.toString());
			return coin;
		} catch (IllegalArgumentException ex) {
			throw new InvalidCoinTypeException(ex);
		}
	}
	
	public static List<Coin> getReversedCoinList(){
		List<Coin> result = Arrays.asList(Coin
				.values());
		result.sort((Coin c1, Coin c2) -> c2
				.getValue() - c1.getValue());
		
		return result;
	}
}
