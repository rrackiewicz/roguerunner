/**
 * Same as Pair class but allows x and y to be edited
 */
public class Coord {
	private int x;
	private int y;
	
	//constructor
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	public void setCoords(int x, int y) {
		this.x=x;
		this.y=y;
		System.out.println("Now at coordinates " + this.x + "," + this.y);
	}
	
	
	public void incX() {
		++this.x;
	}
	
	public void dincX() {
		--this.x;
	}
	
	public void incY() {
		++this.y;
	}
	
	public void dincY() {
		--this.y;
	}

}
