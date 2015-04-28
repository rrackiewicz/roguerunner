
public class Trigger extends Thing {
	private int targets;
	private Coord targetCoord;
	private TriggerType type;
	private boolean visible;
	private int ID;
	private boolean enduresFlag;
	public boolean isLit;
	public int lanternRadius;
	
	public Trigger(int ID, boolean isLit, boolean enduresFlag) {
		this.ID = ID;
		this.enduresFlag = enduresFlag;
		this.lanternRadius = 6;
		this.isLit = isLit;
	}
	
	public void setTargets(int targets) {
		this.targets = targets;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public int getTargets() {
		return this.targets;
	}
	
	public void setType(TriggerType type) {
		this.type = type;
	}
	
	public TriggerType getType() {
		return this.type;
	}
	
	public boolean getEnduresFlag() {
		return this.enduresFlag;
	}
	
	//Create an interface for emote so I don't have to repeat it
	
}
