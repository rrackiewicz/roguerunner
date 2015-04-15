public class Pixie extends Entity {
 	
	private Spirit spirit;
	
	public Pixie(int x, int y, Direction dir, Spirit spirit) {
		coord = new Coord(x,y);
		setDirection(dir);
		this.moveCooldown = true;
		setForecolor(Rating.WHITE);
		setBackcolor(Rating.BLACK);
		setLanternRadius(4);
		this.lanternRadiusBuffer = getLanternRadius();
		setMoveRate(500);
		this.spirit = spirit;
	}
	
	public Spirit getSpirit() {
		return spirit;
	}
	
	public void setSpirit(Spirit spirit) {
		this.spirit = spirit;
	}
}
