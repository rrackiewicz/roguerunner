import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
//import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.screen.Screen;

import java.util.*;
import java.nio.charset.Charset;
/**
 * The main class for RogueCraft
 * A state machine for the game
 */

public class Game {  
	// Defines the current state of the game 
	private GameState currentState = GameState.PLAYING; 
	// Creates a new Level object stored in an ArrayList
	public List<Level> level = new ArrayList<Level>(); 
	// Sets the current game level
	public int currentLevel = 0; 
	// Sets the number of players
	public int numberPlayers = 0;
	// Creates a new Player object stored in an ArrayList
	public List<Player> players = new ArrayList<Player>();
	// Creates a new Terminal object
	public static Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
	// Creates a new Screen buffer object
	public static Screen screen = new Screen(terminal);
	// Gets the size of the visible Terminal
	public static TerminalSize screenSize;
   
	public Game() {
		// Calls a method to initialize the game
		initGame();
	  
		//A loop that will run as long that currentState is PLAYING
		while (currentState == GameState.PLAYING) {
			Key key = terminal.readInput();
			if (key != null) {
	       		System.out.println(key);
	       		System.out.println(key.getCharacter());
	       		
	       		if (key.getKind() == Key.Kind.Escape) {
	       			System.exit(0);
	       		}
	       		String input = String.valueOf(key.getCharacter());
	       		
	       		switch(input) {
		       		case "w":
					boolean canMove = level.get(currentLevel).detectCollision(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);
					if (canMove) {
						switch(players.get(numberPlayers).dir) {
							case 0b0001:
								++players.get(numberPlayers).x;
								break;
							case 0b0010:
								--players.get(numberPlayers).y;
								break;
							case 0b0100:
								--players.get(numberPlayers).x;
								break;
							case 0b1000:
								++players.get(numberPlayers).y;
								break;
						} // end inner switch
						level.get(currentLevel).updateBuffer(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);
					} // end if
					level.get(currentLevel).updatePlayer(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);   
	       			break;
	       			
		   			case "a":
		   				players.get(numberPlayers).turnLeft();
						level.get(currentLevel).updatePlayer(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);   
		   				break;
		   			case "d":
		   				players.get(numberPlayers).turnRight();
						level.get(currentLevel).updatePlayer(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);   
		   				break;
		   			case "q":
	       				boolean cantDig = level.get(currentLevel).detectCollision(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);
						if (!cantDig) {
							level.get(currentLevel).removeWall(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);		
							level.get(currentLevel).calcLevel();
						}
						break;
		   			case "l":
		   				currentState = GameState.SHOPPING;
		   				break;
	       		}// end switch
			} // end if
		} // end while
   } // end Game()
 
   public void initGame() {  
	   // Starts the screen buffer
	   screen.startScreen(); 
	   // Draws the title screen
	   titleScreen();
	   // Adds a new player to the game
	   players.add(new Player(0,0, 0b1000));
	   // Adds a new level
	   level.add(new Level());
	   // Generates a new level
	   level.get(currentLevel).newLevel();
	   // Generates the walls
	   level.get(currentLevel).calcLevel();
	   // Buffers the cell under the player before putting the player in that cell, that way
	   // when the player moves, we can replace the previous cell 
	   level.get(currentLevel).bufferCell(players.get(numberPlayers).x, players.get(numberPlayers).y);
	   // Updates the player's position on the map
	   level.get(currentLevel).updatePlayer(players.get(numberPlayers).x, players.get(numberPlayers).y, players.get(numberPlayers).dir);
	// Draws interface
	   paintInterface();
   } // end initGame
 
   public void titleScreen() {
	   String message = "Resize your screen then press Enter to contine";
	   int startXPos = message.length() / 2;
	   Game.screen.putString(startXPos,15, message, Terminal.Color.WHITE, Terminal.Color.BLACK);
	   screen.refresh();
	   boolean flag = true;
	   while (flag) { 
		   Key key = terminal.readInput();
		   if (key != null) {
	   			if (key.getKind() == Key.Kind.Enter) {
	   				flag = false;
	   			} 
		   }
	   }
	   screen.clear();
	   screen.refresh();
	   screenSize = screen.getTerminalSize();	   
   	}
   
   public void paintInterface() {
	   for (int i=0; i<screen.getTerminalSize().getColumns(); ++i) {
		   Game.screen.putString(i, 0, " ", Terminal.Color.WHITE, Terminal.Color.WHITE);
	   }
	   Game.screen.putString(1,0, "RogueRunner v .1", Terminal.Color.BLACK, Terminal.Color.WHITE);
	   screen.refresh();
   }
   
   /** The entry main() method */
   public static void main(String[] args) {	 
      new Game();  
   } // end main
}