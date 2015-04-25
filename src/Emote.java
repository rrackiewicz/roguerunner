
public class Emote extends Trap {

	private Coord coord;
	private String message;
	private int pauseTime;
	private Direction origin;
	
	public Emote(int x, int y,String message, Direction origin, int pauseTime) {
		coord = new Coord(x,y);
		this.message = message;
		this.pauseTime = pauseTime;
		this.origin = origin;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getDelay() {
		return this.pauseTime;
	}
	
	public Direction getOrigin() {
		return this.origin;
	}
}
