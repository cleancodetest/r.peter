package paymentmachine;

import java.util.Random;

public class Ticket {
	private static Integer idCounter = 0;
	private static Random random = new Random(); 
	
	private Integer id;
	private Integer price;
	
	public Integer getId() {
		return id;
	}

	public Integer getPrice() {
		return price;
	}

	public Ticket(){
		id = ++idCounter;
		Integer price = random.nextInt(500) + 500;
		this.price = price - price % 5;
	}
	
	@Override
	public String toString() {
		return "Ticket id:" + id + " - Price: " + price;
	}
}
