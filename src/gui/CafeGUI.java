package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cafe.Cafe;
import cafe.MealItem;
import user.CardUser;
import user.Expense;

public class CafeGUI {
	private CardUser user;
	private Cafe cafe;
	
	private String cartString = "";
	private double total = 0.0;
	private static DecimalFormat twoDigits = new DecimalFormat(".##");
	
	public CafeGUI(CardUser user, Cafe cafe) {
		this.user = user;
		this.cafe = cafe;
	}
	
	public void openGUI(boolean isVendingMachine) {
		ArrayList<MealItem> cart = new ArrayList<MealItem>();
		
		JFrame cafeWindow = new JFrame(this.cafe.getName());
		cafeWindow.setSize(800, 600);
		cafeWindow.setVisible(true);
		cafeWindow.setLocationRelativeTo(null);
		
		Container container = cafeWindow.getContentPane();
		container.setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0,2));
		
		JLabel cartLBL = new JLabel();
		JLabel totalLBL = new JLabel("Total: $0.00");
		
		cartString = "";
		total = 0.0;
		
		// Create a button for all meals available at the cafe, and set their actions
		for (MealItem meal : this.cafe.getMeals()) {
			String buttonName = meal.getName() + "\t\t $" + twoDigits.format(meal.getCost());
			JButton mealBTN = new JButton(buttonName);
			
			// When button is pressed, meal is added to the cart, and the corresponding labels are updated
			mealBTN.addActionListener( new ActionListener() {     
	      		public void actionPerformed(ActionEvent event){    				
	      			cart.add(meal);
	      			if (cartString.equals("")) cartString += meal.getName();
	      			else cartString += ", " + meal.getName();
	      			
	      			cartLBL.setText(cartString);
	      			total += meal.getCost();
	      			total = Double.parseDouble(twoDigits.format(total));
	      			String totalStr = "Total: $" + total;
	      			totalLBL.setText(totalStr);
	      		} });
			
			panel1.add(mealBTN);
		}
		
		
		JPanel panel2 = new JPanel();
		panel2.add(cartLBL);
		
		JLabel errorLBL = new JLabel("Insufficient funds, add money in profile section");
		errorLBL.setForeground(Color.red);
		errorLBL.setVisible(false);
		
		
		// Upon checkout, each items cost is individually written to the file, 
		// and cost subtracted from their available funds
		JButton pickUpBTN = new JButton("Checkout (Pick-Up)");
		pickUpBTN.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    		
      			
      			if (total > user.profile.getAvailableFunds()) {
      				errorLBL.setVisible(true);
      				return;
      			} else errorLBL.setVisible(false);
      			
      			for (MealItem meal : cart) {
      				Expense newExp = new Expense(meal.getCost(), new Date());
      				if (!user.profile.addExpense(newExp)) System.err.println("Total exceeds available funds");
      				user.writeToFile(meal.getCost());
      			
      			}
      			
      			provideTimeAndPlace(cartString, "pick-up", false);
      			cafeWindow.dispatchEvent(new WindowEvent(cafeWindow, WindowEvent.WINDOW_CLOSING));
      		} });
		
		
		JButton eatInBTN = new JButton("Checkout (Eat-In)");
		
		if (isVendingMachine) {
			pickUpBTN.setVisible(false);
			eatInBTN.setText("Checkout");
		}
		
		eatInBTN.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    		
      			
      			if (total > user.profile.getAvailableFunds()) {
      				errorLBL.setVisible(true);
      				return;
      			} else errorLBL.setVisible(false);
      			
      			for (MealItem meal : cart) {
      				Expense newExp = new Expense(meal.getCost(), new Date());
      				if (!user.profile.addExpense(newExp)) System.err.println("Total exceeds available funds");
      				user.writeToFile(meal.getCost());
      			
      			}
      			provideTimeAndPlace(cartString, "eating-in", isVendingMachine);
      			cafeWindow.dispatchEvent(new WindowEvent(cafeWindow, WindowEvent.WINDOW_CLOSING));
      		} });
		
		panel2.add(pickUpBTN);
		panel2.add(eatInBTN);
		panel2.add(totalLBL);
		panel2.add(errorLBL);
		
		
		cafeWindow.add(panel1, BorderLayout.CENTER);
		cafeWindow.add(panel2, BorderLayout.SOUTH);
	}
	
	private void provideTimeAndPlace(String cartString, String option, boolean isVendingMachine) {
		
		JFrame messageWindow = new JFrame(this.cafe.getName());
		messageWindow.setSize(700, 100);
		messageWindow.setVisible(true);
		messageWindow.setLocationRelativeTo(null);
		
		Container container = messageWindow.getContentPane();
		container.setLayout(new BorderLayout());
		
		int time = (int) (Math.random() * 19) + 1;
		String output = "Meal items: " + cartString;
		output += ", will be available for " + option + " in approximately " + time + " minute(s)";
		
		if (isVendingMachine) {
			output = "Snacks: " + cartString;
			output += ", are being dispensed";
		}
		
		JLabel messageLBL = new JLabel(output);
		
		JButton okBTN = new JButton("OK");
		okBTN.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    		
      			messageWindow.dispatchEvent(new WindowEvent(messageWindow, WindowEvent.WINDOW_CLOSING));
      		} });
		
		container.add(messageLBL, BorderLayout.NORTH);
		container.add(okBTN, BorderLayout.SOUTH);
	}
}
