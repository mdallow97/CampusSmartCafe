package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import user.CardUser;
import user.Expense;

public class GraphGUI {
	private CardUser user;
	private Map<String, ArrayList<Expense>> map;
	
	public GraphGUI(CardUser user) {
		this.user = user;
//		user.parse();
		this.map = new HashMap<String, ArrayList<Expense>>();
		
		for (Expense expense : this.user.profile.getExpenses()) {
			LocalDate localDate = expense.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			MonthYear monthYear = new MonthYear(localDate.getMonthValue(), localDate.getYear());
			
			String monthYear = "" + localDate.getMonthValue() + ":" + localDate.getYear();
			
			if (map.containsKey(monthYear)) {
				map.get(monthYear).add(expense);
				
			} else {
				ArrayList<Expense> monthlyExpense = new ArrayList<Expense>();
				monthlyExpense.add(expense);
				map.put(monthYear, monthlyExpense);
			}
		}
		
	}
	
	public void openGUI() {
		JFrame graphWindow = new JFrame(user.getUserID() + "'s Expense Profile");
		
		String [] dates = new String[6];
		double [] totals = new double[6];
		
		LocalDate currentDate = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		
		for (int i = 0; i < 6; i++) {

			
			
			String key = "" + month + ":" + year;
			double total = 0.0;
			
			if (map.containsKey(key)) total = getMonthlyTotal(key);
			
			
			dates[5-i] = "" + month + "/" + year;
			totals[5-i] = total;
			
			
			if (month > 1) month--;
			else {
				month = 12;
				year--;
			}
			
			System.out.println("Date: " + dates[5-i] + " Total: " + total);
		}
		
		Map<Color, MonthlyExpense> graphInput = new LinkedHashMap<Color, MonthlyExpense>();
		Random randomGenerator = new Random();
		
		for (int i = 0; i < 6; i++) {
			MonthlyExpense monthlyExpense = new MonthlyExpense(dates[i], totals[i]);
			
			int red = randomGenerator.nextInt(256);
            int green = randomGenerator.nextInt(256);
            int blue = randomGenerator.nextInt(256);

            Color randomColor = new Color(red, green, blue);
            graphInput.put(randomColor, monthlyExpense);
		}
		
		BarChart chart = new BarChart(graphInput);
		chart.setSize(700, 500);
		graphWindow.setSize(800, 600);
		graphWindow.getContentPane().add(chart);
		graphWindow.setVisible(true);
		graphWindow.setLocationRelativeTo(null);
		
	}
	
	private double getMonthlyTotal(String monthYear) {
		double sum = 0.0;
		for (Expense expense : map.get(monthYear)) {
			sum += expense.getAmount();
		}
		
		
		return sum;
	}
}

class MonthlyExpense {
	private String date;
	private double total;
	
	public MonthlyExpense(String date, double total) {
		this.date = date;
		this.total = total;
	}
	
	public double getTotal() {
		return this.total;
	}
	
	public String getDate() {
		return this.date;
	}
}

class BarChart extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Color, Double> bars = new LinkedHashMap<Color, Double>();

    public BarChart(Map<Color, MonthlyExpense> data) {
            for (Color keyColor : data.keySet()) {
                    MonthlyExpense monthlyExpense = data.get(keyColor);
                    bars.put(keyColor, new Double(monthlyExpense.getTotal()));
            }
    }

    protected void paintComponent(Graphics gp) {
            super.paintComponent(gp);
            // Cast the graphics objects to Graphics2D
            Graphics2D g = (Graphics2D) gp;
            // determine longest bar
            double max = Integer.MIN_VALUE;
            for (Double value : bars.values()) {
                    max = Math.max(max, value);
            }
            // paint bars

            int width = (getWidth() / bars.size()) - 2;
            int x = 1;
            for (Color color : bars.keySet()) {
                    double value = bars.get(color);
                    int height = (int) ((getHeight() - 5) * ((double) value / max));
                    g.setColor(color);
                    g.fillRect(x, getHeight() - height, width, height);
                    g.setColor(Color.black);
                    g.drawRect(x, getHeight() - height, width, height);
                    x += (width + 2);
            }
    }

    public Dimension getPreferredSize() {
            return new Dimension(bars.size() * 10 + 2, 50);
    }
}
