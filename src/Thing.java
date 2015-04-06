
public abstract class Thing {

	public int x;
	public int y;
	private int level;
	
	public Thing() {	
	}
	
	public void setLevel (int level){
		this.level = level;
	}
	
	public void incLevel (int inc) {
		++this.level;
	}
	
	public boolean cycleLevel() {
		--this.level;
		return (this.level > 0) ? false : true;
	}
	
	//Need a method in here that calculates the level of the new wall block from some parameter passed to the constructor by the game
	public int getLevel() {
		return this.level;
	}
	
	public Rating getForeColor() {
		switch(this.level) {
		case 1:
			return Rating.GREEN;
		case 2:
			return Rating.YELLOW;
		case 3:
			return Rating.RED;
		case 4:
			return Rating.WHITE;	
		
		}
		return Rating.WHITE;
	}
	
	public Rating getBackColor() {
		return Rating.BLACK;
	}
}
