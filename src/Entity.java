
public abstract class Entity {

	public int x;
	public int y;
	public int health;
	public int dir; //direction player is facing stored as a 4-bit binary number
	public Rating foreColor;
	public Rating backColor;
	public int lanternRadius;
	
	public Entity() {
	}
	
	public Rating getForeColor() {
		return Rating.WHITE;
	}
	
	public Rating getBackColor() {
		return Rating.BLACK;
	}
	
}
