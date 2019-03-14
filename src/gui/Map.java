package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import user.CardUser;

import cafe.Cafe;
import cafe.MealItem;

public class Map extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DecimalFormat twoDigits = new DecimalFormat(".##");
	private ArrayList<Cafe> cafes;
	private ArrayList<Cafe> vendingMachines;
	private CardUser user; // Users profile is public, user.profile. 
	
	// GUI
	private JLabel cscLBL;
	private JTextArea prfLBL;
	private JPanel panel1, profileView;
	private ImagePanel mapView;
	private int LBLwidth = 60, LBLheight = 25;
	
	public Map(CardUser user) {
		this.user = user;
		this.cafes = new ArrayList<Cafe>();
		this.vendingMachines = new ArrayList<Cafe>();
		
		// Initialize meals and cafes (give name, and cost for meals)
		for (int i = 0; i < 4; i++) {
			Cafe cafe = new Cafe("Cafe" + (i+1));
			Cafe vendingMachine = new Cafe("Vending Machine " + (i+1));
			
			for (int j = 0; j < 10; j++) {
				double cost = (Math.random() * 19) + 1;
				String temp = "" + twoDigits.format(cost);
				cost = Double.parseDouble(temp);
				cafe.addMeal(new MealItem("Meal" + (j+1), cost));
				
				cost = (Math.random() * 4) + 1;
				temp = "" + twoDigits.format(cost);
				cost = Double.parseDouble(temp);
				vendingMachine.addMeal(new MealItem("Snack" + (j+1), cost));
			}
			cafes.add(cafe);
			vendingMachines.add(vendingMachine);
		}
		
		// set location of cafes on the map
		cafes.get(0).setLocation(460, 150);
		cafes.get(1).setLocation(240, 400);
		cafes.get(2).setLocation(390, 290);
		cafes.get(3).setLocation(575, 400);
		
		// set location of vending machines on the map
		vendingMachines.get(0).setLocation(500, 300);
		vendingMachines.get(1).setLocation(250, 300);
		vendingMachines.get(2).setLocation(600, 250);
		vendingMachines.get(3).setLocation(420, 150);
	}
	
	public void open(Container container) {

		setSize(1000, 700);
		container.setLayout(new BorderLayout());
		
		cscLBL = new JLabel("<html> <font size = 5> Campus Smart Cafe </font></html>");
		
		panel1 = new JPanel();
		panel1.add(cscLBL);
		
		// Setup profile view
		JLabel expReportLBL = new JLabel("Expense Report:   ", SwingConstants.RIGHT);
		prfLBL = new JTextArea();
		prfLBL.setEditable(false);
		
		JButton reloadBTN = new JButton("Refresh Expense Report");
		
		// Parse and provide user with expense profile when refresh button is pressed
		reloadBTN.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    				
      			user.parse();
      			String output = user.profile.toString();
      			prfLBL.setText(output);
      		} });
		
		JTextField textInput = new JTextField(10);
		textInput.setBackground(Color.LIGHT_GRAY);
		
		JButton monthBudgetBTN = new JButton("Set monthly budget");
		monthBudgetBTN.setForeground(Color.green);
		
		JButton addFundsBTN = new JButton("Add funds directly");
		addFundsBTN.setForeground(Color.green);
		
		JLabel invalidFundsLBL = new JLabel("^^ Enter amount ^^", SwingConstants.CENTER);
		invalidFundsLBL.setForeground(Color.green);
		
		//Implement the act of adding a monthly budget
		monthBudgetBTN.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event){

				String funds = textInput.getText();
				try{
					double fundsDB = Double.parseDouble(funds);
					
					String temp = "" + twoDigits.format(fundsDB);
					fundsDB = Double.parseDouble(temp);
					
					// Make sure monthly budget is a positive number
					if (fundsDB >= 0.0) {
						user.profile.setMonthlyBudget(fundsDB);
						
						// below value is 0.0 because nothing is being purchased,
						// just letting user know that funds have been updated
						user.writeToFile(0.0); 
						
						invalidFundsLBL.setText("Monthly budget set to $" + fundsDB);
						invalidFundsLBL.setForeground(Color.green);
					} else {
						invalidFundsLBL.setText("^^ Invalid input ^^");
						invalidFundsLBL.setForeground(Color.red);
					}
					
				}
				catch(NumberFormatException e){
					invalidFundsLBL.setText("^^ Invalid input ^^");
					invalidFundsLBL.setForeground(Color.red);
				}
			}
		});
		
		//Implements the act of adding funds directly
		addFundsBTN.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event){

				String funds = textInput.getText();
				try{
					double fundsDB = Double.parseDouble(funds);
					
					String temp = "" + twoDigits.format(fundsDB);
					fundsDB = Double.parseDouble(temp);
					
					// Make sure the funds they are adding are positive
					if (fundsDB >= 0) {
						user.profile.addFunds(fundsDB);
						invalidFundsLBL.setText("Successfully added $" + fundsDB);
						
						// below value is 0.0 because nothing is being purchased,
						// just letting user know that funds have been updated
						user.writeToFile(0.0);
						invalidFundsLBL.setForeground(Color.green);
					} else {
						invalidFundsLBL.setText("^^Invalid input^^");
						invalidFundsLBL.setForeground(Color.red);
					}
					
				}
				catch(NumberFormatException e){
					invalidFundsLBL.setText("^^Invalid input^^");
					invalidFundsLBL.setForeground(Color.red);
				}
			}
		});
		
		JButton graphBTN = new JButton("Show monthly expense graph");
		graphBTN.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// Implement graph here
			}
		});
		
		// Initialize profile view and add necessary components
		profileView = new JPanel(new GridLayout(3,3));
		
		profileView.add(textInput);
		profileView.add(addFundsBTN);
		profileView.add(monthBudgetBTN);
		
		profileView.add(invalidFundsLBL);
		profileView.add(new JLabel(""));
		profileView.add(graphBTN);
		
		profileView.add(expReportLBL);
		profileView.add(prfLBL);
		profileView.add(reloadBTN);
		
		
		
		
		// MAP SETUP
		BufferedImage cafeIcon = null, vmIcon = null, image = null;
		int BTNwidth = 25, BTNheight = 25;	
		
		// Resize the button images (cafes) and the background map image for mapView
		try {
			cafeIcon = ImageIO.read(new File("cafe.png"));
			cafeIcon = resize(cafeIcon, BTNwidth, BTNheight);
			
			vmIcon = ImageIO.read(new File("vending_machine.png"));
			vmIcon = resize(vmIcon, BTNwidth, BTNheight);
			
			image = resize(ImageIO.read(new File("map.jpg")), this.getSize().width, this.getSize().height - (panel1.getSize().height+40));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		mapView = new ImagePanel(image);
		mapView.setLayout(null);
		
		// Use location set in constructor to place cafes onto the map
		for (Cafe cafe : cafes) {
			
			JLabel cafeNameLBL = new JLabel(cafe.getName());
			mapView.add(cafeNameLBL);
			cafeNameLBL.setLocation(cafe.getX(), cafe.getY()+20);
			cafeNameLBL.setSize(LBLwidth, LBLheight);
			
			// Create a button that uses an image as the icon
			JButton cafeBTN = new JButton(new ImageIcon(cafeIcon));
			mapView.add(cafeBTN);
			cafeBTN.setLocation(cafe.getX(), cafe.getY());
			cafeBTN.setSize(BTNwidth, BTNheight);
			
			// When button pressed, open the Cafe GUI
			cafeBTN.addActionListener( new ActionListener() {     
	      		public void actionPerformed(ActionEvent event){    
	      			CafeGUI cafeGUI = new CafeGUI(user, cafe);
	      			cafeGUI.openGUI(false);
	      		} });
		}
		
		// Use location set in constructor to place vending machines onto the map
				for (Cafe vendingMachine : vendingMachines) {
					
					// Create a button that uses an image as the icon
					JButton vmBTN = new JButton(new ImageIcon(vmIcon));
					mapView.add(vmBTN);
					vmBTN.setLocation(vendingMachine.getX(), vendingMachine.getY());
					vmBTN.setSize(BTNwidth, BTNheight);
					
					// When button pressed, open the Cafe GUI
					vmBTN.addActionListener( new ActionListener() {     
			      		public void actionPerformed(ActionEvent event){    	
			      			CafeGUI vmGUI = new CafeGUI(user, vendingMachine);
			      			vmGUI.openGUI(true);
			      		} });
				}
		
		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("Map", mapView);
		tabbedPanel.addTab("Profile", profileView);
		
		container.add(panel1, BorderLayout.NORTH);
		container.add(tabbedPanel, BorderLayout.CENTER);
	}
	
	// Below function is used to scale an image to a certain size
	private static BufferedImage resize(BufferedImage img, int width, int height) {
        Image temp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.drawImage(temp, 0, 0, null);
        g.dispose();
        return output;
    }

}

// This class is used to create a panel with an image as the background
class ImagePanel extends JPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private BufferedImage img;

  public ImagePanel(BufferedImage img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }

}
