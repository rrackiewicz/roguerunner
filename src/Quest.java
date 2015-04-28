
public class Quest {
	private String description;
	private int goal;
	private int currentValue;
	
	public Quest(String description, int goal) {
		this.description = description;
		this.goal = goal;
		this.currentValue = 0;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setGoal(int goal) {
		this.goal = goal;
	}
	
	public int getGoal() {
		return this.goal;
	}
	
	public void incCurrentValue() {
		++this.currentValue;
	}
	
	public void setCurrentValue() {
		this.currentValue = currentValue;
	}
	
	public int getCurrentValue() {
		return this.currentValue;
	}
	
	public String getStatus() {
		return currentValue + "/" + goal + " : " + description;
	}
}
