
/*
 * Resource objects store information about mana and gems
 */
public class Resource extends Thing {
	
	private int glowRadius;
	
	public Resource() {
		this.glowRadius = 2;
		setLevel(2);
	}
	
	public int getGlowRadius() {
		return this.glowRadius;
	}
	
}
