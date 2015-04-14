public class Player extends Entity {
		
	public Player(int x, int y, Direction dir) {
		coord = new Coord(x,y);
		setDirection(dir);
		this.moveCooldown = true;
		setForecolor(Rating.GREEN);
		setBackcolor(Rating.BLACK);
		setLanternRadius(3);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(250);
	}
}
