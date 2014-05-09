package paymentmachine.exceptions;

public class InvalidCoinType extends RuntimeException {

	private static final long serialVersionUID = -6931386731928636600L;

	public InvalidCoinType() {
		// TODO Auto-generated constructor stub
	}

	public InvalidCoinType(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidCoinType(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidCoinType(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidCoinType(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
