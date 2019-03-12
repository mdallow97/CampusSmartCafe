package user;

import java.util.ArrayList;

public class ExpenseProfile {
	private double availableFunds;
	private double monthlyBudget;
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
	
	public boolean addExpense(Expense expense) {
		if (this.availableFunds - expense.getAmount() < 0.0) {
			return false;
		} else {
			this.availableFunds -= expense.getAmount();
			expenses.add(expense);
			return true;
		}
	}
	
	public double sumExpenses() {
		double sum = 0.0;
		for (Expense exp : expenses) {
			sum += exp.getAmount();
		}
		
		return sum;
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
