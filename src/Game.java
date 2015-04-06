/***************************************************************************************************************
* File        : Game.java
*
* Purpose     : This file is the entry point for the game. The game manages levels, players, keystrokes and 
* 				event timers. 
* 
* Collections :	List<Level> level = new ArrayList<Level>() 		A collection of levels
* 				List<Player> players = new ArrayList<Player>() 	A collection of players
* 
* Objects     :	Terminal terminal (global)						An object that controls the terminal			
* 			  :	Screen screen (global)							An object that controls the screen buffer
* 
* Enums		  :	GameState currentState							The current state of the game
*  
***************************************************************************************************************/

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
//import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.screen.Screen;

import java.util.*;
import java.nio.charset.Charset;

public class Game {  
	private GameState currentState = GameState.PLAYING; 
	public List<Level> level = new ArrayList<Level>(); 
	public int currentLevel = 0; 
	public List<Player> players = new ArrayList<Player>();
	public int activePlayer = 0;
	public List<Pixie> pixies = new ArrayList<Pixie>();
	public int activePixie = 0;	
	public static Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
	public static Screen screen = new Screen(terminal);
	public static TerminalSize screenSize;
	public Spirit spirit;
	boolean playerControl = true;
	public static boolean log = true;
	public static boolean fogOfWar = false;
 
	/***************************************************************************************************************
	* Method      : Game()
	*
	* Purpose     : Constructor for Class Game. The while statement creates a game loop that constantly checks for user
	* 				input and manages timers that govern activity in the game (enemy movement, projectile movement,
	* 				character movement etc.). 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public Game() {
		initGame();

		while (currentState == GameState.PLAYING) {
			Key key = terminal.readInput();
			if (key != null) {

				if (Game.log) System.out.println(key);
	       		//System.out.println(key.getCharacter());
	       		
	       		if (key.getKind() == Key.Kind.Escape) {
	       			System.exit(0);
	       		}
	       		
	       		String input = String.valueOf(key.getCharacter());
	       		switch(input.toLowerCase()) {
		       		case "w":
		       			boolean isBlocked = level.get(currentLevel).detectCollision(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);
						if (!isBlocked) {
							level.get(currentLevel).restoreTile(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);

							switch(players.get(activePlayer).dir) {
								case 0b0001:
									++players.get(activePlayer).x;
									break;
								case 0b0010:
									--players.get(activePlayer).y;
									break;
								case 0b0100:
									--players.get(activePlayer).x;
									break;
								case 0b1000:
									++players.get(activePlayer).y;
									break;
							} 
						} // end if
						break;
		   			case "a":
		   				turnLeft(players.get(activePlayer).dir);
						level.get(currentLevel).updateAvatar(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);   
		   				break;
		   			case "d":
		   				turnRight(players.get(activePlayer).dir);
						level.get(currentLevel).updateAvatar(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);   
		   				break; 
		   			case "q":
		   				level.get(currentLevel).digMap(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);
						break;
		   			case "e":	
		   				boolean canPush = level.get(currentLevel).pushBlock(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);
		   		
		   				if (canPush) {
		   					level.get(currentLevel).restoreTile(players.get(activePlayer).x, players.get(activePlayer).y, players.get(activePlayer).dir);
		   					switch(players.get(activePlayer).dir) {
				   				case 0b0001:
									++players.get(activePlayer).x;
									break;
								case 0b0010:
									--players.get(activePlayer).y;
									break;
								case 0b0100:
									--players.get(activePlayer).x;
									break;
								case 0b1000:
									++players.get(activePlayer).y;
									break;
				   			}//end switch 		
		   				}//end if
		   				break;
		   			case "l":
		   				if (log) {
		   					log = false;
		   					System.out.println("Log OFF");
		   				}
		   				else {
		   					log = true;
		   					System.out.println("Log ON");
		   				}
		   				break;
		   			case "f":
		   				if (fogOfWar) {
		   					fogOfWar = false;
		   					System.out.println("Fog of War OFF");
		   				}
		   				else {
		   					fogOfWar = true;
		   					System.out.println("Fog of War ON");
		   				}
		   				break;
		   			case "1":
		   				if (level.get(currentLevel).pixieFollow) {
		   					level.get(currentLevel).pixieFollow = false;
		   					System.out.println("Pixie follow OFF");
		   				}
		   				else {
		   					level.get(currentLevel).pixieFollow = true;
		   					System.out.println("Pixie follow ON");
		   				}
		   				break;
		   			default:
	       		}// end switch
			} // end if
		} // end while
	} // end Game()
	
	/***************************************************************************************************************
	* Method      : initGame()
	*
	* Purpose     : Responsible for setting the initial state of the game. This includes calling methods to: start 
	* 				the screen buffer, display the title screen, add a player to the game, add a level to the game, 
	* 				render a new level, render a player in the level, render the UI.
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	* 
	***************************************************************************************************************/
	public void initGame() {  
		screen.startScreen(); 
		titleScreen();
		players.add(new Player(0,0, 0b1000));
		pixies.add(new Pixie(0,-1, 0b0000));
		//spirit = Spirit.PUSH;
		level.add(new Level(players.get(activePlayer).lanternRadius, pixies.get(activePixie).lanternRadius));
		level.get(currentLevel).newLevel();
		level.get(currentLevel).calcLevel();
		level.get(currentLevel).bufferCell(players.get(activePlayer).x, players.get(activePlayer).y);
		level.get(currentLevel).newPlayer(0,0, players.get(activePlayer).getForeColor(), players.get(activePlayer).getBackColor());
		level.get(currentLevel).bufferCell(pixies.get(activePixie).x, pixies.get(activePixie).y);
		level.get(currentLevel).newPixie(0,-1, pixies.get(activePixie).getForeColor(), pixies.get(activePixie).getBackColor());
		level.get(currentLevel).updateLantern(players.get(activePlayer).x, players.get(activePlayer).y ,players.get(activePlayer).lanternRadius);
		//level.get(currentLevel).updateLantern(players.get(activePixie).x, players.get(activePixie).y,pixies.get(activePixie).lanternRadius);
	
		//level.get(currentLevel).newPixie(0,-1);
		paintInterface();
	} // end initGame
    
	/***************************************************************************************************************
	* Method      : titleScreen()
	*
	* Purpose     : The titleScreen method draws the title screen 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
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
		//Clears the screen buffer
		screen.clear();
		screen.refresh();
		screenSize = screen.getTerminalSize();	   
	}//end titleScreen

	/***************************************************************************************************************
	* Method      : paintInterface()
	*
	* Purpose     : Draws the user interface
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void paintInterface() {
		for (int i=0; i<screen.getTerminalSize().getColumns(); ++i) {
			Game.screen.putString(i, 0, " ", Terminal.Color.WHITE, Terminal.Color.WHITE);
		}
	
		Game.screen.putString(1,0, "RogueRunner v .1", Terminal.Color.BLACK, Terminal.Color.WHITE);
		screen.refresh();
	}// end paintInterface
	   
	/***************************************************************************************************************
	* Method      : main(String[] args)
	*
	* Purpose     : Passes control of the game to the constructor of the Game Class
	*
	* Parameters  : No command line args are currently used
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public static void main(String[] args) {	 
		new Game();  
	}// end main
	
	public void turnRight(int dir) {
		switch(dir){
		case 0b0001: //if facing east
			if (playerControl) {
				players.get(activePlayer).dir = 0b0010;
			}
			else {
				//change direction of spirit
			}
			break;
		case 0b0010: //if facing south
			if (playerControl) {
				players.get(activePlayer).dir = 0b0100;
			}
				else {
				//change direction of spirit
			}
			break;
		case 0b0100: //if facing west
			if (playerControl) {
				players.get(activePlayer).dir = 0b1000;
			}
			else {
				//change direction of spirit
			}
			break;
		case 0b1000: //if facing north
			if (playerControl) {
				players.get(activePlayer).dir = 0b0001;
			}
			else {
				//change direction of spirit
			}
			break;
		}
	}
	
	public void turnLeft(int dir) {
		switch(dir){
		case 0b0001: //if facing east
			players.get(activePlayer).dir = 0b1000;
			break;
		case 0b0010: //if facing south
			players.get(activePlayer).dir = 0b0001;
			break;
		case 0b0100: //if facing west
			players.get(activePlayer).dir = 0b0010;
			break;
		case 0b1000: //if facing north
			players.get(activePlayer).dir = 0b0100;
			break;
		}	
	}

}// end Game