/**
 * The Seed enum is a repository for all the elements that can be rendered in cells
 * This enum is stored in the content field of class Cell
 */
public enum Seed {	
	
	//Character Seeds below
	//Values in () are arguments passed to Seed constructor
	PE 	  		("\u25b6", "me", true), // Avatar facing east
	PEC			("\u25b7", "me", true),
	PS 	  		("\u25bc", "me", true), // Avatar facing south
	PSC			("\u25bd", "me", true),
	PW 	  		("\u25c0", "me", true), // Avatar facing west
	PWC			("\u25c1", "me", true),
	PN 	  		("\u25b2", "me", true), // Avatar facing north
	PNC			("\u25b3", "me", true),
	PC 	  		("\u25a3", "me", false), // Avatar centered
	PCC			("\u25a1", "me", false),
//	P1 	  		("\u2b29", "pixie"), // Pixie 1
	
	//Map Seeds below
	EARTH 		("\u002e", "earth", false), //0020
	FLOOR		("\u002e", "floor", false), //u002e
	PIT			("\u2573", "pit", true),
	BLOCK 		("\u25a2", "block", true),
	WATER		("\u2591", "water", true),
	GRASS1 		("\u0348", "grass", false),
	GRASS2		("\u0349", "grass", false),
	
	//Sees without array containers
	BUSH 		("\u2741", "bush", false),

	//Wall Seeds below
	CALC  		("\u2573", "wall", false), // Wall to be calculated
	NWD   		("\u2554", "wall", true), // NW corner double
	NED   		("\u2557", "wall", true), // NE corner double
	SED   		("\u255d", "wall", true), // SE corner double
	SWD   		("\u255a", "wall", true), // SW corner double
	VD    		("\u2551", "wall", true), // Vertical double
	HD    		("\u2550", "wall", true), // Horizontal doubled
	NTD   		("\u2566", "wall", true), // North T Junction double
	ETD   		("\u2563", "wall", true), // East T Junction double
	STD   		("\u2569", "wall", true), // South T Junction double
	WTD   		("\u2560", "wall", true), // West T Junction double
	FWD			("\u256c", "wall", true), // 4-Way double
	LOCK		("\u25cd", "wall", false),	// For debugging
	CLOSE		("\ua202", "close", false),
	SPACE       ("\u0020", "empty", false),
	
	NWS			("\u250c", "emote", false), // NW corner single
	NES			("\u2510", "emote", false), // NW corner single
	SES			("\u2518", "emote", false), // NW corner single
	SWS			("\u2514", "emote", false), // NW corner single
	VS			("\u2502", "emote", false), // NW corner single
	HS			("\u2500", "emote", false), // NW corner single
	
	NWR			("\u256d", "emote", false), // NW corner single
	NER			("\u256e", "emote", false), // NW corner single
	SER			("\u256f", "emote", false), // NW corner single
	SWR			("\u2570", "emote", false), // NW corner single

	; //needed when fields follow
	
	//fields for Seed enum
	String ID, type;
	boolean canCollide;
	
	//Seed constructor 
	Seed(String ID, String type, boolean canCollide) {
		this.ID = ID;
		this.type = type;
		this.canCollide = canCollide;
	}
}