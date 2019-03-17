package user;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExpenseProfile {
	private double availableFunds;
	private double monthlyBudget;
	private ArrayList<Expense> expenses;
	
	public ExpenseProfile() {
		this.expenses = new ArrayList<Expense>();
	}
	
	public void setAvailableFunds(double funds) {
		if (funds >= 0.0) this.availableFunds = funds;
		else System.err.println("Available funds cannot be set to 0");
	}
	
	public void addFunds(double funds) {
		if (funds >= 0.0) this.availableFunds += funds;
		else System.err.println("Available funds cannot be set to 0");
	}
	
	public double getAvailableFunds() {
		return Double.parseDouble((new DecimalFormat(".##")).format(this.availableFunds));
	}
	
	public boolean addExpense(Expense expense) {
		if (this.availableFunds - expense.getAmount() < 0.0) {
			return false;
		} else {
			this.availableFunds -= expense.getAmount();
			expenses.add(expense);
			return true;
		}
	}
	
	public ArrayList<Expense> getExpenses() {
		return this.expenses;
	}

	public double getMonthlyBudget() {
		return monthlyBudget;
	}
	
	public boolean setMonthlyBudget(double monthlyBudget) {
		if (monthlyBudget >= 0.0) {
			this.monthlyBudget = monthlyBudget;
			return true;
		}
		else return false;
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
