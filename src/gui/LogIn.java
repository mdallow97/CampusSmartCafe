package gui;

import user.CardUser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LogIn extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container container;
	JPanel panel1, panel2, panel3;
	JTextField userIdTF;
	JLabel addIdLBL, invalidIdLBL;
	JButton logInBTN;
	
	
	public LogIn() {
		super("Campus Smart Cafe");
		setSize(500, 200);
		
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		addIdLBL = new JLabel("Enter Card Number: ");
		userIdTF = new JTextField(15);
		
		invalidIdLBL = new JLabel("Invalid Card Number: Must be 9 digits and all numbers");
		invalidIdLBL.setVisible(false);
		invalidIdLBL.setForeground(Color.red);
		
		
		logInBTN = new JButton("Log In");
		logInBTN.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    				
      			logIn();
      		} });
		
		panel1 = new JPanel();
		panel1.add(addIdLBL);
		panel1.add(userIdTF);
		
		panel2 = new JPanel();
		panel2.add(invalidIdLBL);
		
		panel3 = new JPanel();
		panel3.add(logInBTN);
		
		container.add(panel1, BorderLayout.NORTH);
		container.add(panel2, BorderLayout.CENTER);
		container.add(panel3, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void logIn() {
		String idStr = userIdTF.getText().replaceAll("\\s+","");
		
		// Card number must be 9 digits
		
		if (idStr.length() != 9) {
			invalidIdLBL.setVisible(true);
			return;
		} else invalidIdLBL.setVisible(false);
		
		int id = -1;
		
		try {
			id = Integer.parseInt(userIdTF.getText());
		} catch (Exception e) {
			invalidIdLBL.setVisible(true);
			return;
		}
		
		if (id < 0) {
			invalidIdLBL.setVisible(true);
			return;
		} else invalidIdLBL.setVisible(false);
		
		
		
		// Card user creation, and parse the database
		CardUser user = new CardUser(idStr);
		user.parse();
		user.profile.updateMonthlyBudget();
		
		Map map = new Map(user);
		
		setSize(1000, 700);
		setLocationRelativeTo(null);
		container.remove(panel1);
		container.remove(panel2);
		container.remove(panel3);
		
		map.open(container);
	}
}
