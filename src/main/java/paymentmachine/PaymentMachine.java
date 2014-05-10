package paymentmachine;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import paymentmachine.exceptions.CommandNotFoundException;
import paymentmachine.exceptions.TicketNotFoundException;

public class PaymentMachine {

	private final InputStream input;
	private final PrintStream output;

	private final Scanner scanner;

	private final CoinStorage coinStorage;
	private final TicketManager ticketManager = TicketManager.instance;

	public PaymentMachine(InputStream input, PrintStream output) {
		this.input = input;
		this.output = output;
		this.scanner = new Scanner(input);
		scanner.useDelimiter("\r");
		this.coinStorage = new CoinStorage();
	}

	public void start() {
		output.println("WELCOME AT THE TICKET MACHINE");
		while (true) {
			String command = scanner.next();
			handleCommand(command);
		}

	}

	private void handleCommand(String command) {
		String[] commandParts = command.trim().split("%s");
		Command actualCommand = null;
		Integer parameter = null;
		try {
			actualCommand = Command.fromString(commandParts[0]);
			if (commandParts.length == 2) {
				parameter = Integer.parseInt(commandParts[1]);
			}

			if (actualCommand.hasParameter() && parameter == null) {
				output.println("The command " + actualCommand.getCommandText()
						+ " required a number parameter");
			}

			handleActualCommand(actualCommand, parameter);
		} catch (CommandNotFoundException cnf) {
			output.println("No such command");
		} catch (NumberFormatException e) {
			output.println("Can't convert " + commandParts[1] + " to number");
		}
	}

	private void handleActualCommand(Command command, Integer parameter) {
		switch (command) {
		case EXIT:
			System.exit(0);
			break;
		case SHOW_COMMANDS:
			showCommands();
			break;
		case SHOW_STORAGE_COINS:
			showStorageCoins();
			break;
		case SHOW_TICKETS:
			showTickets();
			break;
		case GET_TICKET:
			getTicket();
		case START_TRANSACTION:
			startTransaction(parameter);
			break;
		}
	}

	private void startTransaction(Integer ticketId) {
		Ticket ticket = ticketManager.findTicket(ticketId);
		try {
			ticketManager.findTicket(ticketId);
			coinStorage.startTransaction(ticket.getPrice());
			
		} catch (TicketNotFoundException e) {
			output.println("There is no such ticket.");
		}

	}

	private void getTicket() {
		Ticket newTicket = ticketManager.getNewTicket();
		output.println("Ticket generated:");
		output.println(newTicket);
	}

	private void showTickets() {
		Map<Integer, Ticket> tickets = ticketManager.getTickets();
		if (tickets.size() > 0) {
			for (Integer ticketId : tickets.keySet()) {
				output.println(tickets.get(ticketId));
			}
		} else {
			output.println("There are no tickets in the system.");
		}
	}

	private void showCommands() {
		List<Command> commands = Arrays.asList(Command.values());
		for (Command command : commands) {
			output.println(command.getCommandText() + " hasparameter="
					+ command.hasParameter());
		}
	}

	private void showStorageCoins() {
		Map<Coin, Integer> coins = coinStorage.getCurrentCoinsInStorage();
		for (Coin coin : coins.keySet()) {
			if (coins.get(coin) > 0) {
				output.println("We have " + coins.get(coin) + " from the coin "
						+ coin.getValue());
			}
		}
	}
}
