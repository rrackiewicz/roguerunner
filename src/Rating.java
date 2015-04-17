import com.googlecode.lanterna.terminal.Terminal;

public enum Rating {
	GREEN 		(Terminal.Color.GREEN), 
	YELLOW 		(Terminal.Color.YELLOW),  
	RED 		(Terminal.Color.RED), 
	WHITE 		(Terminal.Color.WHITE),
	BLACK		(Terminal.Color.BLACK),
	CYAN		(Terminal.Color.CYAN),
	MAGENTA		(Terminal.Color.MAGENTA),
	BLUE		(Terminal.Color.BLUE)
	; //needed when fields follows
	
	//fields for Rating enum
	Terminal.Color color;
	
	//Seed constructor 
	Rating(Terminal.Color color) {
		this.color = color;
	}
}
