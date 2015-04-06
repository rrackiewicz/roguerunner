
public class Player extends Entity {
 
	public int dir; //direction player is facing stored as a 4-bit binary number
	Rating foreColor;
	Rating backColor;
	
	public Player(int x, int y, int dir) {
		if (Game.log) System.out.println("Player created.");
		this.x = x; //move to base class Thing
		this.y = y; //move to base class Thing
		this.dir = dir;
		this.foreColor = Rating.WHITE;
		this.backColor = Rating.BLACK;
		this.lanternRadius = 3;
	}
}
