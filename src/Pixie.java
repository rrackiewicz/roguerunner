import com.googlecode.lanterna.terminal.Terminal;

public class Pixie extends Entity {
 	
	private Spirit spirit;
	
	public Pixie(int x, int y, Direction dir, Spirit spirit) {
		coord = new Coord(x,y);
		setDirection(dir);
		setMoveCooldown(true);
		setForecolor(Terminal.Color.WHITE);
		setBackcolor(Terminal.Color.BLACK);
		setLanternRadius(8);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(250);
		this.spirit = spirit;
		setMaxHealth(15);
		setCurrentHealth(getMaxHealth());
		setLanternRadius(getCurrentHealth()/3);
	}
	
	public Spirit getSpirit() {
		return spirit;
	}
	
	public void setSpirit(Spirit spirit) {
		this.spirit = spirit;
	}
}
