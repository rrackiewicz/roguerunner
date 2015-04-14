
/*
 * Pit objects store a floor's current rating (e.g. Green, Yellow, Red, White);
 */
public class Pit extends Thing {
	public Pit() {
		//if (Game.log) System.out.println("New pit created.");
		setLevel(1);
	}
	
	@Override public boolean cycleLevel() {
		incLevel(1);
		return (getLevel() < 4) ? false : true;
	}
	
	@Override public Rating getForeColor() {
		switch(getLevel()) {
		case 1:
			return Rating.GREEN;
		case 2:
			return Rating.YELLOW;
		case 3:
			return Rating.RED;
		}
		return Rating.RED;
	}
}