package gui;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import user.CardUser;
import user.ExpenseProfile;

import cafe.Cafe;

public class Map extends JFrame {
	private ArrayList<Cafe> cafes;
	private CardUser user; // Users profile is public, user.profile. 
	
	// GUI
	private JLabel cscLBL;
	private JPanel panel1, mapView, profileView;
	
	public Map(CardUser user) {
		this.user = user;
	}
	
	public void load() {
		
	}
	
	public void open(Container container) {
		
		// current size is 1000(X), 700(Y)
		container.setLayout(new BorderLayout());
		
		cscLBL = new JLabel("<html> <font size = 5> Campus Smart Cafe </font></html>");
		
		panel1 = new JPanel();
		panel1.add(cscLBL);
		
		// Setup profile view
		JLabel expReportLBL = new JLabel("Expense Report:");
		JButton reloadBTN = new JButton("Refresh");
		reloadBTN.addActionListener( new ActionListener() {     
      		public void actionPerformed(ActionEvent event){    				
      			user.parse();
      			String output = user.profile.toString();
      		} });
		
		
		profileView = new JPanel();
		profileView.add(expReportLBL);
		profileView.add(reloadBTN);
		
		// Setup map view
		mapView = new JPanel();
		
		
		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("Map", mapView);
		tabbedPanel.addTab("Profile", profileView);
		
		container.add(panel1, BorderLayout.NORTH);
		container.add(tabbedPanel, BorderLayout.CENTER);
	}

}
