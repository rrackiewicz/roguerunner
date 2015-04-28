import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.terminal.Terminal;

public class Player extends Entity {
	
	public List<Buff> buffs = new ArrayList<Buff>();
	private int gems;
	
	public Player(int x, int y, Direction dir) {
		coord = new Coord(x,y);
		setDirection(dir);
		setMoveCooldown(true);
		setForecolor(Terminal.Color.MAGENTA);
		setBackcolor(Terminal.Color.BLACK);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(300);
		setMaxHealth(8);//8
		setCurrentHealth(getMaxHealth());
		setLanternRadius(getCurrentHealth()/3);
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
