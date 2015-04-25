import com.googlecode.lanterna.terminal.Terminal;


/*
 * Earth objects store a earths current rating (e.g. Green, Yellow, Red, White);
 */
public class Earth extends Thing {
	
	public Earth() {
		setLevel(3);
	}
	
	@Override public Terminal.Color getForeColor() {
		return Terminal.Color.BLACK;
	}
	
	@Override public Terminal.Color getBackColor() {	
	switch(this.getLevel()) {
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
}
