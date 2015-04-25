/**
 * The TriggerType enum represents all the types of triggers in the game
 */
public enum TriggerType {	
	MESSAGE 	("message"),
	LEVEL		("level"),
	EMOTE		("emote"),
	TRAP		("trap"), //be more specific here
	SPAWN		("spawn"),
	CUTSCENE	("cutscene")
	; //needed when fields follows
	
	String type;
	
	//TriggerType constructor 
	TriggerType(String type) {
		this.type = type;
	}
	
}