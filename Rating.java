import com.googlecode.lanterna.terminal.Terminal;

public enum Rating {
	GREEN 		(1, Terminal.Color.GREEN), 
	YELLOW 		(2, Terminal.Color.YELLOW),  
	RED 		(3, Terminal.Color.RED), 
	WHITE 		(0, Terminal.Color.WHITE)
	; //needed when fields follows
	
	//fields for Rating enum
	int level;
	Terminal.Color color;
	
	//Seed constructor 
	Rating(int level, Terminal.Color color) {
		this.level = level;
		this.color = color;
	}
}
