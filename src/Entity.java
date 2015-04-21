import com.googlecode.lanterna.terminal.Terminal;


public abstract class Entity {

	public Coord coord;
	private int maxHealth;
	private int currentHealth;
	private Direction dir; //direction player is facing stored as a 4-bit binary number
	private Terminal.Color foreColor;
	private Terminal.Color backColor;
	private int lanternRadius;
	public int lanternRadiusBuffer;
	private int moveRate;
	private boolean moveCooldown;
	
	public Entity() {
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getMaxHealth(){
		return this.maxHealth;
	}
	
	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}
	
	public int getCurrentHealth(){
		return this.currentHealth;
	}
	
	public Terminal.Color getForeColor() {
		return this.foreColor;
	}
	
	public void setForecolor(Terminal.Color color) {
		this.foreColor = color;
	}
	
	public Terminal.Color getBackColor() {
		return this.backColor;
	}
	
	public void setBackcolor(Terminal.Color color) {
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
	
	public boolean getMoveCooldown() {
		return this.moveCooldown;
	}
	
	public void setMoveCooldown(boolean val) {
		this.moveCooldown = val;
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
