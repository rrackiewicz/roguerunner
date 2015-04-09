/*
 * Floor objects store a wall's current rating (e.g. Green, Yellow, Red, White);
 */
public class Floor extends Thing {
	//an enumation of the walls level
	public int rating;
	
	public Floor(int rating) {
		this.rating = rating;		
	}
}
