package paymentmachine.exceptions;

public class NotEnoughCoinException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8753468548342567065L;

	public NotEnoughCoinException() {
		// TODO Auto-generated constructor stub
	}

	public NotEnoughCoinException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughCoinException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughCoinException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughCoinException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
