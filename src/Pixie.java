public class Pixie extends Entity {
 
	public int dir; //direction player is facing stored as a 4-bit binary number
	Rating foreColor;
	Rating backColor;
	
	//anything is like move to constructor of superclass and call constructor using super
	public Pixie(int x, int y, int dir) {
		if (Game.log) System.out.println("Pixie created.");
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.foreColor = Rating.WHITE;
		this.backColor = Rating.BLACK;
		this.lanternRadius = 7;
	}
}
