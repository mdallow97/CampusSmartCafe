package cafe;

import java.util.ArrayList;

public class Cafe {
	private String name;
	private ArrayList<MealItem> meals;
	private int locationX, locationY;
	
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
}
