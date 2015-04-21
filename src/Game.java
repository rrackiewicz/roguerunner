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
	
	public List<Level> level = new ArrayList<Level>(); 
	public int currentLevel; 
	public List<Player> players = new ArrayList<Player>();
	public int selectedPlayer;
	public List<Pixie> pixies = new ArrayList<Pixie>();
	public int selectedPixie;	
	public static Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
	public static Screen screen = new Screen(terminal);
	public static TerminalSize screenSize;
	public static boolean log = true;
	public static Lantern lantern = Lantern.CONE;
	public static Spirit spirit;
	public Timer moveTimer;
	public boolean cardinalFlag;
	
	private GameState currentState; 
	public static boolean playerControl = true;
	public boolean disableCooldown = false;
 
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
//		System.out.println("Left is " + Level.leftScreen);
//		System.out.println("Right is " + Level.rightScreen);
//		System.out.println("Top is " + Level.topScreen);
//		System.out.println("Bottom is " + Level.bottomScreen);
//		System.out.println("Width of level is " + Level.levelWidth);
//		System.out.println("Height of level is " + Level.levelHeight);
		while (currentState != GameState.GAMEOVER) {
			Key key = terminal.readInput();
			if (key != null) {
				if (Game.log) System.out.println(key);
	       		if (key.getKind() == Key.Kind.Escape) {
	       			System.exit(0);
	       		}
	       		
	       		if (!playerControl) {
		       		if (key.getKind() == Key.Kind.ArrowUp) {
		       			if (!playerControl) {
		       				commandMoveFacing();
		       			}
			       		//continue;
		       		}
		       		
		       		if (key.getKind() == Key.Kind.ArrowDown) {
		       			if (!playerControl) {
		   					switch(pixies.get(selectedPixie).getSpirit()) {
		   					case PUSH:
		   						commandPush();
		   						break;
		   					case DIG:
		   						commandDig();
		   						break;
		   					case DROP:
		   						commandDrop();
		   						break;
		   					case ALL:
		   						break;
		   					}
		   				}
			       		//continue;
		       		}
		       		
		       		if (key.getKind() == Key.Kind.ArrowLeft) {
	   					pixies.get(selectedPixie).setDirection(turnLeft(pixies.get(selectedPixie).getDirection()));
	   					level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
	   					//continue;	
		       		}
		       		
		       		if (key.getKind() == Key.Kind.ArrowRight) {
	   					pixies.get(selectedPixie).setDirection(turnRight(pixies.get(selectedPixie).getDirection()));
	   					level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
			       		//continue;
		       		}
		       		//continue;
	       		}

	       		String input = String.valueOf(key.getCharacter());
	       		switch(input.toLowerCase()) {
	       			case " ":
	       				commandControlPixie();
	       				break;
	       			case "i": //cell info for debugging
	       				if (playerControl) {
	       					
	       					level.get(currentLevel).cellInfo(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
	       				}
	       				else {
	       					level.get(currentLevel).cellInfo(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());	
	       				}
	       				break;
	       			case "w":
		       			if (playerControl) {
		       				commandMoveFacing();
		       			}
		       			else commandMoveCardinal(Direction.NORTH);
						break;
		   			case "a":
		   				if (playerControl) { 
		   					players.get(selectedPlayer).setDirection(turnLeft(players.get(selectedPlayer).getDirection()));
		   					level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
		   				}
		   				else commandMoveCardinal(Direction.WEST);
						break;
		   			case "d":
		   				if (playerControl) {
		   					players.get(selectedPlayer).setDirection(turnRight(players.get(selectedPlayer).getDirection()));
		   					level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
		   					}
		   				else commandMoveCardinal(Direction.EAST);
		   				break; 
		   			case "s":
		   				if (playerControl) {
		   					switch(pixies.get(selectedPixie).getSpirit()) {
		   					case PUSH:
		   						pixies.get(selectedPixie).setSpirit(Spirit.PUSH);
		   						commandPush();
		   						break;
		   					case DIG:
		   						pixies.get(selectedPixie).setSpirit(Spirit.DIG);
		   						commandDig();
		   						break;
		   					case DROP:
		   						pixies.get(selectedPixie).setSpirit(Spirit.DROP);
		   						commandDrop();
		   						break;
		   					case ALL:
		   						pixies.get(selectedPixie).setSpirit(Spirit.ALL);
		   						break;
		   					}
		   				}
		   				else commandMoveCardinal(Direction.SOUTH);
		   				break;
		   			case "1":
		   				System.out.println("DIG pixie");
		   				pixies.get(selectedPixie).setSpirit(Spirit.DIG);
		   				break;
		   			case "2":
		   				System.out.println("PUSH pixie");
		   				pixies.get(selectedPixie).setSpirit(Spirit.PUSH);
		   				break;
		   			case "3":
		   				System.out.println("DROP pixie");
		   				pixies.get(selectedPixie).setSpirit(Spirit.DROP);
		   				break;
		   			case "8":
		   				lantern = Lantern.FOW;
		   				break;
		   			case "9":
		   				lantern = Lantern.CONE;
		   				break;
		   			case "0":
		   				lantern = Lantern.FULL;
		   				players.get(selectedPlayer).setLanternRadius(0);
		   				pixies.get(selectedPixie).setLanternRadius(0);
		   				level.get(currentLevel).lightLevel();
		   				break;
		   			case "g":
		   				if (log) {
		   					log = false;
		   					System.out.println("Log OFF");
		   				}
		   				else {
		   					log = true;
		   					System.out.println("Log ON");
		   				}
		   				break;
		   			case "e":
		   				if (playerControl) {
		   					level.get(currentLevel).emote("ZZZ...", players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), Direction.WEST, 2000);
		   				}
		   				else {
		   					level.get(currentLevel).emote("I'M A PIXIE", pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), Direction.EAST, 1000);
				   				
		   				}
		   				break;
		   			case "y":
		   				clearMessage();
		   				break;
		   			default:
	       		}// end switch
			} // end if
			paintInterface();
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
		terminal.setCursorVisible(false);
		entryScreen();
		//titleScreen();
		level1();
		//level2();	
	} // end initGame
	
	/***************************************************************************************************************
	* Method      : turnRight()
	*
	* Purpose     : Updates a player's direction when turning right
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public Direction turnRight(Direction dir) {
		switch(dir){
		case EAST: 
			return Direction.SOUTH;
		case SOUTH: 
			return Direction.WEST;
		case WEST: 
			return Direction.NORTH;
		case NORTH: 
			return Direction.EAST;
		}
		return Direction.NONE;
	}
	
	/***************************************************************************************************************
	* Method      : turnLef()
	*
	* Purpose     : Updates a player's direction when turning left  
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public Direction turnLeft(Direction dir) {
		switch(dir){
		case EAST: //if facing east
			return Direction.NORTH;
		case SOUTH: //if facing south
			return Direction.EAST;
		case WEST: //if facing west
			return Direction.SOUTH;
		case NORTH: //if facing north
			return Direction.WEST;
		}	
		return Direction.NONE;
	}
	
	/***************************************************************************************************************
	* Method      : commandMoveFacing()
	*
	* Purpose     : Updates a player's coordindates when playerControl = true 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void commandMoveFacing() {
		if (playerControl) {
			boolean isBlocked = level.get(currentLevel).detectCollision(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
			if (isOffScreen(players.get(selectedPlayer).getDirection())) {
				System.out.println("Edge of map");
				level.get(currentLevel).scrollMap(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
			}
			if (!isBlocked && players.get(selectedPlayer).getMoveCooldown()) {
				level.get(currentLevel).moveEntity(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());		
				level.get(currentLevel).avatarFollow(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());
				pixies.get(selectedPixie).coord.setX(players.get(selectedPlayer).coord.getX());
				pixies.get(selectedPixie).coord.setY(players.get(selectedPlayer).coord.getY());
				switch(players.get(selectedPlayer).getDirection()) {
					case EAST:
						players.get(selectedPlayer).coord.incX();
						break;
					case SOUTH:
						players.get(selectedPlayer).coord.dincY();
						break;
					case WEST:
						players.get(selectedPlayer).coord.dincX();
						break;
					case NORTH:
						players.get(selectedPlayer).coord.incY();
						break;
				}// end switch 
				if (!disableCooldown) {
					moveTimer();
				}
			}
		}// end if
		else {
			boolean isBlocked = level.get(currentLevel).detectCollision(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
			if (!isBlocked && pixies.get(selectedPixie).getMoveCooldown()) {
				if (isOffScreen(pixies.get(selectedPixie).getDirection())) {
					System.out.println("Edge of map");
					//panMap();
					isBlocked = true;
				}
				level.get(currentLevel).moveEntity(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());
				//When lanterns overlap we have to restore tiles that were turned off
				level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getLanternRadius());
				switch(pixies.get(selectedPixie).getDirection()) {
					case EAST:
						pixies.get(selectedPixie).coord.incX();
						break;
					case SOUTH:
						pixies.get(selectedPixie).coord.dincY();
						break;
					case WEST:
						pixies.get(selectedPixie).coord.dincX();
						break;
					case NORTH:
						pixies.get(selectedPixie).coord.incY();
						break;
				}// end switch 
				if (!disableCooldown) {
					moveTimer();
				}
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : moveCardinal(Direction dir)
	*
	* Purpose     : Updates a player's coordinates when moving in cardinal directions when playerControl = false
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void commandMoveCardinal(Direction dir) {
		boolean isBlocked = level.get(currentLevel).detectCollision(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir);
		if (isOffScreen(players.get(selectedPlayer).getDirection())) {
			System.out.println("Edge of map");
			//panMap();
			isBlocked = true;
		}
		if (!isBlocked && (players.get(selectedPlayer).getMoveCooldown() || disableCooldown)) {	
			System.out.println("X");
			//Since player isn't facing a direction just pass the direction they are moving
			level.get(currentLevel).moveEntity(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir, players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());
			//When lanterns overlap we have to restore tiles that were turned off
			level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getLanternRadius());
			
			switch(dir) {
			case EAST:
				players.get(selectedPlayer).coord.incX();
				break;
			case SOUTH:
				players.get(selectedPlayer).coord.dincY();
				break;
			case WEST:
				players.get(selectedPlayer).coord.dincX();
				break;
			case NORTH:
				players.get(selectedPlayer).coord.incY();
				break;
			} // end switch
			cardinalFlag = true;
			if (!disableCooldown) {
				moveTimer();
			}
		} // end if	
	}
	
	public boolean isOffScreen(Direction dir) {
		if (playerControl) {
			switch(dir) {
			case EAST:
				if (players.get(selectedPlayer).coord.getX() >= Level.rightScreen - 1) {
					return true;
				}
				break;
			case SOUTH:
				if (players.get(selectedPlayer).coord.getY() <= Level.bottomScreen + 1) {
					return true;
				}
				break;
			case WEST:
				if (players.get(selectedPlayer).coord.getX() <= Level.leftScreen + 1) {
					return true;
				}
				break;
			case NORTH:
				if (players.get(selectedPlayer).coord.getY() >= Level.topScreen - 1) {
					return true;
				}
				break;
			}
			return false;
		}
		else {
			switch(dir) {
			case EAST:
				if (pixies.get(selectedPixie).coord.getX() >= Level.rightScreen - 1) {
					return true;
				}
				break;
			case SOUTH:
				if (pixies.get(selectedPixie).coord.getY() <= Level.bottomScreen + 1) {
					return true;
				}
				break;
			case WEST:
				if (pixies.get(selectedPixie).coord.getX() <= Level.leftScreen + 1) {
					return true;
				}
				break;
			case NORTH:
				if (pixies.get(selectedPixie).coord.getY() >= Level.topScreen - 1) {
					return true;
				}
				break;
			}
			return false;
		}
	}
	
	
	public void commandPush() {
		if (pixies.get(selectedPixie).getSpirit() == Spirit.ALL || pixies.get(selectedPixie).getSpirit() == Spirit.PUSH) {
			if (playerControl) {
				boolean canPush = level.get(currentLevel).pushBlock(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
			
				if (canPush) {
					level.get(currentLevel).moveEntity(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());
					//When lanterns overlap we have to restore tiles that were turned off
					level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getLanternRadius());
				
   					switch(players.get(selectedPlayer).getDirection()) {
		   				case EAST:
							players.get(selectedPlayer).coord.incX();
							break;
						case SOUTH:
							players.get(selectedPlayer).coord.dincY();
							break;
						case WEST:
							players.get(selectedPlayer).coord.dincX();
							break;
						case NORTH:
							players.get(selectedPlayer).coord.incY();;
							break;
		   			}//end switch 		
   				}//end if
			}//end if
			else {
				boolean canPush = level.get(currentLevel).pushBlock(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
				level.get(currentLevel).moveEntity(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());
				//When lanterns overlap we have to restore tiles that were turned off
				level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getLanternRadius());
			
				if (canPush) {
   					switch(pixies.get(selectedPixie).getDirection()) {
		   				case EAST:
							pixies.get(selectedPixie).coord.incX();
							break;
						case SOUTH:
							pixies.get(selectedPixie).coord.dincY();
							break;
						case WEST:
							pixies.get(selectedPixie).coord.dincX();
							break;
						case NORTH:
							pixies.get(selectedPixie).coord.incY();;
							break;
		   			}//end switch 		
   				}//end if
			}
		}		
	}
	
	public void commandDig() {
		if (pixies.get(selectedPixie).getSpirit() == Spirit.ALL || pixies.get(selectedPixie).getSpirit() == Spirit.DIG) {
			if (playerControl) {
				level.get(currentLevel).digMap(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
			}
			else {
				level.get(currentLevel).digMap(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
			}
		}
	}
	
	
	public void commandDrop() {
		if (pixies.get(selectedPixie).getSpirit() == Spirit.ALL || pixies.get(selectedPixie).getSpirit() == Spirit.DROP) {
			if (playerControl) {
				boolean isBlocked = level.get(currentLevel).detectCollision(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
				if (!isBlocked) {
					level.get(currentLevel).dropBlock(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
				}
			}
			else {
				boolean isBlocked = level.get(currentLevel).detectCollision(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
				if (!isBlocked) {
					level.get(currentLevel).dropBlock(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
				}
			}
		}	
	}

	public void commandControlPixie() { {
		if (playerControl) {
			System.out.println("Player control = false");
			System.out.println(disableCooldown);
				playerControl = false;
				pixies.get(selectedPixie).setDirection(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(Direction.NONE);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());   			
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   
			}
			else {
				System.out.println("Player control = true");
				System.out.println(disableCooldown);
				playerControl = true;
				players.get(selectedPlayer).setDirection(pixies.get(selectedPixie).getDirection());
				pixies.get(selectedPixie).setDirection(Direction.NONE);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(),players.get(selectedPlayer).getLanternRadius());   	
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   		
			}
		}
	}
	
	//change priority to an enum
	public void sendMessage(String message, Priority priority, int pauseTime) {
		for (int i=0; i<screen.getTerminalSize().getColumns(); ++i) {
			Game.screen.putString(i, 29, " ", Terminal.Color.WHITE, Terminal.Color.WHITE);
		}
		int startXPos = Game.screenSize.getColumns() / 2 - message.length() / 2;
		Game.screen.putString(startXPos, 29, message, priority.color, Terminal.Color.WHITE);
		if (pauseTime > 0) {
			pause(pauseTime);
		}
		screen.refresh();
	}
	
	
	public void clearMessage() {
		for (int i=0; i<screen.getTerminalSize().getColumns(); ++i) {
			Game.screen.putString(i, 29, " ", Terminal.Color.WHITE, Terminal.Color.WHITE);
		}
		screen.refresh();
		paintInterface();
	}
	
	/***************************************************************************************************************
	* Method      : moveTimer()
	*
	* Purpose     : Creates a new movement timer 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void moveTimer() {
		moveTimer = new Timer();
		System.out.println("Player control is " + playerControl);
		System.out.println("Cardinal flag is " + cardinalFlag);
		if (playerControl) {
			moveTimer.schedule(new resetTimer(), players.get(selectedPlayer).getMoveRate());
			players.get(selectedPlayer).setMoveCooldown(false);
			level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());  
		}
		else if (!playerControl && !cardinalFlag) {
			moveTimer.schedule(new resetTimer(), pixies.get(selectedPixie).getMoveRate());
			pixies.get(selectedPixie).setMoveCooldown(false);
			level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
		}
		else {
			moveTimer.schedule(new resetTimer(), players.get(selectedPlayer).getMoveRate());
			players.get(selectedPlayer).setMoveCooldown(false);
			level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());  
		}
			
	}
	
	/***************************************************************************************************************
	* Class       : resetTimer
	*
	* Purpose     : Destroys the player movement timer when it has expired
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	class resetTimer extends TimerTask {
		public void run() {
			if (playerControl) {
				players.get(selectedPlayer).setMoveCooldown(true);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
				cardinalFlag = false;
			}
			else if (!playerControl && !cardinalFlag) {
				pixies.get(selectedPixie).setMoveCooldown(true);
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
			}
			else {
				System.out.println("Timer up");
				players.get(selectedPlayer).setMoveCooldown(true);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
				cardinalFlag = false;	
			}
		    moveTimer.cancel(); 
		}
	}
	
	/***************************************************************************************************************
	* Class       : titleScreen
	*
	* Purpose     : Displays the title screen
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void titleScreen() {
		level.add(new Level());
		putCharacter("R",-25,1,100);
		putCharacter("O",-20,1,100);
		putCharacter("G",-15,1,100);
		putCharacter("U",-10,1,100);
		putCharacter("E",-5,1,100);
		putCharacter("R",0,1,100);
		putCharacter("U",5,1,100);
		putCharacter("N",10,1,100);
		putCharacter("N",15,1,100);
		putCharacter("E",20,1,100);
		putCharacter("R",25,1,100);
		putUnicode(Seed.HD,25,3,100);
		putUnicodeTrail(Seed.HS,Direction.WEST,49,25,3,5);
		putUnicode(Seed.HD,-25,3,100);
		putUnicode(Seed.HD,-25,-1,100);
		putUnicodeTrail(Seed.HS,Direction.EAST,49,-25,-1,5);
		putUnicode(Seed.HD,25,-1,100);
		
		String message = "Press Enter to Contine";
		int startXPos = message.length() / 2;
		screen.putString((-startXPos + Level.widthFactor + Level.xOffset), (Level.levelHeight - (-14 + Level.heightFactor + Level.yOffset +1)),message,Terminal.Color.WHITE, Terminal.Color.BLACK);
		screen.refresh();
		boolean flag = true;
		while (flag) { 
			flag = enterToContinue();
		}
		//Clears the screen buffer
		screen.clear();
		screen.refresh();
		screenSize = screen.getTerminalSize();	   
		++currentLevel;	
	}
	
	/***************************************************************************************************************
	* Method      : entryScreen()
	*
	* Purpose     : The titleScreen method draws the title screen 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void entryScreen() {
		screenSize = screen.getTerminalSize();
		String message = "Resize your screen then press Enter to contine";
		int startXPos = Game.screenSize.getColumns() / 2 - message.length() / 2;
		Game.screen.putString(startXPos,15, message, Terminal.Color.WHITE, Terminal.Color.BLACK);
		screen.refresh();
		boolean flag = true;
		while (flag) { 
			flag = enterToContinue();
		}
		//Clears the screen buffer
		screen.clear();
		screen.refresh();	   
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
		
		String playerHeartString = Seed.HEART.ID;
		String pixieHeartString = Seed.HEART.ID;
		String playerString = players.get(selectedPlayer).getSymbol().ID + " Player:" + "   " + players.get(selectedPlayer).getMaxHealth() + "/" + players.get(selectedPlayer).getCurrentHealth();
		String pixieString = pixies.get(selectedPixie).getSymbol().ID + " Pixie:" + "   " + pixies.get(selectedPixie).getMaxHealth() + "/" + pixies.get(selectedPixie).getCurrentHealth() + "  " + "#[" + pixies.get(selectedPixie).getSymbol().ID + " " + pixies.get(selectedPixie).getSpirit().text + "]+";
		String goalString = "Goal: " + Seed.NEA.ID;
		String gemString = "Gems: " + players.get(selectedPlayer).getGems();
		String levelString = "Level: " + currentLevel;
		String playerCoordString =  "Player: " + players.get(selectedPlayer).coord.getX() + "," + players.get(selectedPlayer).coord.getY();
		String pixieCoordString =  "Pixie: " + pixies.get(selectedPixie).coord.getX() + "," + pixies.get(selectedPixie).coord.getY();

		Game.screen.putString(3, 0, playerString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(13, 0, playerHeartString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		Game.screen.putString(22, 0, pixieString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(31, 0, pixieHeartString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		Game.screen.putString(80, 0, gemString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(90, 0, goalString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(3, 29, levelString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(50, 0, playerCoordString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		Game.screen.putString(65, 0, pixieCoordString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		
		screen.refresh();
	}// end paintInterface
	
	/***************************************************************************************************************
	* Method      : placePlayer(int x, int y)
	*
	* Purpose     : Place a player at the specified x,y coordinates in the specified direction
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void placePlayer(int x, int y) {
		players.get(selectedPlayer).coord.setX(x);
		players.get(selectedPlayer).coord.setY(y);
		players.get(selectedPlayer).setLanternRadius(0);
		level.get(currentLevel).newPlayer(players.get(selectedPlayer).coord.getX(),players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getForeColor(), players.get(selectedPlayer).getBackColor(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());	
	}
	
	/***************************************************************************************************************
	* Method      : placePixie(int x, int y, Direction dir)
	*
	* Purpose     : Place a pixie at the specified coordinates in the specified direction
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void placePixie(int x, int y, Direction dir) {
		pixies.get(selectedPixie).coord.setX(x);
		pixies.get(selectedPixie).coord.setY(y);
		pixies.get(selectedPixie).setDirection(dir);
		level.get(currentLevel).newPixie(pixies.get(selectedPixie).coord.getX(),pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getForeColor(), pixies.get(selectedPixie).getBackColor(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());	
	}
	
	/***************************************************************************************************************
	* Method      : playerLanternOn()
	*
	* Purpose     : Turns player lantern on
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void playerLanternOn() {
		int tempRadius = players.get(selectedPlayer).lanternRadiusBuffer;
		players.get(selectedPlayer).lanternRadiusBuffer = players.get(selectedPlayer).getLanternRadius();
		players.get(selectedPlayer).setLanternRadius(tempRadius);
		level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : playerLanternOff()
	*
	* Purpose     : Turns player lantern off
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void playerLanternOff() {		
		level.get(currentLevel).blackLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		players.get(selectedPlayer).setLanternRadius(0);
		level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : pixeLanternOn()
	*
	* Purpose     : Turns pixieslantern on
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void pixieLanternOn() {
		int tempRadius = pixies.get(selectedPixie).lanternRadiusBuffer;
		pixies.get(selectedPixie).lanternRadiusBuffer = pixies.get(selectedPixie).getLanternRadius();
		pixies.get(selectedPixie).setLanternRadius(tempRadius);
		level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),pixies.get(selectedPixie).getLanternRadius());
		screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : pixieLanternOff()
	*
	* Purpose     : Turns pixie lantern off
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void pixieLanternOff() {		
		level.get(currentLevel).blackLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),pixies.get(selectedPixie).getLanternRadius());
		pixies.get(selectedPixie).setLanternRadius(0);
		level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),pixies.get(selectedPixie).getLanternRadius());
		screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : lightLevel()
	*
	* Purpose     : Illuminates the entire level [for playtesting]
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void lightLevel() {
		lantern = Lantern.FULL;
		players.get(selectedPlayer).setLanternRadius(0);
		pixies.get(selectedPixie).setLanternRadius(0);
		level.get(currentLevel).lightLevel();
	}
	
	/***************************************************************************************************************
	* Method      : setPlayerLantern()
	*
	* Purpose     : Sets the size of the player lantern
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void setPlayerLantern(int radius) {
		level.get(currentLevel).blackLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		players.get(selectedPlayer).setLanternRadius(radius);
		level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		screen.refresh();	
	}

	/***************************************************************************************************************
	* Method      : setPixieLantern()
	*
	* Purpose     : Place a pixie at the specified coordinates in the specified direction
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void setPixieLantern() {
		
	}
	
	/***************************************************************************************************************
	* Method      : movePlayer()
	*
	* Purpose     : Move player a number of cells in a specified direction pausing between each movement
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void movePlayer(Direction dir, int num, int pauseTime) {
		for (int i=0; i<num; i++) {
			commandMoveCardinal(dir);
			pause(pauseTime);
		}
	}
	
	/***************************************************************************************************************
	* Method      : movePixie()
	*
	* Purpose     : Move pixie a number of cells in a specified direction pausing between each movement
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void movePixie(Direction dir, int num, int pauseTime) {
		for (int i=0; i<num; i++) {
			commandMoveFacing();
			pause(pauseTime);
		}
	}
	
	/***************************************************************************************************************
	* Method      : movePixie()
	*
	* Purpose     : Move pixie a number of cells in a specified direction pausing between each movement
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void movePixie(Direction dir, int num) {
		for (int i=0; i<num; i++) {
			commandMoveCardinal(dir);
			try {
				Thread.sleep(pixies.get(selectedPixie).getMoveRate()+500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : turnPlayer()
	*
	* Purpose     : Turns the player a number of 90 degree rotations in the direction specified "cw" or "ccw"
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void turnPlayer(String dir, int num, int pauseTime) {
		for (int i=0; i<num; i++) {
			if (dir == "cw") {
				Direction newDir = turnRight(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(newDir);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   			
				pause(pauseTime);
			}
			else {
				Direction newDir = turnLeft(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(newDir);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   	
				pause(pauseTime);
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : turnPixie()
	*
	* Purpose     : Turns the pixie a number of 90 degree rotations in the direction specified "cw" or "ccw"
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void turnPixie(String dir, int num, int pauseTime) {
		for (int i=0; i<num; i++) {
			if (dir == "cw") {
				Direction newDir = turnRight(pixies.get(selectedPixie).getDirection());
				pixies.get(selectedPixie).setDirection(newDir);
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
				pause(pauseTime);
			}
			else {
				Direction newDir = turnLeft(pixies.get(selectedPixie).getDirection());
				players.get(selectedPixie).setDirection(newDir);
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   	
				pause(pauseTime);
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : putCharacter()
	*
	* Purpose     : Puts a string character at specified coordinate then pauses for a specified time
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void putCharacter(String text, int x, int y, int pauseTime) {
		Terminal.Color foreColor;
		Terminal.Color backColor;
		foreColor = Rating.WHITE.color;
		backColor = Rating.BLACK.color;
		Game.screen.putString((x + Level.widthFactor + Level.xOffset), (Level.levelHeight - (y + Level.heightFactor + Level.yOffset)), text, foreColor, backColor);
		screen.refresh();
		pause(pauseTime);
	}
	
	/***************************************************************************************************************
	* Method      : putUnicode()
	*
	* Purpose     : Puts a Seed character at the specified coordinate then pauses for a specified time
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public static void putUnicode(Seed symbol, int x, int y, int pauseTime) {
		Terminal.Color foreColor;
		Terminal.Color backColor;
		foreColor = Rating.WHITE.color;
		backColor = Rating.BLACK.color;
		Game.screen.putString((x + Level.widthFactor + Level.xOffset), (Level.levelHeight - (y + Level.heightFactor + Level.yOffset)), symbol.ID, foreColor, backColor);
		screen.refresh();
		pause(pauseTime);
	}
	
	/***************************************************************************************************************
	* Method      : putUnicodeTrail()
	*
	* Purpose     : Draws Seed character at the specified coordinate x times, then pausing between each character
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public static void putUnicodeTrail(Seed symbol, Direction dir, int num, int x, int y, int pauseTime) {
		for (int i=0; i<num; ++i) {
			switch (dir) {
			case NORTH:
				++y;
				break;
			case SOUTH:
				--y;
				break;
			case EAST:
				++x;
				break;
			case WEST:
				--x;
				break;
			}
			putUnicode(symbol, x , y, pauseTime);
		}
	}
	
	public void playerEmote (String emote, Direction dir, int pauseTime) {
		level.get(currentLevel).emote(emote,players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),dir,pauseTime);	
	}
	
	public void pixieEmote (String emote, Direction dir, int pauseTime) {
		level.get(currentLevel).emote(emote,pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),dir,pauseTime);	
	}
	
	public void otherEmote (String emote, int x, int y, Direction dir, int pauseTime) {
		level.get(currentLevel).emote(emote,x,y,dir,pauseTime);		
	}
	
	/***************************************************************************************************************
	* Method      : pause()
	*
	* Purpose     : Pauses the number of milliseconds as specified in the parameter pauseTime
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public static void pause(int pauseTime) {
		try {
			Thread.sleep(pauseTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}	
	}
	
	/***************************************************************************************************************
	* Method      : enterToContinue()
	*
	* Purpose     : Pauses play until enter is pressed
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public static boolean enterToContinue() {
		Key key = terminal.readInput();
		if (key != null) {

			if (Game.log) System.out.println(key);
       		//System.out.println(key.getCharacter());
       		
       		if (key.getKind() == Key.Kind.Enter) {
       			return false;
       		}
		} // end if
		return true;
	}
	
	public void playerControl() {
		playerControl = true;
		disableCooldown = false;
		players.get(selectedPlayer).setDirection(pixies.get(selectedPixie).getDirection());
		pixies.get(selectedPixie).setDirection(Direction.NONE);
		level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(),players.get(selectedPlayer).getLanternRadius());   	
		level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   		
	}
	
	/***************************************************************************************************************
	* Method      : level1()
	*
	* Purpose     : Scripted event for 1st Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level1() {
		
		//DON'T TOUCH THIS CODE TO NEXT COMMENT BELOW
		level.add(new Level());
		level.get(currentLevel).newLevel1(); 
		playerControl = false;
		disableCooldown = true;
		players.add(new Player(0,0,Direction.NONE));
		pixies.add(new Pixie(0,1, Direction.EAST, Spirit.DIG));
		//DON'T TOUCH THIS CODE TO NEXT COMMENT ABOVE

		//lightLevel(); //ONLY USE THIS FOR DEBUGGING PURPOSES TO ILLUMINATE LEVEL
		pause(5000);
		placePlayer(-14,0);
		playerEmote("Where is that match?!",Direction.SW, 3000);
		pause(3000);
		playerLanternOn();
		pause(1000);
		playerEmote("That's more like it!",Direction.NE, 3000);
		pause(3000);
		placePixie(6,0,Direction.WEST);
		pixieLanternOff();
		pause(1000);
		movePlayer(Direction.EAST,9,500);
		pause(1000);
		movePlayer(Direction.NORTH,2,500);
		pause(1000);
		movePlayer(Direction.EAST,5,500);
		pause(1000);
		movePlayer(Direction.SOUTH,2,500);
		pause(1000);
		movePixie(Direction.WEST,3,500);
		pixieLanternOn();
		movePixie(Direction.WEST,2,500);
		pause(1000);
		pixieEmote("...",Direction.NE, 2000);
		pause(1000);
		playerEmote("Hello my little friend",Direction.NW, 3000);
		pause(1000);
		turnPixie("cw",2,250);
		pause(1000);
		movePixie(Direction.EAST,11,500);
		pause(1000);
		commandDig();
		pause(1000);
		otherEmote("Moo!",13,0, Direction.SE, 3000);
		pause(1000);
		turnPixie("cw",2,250);
		pixieEmote("...",Direction.SW, 2000);
		pause(1000);
		movePlayer(Direction.EAST,11,500);
		playerEmote("O_O...Lunch!",Direction.SW, 3000);
		sendMessage("Welcome to RogueRunner", Priority.LOW, 0);
		disableCooldown = false;
	}
	
	public void level2() {	
		players.add(new Player(-1,0, Direction.EAST));
		pixies.add(new Pixie(7,0, Direction.NONE, Spirit.DIG));
		level.add(new Level());
		level.get(currentLevel).newLevel2();

		level.get(currentLevel).newPlayer(players.get(selectedPlayer).coord.getX(),players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getForeColor(), players.get(selectedPlayer).getBackColor(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());
		level.get(currentLevel).newPixie(pixies.get(selectedPixie).coord.getX(),pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getForeColor(), pixies.get(selectedPixie).getBackColor(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());	
		sendMessage("Welcome to RogueRunner", Priority.LOW, 0);
	}

	
}// end Game