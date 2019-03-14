package user;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class ExpenseProfile {
	private double availableFunds;
	private double monthlyBudget;
	private ArrayList<Expense> expenses;
	private CardUser user;
	
	public ExpenseProfile(CardUser user) {
		this.expenses = new ArrayList<Expense>();
		this.user = user;
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
	
	public void updateMonthlyBudget() {
		if (expenses.size() == 0 || this.monthlyBudget == 0.0) return;
		Date dateOfLastExpense = expenses.get(expenses.size()-1).getDate();
		LocalDate localDateOfLastExpense = dateOfLastExpense.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		Date current = new Date();
		LocalDate localCurrent = current.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		int pastMonth = localDateOfLastExpense.getMonthValue();
		int currentMonth = localCurrent.getMonthValue();
		
		int pastYear = localDateOfLastExpense.getYear();
		int currentYear = localCurrent.getYear();
		
		if ((pastMonth < currentMonth) || (pastYear < currentYear)) {
			int yearDif = currentYear - pastYear;
			int numUpdates = (currentMonth - pastMonth) + (12 * yearDif);
			double amountToAdd = (double) numUpdates * monthlyBudget;
			addFunds(Math.abs(amountToAdd));
			user.writeToFile(0.0); // Update with new funds
		}
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
