
public abstract class Buff {
	private int duration;
	private boolean isPermanent;
	private String name;
	private int value;
	
	public Buff () {
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public boolean getState() {
		return this.isPermanent;
	}
	
	public void toggleState() {
		this.isPermanent = (isPermanent) ? false : true;
	}
	
	public void stateOn() {
		this.isPermanent = true;
	}
	
	public void stateOff() {
		this.isPermanent = false;
	}
}
