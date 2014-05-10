package paymentmachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import paymentmachine.exceptions.TicketNotFoundException;

public class TicketManager {
	public final static TicketManager instance = new TicketManager();

	private TicketManager() {
	}
	
	private Map<Integer, Ticket> tickets = new HashMap<>();

	public Ticket getNewTicket() {
		Ticket t = new Ticket();
		tickets.put(t.getId(),t);
		return t;
	}
	
	public Ticket findTicket(Integer ticketId){
		ensureTicketExistanceById(ticketId);
		
		return tickets.get(ticketId);
	}
	
	public void removeTicket(Ticket ticket){
		ensureTicketExistanceByTicket(ticket);
		
		tickets.remove(ticket.getId());
	}
	
	public Map<Integer, Ticket> getTickets(){
		return Collections.unmodifiableMap(tickets);
	}
	
	private void ensureTicketExistanceById(Integer ticketId){
		if(!tickets.containsKey(ticketId)){
			throw new TicketNotFoundException(ticketId.toString());
		}
	}
	
	private void ensureTicketExistanceByTicket(Ticket ticket){
		ensureTicketExistanceById(ticket.getId());
	}
}
