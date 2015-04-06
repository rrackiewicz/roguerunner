
/*
 * Wall objects store a wall's current rating (e.g. Green, Yellow, Red, White);
 */
public class Block extends Thing {
	
	public Block() {
		if (Game.log) System.out.println("New block created.");
		setLevel(3);
	}
}
