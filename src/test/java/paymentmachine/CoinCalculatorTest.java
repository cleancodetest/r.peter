package paymentmachine;

import org.testng.annotations.Test;

import paymentmachine.CoinStorage.CoinCalculator;
import paymentmachine.exceptions.InvalidStorageStateException;

public class CoinCalculatorTest {
	@Test(expectedExceptions = InvalidStorageStateException.class)
	public void testgetCurrentTransactionValueWhenNotInTransaction() {
		CoinStorage cs = new CoinStorage();
		CoinCalculator cc = cs.getCoinCalculator();

		cc.getCurrentTransactionValue();
	}
}
