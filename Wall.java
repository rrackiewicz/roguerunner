/*
 * Wall objects store a wall's current rating (e.g. Green, Yellow, Red, White);
 */
public class Wall extends Thing {
	//an enumation of the walls level
	public int rating;
	
	public Wall(int rating) {
		this.rating = rating;		
	}
}
