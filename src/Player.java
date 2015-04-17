import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.terminal.Terminal;

public class Player extends Entity {
	
	public List<Buff> buffs = new ArrayList<Buff>();
	private int gems;
	
	public Player(int x, int y, Direction dir) {
		coord = new Coord(x,y);
		setDirection(dir);
		this.moveCooldown = true;
		setForecolor(Terminal.Color.MAGENTA);
		setBackcolor(Terminal.Color.BLACK);
		setLanternRadius(3);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(250);
		setMaxHealth(3);
		setCurrentHealth(getMaxHealth());
		this.gems=0;
		//buffs.add(new MaxHealth());
		//System.out.println(buffs.get(0).getValue());
	}
	
	public void incGems(int val) {
		gems+=val;
	}
	
	public int getGems() {
		return this.gems;
	}
	
	
}
