
public abstract class Entity {

	public Coord coord;
	public int health;
	public int dir; //direction player is facing stored as a 4-bit binary number
	public Rating foreColor;
	public Rating backColor;
	public int lanternRadius;
	public int moveRate;
	public boolean moveCooldown;
	
	public Entity() {
	}
	
	public Rating getForeColor() {
		return Rating.WHITE;
	}
	
	public Rating getBackColor() {
		return Rating.BLACK;
	}
}
