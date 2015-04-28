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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import java.util.*;
import java.nio.charset.Charset;

public class Game {  	
	public List<Level> level = new ArrayList<Level>(1); 
	public int currentLevel; 
	public List<Player> players = new ArrayList<Player>(1);
	public int selectedPlayer;
	public List<Pixie> pixies = new ArrayList<Pixie>(1);
	public int selectedPixie;	
	public List<Enemy> enemies = new ArrayList<Enemy>(1);
	public int selectedEnemy;
	public List<Quest> quests = new ArrayList<Quest>(1);
	public int selectedQuest;
	public static Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
	public static Screen screen = new Screen(terminal);
	public static TerminalSize screenSize;
	public static boolean log = true;
	public static Lantern lantern = Lantern.CONE;
	public static Spirit spirit;
	public Timer moveTimer;
	public boolean cardinalFlag;
	public boolean pixieBonded = false;
	public String activeMessage;
	public boolean pixieVisible = false;
	///////REMOVE THIS CRAP LATER/////////////
	public static int tempID;
	public static boolean isRiver = false;
	public boolean destroyEnemies = false;
	public static boolean isTrapped = false;
	public static boolean overTrigger = false;
	public int trappedTotal;
	//////////////////////////////////////////
	
	private GameState currentState; 
	public static boolean playerControl = true;
	public boolean disableCooldown = false;
 
	/***************************************************************************************************************
	* Method      : main(String[] args)
	*
	* Purpose     : Passes control of the game to the constructor of the Game Class.
	*
	* Parameters  : No command line args are currently used
	*
	* Returns     : This method does not return a value.
	 * @throws InvalidMidiDataException 
	 * @throws IOException 
	 * @throws MidiUnavailableException 
	*  
	***************************************************************************************************************/
	public static void main(String[] args) throws MidiUnavailableException, IOException, InvalidMidiDataException {	 
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
	 * @throws InvalidMidiDataException 
	 * @throws IOException 
	 * @throws MidiUnavailableException 
	*  
	***************************************************************************************************************/
	public Game() throws MidiUnavailableException, IOException, InvalidMidiDataException {
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
	       		
//	       		if (!playerControl) {
//		       		if (key.getKind() == Key.Kind.ArrowUp) {
//		       			if (!playerControl) {
//		       				commandMoveFacing();
//		       			}
//			       		//continue;
//		       		}
//		       		
//		       		if (key.getKind() == Key.Kind.ArrowDown) {
//		       			if (!playerControl) {
//		   					switch(pixies.get(selectedPixie).getSpirit()) {
//		   					case PUSH:
//		   						commandPush();
//		   						break;
//		   					case DIG:
//		   						commandDig();
//		   						break;
//		   					case DROP:
//		   						commandDrop();
//		   						break;
//		   					case ALL:
//		   						break;
//		   					}
//		   				}
//			       		//continue;
//		       		}
//		       		
//		       		if (key.getKind() == Key.Kind.ArrowLeft) {
//	   					pixies.get(selectedPixie).setDirection(turnLeft(pixies.get(selectedPixie).getDirection()));
//	   					level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
//	   					//continue;	
//		       		}
//		       		
//		       		if (key.getKind() == Key.Kind.ArrowRight) {
//	   					pixies.get(selectedPixie).setDirection(turnRight(pixies.get(selectedPixie).getDirection()));
//	   					level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
//			       		//continue;
//		       		}
		       		//continue;
//	       		}

	       		String input = String.valueOf(key.getCharacter());
	       		switch(input.toLowerCase()) {
	       			case " ":
	       				commandControlPixie();
	       				break;
	       			case "n": //cell info for debugging
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
		   					level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
		   				}
		   				else commandMoveCardinal(Direction.WEST);
						break;
		   			case "d":
		   				if (playerControl) {
		   					players.get(selectedPlayer).setDirection(turnRight(players.get(selectedPlayer).getDirection()));
		   					level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   
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
		   			case "i":
		       			if (!playerControl) {
		       				commandMoveFacing();
		       			}
				       	break;
		   			case "j":
		   				if (!playerControl) {
		   					pixies.get(selectedPixie).setDirection(turnLeft(pixies.get(selectedPixie).getDirection()));
		   					level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
		   				}
	   					break;
		   			case "l":
		   				if (!playerControl) {
		   					pixies.get(selectedPixie).setDirection(turnRight(pixies.get(selectedPixie).getDirection()));
		   					level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());  
		   				}
		   				break;
		   			case "k":
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
		       			break;
//		   			case "g":
//		   				if (log) {
//		   					log = false;
//		   					System.out.println("Log OFF");
//		   				}
//		   				else {
//		   					log = true;
//		   					System.out.println("Log ON");
//		   				}
//		   				break;
//		   			case "m":
//		   					level.get(currentLevel).emote("ZZZ...", players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), Direction.EAST, 2000, true);
//		   				break;
//		   			case "y":
//		   				clearMessage();
//		   				break;
//		   			case "u":
//		   				otherEmote(" ",0,0,Direction.LOADER,1,true);
//		   				break;
		   			default:
	       		}// end switch
			} // end if
			paintInterface();
			manageEnemies();
			//System.out.println("Disable status: " + disableCooldown + " Player Cooldown status " + players.get(selectedPlayer).getMoveCooldown());
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
	 * @throws InvalidMidiDataException 
	 * @throws IOException 
	 * @throws MidiUnavailableException 
	* 
	***************************************************************************************************************/
	public void initGame() throws MidiUnavailableException, IOException, InvalidMidiDataException {  
		screen.startScreen();
		terminal.setCursorVisible(false);
		entryScreen();
		players.add(new Player(0,0,Direction.NONE));
		pixies.add(new Pixie(0,1, Direction.EAST, Spirit.DIG));
		//titleScreen();
		level1Scene1();
		//level2Scene1();	 
		sendMessage("Welcome to RogueRunner", Priority.HIGH, 5000);
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
			boolean isTrigger = level.get(currentLevel).manageTrigger(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
			
			if (isOffScreen(players.get(selectedPlayer).getDirection(),EntityType.PLAYER)) {
				System.out.println("Edge of map");
			}
			
			if (!isBlocked && players.get(selectedPlayer).getMoveCooldown()) {
				level.get(currentLevel).moveEntity(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius(), EntityType.PLAYER);		
				level.get(currentLevel).avatarFollow(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());
				pixies.get(selectedPixie).coord.setX(players.get(selectedPlayer).coord.getX());
				pixies.get(selectedPixie).coord.setY(players.get(selectedPlayer).coord.getY());
				for (int j=0; j<=enemies.size()-1; ++j) {
					level.get(currentLevel).updateLantern(enemies.get(j).coord.getX(), enemies.get(j).coord.getY(),enemies.get(j).getLanternRadius());
				}
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
					moveTimer(EntityType.PLAYER);
				}
				
				if (isTrigger) {
					triggerDispatcher();
				}

			}
		}// end if
		else {
			boolean isBlocked = level.get(currentLevel).detectCollision(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
			if(isRiver) isBlocked = false;
			if (!isBlocked && pixies.get(selectedPixie).getMoveCooldown()) {
				if (isOffScreen(pixies.get(selectedPixie).getDirection(), EntityType.PIXIE)) {
					System.out.println("Edge of map");
					//panMap();
					isBlocked = true;
				}
				level.get(currentLevel).moveEntity(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius(), EntityType.PIXIE);
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
					moveTimer(EntityType.PIXIE);
				}
				isRiver = false;
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : moveCardinal
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
		//boolean isResource = level.get(currentLevel).manageResource(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir);
		boolean isTrigger = level.get(currentLevel).manageTrigger(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir);
		Coord coords = level.get(currentLevel).manageFireflies(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir);
		
//		if (isResource) {
//			boolean updateLantern = players.get(selectedPlayer).incMaxHealth(1);
//			if (updateLantern) {
//				level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getLanternRadius());
//				sendMessage("+1 Health, +1 Lantern Radius", Priority.LOW, 1500);
//			}
//			else {
//				sendMessage("+1 Health", Priority.LOW, 1500);
//			}
//		}
		
		if (!(coords.getX() == 0 && coords.getY() == 0)){
			for (int i=0; i<=enemies.size()-1; ++i) {
				if (enemies.get(i).coord.getX() == coords.getX() && enemies.get(i).coord.getY() == coords.getY()) {
					eatFirefly(i);
				}
			}
		}
		
		if (isOffScreen(players.get(selectedPlayer).getDirection(), EntityType.PLAYER)) {
			System.out.println("Edge of map");
			//panMap();
			isBlocked = true;
		}
		
		if (!isBlocked && (players.get(selectedPlayer).getMoveCooldown() || disableCooldown)) {	
			//Since player isn't facing a direction just pass the direction they are moving
			level.get(currentLevel).moveEntity(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), dir, players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius(), EntityType.PLAYER);
			//When lanterns overlap we have to restore tiles that were turned off
			if (pixieVisible) level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getLanternRadius());
			for (int j=0; j<=enemies.size()-1; ++j) {
				level.get(currentLevel).updateLantern(enemies.get(j).coord.getX(), enemies.get(j).coord.getY(),enemies.get(j).getLanternRadius());
			}
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
				moveTimer(EntityType.PLAYER);
			}
			
			if (isTrigger) {
				triggerDispatcher();
			}
			isRiver=false;

		} // end if	
	}
	
	/***************************************************************************************************************
	* Method      : ifOffSceen
	*
	* Purpose     : Determines if the player is about to move offscreen
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public boolean isOffScreen(Direction dir, EntityType entity) {
		if (entity == EntityType.PLAYER) {
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
		else if (entity == EntityType.PIXIE) {
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
		else if (entity == EntityType.ENEMY) {
			switch(dir) {
			case EAST:
				if (enemies.get(selectedEnemy).coord.getX() >= Level.rightScreen - 1) {
					return true;
				}
				break;
			case SOUTH:
				if (enemies.get(selectedEnemy).coord.getY() <= Level.bottomScreen + 1) {
					return true;
				}
				break;
			case WEST:
				if (enemies.get(selectedEnemy).coord.getX() <= Level.leftScreen + 1) {
					return true;
				}
				break;
			case NORTH:
				if (enemies.get(selectedEnemy).coord.getY() >= Level.topScreen - 1) {
					return true;
				}
				break;
			}
			return false;
		}
		return false;
	}
	
	/***************************************************************************************************************
	* Method      : eatFirefly()
	*
	* Purpose     : Dispatcher for catching flies. REFACTOR LATER.
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void eatFirefly(int index) {
		enemies.remove(index);
		boolean updateLantern = players.get(selectedPlayer).incMaxHealth(1);
		if (updateLantern) {
			level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getLanternRadius());
			sendMessage("+1 Health, +1 Lantern Radius", Priority.LOW, 1500);
		}
		else {
			sendMessage("+1 Health", Priority.LOW, 1500);
		}
		quests.get(selectedQuest).incCurrentValue();
		sendMessage(quests.get(selectedQuest).getStatus(), Priority.LOW, 1500);
		if (quests.get(selectedQuest).getCurrentValue() == 1) {
			otherEmote("Fireflies increase your health.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("The healthier  you are, the farther you can see.", 0,-10, Direction.NONE, 5000, true);
		}
		checkQuestCompletion();
	}
	
	/***************************************************************************************************************
	* Method      : commandPush
	*
	* Purpose     : Facilitates the push pixie action
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void commandPush() {
		if (pixies.get(selectedPixie).getSpirit() == Spirit.ALL || pixies.get(selectedPixie).getSpirit() == Spirit.PUSH) {
			if (playerControl && players.get(selectedPlayer).getMoveCooldown()) {
				boolean canPush = level.get(currentLevel).pushBlock(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
				boolean isTrigger = level.get(currentLevel).manageTrigger(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());
				//crapola
				if (isRiver) canPush = true;
				if (canPush) {
					level.get(currentLevel).moveEntity(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius(), EntityType.PLAYER);
					level.get(currentLevel).avatarFollow(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());
					pixies.get(selectedPixie).coord.setX(players.get(selectedPlayer).coord.getX());
					pixies.get(selectedPixie).coord.setY(players.get(selectedPlayer).coord.getY());
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
				
				if (!disableCooldown) {
					moveTimer(EntityType.PLAYER);
				}
				
				if (isTrigger) {
					triggerDispatcher();
				}
				
				if(overTrigger) {
					level2Scene4();
				}
				isRiver = false;
			}//end if
			else if (!playerControl && players.get(selectedPlayer).getMoveCooldown()){
				boolean canPush = level.get(currentLevel).pushBlock(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection());
				boolean isTrigger = level.get(currentLevel).manageTrigger(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getDirection());				
				//crapola
				if (isRiver) canPush = true;
				if (canPush) {
					level.get(currentLevel).moveEntity(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getDirection(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius(), EntityType.PIXIE);
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
							pixies.get(selectedPixie).coord.incY();;
							break;
		   			}//end switch 		
   				}//end if
				
				if (!disableCooldown) {
					moveTimer(EntityType.PLAYER);
				}
				
				if (isTrigger) {
					triggerDispatcher();
				}
				isRiver = false;
			}
		}		
	}
	
	/***************************************************************************************************************
	* Method      : commandDig
	*
	* Purpose     : Facilitates the dig pixie action
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
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
	
	/***************************************************************************************************************
	* Method      : commandDrop
	*
	* Purpose     : Facilitates the drop pixie action
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
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

	/***************************************************************************************************************
	* Method      : commandControlPixie
	*
	* Purpose     : Switches control between player and pixie
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void commandControlPixie() { {
		if (playerControl) {
				playerControl = false;
				pixies.get(selectedPixie).setDirection(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(Direction.NONE);
				level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());   			
				level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   
			}
			else {
				playerControl = true;
				players.get(selectedPlayer).setDirection(pixies.get(selectedPixie).getDirection());
				pixies.get(selectedPixie).setDirection(Direction.NONE);
				level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(),players.get(selectedPlayer).getLanternRadius());   	
				level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   		
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : checkQuestCompletion()
	*
	* Purpose     : Checks to see if you have completed the current quest
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void checkQuestCompletion(){
		if (quests.get(selectedQuest).getCurrentValue() == quests.get(selectedQuest).getGoal()) {
			otherEmote("QUEST COMPLETE: " + quests.get(selectedQuest).getDescription() , 0,-10, Direction.NONE, 3000, true);
			if(trappedTotal == 3) {
				level2Scene3();
			}
			quests.remove(selectedQuest);
		}
	}
	
	/***************************************************************************************************************
	* Method      : sendMessage
	*
	* Purpose     : A method for displaying messages at the bottom of the UI
	*
	* Parameters  : String message - the message to display
	* 			  : Priority priority - an enum of priorities
	* 			  : int duration - the duration of the message
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void sendMessage(String message, Priority priority, int duration) {
		for (int i=0; i<screen.getTerminalSize().getColumns(); ++i) {
			Game.screen.putString(i, 29, " ", Terminal.Color.WHITE, Terminal.Color.WHITE);
		}
		int startXPos = Game.screenSize.getColumns() / 2 - message.length() / 2;
		Game.screen.putString(startXPos, 29, message, priority.color, Terminal.Color.WHITE);
		screen.refresh();
		messageTimer(duration);
	}
	
	/***************************************************************************************************************
	* Method      : clearMessage
	*
	* Purpose     : A method to clear the message at the bottom of the UI
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void clearMessage() {
		for (int i=0; i<screen.getTerminalSize().getColumns(); ++i) {
			Game.screen.putString(i, 29, " ", Terminal.Color.WHITE, Terminal.Color.WHITE);
		}
		screen.refresh();
		paintInterface();
	}
	
	/***************************************************************************************************************
	* Method      : spawnTimer()
	*
	* Purpose     : Creates a new message timer 
	*
	* Parameters  : int duration - time until message disappears
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void spawnTimer(int duration, int ID) {
		Timer messageTimer = new Timer();
		messageTimer.schedule(new spawnEvent(ID), duration);
	}
		
	/***************************************************************************************************************
	* Class       : spawnEvent
	*
	* Purpose     : Destroys the message event timer when it has expired
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	class spawnEvent extends TimerTask {
		int ID;
		public spawnEvent(int ID) {
			this.ID = ID;
		}
		public void run() {
			spawnDispatcher(ID); 
		}
	}
	
	/***************************************************************************************************************
	* Method      : messageTimer()
	*
	* Purpose     : Creates a new message timer 
	*
	* Parameters  : int duration - time until message disappears
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void messageTimer(int duration) {
		Timer messageTimer = new Timer();
		messageTimer.schedule(new messageEvent(), duration);
	}
		
	/***************************************************************************************************************
	* Class       : messageEvent
	*
	* Purpose     : Destroys the message event timer when it has expired
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	class messageEvent extends TimerTask {
		public void run() {
			clearMessage();
		    //messageTimer.cancel(); 
		}
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
	public void moveTimer(EntityType type) {
		moveTimer = new Timer();
		if (type == EntityType.PLAYER && !cardinalFlag) {
			moveTimer.schedule(new resetMoveTimer(type, selectedPlayer), players.get(selectedPlayer).getMoveRate());
			players.get(selectedPlayer).setMoveCooldown(false);
			level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());  
		}
		else if (type == EntityType.PIXIE && !cardinalFlag) {
			moveTimer.schedule(new resetMoveTimer(type, selectedPixie), pixies.get(selectedPixie).getMoveRate());
			pixies.get(selectedPixie).setMoveCooldown(false);
			level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
		}
		else if (type == EntityType.ENEMY) {
			moveTimer.schedule(new resetMoveTimer(type, selectedEnemy), enemies.get(selectedEnemy).getMoveRate());
			enemies.get(selectedEnemy).setMoveCooldown(false);
			level.get(currentLevel).updateSymbol(enemies.get(selectedEnemy).coord.getX(), enemies.get(selectedEnemy).coord.getY(), enemies.get(selectedEnemy).getSymbol());   
		}
		else { 
			moveTimer.schedule(new resetMoveTimer(type, selectedPlayer), players.get(selectedPlayer).getMoveRate());
			players.get(selectedPlayer).setMoveCooldown(false);
			level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());  
		}	
	}
	
	/***************************************************************************************************************
	* Class       : resetMoveTimer
	*
	* Purpose     : Destroys the player movement timer when it has expired
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	class resetMoveTimer extends TimerTask {
		public EntityType type;
		public int thisEntity;
		public resetMoveTimer(EntityType type, int thisEntity) {
			this.type = type;
			this.thisEntity = thisEntity;
		}
		public void run() {
			if (type == EntityType.PLAYER && !cardinalFlag) {
				players.get(thisEntity).setMoveCooldown(true);
				level.get(currentLevel).updateSymbol(players.get(thisEntity).coord.getX(), players.get(thisEntity).coord.getY(), players.get(thisEntity).getSymbol());   
			}
			else if (type == EntityType.PIXIE && !cardinalFlag) {
				pixies.get(thisEntity).setMoveCooldown(true);
				level.get(currentLevel).updateSymbol(pixies.get(thisEntity).coord.getX(), pixies.get(thisEntity).coord.getY(), pixies.get(thisEntity).getSymbol());   
			}
			else if (type == EntityType.ENEMY) {
				enemies.get(thisEntity).setMoveCooldown(true);
				enemies.get(thisEntity).toggleBlinkState();
				level.get(currentLevel).updateSymbol(enemies.get(thisEntity).coord.getX(), enemies.get(thisEntity).coord.getY(), enemies.get(thisEntity).getSymbol());   
			}
			else {
				players.get(thisEntity).setMoveCooldown(true);
				level.get(currentLevel).updateSymbol(players.get(thisEntity).coord.getX(), players.get(thisEntity).coord.getY(), players.get(thisEntity).getSymbol());   
				cardinalFlag = false;	
			}
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
	 * @throws MidiUnavailableException 
	 * @throws InvalidMidiDataException 
	 * @throws IOException 
	*  
	***************************************************************************************************************/
	public void titleScreen() throws MidiUnavailableException, IOException, InvalidMidiDataException {
		level.add(new Level());
		level.get(currentLevel).newLevel0();
		pause(3000);
		Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        InputStream is = new BufferedInputStream(new FileInputStream(new File("FromHere.mid")));
        sequencer.setSequence(is);
        
        sequencer.start();
		otherEmote("You awake as if from a dream...",0,0, Direction.NONE, 3000, false);
		pause(1000);
		otherEmote("Or is it a dream?",0,0, Direction.NONE, 3000, false);
		pause(3000);
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
		pause(1000);
		putCharacter("<",-2,-3,0);
		putCharacter("Z",0,-3,0);
		putCharacter(">",2,-3,0);
		pause(2000);
		centerMessage("Press ENTER to continue");
		
		boolean flag = true;
		while (flag) { 
			flag = enterToContinue();
		}
		//Clears the screen buffer
		screen.clear();
		screen.refresh();
		screenSize = screen.getTerminalSize();	   
		++currentLevel;
		sequencer.stop();
	}
	
	/***************************************************************************************************************
	* Method      : centerMessage()
	*
	* Purpose     : Writes a message to screen buffer then refreshes the Terminal
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void centerMessage(String message) {
		int startXPos = message.length() / 2;
		screen.putString((-startXPos + Level.widthFactor + Level.xOffset), (Level.levelHeight - (-14 + Level.heightFactor + Level.yOffset +1)),message,Terminal.Color.WHITE, Terminal.Color.BLACK);
		screen.refresh();
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
		String playerCoordString =  "(" + players.get(selectedPlayer).coord.getX() + "," + players.get(selectedPlayer).coord.getY() + ")";
		String playerString = players.get(selectedPlayer).getSymbol().ID + " Player:" + "   " + players.get(selectedPlayer).getMaxHealth() + "/" + players.get(selectedPlayer).getCurrentHealth() + "  " + playerCoordString;
		String goalString = "Goal: " + Seed.NEA.ID;
		String gemString = "Gems: " + players.get(selectedPlayer).getGems();
		String levelString = "Level: " + (currentLevel + 1);
		String pixieCoordString =  "(" + pixies.get(selectedPixie).coord.getX() + "," + pixies.get(selectedPixie).coord.getY() + ")";
		String pixieString = pixies.get(selectedPixie).getSymbol().ID + " Pixie:" + "   " + pixies.get(selectedPixie).getMaxHealth() + "/" + pixies.get(selectedPixie).getCurrentHealth() + "  " + "[" + pixies.get(selectedPixie).getSpirit().text + "]" + "  " + pixieCoordString;
		
		Game.screen.putString(3, 0, playerString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(13, 0, playerHeartString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		Game.screen.putString(80, 0, gemString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(90, 0, goalString, Terminal.Color.BLACK, Terminal.Color.WHITE);
		Game.screen.putString(3, 29, levelString, Terminal.Color.BLACK, Terminal.Color.WHITE);

		if (pixieBonded) {
			Game.screen.putString(30, 0, pixieString, Terminal.Color.BLACK, Terminal.Color.WHITE);
			Game.screen.putString(39, 0, pixieHeartString, Terminal.Color.GREEN, Terminal.Color.WHITE);
		}
		screen.refresh();
	}// end paintInterface
	
	/***************************************************************************************************************
	* Method      : newQuest
	*
	* Purpose     : Creates a new Enemy object
	*
	* Parameters  : String description
	* 			  : int goal
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newQuest(String description, int goal) {
		quests.add(new Quest(description,goal));
		selectedQuest = quests.size()-1;
		otherEmote("NEW QUEST --> " + quests.get(selectedQuest).getStatus(), 0,-10, Direction.NONE, 3000, true);
	}
	
	/***************************************************************************************************************
	* Method      : newEnemy
	*
	* Purpose     : Creates a new Enemy object
	*
	* Parameters  : int x - the x coordinate of the enemy
	* 			  : int y - the y coordinate of the enemy
	* 			  : Direction dir - the direction the enemy is facing
	* 			  : Monster monster - an enum of enemy types
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newEnemy(int x, int y, Direction dir, Monster monster) {
		enemies.add(new Enemy(x,y,dir, monster));
		//System.out.println(enemies.size());
		int index = enemies.size()-1;
		//System.out.println("New enemy index is " + index);
		enemies.get(index).coord.setX(x);
		enemies.get(index).coord.setY(y);
		level.get(currentLevel).newEntity(enemies.get(index).coord.getX(),enemies.get(index).coord.getY(), enemies.get(index).getForeColor(), enemies.get(index).getBackColor(), enemies.get(index).getSymbol(), enemies.get(index).getLanternRadius());	
		//System.out.println("Enemy " + index + " lantern radius is " + enemies.get(index).getLanternRadius());
	}
	
	/***************************************************************************************************************
	* Method      : manageEnemies()
	*
	* Purpose     : Manages enemy movement and collision
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void manageEnemies() {
		Direction direction = Direction.NONE;
		boolean meCollide = true;
		for(int i=0; i<=enemies.size()-1; ++i) {
			//crapola
			if (destroyEnemies) {
				continue;
			}
			selectedEnemy = i;
			if (enemies.get(selectedEnemy).getMoveCooldown() && enemies.get(selectedEnemy).canMove) {
				switch (enemies.get(selectedEnemy).getMonster()) {
					case FIREFLY:
						while (meCollide) { //so firefly won't collide with a player
							direction = enemies.get(selectedEnemy).moveRand();	
							meCollide = level.get(currentLevel).manageMe(enemies.get(selectedEnemy).coord.getX(), enemies.get(selectedEnemy).coord.getY(), direction);	
							if (meCollide) System.out.println("Wants to collide with me");
						}
					break;
					case RAVAGER:
						direction = pursuePlayer();	
					break;		
				}
			
				boolean isBlocked = level.get(currentLevel).detectCollision(enemies.get(selectedEnemy).coord.getX(), enemies.get(selectedEnemy).coord.getY(), direction);
				
				if (!isBlocked) {
					level.get(currentLevel).moveEntity(enemies.get(selectedEnemy).coord.getX(), enemies.get(selectedEnemy).coord.getY(), direction, enemies.get(selectedEnemy).getSymbol(), enemies.get(selectedEnemy).getLanternRadius(), EntityType.ENEMY);
					level.get(currentLevel).updateLantern(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getLanternRadius());
					if (pixieVisible) level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getLanternRadius());
					for (int j=0; j<=enemies.size()-1; ++j) {
						if (j != selectedEnemy) { //Don't re-update your own lantern
							level.get(currentLevel).updateLantern(enemies.get(j).coord.getX(), enemies.get(j).coord.getY(),enemies.get(j).getLanternRadius());
						}
					}
					switch(direction) {
						case EAST:
							enemies.get(selectedEnemy).coord.incX();
							break;
						case SOUTH:
							enemies.get(selectedEnemy).coord.dincY();
							break;
						case WEST:
							enemies.get(selectedEnemy).coord.dincX();
							break;
						case NORTH:
							enemies.get(selectedEnemy).coord.incY();
							break;
					} // end switch
					moveTimer(EntityType.ENEMY);
					//crapola
					if (isTrapped) {
						++trappedTotal;
						System.out.println("DONT GO HERE!");
						enemies.get(selectedEnemy).canMove = false;
						quests.get(selectedQuest).incCurrentValue();
						sendMessage(quests.get(selectedQuest).getStatus(), Priority.LOW, 1500);
						checkQuestCompletion();
					}
					isTrapped=false;
				}
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : pursuePlayer()
	*
	* Purpose     : Pathfinding code for Ravager enemies
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public Direction pursuePlayer() {
		Direction tempDirection;
		boolean isBlocked = false;
		Direction xMajor;
		Direction yMajor;
		Direction xMinor;
		Direction yMinor;
		Direction lastDirection = enemies.get(selectedEnemy).lastDirection;
		ArrayList<Direction> que = new ArrayList<>(1);
		int x = enemies.get(selectedEnemy).coord.getX();
		int y = enemies.get(selectedEnemy).coord.getY();
		int playerX = players.get(selectedPlayer).coord.getX();
		int playerY = players.get(selectedPlayer).coord.getY();
		int deltaX = Math.abs(x - playerX);
		int deltaY = Math.abs(y - playerY);
		if (x < playerX) {
			xMajor = Direction.EAST;
			xMinor = Direction.WEST;
		}
		else if (x > playerX) {
			xMajor = Direction.WEST;
			xMinor = Direction.EAST;
		}
		else { //choose a random direction in event that x == playerX
			int tempDir = (int)(Math.random()*2+1);
			//System.out.println("Random direction (1-2) = " + tempDir);
			if (tempDir == 1) {
				xMajor = Direction.EAST;
				xMinor = Direction.WEST;
			}
			else {
				xMajor = Direction.WEST;
				xMinor = Direction.EAST;
			}
		}
		
		if (y < playerY) {  //choose a random direction in event that y == playerY
			yMajor = Direction.NORTH;
			yMinor = Direction.SOUTH;
		}
		else if (y > playerY) {
			yMajor = Direction.SOUTH;
			yMinor = Direction.NORTH;
		}
		else {
			int tempDir = (int)(Math.random()*2+1);
			//System.out.println("Random direction (1-2) = " + tempDir);
			if (tempDir == 1) {
				yMajor = Direction.NORTH;
				yMinor = Direction.SOUTH;
			}
			else {
				yMajor = Direction.SOUTH;
				yMinor = Direction.NORTH;
			}
		}	

		if (deltaY > deltaX) {
			if (yMajor != lastDirection) que.add(yMajor);
			if (xMajor != lastDirection) que.add(xMajor);
			if (xMinor != lastDirection) que.add(xMinor);
			if (yMinor != lastDirection) que.add(yMinor);
		}
		else if(deltaX > deltaY) {
			if (xMajor != lastDirection) que.add(xMajor);
			if (yMajor != lastDirection) que.add(yMajor);
			if (yMinor != lastDirection) que.add(yMinor);
			if (xMinor != lastDirection) que.add(xMinor);
		}
		else { //deltaX == deltaY
			int tempDir = (int)(Math.random()*2+1);
			if (tempDir == 1) {
				if (yMajor != lastDirection) que.add(yMajor);
				if (xMajor != lastDirection) que.add(xMajor);
				if (xMinor != lastDirection) que.add(xMinor);
				if (yMinor != lastDirection) que.add(yMinor);
			}
			else {
				if (xMajor != lastDirection) que.add(xMajor);
				if (yMajor != lastDirection) que.add(yMajor);
				if (yMinor != lastDirection) que.add(yMinor);
				if (xMinor != lastDirection) que.add(xMinor);
			}
				
		}
		//System.out.println("Length of que is " + (que.size()-1));
		for (int i=0; i<que.size()-1;++i) {
			isBlocked = level.get(currentLevel).detectCollision(x, y, que.get(i));
			if (!isBlocked) {
				enemies.get(selectedEnemy).lastDirection = lastDirection.getOpposite(que.get(i));
				return que.get(i);
			}
		}
		//If code falls through to here it means that the enemy has no directions to go
		//So we let the enemy go backward in the restricted direction, while at the same
		//time, making it so it can't bounce back and forth
		switch(lastDirection){
		case NORTH:
			enemies.get(selectedEnemy).lastDirection = Direction.SOUTH;
			return Direction.NORTH;
		case SOUTH:
			enemies.get(selectedEnemy).lastDirection = Direction.NORTH;
			return Direction.SOUTH;
		case EAST:
			enemies.get(selectedEnemy).lastDirection = Direction.WEST;
			return Direction.EAST;
		case WEST:
			enemies.get(selectedEnemy).lastDirection = Direction.EAST;
			return Direction.WEST;
	}
		System.out.println("Code should not hit here.");
		//NOTE: return lastDirection causes enemies to bounce back and forth
		return Direction.NONE;
	}
	
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
	public void placePlayer(int x, int y, Direction dir) {
		players.get(selectedPlayer).coord.setX(x);
		players.get(selectedPlayer).coord.setY(y);
		players.get(selectedPlayer).setDirection(dir);
		level.get(currentLevel).newEntity(players.get(selectedPlayer).coord.getX(),players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getForeColor(), players.get(selectedPlayer).getBackColor(), players.get(selectedPlayer).getSymbol(), players.get(selectedPlayer).getLanternRadius());	
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
		pixieVisible = true;
		pixies.get(selectedPixie).coord.setX(x);
		pixies.get(selectedPixie).coord.setY(y);
		pixies.get(selectedPixie).setDirection(dir);
		level.get(currentLevel).newEntity(pixies.get(selectedPixie).coord.getX(),pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getForeColor(), pixies.get(selectedPixie).getBackColor(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());	
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
	public void setPixieLantern(int radius) {
		level.get(currentLevel).blackLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),pixies.get(selectedPixie).getLanternRadius());
		pixies.get(selectedPixie).setLanternRadius(radius);
		level.get(currentLevel).updateLantern(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),pixies.get(selectedPixie).getLanternRadius());
		screen.refresh();
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
			if (pixieBonded) {
				commandMoveFacing();
				pause(pauseTime);
			}
			else {
				commandMoveCardinal(dir);
				pause(pauseTime);
			}
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
	public void movePixie(int num, int pauseTime) {
		for (int i=0; i<num; i++) {
			commandMoveFacing();
			pause(pauseTime);
		}
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
				level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   			
				pause(pauseTime);
			}
			else {
				Direction newDir = turnLeft(players.get(selectedPlayer).getDirection());
				players.get(selectedPlayer).setDirection(newDir);
				level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol());   	
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
				level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   
				pause(pauseTime);
			}
			else {
				Direction newDir = turnLeft(pixies.get(selectedPixie).getDirection());
				players.get(selectedPixie).setDirection(newDir);
				level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol());   	
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
	
	/***************************************************************************************************************
	* Method      : playerEmote
	*
	* Purpose     : A scripting method for displaying player emotes
	*
	* Parameters  : String emote - the emote string
	* 		 	  : Direction dir - the enum of directions
	* 			  : int pauseTime - the amount of time to pause before deleting the message
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void playerEmote (String emote, Direction dir, int pauseTime, boolean isHighlighted) {
		System.out.println("Player is emoting from " + players.get(selectedPlayer).coord.getX() + "," + players.get(selectedPlayer).coord.getY());
		level.get(currentLevel).emote(emote,players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(),dir,pauseTime,isHighlighted);	
	}
	
	/***************************************************************************************************************
	* Method      : pixieEmote
	*
	* Purpose     : A scripting method for displaying pixie emotes
	*
	* Parameters  : String emote - the emote string
	* 		 	  : Direction dir - the enum of directions
	* 			  : int pauseTime - the amount of time to pause before deleting the message
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void pixieEmote (String emote, Direction dir, int pauseTime, boolean isHighlighted) {
		level.get(currentLevel).emote(emote,pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(),dir,pauseTime,isHighlighted);	
	}
	
	/***************************************************************************************************************
	* Method      : otherEmote
	*
	* Purpose     : Updates a player's coordinates when moving in cardinal directions when playerControl = false
	*
	* Parameters  : String emote - the emote string
	* 		 	  : Direction dir - the enum of directions
	* 			  : int pauseTime - the amount of time to pause before deleting the message
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void otherEmote (String emote, int x, int y, Direction dir, int pauseTime, boolean isHighlighted) {
		level.get(currentLevel).emote(emote,x,y,dir,pauseTime,isHighlighted);		
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
	
	/***************************************************************************************************************
	* Method      : controlPlayer
	*
	* Purpose     : A scripting method to take control of player
	*
	* Parameters  : String emote - the emote string
	* 		 	  : Direction dir - the enum of directions
	* 			  : int pauseTime - the amount of time to pause before deleting the message
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void controlPlayer() {
		playerControl = true;
		disableCooldown = false;
		players.get(selectedPlayer).setDirection(pixies.get(selectedPixie).getDirection());
		pixies.get(selectedPixie).setDirection(Direction.NONE);
		level.get(currentLevel).updateSymbol(players.get(selectedPlayer).coord.getX(), players.get(selectedPlayer).coord.getY(), players.get(selectedPlayer).getSymbol(),players.get(selectedPlayer).getLanternRadius());   	
		level.get(currentLevel).updateSymbol(pixies.get(selectedPixie).coord.getX(), pixies.get(selectedPixie).coord.getY(), pixies.get(selectedPixie).getSymbol(), pixies.get(selectedPixie).getLanternRadius());   		
	}
	
	/***************************************************************************************************************
	* Method      : spawnDispatcher()
	*
	* Purpose     : Dispatcher for timer events
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void spawnDispatcher(int ID) {
		switch(ID) {
		case 1:
			newEnemy(-5,2,Direction.NONE, Monster.RAVAGER);
			newEnemy(-5,-2,Direction.NONE, Monster.RAVAGER);
			newQuest("Evade your pursuers and find the secret passage", 1);
			break;
		case 2:
			newEnemy(-28,0,Direction.NONE, Monster.RAVAGER);
			break;
		case 3:
			newEnemy(-28,0,Direction.NONE, Monster.RAVAGER);
			break;
		case 4:
			newEnemy(-28,0,Direction.NONE, Monster.RAVAGER);
			break;
		case 5:
			newEnemy(-34,0,Direction.NONE, Monster.RAVAGER);
			newEnemy(-36,0,Direction.NONE, Monster.RAVAGER);
			newEnemy(-38,0,Direction.NONE, Monster.RAVAGER);
			newEnemy(-40,0,Direction.NONE, Monster.RAVAGER);
			break;
		}
		
	}
	
	/***************************************************************************************************************
	* Method      : triggerDispatcher
	*
	* Purpose     : Dispatcher for trigger events 
	*
	* Parameters  : None
	*
	* Returns     : None
	*  
	***************************************************************************************************************/
	public void triggerDispatcher() {
		//System.out.println("Running dispatcher");
		switch(tempID) {
		case 0: //STARTING TRIGGER
			pause(1000);
			otherEmote("The * represents your avatar", 0,-10, Direction.NONE, 3000, true);
			otherEmote("The box surrounding * is the limit of your vision", 0,-10, Direction.NONE, 3000, true);
			pause(2000);
			newQuest("Investigate the glow to east", 1);
			pause(2000);
			otherEmote("Press the W,S,D,A keys to move North, South, East or West.", 0,-10, Direction.NONE, 5000, true);
			break;
		case 1: //SECOND SHRINE
			pause(1000);
			otherEmote("A miniature stone shrine lies before you.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("When you touch it, it crumbles to the ground.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("Fireflies jet outward from its ruin.", 0,-10, Direction.NONE, 3000, true);
			level.get(currentLevel).clearLamp();
			newEnemy(-18,3,Direction.NONE, Monster.FIREFLY);
			newEnemy(-18,1,Direction.NONE, Monster.FIREFLY);
			newEnemy(-18,-1,Direction.NONE, Monster.FIREFLY);
			newEnemy(-18,-3,Direction.NONE, Monster.FIREFLY);
			quests.get(selectedQuest).incCurrentValue();
			checkQuestCompletion();
			newQuest("Catch Fireflies", 4);
			break;
		case 2: //Player arrives at bridge
			pause(1000);
			otherEmote("You stand on an ancient bridge.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("A dark river roils below.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("A decrepit garden lies to the east.", 0,-10, Direction.NONE, 3000, true);
			newQuest("Discover Shrines", 3);
			//Check to make sure player has collected all the mana
			break;
		case 3: //SECOND shrine
			pause(1000);
			otherEmote("Shrines provide valuable information about the world.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("TIP: Press Escape to end your journey.", 0,-10, Direction.NONE, 3000, true);
			otherEmote("Then come back when you have your big boy/girl pants on!", 0,-10, Direction.NONE, 5000, true);
			quests.get(selectedQuest).incCurrentValue();
			sendMessage(quests.get(selectedQuest).getStatus(), Priority.LOW, 1500);
			checkQuestCompletion();
			break;
		case 4: //THIRD shrine
			pause(1000);
			otherEmote("Your max/current health",-33,15,Direction.SOUTH, 3000, true);
			otherEmote("Your map coordinates",-25,15,Direction.SOUTH, 3000, true);
			quests.get(selectedQuest).incCurrentValue();
			sendMessage(quests.get(selectedQuest).getStatus(), Priority.LOW, 1500);
			checkQuestCompletion();
			break;	
		case 5: //FOURTH shrine
			pause(1000);
			otherEmote("The game level",-40,-14,Direction.NE, 3000, true);
			sendMessage("All your base are belong to us", Priority.HIGH, 4000);
			otherEmote("Messages are displayed here",-0,-14,Direction.NORTH, 3000, true);
			quests.get(selectedQuest).incCurrentValue();
			sendMessage(quests.get(selectedQuest).getStatus(), Priority.LOW, 2000);
			checkQuestCompletion();
			break;
		case 6: //The temple
			pause(1000);
			placePixie(8,0,Direction.WEST);
			pixieVisible = true;
			otherEmote("You enter a well-lit temple.",0,-10,Direction.NONE, 5000, true);
			otherEmote("The source of the light is something out of a fairy tale...a pixie.",0,-10,Direction.NONE, 5000, true);
			pause(2000);
			level1Scene2();
			spawnTimer(10000,1);
			break;
		case 7: //The chase
//			pause(1000);
//			newEnemy(-5,2,Direction.NONE, Monster.RAVAGER);
//			newEnemy(-5,-2,Direction.NONE, Monster.RAVAGER);
//			newQuest("Evade your pursuers and find the secret passage", 1);
//			break;
		case 8: //The Descent
			pixieEmote("Here it is!",Direction.NW,2000,false);
			pause(2000);
			quests.get(selectedQuest).incCurrentValue();
			checkQuestCompletion();
			pause(2000);
			otherEmote(" ",0,0,Direction.LOADER,1,true);
			//Clean up enemies
			int counter = enemies.size()-1;
			System.out.println("Counter = " + counter);
			for (int j=0; j<=counter; ++j) {
				System.out.println(j);
				level.get(currentLevel).removeEnemy(enemies.get(0).coord.getX(), enemies.get(0).coord.getY());
				level.get(currentLevel).blackLantern(enemies.get(0).coord.getX(), enemies.get(0).coord.getY(), enemies.get(0).getLanternRadius());
				enemies.remove(0);
				destroyEnemies = true;
			}
			pause(500);//to clear out any existing timers
			moveTimer.cancel(); //Cancel outstanding timers	
			level2Scene1();
			break;
		case 9: //The Chamber
			pause(3000);
			newEnemy(-25,7,Direction.NONE, Monster.TORCH);
			newEnemy(-20,7,Direction.NONE, Monster.TORCH);
			newEnemy(-15,7,Direction.NONE, Monster.TORCH);
			newEnemy(-10,7,Direction.NONE, Monster.TORCH);
			newEnemy(-5,7,Direction.NONE, Monster.TORCH);
			newEnemy(-25,-7,Direction.NONE, Monster.TORCH);
			newEnemy(-20,-7,Direction.NONE, Monster.TORCH);
			newEnemy(-15,-7,Direction.NONE, Monster.TORCH);
			newEnemy(-10,-7,Direction.NONE, Monster.TORCH);
			newEnemy(-5,-7,Direction.NONE, Monster.TORCH);
			quests.get(selectedQuest).incCurrentValue();
			checkQuestCompletion();
			pause(2000);
			otherEmote("You enter a vast chamber lined with massive serpentine columns.",0,-10,Direction.NONE, 5000, true);
			otherEmote("Torches flicker on the north and south walls.",0,-10,Direction.NONE, 5000, true);
			otherEmote("You hear the sound of rushing water somewhere to the east.",0,-10,Direction.NONE, 5000, true);
			level2Scene2();
			break;
		case 10:
			pause(2000);
			quests.get(selectedQuest).incCurrentValue();
			checkQuestCompletion();
			level2Scene4();
			break;
		}
	}
	
	/***************************************************************************************************************
	* Method      : level1Scene1()
	*
	* Purpose     : Scripted event for 1st Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level1Scene1() {
		
		//DON'T TOUCH THIS CODE TO NEXT COMMENT BELOW
		level.add(new Level());
		level.get(currentLevel).newLevel1(); 
		playerControl = false;
		disableCooldown = false;
		//DON'T TOUCH THIS CODE TO NEXT COMMENT ABOVE

		//lightLevel(); //ONLY USE THIS FOR DEBUGGING PURPOSES TO ILLUMINATE LEVEL

		placePlayer(-35,0,Direction.NONE); //45,0 AND -35,0
		pause(3000);
		playerEmote("Hello...?",Direction.NE, 3000, false);
		pause(3000);
		movePlayer(Direction.NORTH,2,250);
		pause(1000);
		playerEmote("Why is it so dark?",Direction.NE, 3000, false);
		pause(3000);
		movePlayer(Direction.EAST,2,250);
		pause(1000);
		playerEmote("Is anyone there?",Direction.NE, 3000, false);
		pause(3000);
		movePlayer(Direction.SOUTH,2,250);
	}
	
	/***************************************************************************************************************
	* Method      : level1Scene2()
	*
	* Purpose     : Scripted event for 1st Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level1Scene2() {
		playerEmote("Hello?",Direction.SW, 3000, false);
		pause(1000);
		pixieEmote("You are in grave danger!",Direction.NE, 2000, false);
		pause(2000);
		playerEmote("But...",Direction.SW, 3000, false);
		pause(1000);
		otherEmote("R O A R !",-48,0,Direction.SE,3000,false);
		pause(1000);
		pixieEmote("Our time is shorter than I feared!",Direction.SE, 3000, false);
		pause(2000);
		playerEmote("What was that?",Direction.NW, 3000, false);
		pause(2000);
		pixieEmote("Let's hope you only have to hear it.",Direction.NW, 3000, false);
		pause(2000);
		playerEmote("What do we do?",Direction.SW, 3000, false);
		pause(2000);
		pixieEmote("There is a secret passage nearby...",Direction.NW, 3000, false);
		pause(2000);
		pixieEmote("So secret that I don't remember where it is.",Direction.NW, 3000, false);
		pause(2000);
		pixieEmote("Follow me!",Direction.NW, 3000, false);
		pause(2000);
		turnPixie("cw",2,500);
		pause(1000);
		movePixie(4,500);
		pause(1000);
		pixieEmote("EQOZ AROTH!",Direction.SW, 3000, false);
		commandDig();
		pause(1000);
		otherEmote("The wall in front of the pixie disappears into dust.",0,-10,Direction.NONE, 5000, true);
		otherEmote("MOO...",13,0,Direction.NE,3000,false);
		pause(2000);
		pixieEmote("!!!",Direction.NW, 3000, false);
		pause(1000);
		playerEmote("HEY! That's my cow!",Direction.SW, 3000, false);
		pause(2000);
		playerEmote("How did she get here?",Direction.SW, 3000, false);
		pause(1000);
		otherEmote("MOO...",13,0,Direction.NE,3000,false);
		pause(1000);
		pixieEmote("This is bad!",Direction.NW, 3000, false);
		pause(2000);
		turnPixie("cw",2,500);
		pause(1000);
		pixieEmote("Our only hope of escape is to bond.",Direction.NW, 3000, false);
		pause(2000);
		movePixie(6,500);
		controlPlayer();
		otherEmote("You feel tingly inside.",0,-10,Direction.NONE, 5000, true);
		pause(2000);
		playerEmote("What have you done?",Direction.NW, 3000, false);
		pause(2000);
		pixieEmote("We have bonded.",Direction.NE, 3000, false);
		pause(2000);
		pixieEmote("Now, you have my powers.",Direction.NE, 3000, false);
		pause(2000);
		pixieEmote("Where you go I follow.",Direction.NE, 3000, false);
		pause(2000);
		playerEmote("But...",Direction.SW, 3000, false);
		pause(1000);
		otherEmote("R O A R !", -10,0,Direction.NW,3000,false);
		otherEmote("While bonded with a pixie press W to move forward, A to turn CCW and D to turn CW",0,-10,Direction.NONE, 5000, true);
		pixieBonded = true;
	}
	
	/***************************************************************************************************************
	* Method      : level2Scene1()
	*
	* Purpose     : Scripted event for 2nd Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level2Scene1() {	
		level.add(new Level());
		++currentLevel;
		level.get(currentLevel).newLevel2();
		placePlayer(-42,0,Direction.EAST);//-42
		placePixie(-43,0,Direction.NONE);//-43
		System.out.println(players.get(selectedPlayer).coord.getX() + "," + players.get(selectedPlayer).coord.getY());
		
		playerControl = true;
		pixieBonded = true;
		disableCooldown = false;
		
		//lightLevel();
		
		pause(3000);
		playerEmote("We are trapped!",Direction.NE, 3000, false);
		pause(2000);
		pixieEmote("You have forgotten...",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("You have my powers now.",Direction.SE, 3000, false);
		pause(2000);
		playerEmote("What powers?",Direction.NE, 3000, false);
		pause(2000);
		pixieEmote("Each of my kind has a unique power.",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("Mine is the manipulation of earth.",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("Step forward to the wall.",Direction.SE, 3000, false);
		pause(2000);
		movePlayer(Direction.EAST,3,250);
		pause(2000);
		otherEmote("When standing in front of a non-white wall, press S to remove that wall.", 0,-10, Direction.NONE, 5000, true);
		pause(2000);
		newQuest("Dig eastward to the next chamber", 1);
		//COMMENT THIS OUT UNDER NORMAL PLAY
		//level2Scene2();//<--COMMENT OUT
		////////////////////////////////////
	}
	
	/***************************************************************************************************************
	* Method      : level2Scene2()
	*
	* Purpose     : Scripted event for 2nd Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level2Scene2() {
		pause(2000);
		otherEmote("R O A R !",-48,0,Direction.SE,3000,false);
		pause(1000);
		pixieEmote("We need to make a stand.",Direction.NE, 2000, false);
		pause(2000);
		pixieEmote("Our pursuers are fast but not too bright.",Direction.NE, 4000, false);
		pause(2000);
		pixieEmote("You can use my power to dig pits in the ground.",Direction.NE, 4000, false);
		pause(2000);
		pixieEmote("Then you can lure them into the pits.",Direction.NE, 2000, false);
		pause(2000);
		playerEmote("I'm a simple farmer!", Direction.SE, 2000, false);
		pause(2000);
		playerEmote("Do I look like a hero?", Direction.SE, 2000, false);
		pause(2000);
		pixieEmote("Trust me...",Direction.NE, 2000, false);
		pause(1000);
		pixieEmote("Big things come in small packages.",Direction.NE, 2000, false);
		pause(2000);
		otherEmote("While facing a non-white floor tile, press S to dig a pit",0,-10,Direction.NONE, 5000, true);
		pause(2000);
		newQuest("Lure 3 enemies into pits",3);
		spawnTimer(10000,2);
		spawnTimer(20000,3);
		spawnTimer(35000,4);
	}
	
	/***************************************************************************************************************
	* Method      : level2Scene3()
	*
	* Purpose     : Scripted event for 2nd Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level2Scene3() {
		pause(2000);
		playerEmote("We did it!",Direction.NE, 3000, false);
		pause(1000);
		pixieEmote("It's not over yet!",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("Those were only scouts.",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("We need to stop them once and for all.",Direction.SE, 3000, false);
		pause(2000);
		playerEmote("But how?",Direction.NE, 3000, false);
		pause(1000);
		pixieEmote("Not only can you transmute earth to air...",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("But you can move earth as if it was air.",Direction.SE, 3000, false);
		pause(2000);
		playerEmote("...",Direction.NE, 3000, false);
		pause(1000);
		pixieEmote("That means...",Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("You can push small quantities of earth.",Direction.SE, 3000, false);
		pause(2000);
		otherEmote("At any time you can switch your pixie power to PUSH by pressing 2.", 0,-10, Direction.NONE, 5000, true);
		otherEmote("Stand in front of any non-white column and press S to push it.", 0,-10, Direction.NONE, 5000, true);
		pause(1000);
		newQuest("Block the western hallway with a non-white block.",1);
		level.get(currentLevel).newTrigger(-28, 0, 10,Seed.TRIGGERF, false, false);
		spawnTimer(25000,5);
	}
	
	/***************************************************************************************************************
	* Method      : level2Scene4()
	*
	* Purpose     : Scripted event for 2nd Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level2Scene4(){
		pixieEmote("That should hold them!",Direction.NE, 2000, false);
		pause(2000);
		playerEmote("I really did it!", Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("Big things really do come in small packages!",Direction.NE, 2000, false);
		pause(2000);
		playerEmote("<smiles>", Direction.SE, 3000, false);
		pause(2000);
		pixieEmote("Don't get too cocky!",Direction.NE, 2000, false);
		pause(2000);
		pixieEmote("This is far from over.",Direction.NE, 2000, false);
		pause(2000);
		pixieEmote("We still have to escape this room.",Direction.NE, 2000, false);
		pause(2000);
		level2Scene5();
	}
	
	/***************************************************************************************************************
	* Method      : level2Scene5()
	*
	* Purpose     : Scripted event for 2nd Level of game
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void level2Scene5() {
		turnPlayer("cw",2,500);
		movePlayer(Direction.EAST,25,250);
		pause(2000);
		pixieEmote("I can cross this river because I can fly.",Direction.NW, 2000, false);
		pause(2000);
		playerEmote("Don't leave me!", Direction.SE, 3000, false);
		pause(2000);
		otherEmote("Press SPACE BAR to toggle between control of you or the pixie.", 0,-10, Direction.NONE, 5000, true);
		otherEmote("To move the pixie press the I,J,K,L keys.", 0,-10, Direction.NONE, 5000, true);
		pause(1000);
		pixieEmote("",Direction.NW, 2000, false);
		pause(2000);
		newQuest("Find a way across the river.", 1);
	}
	
	

	
}// end Game