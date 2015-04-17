import com.googlecode.lanterna.terminal.Terminal;


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
	
	@Override public Terminal.Color getForeColor() {
		switch(getLevel()) {
		case 1:
			return Terminal.Color.GREEN;
		case 2:
			return Terminal.Color.YELLOW;
		case 3:
			return Terminal.Color.RED;
		}
		return Terminal.Color.RED;
	}
}