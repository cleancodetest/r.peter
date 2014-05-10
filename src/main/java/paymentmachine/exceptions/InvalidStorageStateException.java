package paymentmachine.exceptions;

import java.util.List;

import paymentmachine.CoinStorage;
import paymentmachine.CoinStorage.State;

public class InvalidStorageStateException extends RuntimeException {
	private static final long serialVersionUID = -3280966297664041339L;
	
	private State currentState;
	private List<State> requiredStates;
	
	@Override
	public String toString() {
		return "Invalid storage state: " + currentState
				+ ", required one of this: " + requiredStates;
	}

	public CoinStorage.State getCurrentState() {
		return currentState;
	}

	public List<CoinStorage.State> getRequiredStates() {
		return requiredStates;
	}

	public InvalidStorageStateException(State currentState, List<CoinStorage.State> requiredStates) {
		super();
		this.currentState = currentState;
		this.requiredStates = requiredStates;
	}
	
	

}
