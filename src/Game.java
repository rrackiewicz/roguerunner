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
	
	private GameState currentState = GameState.OPENING; 
	public static boolean playerControl = false;
	public boolean disableCooldown = true;
 
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
		System.out.println("Left is " + Level.leftScreen);
		System.out.println("Right is " + Level.rightScreen);
		System.out.println("Top is " + Level.topScreen);
		System.out.println("Bottom is " + Level.bottomScreen);
		System.out.println("Width of level is " + Level.levelWidth);
		System.out.println("Height of level is " + Level.levelHeight);
		while (currentState == GameState.PLAYING) {
			Key key = terminal.readInput();
			if (key != null) {

				if (Game.log) System.out.println(key);
	       		//System.out.println(key.getCharacter());
	       		
	       		if (key.getKind() == Key.Kind.Escape) {
	       			System.exit(0);
	       		}
	       		
	       		
	       		if (!playerControl) {
		       		if (key.getKind() == Key.Kind.ArrowUp) {
		       			if (!playerControl) {
		       				commandMoveFacing();
		       			}
			       		continue;
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
			       		continue;
		       		}
		       		
		       		if (key.getKind() == Key.Kind.ArrowLeft) {
	   					pixies.get(selectedPixie).setDirection(turnLeft(pixies.get(selectedPixie).getDirection()));
	   					level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
	   					continue;	
		       		}
		       		
		       		if (key.getKind() == Key.Kind.ArrowRight) {
	   					pixies.get(selectedPixie).setDirection(turnRight(pixies.get(selectedPixie).getDirection()));
	   					level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
			       		continue;
		       		}
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
		   			case "e":
		   				if (playerControl) {
		   					level.get(currentLevel).emote("Abandon hope all ye that enter!", players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), Direction.NONE, 1000);
		   				}
		   				else {
		   					level.get(currentLevel).emote("I'M A PIXIE", pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), Direction.SW, 1000);
				   				
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
		titleScreen();
		scriptedEvent();

		players.add(new Player(-1,0, Direction.EAST));
		pixies.add(new Pixie(7,0, Direction.NONE, Spirit.DIG));
		pixies.get(selectedPixie).setSpirit(Spirit.DIG);
		level.add(new Level());
		level.get(currentLevel).newLevel();
		level.get(currentLevel).calcLevel();
		//Add player to map
		level.get(currentLevel).newPlayer(players.get(selectedPlayer).coord.getX(),players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getForeColor(), players.get(selectedPlayer).getBackColor(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());
		//Add pixie to map
		level.get(currentLevel).newPixie(pixies.get(selectedPixie).coord.getX(),pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getForeColor(), pixies.get(selectedPixie).getBackColor(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());
		paintInterface();
		sendMessage("Welcome to RogueRunner", Priority.HIGH, 0);
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
			if (!isBlocked && players.get(selectedPlayer).moveCooldown) {
				level.get(currentLevel).restoreTile(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getLanternRadius());
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
			if (!isBlocked && pixies.get(selectedPixie).moveCooldown) {
				if (isOffScreen(pixies.get(selectedPixie).getDirection())) {
					System.out.println("Edge of map");
					//panMap();
					isBlocked = true;
				}
				level.get(currentLevel).restoreTile(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection(), pixies.get(selectedPixie).getLanternRadius());
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
	* Method      : moveCardinal()
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
		if (!isBlocked && (players.get(selectedPlayer).moveCooldown || disableCooldown)) {		
			if (playerControl) {
				level.get(currentLevel).restoreTile(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getLanternRadius());
			}
			else { //Since player isn't facing a direction just pass the direction they are moving
				level.get(currentLevel).restoreTile(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir, players.get(selectedPlayer).getLanternRadius());
			}
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
				if (players.get(selectedPlayer).coord.getX() >= Level.rightScreen-3) {
					return true;
				}
				break;
			case SOUTH:
				if (players.get(selectedPlayer).coord.getY() <= Level.bottomScreen+4) {
					return true;
				}
				break;
			case WEST:
				if (players.get(selectedPlayer).coord.getX() <= Level.leftScreen+3) {
					return true;
				}
				break;
			case NORTH:
				if (players.get(selectedPlayer).coord.getY() >= Level.topScreen-6) {
					return true;
				}
				break;
			}
			return false;
		}
		else {
			switch(dir) {
			case EAST:
				if (pixies.get(selectedPixie).coord.getX() >= Level.rightScreen-3) {
					return true;
				}
				break;
			case SOUTH:
				if (pixies.get(selectedPixie).coord.getY() <= Level.bottomScreen+4) {
					return true;
				}
				break;
			case WEST:
				if (pixies.get(selectedPixie).coord.getX() <= Level.leftScreen+3) {
					return true;
				}
				break;
			case NORTH:
				if (pixies.get(selectedPixie).coord.getY() >= Level.topScreen-6) {
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
				level.get(currentLevel).restoreTile(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getLanternRadius());
				//When lanterns overlap we have to restore tiles that were turned off
				level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getLanternRadius());
			
				if (canPush) {
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
				level.get(currentLevel).restoreTile(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection(), pixies.get(selectedPixie).getLanternRadius());
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
				playerControl = false;
				pixies.get(selectedPixie).setDirection(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(Direction.NONE);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());   			
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   
			
			}
			else {
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
		if (playerControl || cardinalFlag) {
			moveTimer.schedule(new resetTimer(), players.get(selectedPlayer).getMoveRate());
			players.get(selectedPlayer).moveCooldown = false;
			level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());  
		}
		else if (!playerControl && !cardinalFlag) {
			moveTimer.schedule(new resetTimer(), pixies.get(selectedPixie).getMoveRate());
			pixies.get(selectedPixie).moveCooldown = false;
			level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
		}
	}
	
	/***************************************************************************************************************
	* Class       : resetTime
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
			if (playerControl || cardinalFlag) {
				players.get(selectedPlayer).moveCooldown = true;
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
				cardinalFlag = false;
			}
			else if (!playerControl && !cardinalFlag) {
				pixies.get(selectedPixie).moveCooldown = true;
				level.get(currentLevel).updateAvatar(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
			}
		    moveTimer.cancel(); 
		}
	}
	
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

		Game.screen.putString(3, 0, playerString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(13, 0, playerHeartString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		Game.screen.putString(22, 0, pixieString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(31, 0, pixieHeartString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		Game.screen.putString(80, 0, gemString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(90, 0, goalString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(3, 29, levelString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		
		
		screen.refresh();
	}// end paintInterface
	
	public void scriptedEvent() {
		
		level.add(new Level());
		level.get(currentLevel).drawOpening();	
		playerControl = false;
		players.add(new Player(0,0, Direction.NONE));
		pixies.add(new Pixie(5,5, Direction.EAST, Spirit.DIG));
		
		//Chris comment out to your name below
		//lightLevel();
	
		placePlayer(0,0,Direction.NONE);
		playerLanternOff();
		pause(5000);
		playerLanternOn();
		pause(1000);
		placePixie(5,5);
		pause(1000);
		movePlayer(Direction.EAST, 5, 250);
		pause(1000);
		//turnPlayer("ccw",1);
		movePlayer(Direction.NORTH, 4, 250);
		pause(1000);
		level.get(currentLevel).emote("Hello my little friend", players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), Direction.NE, 2000);
		pause(1000);
		level.get(currentLevel).emote("...", pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), Direction.NW, 2000);
		pause(1000);
		turnPlayer("cw",2);
		pause(1000);
		movePlayer(Direction.SOUTH, 7, 250);
		
		//Chris
		screen.refresh();
		
		//level.get(currentLevel).emote("Hello my little friend", players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(), Direction.NW, 5000);
		while (currentState == GameState.OPENING) {

		} // end while
		currentLevel = 1;
		disableCooldown = false;
	}

	public void placePlayer(int x, int y, Direction dir) {
		players.get(selectedPlayer).coord.setX(x);
		players.get(selectedPlayer).coord.setY(y);
		players.get(selectedPlayer).setDirection(dir);
		level.get(currentLevel).bufferCell(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY());
		level.get(currentLevel).newPlayer(players.get(selectedPlayer).coord.getX(),players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getForeColor(), players.get(selectedPlayer).getBackColor(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());	
	}
	
	public void placePixie(int x, int y) {
		pixies.get(selectedPixie).coord.setX(x);
		pixies.get(selectedPixie).coord.setY(y);
		level.get(currentLevel).bufferCell(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY());
		level.get(currentLevel).newPixie(pixies.get(selectedPixie).coord.getX(),pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getForeColor(), pixies.get(selectedPixie).getBackColor(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());	
	}
	
	public void playerLanternOn() {
		int tempRadius = players.get(selectedPlayer).lanternRadiusBuffer;
		players.get(selectedPlayer).lanternRadiusBuffer = players.get(selectedPlayer).getLanternRadius();
		players.get(selectedPlayer).setLanternRadius(tempRadius);
		level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		screen.refresh();
	}
	
	public void playerLanternOff() {		
		level.get(currentLevel).blackLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		players.get(selectedPlayer).setLanternRadius(0);
		level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		screen.refresh();
	}
	public void lightLevel() {
		lantern = Lantern.FULL;
		players.get(selectedPlayer).setLanternRadius(0);
		pixies.get(selectedPixie).setLanternRadius(0);
		level.get(currentLevel).lightLevel();
	}
	
	public void setPlayerLantern(int radius) {
		level.get(currentLevel).blackLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		players.get(selectedPlayer).setLanternRadius(radius);
		level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),players.get(selectedPlayer).getLanternRadius());
		screen.refresh();	
	}
	
	//create a method to progressively increment and decrement a lantern
	//create a method to dig in a direciton
	//add a timer into turn
	
	public void setPixieLantern() {
		
	}
	
	public void movePlayer(Direction dir, int num, int pauseTime) {
		for (int i=0; i<num; i++) {
			commandMoveCardinal(dir);
			pause(pauseTime);
		}
	}
	
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
	
	public void turnPlayer(String dir, int num) {
		for (int i=0; i<num; i++) {
			if (dir == "cw") {
				Direction newDir = turnRight(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(newDir);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
						
			}
			else {
				Direction newDir = turnLeft(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(newDir);
				level.get(currentLevel).updateAvatar(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   	
			}
		}
	}
	
	public void putCharacter(String text, int x, int y, int pauseTime) {
		Terminal.Color foreColor;
		Terminal.Color backColor;
		foreColor = Rating.WHITE.color;
		backColor = Rating.BLACK.color;
		Game.screen.putString((x + Level.widthFactor + Level.xOffset), (Level.levelHeight - (y + Level.heightFactor + Level.yOffset)), text, foreColor, backColor);
		screen.refresh();
		pause(pauseTime);
	}
	
	public void putUnicode(Seed symbol, int x, int y, int pauseTime) {
		Terminal.Color foreColor;
		Terminal.Color backColor;
		foreColor = Rating.WHITE.color;
		backColor = Rating.BLACK.color;
		Game.screen.putString((x + Level.widthFactor + Level.xOffset), (Level.levelHeight - (y + Level.heightFactor + Level.yOffset)), symbol.ID, foreColor, backColor);
		screen.refresh();
		pause(pauseTime);
	}
	
	public void putUnicodeTrail(Seed symbol, Direction dir, int num, int x, int y, int pauseTime) {
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
	
	public static void pause(int pauseTime) {
		try {
			Thread.sleep(pauseTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}	
	}
	
	public static boolean enterToContinue() {
		Key key = terminal.readInput();
		if (key != null) {

			if (Game.log) System.out.println(key);
       		System.out.println(key.getCharacter());
       		
       		if (key.getKind() == Key.Kind.Enter) {
       			return false;
       		}
		} // end if
		return true;
	}
	
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
	
}// end Game