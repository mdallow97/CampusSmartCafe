package user;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CardUser {
	private String userId;
	public ExpenseProfile profile;
	
	public CardUser(String userId) {
		this.userId = userId;
		this.profile = new ExpenseProfile();
	}
	
	public void parse() {

		// Parse the log and store information
		// Every line should follow format: userId:date;transactionCost:availableFunds
													 //^ notice semi-colon
		this.profile = new ExpenseProfile();
		
		Scanner scanner;
		try {
			(new File("log.txt")).createNewFile(); // creates a new file if it does not exist
			scanner = new Scanner(new File("log.txt"));
			
			String line = "";
			int lineCount = 0;
			
			while (scanner.hasNextLine()) {
				lineCount++;
				line = scanner.nextLine();
				
				if (line.charAt(9) != ':') {
					System.err.println("Incorrect Card Number format on file line " + lineCount);
					continue;
				}
				
				String fileId = line.substring(0, 9);
				if (!fileId.equals(this.userId)) continue;
				
				
				
				if (line.charAt(29) != ';') {
					System.err.println("Incorrect date format on file line " + lineCount);
					continue;
				}

				String dateStr = line.substring(10, 29);
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
				
				int index = line.lastIndexOf(':');
				if (index < 30) {
					System.err.println("Incorrect transaction cost format on file line " + lineCount);
					continue;
				}
				
				String transCostStr = line.substring(30, index);
				double cost = Double.parseDouble(transCostStr);
				
				int semiIndex = line.lastIndexOf(';');
				
				String fundsStr = line.substring(index+1, semiIndex);
				double availableFunds = Double.parseDouble(fundsStr);
				
				if (cost < 0.0 || availableFunds < 0.0) {
					System.err.println("Invalid negative transaction cost or funds on file line " + lineCount);
					continue;
				} else {
					Expense expense = new Expense(cost, date);
					profile.addExpense(expense);
					profile.setAvailableFunds(availableFunds);
				}
				
				String monthlyBudgetStr = line.substring(semiIndex+1);
				double monthlyBudget = Double.parseDouble(monthlyBudgetStr);
				
				if (monthlyBudget < 0.0 ) {
					System.err.println("Invalid negative budget on file line " + lineCount);
					continue;
				} else {
					profile.setMonthlyBudget(monthlyBudget);
				}
				// At this point, all valid expenses and the current available funds are stored in the expense profile
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeToFile(double transactionCost) {
		// Every line should follow format: userId:date;transactionCost:availableFunds
		 											 //^ notice semi-colon
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String output = this.userId + ":" + format.format(new Date()) + ";";
		output += (new DecimalFormat(".##")).format(transactionCost) + ":";
		output += profile.getAvailableFunds() + ";" + profile.getMonthlyBudget() + "\n";
		
		try {
			FileWriter writer = new FileWriter(new File("log.txt"), true);
			writer.write(output);
			writer.close();
		} catch (Exception e) {
			System.err.println("Unable to write to file");
		}
	}
	
	public void updateMonthlyBudget() {
		ArrayList<Expense> expenses = profile.getExpenses();
		
		if (expenses.size() == 0 || profile.getMonthlyBudget() == 0.0) return;
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
			double amountToAdd = (double) numUpdates * profile.getMonthlyBudget();
			profile.addFunds(Math.abs(amountToAdd));
			writeToFile(0.0); // Update with new funds
		}
	}
	
	public String getUserID() {
		return this.userId;
	}
}
