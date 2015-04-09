// I think that we need to have this class inherit from a base abstract class called Thing
public class Player extends Thing {
//	public int x;
//	public int y; 
	public int dir; //direction player is facing stored as a 4-bit binary number
	
	public Player(int x, int y, int dir) {
		this.x = x; //move to base class Thing
		this.y = y; //move to base class Thing
		this.dir = dir;
	}
	
	// Does it make sense to keep these methods in here?
	// Unless I can manipulate these values by bit shifting, I should probably change directions to strings e.g. "North"
	public void turnRight() {
		switch(dir){
		case 0b0001: //if facing east
			dir = 0b0010; //turn south
			break;
		case 0b0010: //if facing south
			dir = 0b0100; //turn west
			break;
		case 0b0100: //if facing west
			dir = 0b1000; //turn north
			break;
		case 0b1000: //if facing north
			dir = 0b0001; //turn east
			break;
		}
	}
	
	public void turnLeft() {
		switch(dir){
		case 0b0001: //if facing east
			dir = 0b1000; //turn north
			break;
		case 0b0010: //if facing south
			dir = 0b0001; //turn east
			break;
		case 0b0100: //if facing west
			dir = 0b0010; //turn south
			break;
		case 0b1000: //if facing north
			dir = 0b0100; //turn west
			break;
		}
	}
}
