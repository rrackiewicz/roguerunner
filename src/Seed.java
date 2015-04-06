/**
 * The Seed enum is a repository for all the elements that can be rendered in cells
 * This enum is stored in the content field of class Cell
 */
public enum Seed {	
	
	//Character Seeds below
	//Values in () are arguments passed to Seed constructor
	PE 	  		("\u25d1", "me"), // Avatar facing east
	PS 	  		("\u25d2", "me"), // Avatar facing south
	PW 	  		("\u25d0", "me"), // Avatar facing west
	PN 	  		("\u25d3", "me"), // Avatar facing north
	PC 	  		("\u25c9", "me"), // Avatar centered
	P1 	  		("\u2055", "pixie"), // Pixie 1
	
	//Map Seeds below
	EARTH 		("\u0020", "earth"),
	FLOOR		("\u002e", "floor"),
	PIT			("\u2573", "pit"),
	BLOCK 		("\u25a2", "block"),
	
	//Wall Seeds below
	CALC  		("\u2573", "wall"), // Wall to be calculated
	NWD   		("\u2554", "wall"), // Northwest corner
	NED   		("\u2557", "wall"), // Northeast corner
	SED   		("\u255d", "wall"), // Southeast corner
	SWD   		("\u255a", "wall"), // Southwest corner
	VD    		("\u2551", "wall"), // Vertical
	HD    		("\u2550", "wall"), // Horizontal
	NTD   		("\u2566", "wall"), // North T Junction
	ETD   		("\u2563", "wall"), // East T Junction
	STD   		("\u2569", "wall"), // South T Junction
	WTD   		("\u2560", "wall"), // West T Junction
	FWD			("\u256C", "wall"), // 4-Way
	LOCK		("\u25cd", "wall")	// For debugging 
	; //needed when fields follow
	
	//fields for Seed enum
	String ID, type;
	
	//Seed constructor 
	Seed(String ID, String type) {
		this.ID = ID;
		this.type = type;
	}
}