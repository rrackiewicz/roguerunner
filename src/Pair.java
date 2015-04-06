/**
 * Java does not implement a pair class.
 * Allows you to store an x,y pair in an object
 */
public class Pair {
	private final int x;
	private final int y;
	private volatile int hashCode;
	
	//constructor
	public Pair(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	//overrides the equals method to avoid collision of multiple pairs with same x,y values
	@Override public boolean equals(Object o) {
	    if(this == o) return true;
	    if(!(o instanceof Pair)) return false;
	    Pair p = (Pair)o;
	    return p.x == x && p.y == y;
	}
	
	//overrides the hashCode method to avoid collision of multiple pairs with same x,y values
	@Override public int hashCode() {
	    int result = hashCode;
	    if(result == 0) {
	        result = 17 * x + 31 * y;
	        hashCode = result;
	    }
	    return result;
	}
}
