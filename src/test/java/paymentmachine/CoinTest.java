package paymentmachine;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import paymentmachine.exceptions.InvalidCoinTypeException;

public class CoinTest {
	@Test
	public void testCoinFromInteger100() {
		Coin c = Coin.fromInteger(100);

		assertEquals(c, Coin.C100);
	}

	@Test(expectedExceptions = InvalidCoinTypeException.class)
	public void testCoinFromInteger1() {
		Coin c = Coin.fromInteger(1);

		assertEquals(c, Coin.C100);
	}
	
	@Test
	public void testGetReversedCoinList(){
		List<Coin> coins = Coin.getReversedCoinList();
		
		assertEquals(coins.get(0), Coin.C20000, "The first coin must be 20000.");
		assertEquals(coins.get(coins.size()-1), Coin.C5, "The last coin must be 5.");
	}
}
