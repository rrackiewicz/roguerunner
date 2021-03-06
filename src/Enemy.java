import com.googlecode.lanterna.terminal.Terminal;

public class Enemy extends Entity {
 	
	private Monster monster;
	private Seed symbol;
	private boolean blinkState;
	public Direction lastDirection;
	public boolean canMove;
	
	public Enemy(int x, int y, Direction dir, Monster monster) {
		coord = new Coord(x,y);
		setDirection(dir);
		setMoveCooldown(true);
		setForecolor(Terminal.Color.MAGENTA);
		setBackcolor(Terminal.Color.BLACK);
		this.lanternRadiusBuffer = getLanternRadius();
		this.monster = monster;
		this.blinkState = true; 
		setStats();
		this.lastDirection = Direction.NONE;
	}
	
	public Monster getMonster() {
		return monster;
	}
	
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	
//	@Override public int getMoveRate() {
//		//int variability = (int)(Math.random()*25);
//		return this.getMoveRate() ;
//	}
	
	public void setBlinkState(boolean blinkState) {
		this.blinkState = blinkState;
	}
	
	public boolean getBlinkState() {
		return this.blinkState;
	}
	
	public void toggleBlinkState() {
		this.blinkState = (blinkState == true) ? false : true;
	}
	
	public void setSymbol(Seed symbol) {
		this.symbol = symbol;
	}
	
	@Override public Seed getSymbol() {
		switch (this.monster) {
		case FIREFLY:
			return Seed.FIREFLY;
		case RAVAGER:
			return Seed.RAVAGER;
		case TORCH:
			return Seed.TORCH;
		}
		return Seed.SPACE;
	}
	
	public void setStats() {
		switch (monster) {
		case FIREFLY:
			setMoveRate(500);
			setMaxHealth(6);
			setCurrentHealth(getMaxHealth());
			setAggroRange(0);
			setSymbol(Seed.FIREFLY);
			canMove = true;
			break;
		case RAVAGER:
			setMoveRate(500);
			setMaxHealth(6);
			setCurrentHealth(getMaxHealth());
			setAggroRange(1000);
			setSymbol(Seed.RAVAGER);
			canMove = true;
			break;
		case TORCH:
			setMoveRate(500);
			setMaxHealth(6);
			setCurrentHealth(getMaxHealth());
			setAggroRange(1000);
			setSymbol(Seed.TORCH);
			canMove = false;
			break;
		}
	}
	
	//this should be implemented as an interface
	public Direction moveRand() {
		int direction = (int)(Math.random()*4+1);
		//System.out.println(direction);
		switch(direction) {
		case 1:
			return Direction.EAST;
		case 2:
			return Direction.SOUTH;
		case 3:
			return Direction.WEST;
		case 4:
			return Direction.NORTH;
		}
		return Direction.NONE;
	}

}
