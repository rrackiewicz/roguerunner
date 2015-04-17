import com.googlecode.lanterna.terminal.Terminal;

public enum Priority {
	LOW 		(Terminal.Color.BLACK), 
	MEDIUM 		(Terminal.Color.YELLOW),  
	HIGH		(Terminal.Color.RED)
	; //needed when fields follows
	
	//fields for Rating enum
	Terminal.Color color;
	
	//Seed constructor 
	Priority(Terminal.Color color) {
		this.color = color;
	}
}