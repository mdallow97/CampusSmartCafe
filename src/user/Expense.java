package user;

import java.util.Date;

public class Expense {
	private Date date;
	private double amount;
	
	public Expense(double amount, Date date) {
		this.amount = amount;
		this.date = date;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public Date getDate() {
		return this.date;
	}
}
