public enum Direction {
	NORTH 		(0b1000), 
	SOUTH 		(0b0010),  
	EAST 		(0b0001), 
	WEST 		(0b0100),
	NW			(0b1100),
	NE			(0b1001),
	SE			(0b0011),
	SW			(0b0110),
	NONE		(0b0000),
	MESSAGE		(0b0000),
	; //needed when fields follows
	
	//fields for Rating enum
	int dir;
	
	//Seed constructor 
	Direction(int dir) {
		this.dir = dir;
	}
}