package paymentmachine;

import org.testng.annotations.Test;

import paymentmachine.exceptions.InvalidCoinType;
import static org.testng.Assert.*;

public class CoinTest {
	@Test
	public void testCoinFromInteger100() {
		Coin c = Coin.fromInteger(100);

		assertEquals(c, Coin.C100);
	}

	@Test(expectedExceptions = InvalidCoinType.class)
	public void testCoinFromInteger1() {
		Coin c = Coin.fromInteger(1);

		assertEquals(c, Coin.C100);
	}
}
