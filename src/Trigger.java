
public abstract class Trigger {
	private int tid;
	private int targets; //number of targets
	//add array of targets of type Coord with targets as dim
	
	private int delay;
	private boolean deactivateOnLeave;
	private boolean visible;
	
	public Trigger(int tid, int targets, int delay, boolean deactivateOnLeave) {
		setTid(tid);
		setTargets(targets);
	}
	
	public void setTargets(int targets) {
		this.targets = targets;
	}
	
	public int getTargets() {
		return this.targets;
	}
	
	public void setTid(int Id) {
		this.tid = tid;
	}
	
	public int getTid() {
		return this.tid;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int getDelay() {
		return this.delay;
	}
	
	public boolean getDeactivateOnLeave() {
		return this.deactivateOnLeave;
	}
	
	//Create an interface for emote so I don't have to repeat it
	
}
