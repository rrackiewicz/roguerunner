/**
 * The Seed enum is a repository for all the elements that can be rendered in cells
 * This enum is stored in the content field of class Cell
 */
public enum Seed {	
	
	//Character Seeds below
	//Values in () are arguments passed to Seed constructor
	PE 	  		("\u25b6", "me", true), // Avatar facing east
	PEC			("\u25b7", "me", true), // Avatar on cooldown facing east
	PS 	  		("\u25bc", "me", true), // Avatar facing south
	PSC			("\u25bd", "me", true), // Avatar on cooldown facing south
	PW 	  		("\u25c0", "me", true), // Avatar facing west
	PWC			("\u25c1", "me", true), // Avatar on cooldown facing west
	PN 	  		("\u25b2", "me", true), // Avatar facing north
	PNC			("\u25b3", "me", true), // Avatar on cooldown facing north
	PC 	  		("\u002a", "me", false), // Avatar no direction u25a3
	PCC			("\u002a", "me", false), //u5a1
	
	//Map Seeds below. Used in base map layer
	EARTH 		("\u2591", "earth", false), //0020
	EBARRIER	("\u2591", "earth", true),
	PBARRIER	("\u2805", "ground", true),
	GBARRIER    ("\u2591", "grass", true),
	SHRINE		("\u06e9", "torch", false),
	VOID		("\u0020", "earth", false),
	GRASS		("\u2591", "grass", false),
//	PLANT1		("\u2034", "plant", false),
//	PLANT2		("\u2032", "plant", false),
//	PLANT3		("\u2033", "plant", false),
//	PLANT4		("\u2057", "plant", false),
//	PLANT5		("\u2035", "plant", false),
//	PLANT6		("\u2036", "plant", false),
//	PLANT7		("\u2037", "plant", false),
	GRND1		("\u2805", "ground", false),
	GRND2		("\u2810", "ground", false),
	GRND3		("\u2851", "ground", false),
	GRND4		("\u288d", "ground", false),
	GRND5		("\u28d5", "ground", false),
	GRND6		("\u28a8", "ground", false),
	GRND7		("\u282f", "ground", false),
	GRND8		("\u283c", "ground", false),
	GRND9		("\u28d1", "ground", false),
	GRND10		("\u28f3", "ground", false),
	
	FLOOR		("\u002e", "floor", false), //u002e
	PIT			("\u2573", "pit", false),
	BLOCK 		("\u25a1", "block", true),
	TRIGGERF	("\u002e", "trigger", false),
	TRIGGERG	("\u2591", "trigger", false),
	TRIGGERS	("\u06e9", "trigger", false),
	TRIGGERV	("\u2502", "trigger", false),
	TRAPWALL	("\u2573", "trap", true),
	HELP		("\u06e9", "trigger",false),
	
	//Enemy elements
	FIREFLY		("\u0046", "enemy", true),
	RAVAGER 	("\u0052", "enemy", true),
	TORCH		("\u00a4", "enemy", true),
	
	//Interface elements
	HEART		("\u2764", "heart", false),
	//Directional Arrows
	NEA			("\u279a", "arrow", false),

	//Box fill and custom seeds that can be used in boxFill and drawTile
	BUSH 		("\u2741", "bush", true), //hedge
	PATH		("\u002e", "path", false),//cobblestone path
	WATERS		("\u2592", "waters", false), //shallow water
	WATERD		("\u2592", "waterd", true), //deep water
	FLR			("\u002e", "flr", false), //floor for house
	FURROW		("\u007e", "furrow", false), //garden furrow
	CABBAGE		("\u0040", "cabbage", false), //garden cabbage
	ROCK		("\u2593", "rock", true), //rock for mountains
	MANA		("\u002b", "mana", false),
	VSTAIR		("\u2502", "stair",false),
	
	
	//Cow seed
	HORNS		("\u0029", "cow", true),
	HEAD		("\u1633", "cow", true),
	LHALF		("\u1455", "cow", true),
	RHALF		("\u1450", "cow", true),
	TAIL		("\u007e", "cow", true),

	//Tree seeds
	NWRT		("\u256d", "tree", true), 
	NERT		("\u256e", "tree", true), 
	SERT		("\u256f", "tree", true), 
	SWRT		("\u2570", "tree", true),
	
	//Wall Seeds below
	CALC  		("\u2573", "wall", false), // Wall to be calculated
	TRAPFLOOR	("\u002e", "trap", false),
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
	
	NWS			("\u250c", "emote", false), 
	NES			("\u2510", "emote", false), 
	SES			("\u2518", "emote", false), 
	SWS			("\u2514", "emote", false), 
	VS			("\u2502", "emote", false), 
	HS			("\u2500", "emote", false), 
	
	NWR			("\u256d", "emote", false), 
	NER			("\u256e", "emote", false), 
	SER			("\u256f", "emote", false), 
	SWR			("\u2570", "emote", false), 
	EV			("\u003e", "emote", false),
	WV			("\u003c", "emote", false),
	NV			("\u005e", "emote", false),
	SV			("\u081c", "emote", false),

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