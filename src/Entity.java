
public abstract class Entity {

	public Coord coord;
	public int health;
	private Direction dir; //direction player is facing stored as a 4-bit binary number
	private Rating foreColor;
	private Rating backColor;
	private int lanternRadius;
	public int lanternRadiusBuffer;
	private int moveRate;
	public boolean moveCooldown;
	
	public Entity() {
	}
	
	public Rating getForeColor() {
		return this.foreColor;
	}
	
	public void setForecolor(Rating color) {
		this.foreColor = color;
	}
	
	public Rating getBackColor() {
		return this.backColor;
	}
	
	public void setBackcolor(Rating color) {
		this.backColor = color;
	}
	
	public Direction getDirection() {
		return this.dir;
	}
	
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
	public int getLanternRadius() {
		return this.lanternRadius;
	}
	
	public void setLanternRadius(int radius) {
		this.lanternRadius = radius;
	}
	
	public void incRadius() {
		++this.lanternRadius;
	}
	
	public void dincRadius() {
		--this.lanternRadius;
	}
	
	public int getMoveRate() {
		return this.moveRate;
	}
	
	public void setMoveRate(int rate) {
		this.moveRate = rate;
	}
	
	public Seed getSymbol() {
		switch(getDirection()) {
		case EAST:
			if (moveCooldown) return Seed.PE;
			else return Seed.PEC;
		case SOUTH:
			if (moveCooldown) return Seed.PS;
			else return Seed.PSC;
		case WEST:
			if (moveCooldown) return Seed.PW;
			else return Seed.PWC;
		case NORTH:
			if (moveCooldown) return Seed.PN;
			else return Seed.PNC;
		case NONE:
			if (moveCooldown) return Seed.PC;
			else return Seed.PCC;
		}
		return Seed.PC;
	}
}
