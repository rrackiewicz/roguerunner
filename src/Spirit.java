/**
 * The Spirit enum represents all the states the spirit can take on
 */
public enum Spirit {	
	PUSH 	("Push"),		//can push blocks and enemies
	DETECT 	("Detect"),		//can detect traps, secret passages, clues, hidden chests, enemies
	DIG		("Dig"),		//can dig walls and pits
	FAST	("Fast"), 		//can move faster
	AGGRO	("Aggro"), 		//attracts mobs
	LEAP	("Leap"),		//can jump over pits
	DROP	("Drop"), 		//can drop blocks
	ETHERAL	("Etheral"), 	//can walk through walls
	INVIS	("Invis"),		//can appear invisible for a short time
	FOW		("Fog of War"),		//implements fog of war on map
	ALL		("All"), 		//has all abilities]
	NONE	("None"),		//has no abilities
	COLOR	("Color"),
	BLINK	("Blink")		//can teleport to the location of the spirit
	; //needed when fields follows
	
	//fields for Rating enum
	String text;
	
	//Seed constructor 
	Spirit(String text) {
		this.text = text;
	}
	
}