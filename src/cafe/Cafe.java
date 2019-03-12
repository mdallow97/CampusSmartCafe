package cafe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import user.CardUser;
import user.Expense;

public class Cafe {
	private static DecimalFormat twoDigits = new DecimalFormat(".##");
	
	private String name = "";
	private double total = 0.0;
	private ArrayList<MealItem> meals;
	private int locationX, locationY;
	
	private String cartString = "";
	
	public Cafe(String name) {
		this.name = name;
		meals = new ArrayList<MealItem>();
	}
	
	public void addMeal(MealItem meal) {
		meals.add(meal);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void removeMeal(MealItem meal) {
		meals.remove(meal);
	}
	
	public String getName() {
		return name;
	}
	
	public void setLocation(int x, int y) {
		this.locationX = x;
		this.locationY = y;
	}
	
	public int getX() {
		return this.locationX;
	}
	
	public int getY() {
		return this.locationY;
	}
	
	public void openCafeGUI(CardUser user) {
		ArrayList<MealItem> cart = new ArrayList<MealItem>();
		
		JFrame cafeWindow = new JFrame(this.name);
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
		for (MealItem meal : meals) {
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
		JButton checkout = new JButton("Checkout");
		checkout.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    				
      			for (MealItem meal : cart) {
      				Expense newExp = new Expense(meal.getCost(), new Date());
      				
      				// Make sure user can afford expense
      				if (!user.profile.addExpense(newExp)) {
      					// Display error message
      					errorLBL.setVisible(true);
      				} else {
      					// Write to file
      					errorLBL.setVisible(false);
      					System.out.println("here");
      					user.writeToFile(meal.getCost());
      					
      					cartString = "";
      				}
      			}
      		} });
		
		panel2.add(checkout);
		panel2.add(totalLBL);
		panel2.add(errorLBL);
		
		
		cafeWindow.add(panel1, BorderLayout.CENTER);
		cafeWindow.add(panel2, BorderLayout.SOUTH);
 	}
}
