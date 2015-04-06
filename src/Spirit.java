/**
 * The Spirit enum represents all the states the spirit can take on
 */
public enum Spirit {	
	PUSH,		//can push blocks and enemies
	DETECT,		//can detect traps, secret passages, clues, hidden chests, enemies
	DIG,		//can dig walls and pits
	FAST, 		//can move faster
	AGGRO, 		//attracts mobs
	LEAP,		//can jump over pits
	ETHERAL, 	//can walk through walls
	INVIS,		//can appear invisible for a short time
	BLINK		//can teleport to the location of the spirit
}