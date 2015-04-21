import com.googlecode.lanterna.terminal.Terminal;

public class Enemy extends Entity {
 	
	private Monster monster;
	
	public Enemy(int x, int y, Direction dir, Monster monster) {
		coord = new Coord(x,y);
		setDirection(dir);
		this.moveCooldown = true;
		setForecolor(Terminal.Color.WHITE);
		setBackcolor(Terminal.Color.BLACK);
		setLanternRadius(4);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(200);
		this.monster = monster;
		setMaxHealth(15); //pass in a value
		setCurrentHealth(getMaxHealth());
	}
	
	public Monster getMonster() {
		return monster;
	}
	
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
}
