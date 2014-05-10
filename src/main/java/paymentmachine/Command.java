package paymentmachine;

import java.util.Arrays;
import java.util.List;

import paymentmachine.exceptions.CommandNotFoundException;

public enum Command {
	SHOW_COMMANDS("SHOW-COMMANDS", false),
	GET_TICKET("GET-TICKET", false),
	SHOW_TICKETS("SHOW-TICKETS", false),
	SHOW_STORAGE_COINS("SHOW-STORAGE-COINS", false),
	EXIT("EXIT", false),
	START_TRANSACTION("START-TRANSACTION", true);

	Command(String text, boolean hasParameter) {
		commandText = text;
		this.hasParameter = hasParameter;
	}

	private String commandText;
	private boolean hasParameter;

	public String getCommandText() {
		return commandText;
	}

	public boolean hasParameter() {
		return hasParameter;
	}

	public static Command fromString(String text) {
		List<Command> commands = Arrays.asList(Command.values());
		Command result = null;
		for (Command command : commands) {
			if (command.commandText.toLowerCase().equals(text.toLowerCase())) {
				result = command;
				break;
			}
		}

		if (result == null) {
			throw new CommandNotFoundException();
		}

		return result;
	}
}
