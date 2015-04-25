import com.googlecode.lanterna.terminal.Terminal;

public class Torch extends Entity {
 	
	private Seed symbol;
	
	public Torch(int x, int y, Seed symbol) {
		coord = new Coord(x,y);
		setForecolor(Terminal.Color.MAGENTA);
		setBackcolor(Terminal.Color.BLACK);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(0);
		setMaxHealth(6);
		setCurrentHealth(getMaxHealth());
		setAggroRange(0);
		setSymbol(symbol);
	}
	
	public void setSymbol(Seed symbol) {
		this.symbol = symbol;
	}
	
	@Override public Seed getSymbol() {
		switch (this.symbol) {
		case SHRINE:
			return Seed.SHRINE;
		case TORCH:
			return Seed.TORCH;
		}
		return Seed.SPACE;
	}
}
