package cafe;

public class MealItem {
	private String name;
	private double cost;
	
	
	public MealItem(String name, double cost) {
		this.name = name;
		if (cost < 0.0) 
			this.cost = 0.0;
		else this.cost = cost;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCost(double cost) {
		if (cost < 0.0) 
			this.cost = 0.0;
		else this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	
	public double getCost() {
		return cost;
	}
 }
