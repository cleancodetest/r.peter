package paymentmachine;

public class App {
	public static void main(String[] args) {
		PaymentMachine pm = new PaymentMachine(System.in, System.out);
		pm.start();
	}
}