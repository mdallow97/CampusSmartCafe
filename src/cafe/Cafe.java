package cafe;

import java.util.ArrayList;

public class Cafe {
	
	private String name = "";
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
	
	public ArrayList<MealItem> getMeals() {
		return this.meals;
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
}
