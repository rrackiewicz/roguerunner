/**
 * The Monster enum represents all the types of enemmies in the game
 */
public enum Monster {	
	RAVAGER 	("Berserker"),		
	OOZE		("Shamble"),
	FIREFLY		("Firefly")
	; //needed when fields follows
	
	//What I want to do with this eventually is to assign one or more powers to each enemy, by assigned a 1d array to the field or an arrayList
	//fields for Rating enum
	String type;
	
	//Seed constructor 
	Monster(String type) {
		this.type = type;
	}
	
}