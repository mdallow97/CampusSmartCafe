package user;

import java.util.ArrayList;

public class ExpenseProfile {
	private double availableFunds;
	private ArrayList<Expense> expenses;
	
	public ExpenseProfile() {
		this.expenses = new ArrayList<Expense>();
	}
	
	public void setAvailableFunds(double funds) {
		this.availableFunds = funds;
	}
	
	public void addFunds(double funds) {
		this.availableFunds += funds;
	}
	
	public double getAvailableFunds() {
		return this.availableFunds;
	}
	
	public void addExpense(Expense expense) {
		expenses.add(expense);
	}
	
	public double sumExpenses() {
		double sum = 0.0;
		for (Expense exp : expenses) {
			sum += exp.getAmount();
		}
		
		return sum;
	}
	
	public String toString() {
		String output = "All past expenses (date|amount):\n";
		for (Expense exp : expenses) {
			output += exp.getDate() + "|" + exp.getAmount() + "\n";
		}
		
		output += "Remaining funds: " + this.availableFunds;
		return output;
	}
}
