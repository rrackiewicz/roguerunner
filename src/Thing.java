import com.googlecode.lanterna.terminal.Terminal;

public abstract class Thing {

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
	
	public Terminal.Color getForeColor() {
		switch(this.level) {
		case 1:
			return Terminal.Color.GREEN;
		case 2:
			return Terminal.Color.YELLOW;
		case 3:
			return Terminal.Color.RED;
		case 4:
			return Terminal.Color.WHITE;	
		case 5:
			return Terminal.Color.BLUE;
		}
		return Terminal.Color.WHITE;
	}
	
	public Terminal.Color getBackColor() {
		return Terminal.Color.BLACK;
	}
}
