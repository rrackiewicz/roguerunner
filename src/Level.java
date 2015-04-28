/***************************************************************************************************************
* File        : Level.java
*
* Purpose     : This file is responsible for drawing and maintaining the contents of each level. This includes:
* 				Drawing the map, updating the map, drawing and managing elements on the map. 
*
* 
* Collections :	HashMap<Pair,Cell> tiles				 		A collection of Cell objects
* 				HashMap<Pair,Wall> walls 						A collection of Wall objects
* 				HashMap<Pair,Floor> floors						A collection of Floor objects
* 				HashMap<Pair,Pits> pits							A collection of Pit objects
* 				HasMap<Pair, Blocks> blocks						A collection of Block objects
* 
* Objects     :	Terminal terminal (global)						An object that controls the terminal			
* 			  :	Screen screen (global)							An object that controls the screen buffer
* 
* Enums		  :	None.
*  
***************************************************************************************************************/
import java.util.HashMap;

import com.googlecode.lanterna.terminal.Terminal;

public class Level {
	public HashMap<Pair, Cell> tiles;
	public HashMap<Pair, Wall> walls;
	public HashMap<Pair, Floor> floors;
	public HashMap<Pair, Pit> pits;
	public HashMap<Pair, Block> blocks;
	public HashMap<Pair, Water> water;
	public HashMap<Pair, Earth> earth;
	public HashMap<Pair, Trigger> triggers;
	public HashMap<Pair, Resource> resources;
	
	public static int levelWidth;
	public static int levelHeight;
	public static int leftLevel;
	public static int rightLevel;
	public static int topLevel; 
	public static int bottomLevel;
	public static int topScreen;
	public static int bottomScreen;
	public static int leftScreen;
	public static int rightScreen;
	public static int widthFactor;
	public static int heightFactor;
	public static int xOffset;
	public static int yOffset;

	/***************************************************************************************************************
	* Method      : Level
	*
	* Purpose     : Constructor for all level objects. Creates new containers for all level elements: tiles, walls, 
	* 				floors, enemies, resources, traps, triggers, etc.
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public Level() {
		levelWidth = Game.screenSize.getColumns();
		levelHeight = Game.screenSize.getRows();
		leftScreen = (levelWidth / 2 * -1); //-50
		rightScreen = levelWidth / 2 - 1; //+49
		topScreen = levelHeight / 2 - 1; //+13
		bottomScreen = (levelHeight / 2 * -1) + 2; //-13 
		leftLevel = leftScreen;
		rightLevel = rightScreen;
		bottomLevel = bottomScreen;
		topLevel = topScreen;
		widthFactor = levelWidth / 2;
		heightFactor = levelHeight / 2;
		tiles = new HashMap<Pair, Cell>();
		walls = new HashMap<Pair, Wall>();
		floors = new HashMap<Pair, Floor>();
		pits = new HashMap<Pair, Pit>();
		blocks = new HashMap<Pair, Block>();
		water = new HashMap<Pair, Water>();
		earth = new HashMap<Pair, Earth>();
		triggers = new HashMap<Pair, Trigger>();
		resources = new HashMap<Pair, Resource>();
	}
	
	/***************************************************************************************************************
	* Method      : cellInfo
	*
	* Purpose     : Press "I" to get debugging info for the cell in front of the entity in the direction they face
	*
	* Parameters  : int x - the x coordinate of the entity 
	* 			  : int y - the y coordinate of the entity
	* 			  : Direction dir - the direction the entity is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void cellInfo(int x, int y, Direction dir) {
		switch(dir) {
		case NORTH:
			System.out.println("Buffered foreColor is " + tiles.get(new Pair(x,y+1)).getBufferForeColor());
			System.out.println("Buffered content is " + tiles.get(new Pair(x,y+1)).getBufferContent());
			System.out.println("ForeColor is " + tiles.get(new Pair(x,y+1)).getForeColor());
			System.out.println("Content is " + tiles.get(new Pair(x,y+1)).getSeed());
			break;
		case SOUTH:
			System.out.println("Buffered foreColor is " + tiles.get(new Pair(x,y-1)).getBufferForeColor());
			System.out.println("Buffered content is " + tiles.get(new Pair(x,y-1)).getBufferContent());
			System.out.println("ForeColor is " + tiles.get(new Pair(x,y-1)).getForeColor());
			System.out.println("Content is " + tiles.get(new Pair(x,y-1)).getSeed());
			break;
		case EAST:
			System.out.println("Buffered foreColor is " + tiles.get(new Pair(x+1,y)).getBufferForeColor());
			System.out.println("Buffered content is " + tiles.get(new Pair(x+1,y)).getBufferContent());
			System.out.println("ForeColor is " + tiles.get(new Pair(x+1,y)).getForeColor());
			System.out.println("Content is " + tiles.get(new Pair(x+1,y)).getSeed());
			break;
		case WEST:
			System.out.println("Buffered foreColor is " + tiles.get(new Pair(x-1,y)).getBufferForeColor());
			System.out.println("Buffered content is " + tiles.get(new Pair(x-1,y)).getBufferContent());
			System.out.println("ForeColor is " + tiles.get(new Pair(x-1,y)).getForeColor());
			System.out.println("Content is " + tiles.get(new Pair(x-1,y)).getSeed());
			break;
		}
	}
	
	/***************************************************************************************************************
	* Method      : scrollMap
	*
	* Purpose     : Re-centers the player when he reaches an edge 
	*
	* Parameters  : int x - the x coordinate of the entity at the edge of the map
	* 			  : int y - the y coordinate of the entity at the edge of the map
	* 			  : Direction dir - the direction the entity is facing at the edge of the map
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void scrollMap(int x, int y, Direction dir) {
		switch(dir) {
		case EAST:
			if (x + 1 > rightLevel)
			break;
		case SOUTH:
			
			break;
		case WEST:
			
			break;
		case NORTH:
			
			break;
	}// end switch 
		
	}

	/***************************************************************************************************************
	* Method      : bufferString
	*
	* Purpose     : Writes the Unicode character of a tile at coordinate x,y to the screen buffer at the foreground
	* 				and background color stored in that tile 
	*
	* Parameters  : int x - the x coordinate of the string to be written to the screen buffer
	* 			  : int y - the t coordinate of the string to be written to the screen buffer
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void bufferString(int x, int y) {
		//System.out.println("LL = " + leftLevel + "  RL = " + rightLevel + "  TL = " + topLevel + "  BL = " + bottomLevel);
		//Don't want to buffer things off screen
		//if(x < leftLevel || x > rightLevel || y < bottomLevel || y > topLevel) {
			Terminal.Color foreColor;
			Terminal.Color backColor;
			//if (Game.log) System.out.println("Drawing " + tiles.get(new Pair(x,y)).content + " at " + x + "," + y);
			if (tiles.get(new Pair(x,y)).getLight()) {
				foreColor = tiles.get(new Pair(x,y)).getForeColor();
				backColor = tiles.get(new Pair(x,y)).getBackColor();
			}
			else 
			{
				foreColor = Terminal.Color.BLACK;
				backColor = Terminal.Color.BLACK;
			}
			Game.screen.putString((x + widthFactor + xOffset), (levelHeight - (y + heightFactor + yOffset)), tiles.get(new Pair(x,y)).content.ID, foreColor, backColor);	
		//}
	}
	
	/***************************************************************************************************************
	* Method      : newPlayer
	*
	* Purpose     : Places the character into the level 
	*
	* Parameters  : int x - the x coordinate of the player
	* 			  :	int y - the y coordinate of the player
	* 			  : Terminal.Color foreColor - the foreground color of the player
	* 			  : Terminal.Color backColor - the backgorund color of the player
	* 			  : Seed symbol - the seed enum used to render the player
	* 			  : int lanternRadius - the radius of the Player's lantern
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newEntity(int x, int y, Terminal.Color foreColor, Terminal.Color backColor, Seed symbol, int lanternRadius) {
		bufferCell(x,y);
		tiles.get(new Pair(x,y)).updateType(symbol);
		tiles.get(new Pair(x,y)).setForeColor(foreColor);
		tiles.get(new Pair(x,y)).setBackColor(backColor);
		updateColor(x,y);
		updateLantern(x,y,lanternRadius);
	}
	
	/***************************************************************************************************************
	* Method      : removeEnemy
	*
	* Purpose     : Removes an Enemy object
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeEnemy(int x, int y) {
		restoreBuffer(x,y);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : boxFill
	*
	* Purpose     : Places the character into the level 
	*
	* Parameters  : int x1 - the x coordinate of the upper-left corner of the fill region
	* 			  :	int y1 - the y coordinate of the upper-left corner of the fill region
	* 				int x2 - the x coordinate of the lower-right corner of the fill region
	* 			  :	int y2 - the y coordinate of the lower-right corner of the fill region
	* 			  : isLit - if true the tile is on by default, if false the tile is off by default
	* 				NOTE: Use isLit = on for title screens
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void boxFill(int x1, int y1, int x2, int y2, Seed seed, boolean isLit) {
		Seed newSeed = Seed.SPACE;
		for (int j=y1; j >= y2; --j) {
			for (int i=x1; i <= x2; ++i){
				if (seed == Seed.PATH) {
					int newRand = (int)(Math.random() * 10 + 1);
					switch(newRand) {
					case 1:
						newSeed = Seed.GRND1;
						break;
					case 2:
						newSeed = Seed.GRND2;
						break;
					case 3:
						newSeed = Seed.GRND3;
						break;
					case 4:
						newSeed = Seed.GRND4;
						break;
					case 5:
						newSeed = Seed.GRND5;
						break;
					case 6:
						newSeed = Seed.GRND6;
						break;
					case 7:
						newSeed = Seed.GRND7;
						break;
					case 8:
						newSeed = Seed.GRND8;
						break;
					case 9:
						newSeed = Seed.GRND9;
						break;
					case 10:
						newSeed = Seed.GRND10;
						break;
					}
				}
				else {
					newSeed = seed;
				}
				if (seed == Seed.FLOOR) {
					newFloor(i,j,4);
				}
				else {
					drawTile(i,j,newSeed,isLit);
				}
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : newEarth
	*
	* Purpose     : Draws a new tile of type EARTH 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Seed seed - the seed enum used to render the earth
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newEarth(int x, int y, int level, Seed seed) {
		//if (Game.log) System.out.println("New earth added.");
		earth.put(new Pair(x,y), new Earth());
		tiles.get(new Pair(x,y)).updateType(seed);
		earth.get(new Pair(x,y)).setLevel(level);
		updateColor(x,y);		
	}
	
	/***************************************************************************************************************
	* Method      : chainWall
	*
	* Purpose     : Draws will tiles at the specified coordinate, in the specified direction, a specified # of times
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Direction dir - the direction to draw the wall
	* 			  : int num - the number of copies of the wall
	*  			  : int level - the level determines the default color of the wall. See Thing class for details.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void chainWall(int x, int y, Direction dir, int num, int level) {
		for (int i=0; i<num; ++i) {
			switch(dir) {
			case NORTH:
				bufferCell(x,y+i);
				walls.put(new Pair(x,y+i), new Wall());
				tiles.get(new Pair(x,y+i)).updateType(Seed.CALC);
				walls.get(new Pair(x,y+i)).setLevel(level);
				updateColor(x,y+i);
				break;
			case SOUTH:
				bufferCell(x,y-i);
				walls.put(new Pair(x,y-i), new Wall());
				tiles.get(new Pair(x,y-i)).updateType(Seed.CALC);
				walls.get(new Pair(x,y-i)).setLevel(level);
				updateColor(x,y-i);
				break;
			case EAST:
				bufferCell(x+i,y);
				walls.put(new Pair(x+i,y), new Wall());
				tiles.get(new Pair(x+i,y)).updateType(Seed.CALC);
				walls.get(new Pair(x+i,y)).setLevel(level);
				updateColor(x+i,y);
				break;
			case WEST:
				bufferCell(x-i,y);
				walls.put(new Pair(x-i,y), new Wall());
				tiles.get(new Pair(x-i,y)).updateType(Seed.CALC);
				walls.get(new Pair(x-i,y)).setLevel(level);
				updateColor(x-i,y);
				break;
			}
		}
	}
	
	/***************************************************************************************************************
	* Method      : newWall
	*
	* Purpose     : Adds a new Wall object. Draws a new tile of type CALC. All tiles of type CALC are converted 
	* 				into their respective Unicode characters in the calcLevel() method. 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*  			  : int level - the level determines the default color of the wall. See Thing class for details.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newWall(int x, int y, int level) {
		bufferCell(x,y);
		walls.put(new Pair(x,y), new Wall());
		tiles.get(new Pair(x,y)).updateType(Seed.CALC);
		walls.get(new Pair(x,y)).setLevel(level);
		updateColor(x,y);
		//Don't buffer wall strings as they will be on calulation
	}
	
	/***************************************************************************************************************
	* Method      : removeWall
	*
	* Purpose     : Removes a Wall object from the walls ArrayList.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeWall(int x, int y) {
		walls.remove(new Pair(x,y));
		newFloor(x,y,4);
	}
	
	/***************************************************************************************************************
	* Method      : newMana
	*
	* Purpose     : Creates a new Mana object
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newMana(int x,int y) {
		//Make this work for gems or Mana
		bufferCell(x,y);
		resources.put(new Pair(x,y), new Resource());
		tiles.get(new Pair(x,y)).updateType(Seed.MANA);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeMana
	*
	* Purpose     : Removes a new Mana object
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeMana(int x, int y) {
		resources.remove(new Pair(x,y));
		restoreBuffer(x,y);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : newTrigger
	*
	* Purpose     : Creates a new Trigger object
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	* 			  : int ID - unique trigger ID
	* 			  : Seed seed - an enum of seed values
	* 			  : boolean enduresFlag - does this trigger persist when activated?
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newTrigger(int x,int y, int ID, Seed seed, boolean isLit, boolean enduresFlag) {
		bufferCell(x,y);
		triggers.put(new Pair(x,y), new Trigger(ID, isLit, enduresFlag));
		tiles.get(new Pair(x,y)).updateType(seed);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeTrigger
	*
	* Purpose     : Removes a Trigger object
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeTrigger(int x, int y){
		triggers.remove(new Pair(x,y));
		restoreBuffer(x,y);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : newBlock
	*
	* Purpose     : Adds a new Block object. Blocks can be pushed onto floor tiles or into pits. 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int level - the level determines the default color of the block. See Thing class for details.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newBlock(int x, int y, int level) {
		removeFloor(x,y);
		bufferCell(x,y);
		blocks.put(new Pair(x,y), new Block());
		tiles.get(new Pair(x,y)).updateType(Seed.BLOCK);
		blocks.get(new Pair(x,y)).setLevel(level);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeBlock
	*
	* Purpose     : Removes a Blocks object from the blocks ArrayList.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeBlock(int x, int y) {
		blocks.remove(new Pair(x,y));
		newFloor(x,y,4);
	}

	/***************************************************************************************************************
	* Method      : newFloor
	*
	* Purpose     : Adds a new Floor object. Draws a new tile of type EMPTY.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newFloor(int x, int y, int level) {
		//if (Game.log) System.out.println("New floor created at " + x + "," + y);
		bufferCell(x,y);
		floors.put(new Pair(x,y), new Floor());
		tiles.get(new Pair(x,y)).updateType(Seed.FLOOR);
		floors.get(new Pair(x,y)).setLevel(level);
		tiles.get(new Pair(x,y)).setForeColor(floors.get(new Pair(x,y)).getForeColor());
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeFloor
	*
	* Purpose     : Removes a Floor object from the floor ArrayList.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeFloor(int x, int y) {
		floors.remove(new Pair(x,y));
	}
	
	/***************************************************************************************************************
	* Method      : newPit
	*
	* Purpose     : Adds a new Pit object. Draws a new tile of type Pit.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int level - the level determines the default color of the pit. See Pit class for details.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newPit(int x, int y, int level) {
		//Why do we have level as a parameter?
		bufferCell(x,y);
		pits.put(new Pair(x,y), new Pit());
		tiles.get(new Pair(x,y)).updateType(Seed.PIT);
		pits.get(new Pair(x,y)).setLevel(level);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removePit
	*
	* Purpose     : Removes a Pit object from the pits ArrayList.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removePit(int x, int y) {
		pits.remove(new Pair(x,y));
		newFloor(x,y,4);
	}
	
	/***************************************************************************************************************
	* Method      : manageResource
	*
	* Purpose     : Determines if a player is facing a resource object 
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	* 			  : Direction dir - an enum of directions
	*
	* Returns     : If the player is facing a resource object then this method yes
	*  
	***************************************************************************************************************/
	public boolean manageResource(int x, int y, Direction dir) {
		switch (dir) {
		case EAST:
			if (tiles.get(new Pair(x+1,y)).content.type == "mana") {
				removeMana(x+1,y);
				return true;
			}
			else {
				return false;
			}
		case SOUTH:
			if (tiles.get(new Pair(x,y-1)).content.type == "mana") {
				removeMana(x,y-1);
				return true;
			}
			else {
				return false;
			}
		case WEST:
			if (tiles.get(new Pair(x-1,y)).content.type == "mana") {
				removeMana(x-1,y);
				return true;
			}
			else {
				return false;
			}
		case NORTH:
			if (tiles.get(new Pair(x,y+1)).content.type == "mana") {
				removeMana(x,y+1);
				return true;
			}
			else {
				return false;
			}
		}
	return false; //code should never hit this line
	}
	
	/***************************************************************************************************************
	* Method      : manageFireflies
	*
	* Purpose     : Determines if a player is facing a FIREFLY enemy 
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	* 			  : Direction dir - an enum of directions
	*
	* Returns     : If the player is facing a resource object then this method yes
	*  
	***************************************************************************************************************/
	public Coord manageFireflies(int x, int y, Direction dir) {
		switch (dir) {
		case EAST:
			if (tiles.get(new Pair(x+1,y)).content == Seed.FIREFLY) {
				removeEnemy(x+1,y);
				Coord tempCoord = new Coord(x+1,y);
				return tempCoord;
			}
			else {
				Coord myCoord = new Coord(x,y);
				return myCoord;
			}
		case SOUTH:
			if (tiles.get(new Pair(x,y-1)).content == Seed.FIREFLY) {
				removeEnemy(x,y-1);
				Coord tempCoord = new Coord(x,y-1);
				return tempCoord;
			}
			else {
				Coord myCoord = new Coord(x,y);
				return myCoord;
			}
		case WEST:
			if (tiles.get(new Pair(x-1,y)).content == Seed.FIREFLY) {
				removeEnemy(x-1,y);
				Coord tempCoord = new Coord(x-1,y);
				return tempCoord;
			}
			else {
				Coord myCoord = new Coord(x,y);
				return myCoord;
			}
		case NORTH:
			if (tiles.get(new Pair(x,y+1)).content == Seed.FIREFLY) {
				removeEnemy(x,y+1);
				Coord tempCoord = new Coord(x,y+1);
				return tempCoord;
			}
			else {
				Coord myCoord = new Coord(x,y);
				return myCoord;
			}
		}
		Coord myCoord = new Coord(x,y);
		return myCoord; //code should never hit this line
	}
	
	/***************************************************************************************************************
	* Method      : manageTriggers
	*
	* Purpose     : Determines if a player is facing a trigger object 
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	* 			  : Direction dir - an enum of directions
	*
	* Returns     : If the player is facing a trigger object then this method yes
	*  
	***************************************************************************************************************/
	public boolean manageTrigger(int x, int y, Direction dir) {
		switch (dir) {
		case EAST:
			if (tiles.get(new Pair(x+1,y)).content.type == "trigger") {
				Game.tempID=triggers.get(new Pair(x+1,y)).getID();
				if (!triggers.get(new Pair(x+1,y)).getEnduresFlag()) {	
					removeTrigger(x+1,y);
				}
				return true;
			}
			else {
				return false;
			}
		case SOUTH:
			if (tiles.get(new Pair(x,y-1)).content.type == "trigger") {
				Game.tempID=triggers.get(new Pair(x,y-1)).getID();
				if (!triggers.get(new Pair(x,y-1)).getEnduresFlag()) {	
					removeTrigger(x,y-1);
				}
				return true;
			}
			else {
				return false;
			}
		case WEST:
			if (tiles.get(new Pair(x-1,y)).content.type == "trigger") {
				Game.tempID=triggers.get(new Pair(x-1,y)).getID();
				if (!triggers.get(new Pair(x-1,y)).getEnduresFlag()) {
					removeTrigger(x-1,y);
				}
				return true;
			}
			else {
				return false;
			}
		case NORTH:
			if (tiles.get(new Pair(x,y+1)).content.type == "trigger") {
				Game.tempID=triggers.get(new Pair(x,y+1)).getID();
				if (!triggers.get(new Pair(x,y+1)).getEnduresFlag()) {	
					removeTrigger(x,y+1);
				}
				return true;
			}
			else {
				return false;
			}
		}
	return false; //code should never hit this line
	}
	
	/***************************************************************************************************************
	* Method      : manageEntities
	*
	* Purpose     : Determines if an enemy is facing a player 
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	* 			  : Direction dir - an enum of directions
	*
	* Returns     : If the enemy is facing a player object then this method yes
	*  
	***************************************************************************************************************/
	public boolean manageMe(int x, int y, Direction dir) {
		switch (dir) {
		case EAST:
			return (tiles.get(new Pair(x+1,y)).content.type == "me") ? true : false;
		case SOUTH:
			return (tiles.get(new Pair(x,y-1)).content.type == "me") ? true : false;
		case WEST:
			return (tiles.get(new Pair(x-1,y)).content.type == "me") ? true : false;
		case NORTH:
			return (tiles.get(new Pair(x,y+1)).content.type == "me") ? true : false;
		}
	return false; //code should never hit this line
	}

	/***************************************************************************************************************
	* Method      : newWater
	*
	* Purpose     : Adds a new Water object. Draws a new tile of type PIT.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newWater(int x, int y) {
		bufferCell(x,y);
		water.put(new Pair(x,y), new Water());
		tiles.get(new Pair(x,y)).updateType(Seed.WATERD);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeWater
	*
	* Purpose     : Replaces water with floor
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeWater(int x,int y) {
		water.remove(new Pair(x,y));
		newFloor(x,y,4);
	}
	
	/***************************************************************************************************************
	* Method      : updateTile
	*
	* Purpose     : Changes the enum type of a tile while RETAINING all directional locks. Only used when converting
	* 				CALCs to other wall types.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Seed seed - the enum value of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateTile(int x, int y, Seed seed) {
		tiles.get(new Pair(x,y)).updateBuffer(seed);
		bufferString(x,y);	
	}
	
	/***************************************************************************************************************
	* Method      : updateColor
	*
	* Purpose     : Updates the color of the contents of a tile
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateColor(int x, int y) {
		switch (tiles.get(new Pair(x,y)).content.type) {
			case "wall":
				tiles.get(new Pair(x,y)).setForeColor(walls.get(new Pair(x,y)).getForeColor());
				tiles.get(new Pair(x,y)).setBackColor(walls.get(new Pair(x,y)).getBackColor());
				break;
			case "earth":
				tiles.get(new Pair(x,y)).setForeColor(earth.get(new Pair(x,y)).getForeColor());
				tiles.get(new Pair(x,y)).setBackColor(earth.get(new Pair(x,y)).getBackColor());
				break;
			case "plant":
				tiles.get(new Pair(x,y)).setForeColor(earth.get(new Pair(x,y)).getForeColor());
				tiles.get(new Pair(x,y)).setBackColor(earth.get(new Pair(x,y)).getBackColor());
				break;
			case "pit":
				tiles.get(new Pair(x,y)).setForeColor(pits.get(new Pair(x,y)).getForeColor());
				tiles.get(new Pair(x,y)).setBackColor(pits.get(new Pair(x,y)).getBackColor());
				break;
			case "mana":
				tiles.get(new Pair(x,y)).setForeColor(resources.get(new Pair(x,y)).getForeColor());
				tiles.get(new Pair(x,y)).setBackColor(resources.get(new Pair(x,y)).getBackColor());
				break;	
			case "stair":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.MAGENTA);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "trigger":
//				System.out.println(tiles.get(new Pair(x,y)).getSeed());
				if (tiles.get(new Pair(x,y)).getSeed() == Seed.TRIGGERG) {
					tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.GREEN);
					tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				}
				else if (tiles.get(new Pair(x,y)).getSeed() == Seed.TRIGGERV) {
					tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.MAGENTA);
					tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				}
				else if (tiles.get(new Pair(x,y)).getSeed() == Seed.TRIGGERF) {
					tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.WHITE);
					tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				}
				else if (tiles.get(new Pair(x,y)).getSeed() == Seed.TRIGGERS) {
					tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.CYAN);
					tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				}
				else if (tiles.get(new Pair(x,y)).getSeed() == Seed.HELP) {
					tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.WHITE);
					tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				}
				else {
					tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.RED);
					tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				}
				//tiles.get(new Pair(x,y)).setBackColor(triggers.get(new Pair(x,y)).getBackColor());
				break;
			case "enemy":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.RED);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "floor":
				//need to figure out how to update all floors/walls etc. when dig is turned on. May require a rerender of screen
				//if (Game.spirit != Spirit.DIG) {
					tiles.get(new Pair(x,y)).setForeColor(floors.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(floors.get(new Pair(x,y)).getBackColor());
				//}
				break;
			case "block":
				tiles.get(new Pair(x,y)).setForeColor(blocks.get(new Pair(x,y)).getForeColor());
				tiles.get(new Pair(x,y)).setBackColor(blocks.get(new Pair(x,y)).getBackColor());
				break;
			case "waters":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.CYAN);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;	
			case "waterd":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.BLUE);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "cow":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.WHITE);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "tree":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.BLACK);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.GREEN);
				break;
			case "ground":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.BLACK);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.GREEN);
				break;				
			case "emote":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.CYAN);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "empty":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.BLACK);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "grass":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.GREEN);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "bush":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.BLACK);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.GREEN);
				break;
			case "flr":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.WHITE);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "furrow":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.GREEN);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "rock":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.YELLOW);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			case "cabbage":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.GREEN);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			//BUILD IN VARIATIONS DEPENDING ON WHAT PLAYING IS STANDING ON
			case "me":
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.WHITE);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
				break;
			default: 
				tiles.get(new Pair(x,y)).setForeColor(Terminal.Color.CYAN);
				tiles.get(new Pair(x,y)).setBackColor(Terminal.Color.BLACK);
		}
		//if (Game.log) System.out.println("Updating color at " + x + " " + y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : overrideTile
	*
	* Purpose     : Changes the enum type of a tile while REMOVING all directional locks  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Seed contents - the enum value of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void overrideTile(int x, int y, Seed content) {
		tiles.get(new Pair(x,y)).overrideBuffer(content);
		//This isn't working because no Walls exist in the new locations yet. When I add that code in carve, then I can uncomment this
		//tiles.get(new Pair(x,y)).setForeColor(walls.get(new Pair(x,y)).getForeColor());
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : drawTile
	*
	* Purpose     : Draws a seed at the specified coordinates either
	*
	* Parameters  :	int x - the x coordinate of the tile to be drawn
	* 			  : int y - the y coordinate of the tile to be drawn
	* 			  :	isLit - if true the tile is on by default, if false the tile is off by default
	* 				NOTE: Use isLit = on for title screens
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void drawTile(int x, int y, Seed seed, boolean isLit) {
		bufferCell(x,y);
		tiles.get(new Pair(x,y)).updateType(seed);
		if (isLit) {
			tiles.get(new Pair(x,y)).setLight(true);
		}
		else {
			tiles.get(new Pair(x,y)).setLight(false);
		}
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : newTitle
	*
	* Purpose     : Fills in title screen with empty space
	*
	* Parameters  :	None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newTitle() {
		// Fills the screen with empty Cells
		for(int y=topScreen; y>=bottomScreen; y--){
	          for (int x=leftScreen; x<=rightScreen; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  newEarth(x,y,2,Seed.SPACE);
	          }
		}
	}
	
	/***************************************************************************************************************
	* Method      : calcLevel()
	*
	* Purpose     : Converts all Cells in variable tiles with an enum type of CALC to their respective
	* 				Unicode character. This is accomplished by evaluating each tile of type "wall", assigning a 
	* 				4-bit binary number based on the proximity of other "walls" in the four cardinal directions, 
	* 				then assigning a Unicode character based on the pattern of the binary number generated. Special 
	* 				cases exist with 3 and 4-way intersections where one or more branches must be coerced to a disabled 
	* 				state to prevent over-branching of adjacent walls.
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void calcLevel() {
		//don't buffer any CALC outside screen limits
		for(int y=topScreen-1; y>bottomScreen+1; y--){
			for (int x = leftScreen + 1; x < rightScreen -1; x++) { 
				if (tiles.get(new Pair(x,y)).getType() == "wall") {
	        		  //create a 4-bit binary number to store the on/off state of cardinal tiles
	        		  int pattern = 0b0000;
	        		  if (tiles.get(new Pair(x+1,y)).getType() == "wall") {
	        			  //bitshift 1 into position 0. NOTE: Bitwise | (or) won't overwrite current bits
	        			  pattern = pattern | (0x1 << 0);
	        		  }
	        		  if (tiles.get(new Pair(x,y-1)).getType() == "wall") {
	        			  //bitshift 1 into position 1
	        			  pattern = pattern | (0x1 << 1);
	        		  }
	        		  if (tiles.get(new Pair(x-1,y)).getType() == "wall") {
	        			  //bitshift 1 into position 2
	        			  pattern = pattern | (0x1 << 2);
	        		  }
	        		  if (tiles.get(new Pair(x,y+1)).getType() == "wall") {
	        			  //bitshift 1 into position 3
	        			  pattern = pattern | (0x1 << 3);
	        		  }
	        		  // System.out.printf("(%d" + "," + "%d) ",x,y);
	        		  // System.out.println(Integer.toString(pattern,2));
	        		  // System.out.println();
	        		  
	        		  // Sometimes a T or 4-way intersection cell needs to be coerced into a new configuration
	        		  // Disable coerced bits using bitwise not (~) and binary mask it to pattern with bitwise (&)
	        		  if (pattern == 0b1011 || pattern == 0b1101 || pattern == 0b1110 || pattern == 0b0111 || pattern == 0b1111) {
	        			  //Build in a construct that unlocks a tile when it no longer needs to be coerced
	        			  if (tiles.get(new Pair(x+1,y)).wlock == true || tiles.get(new Pair(x,y)).elock == true)  {
	        				  pattern &= ~0b0001;  
	        			  }
	        			  if (tiles.get(new Pair(x,y-1)).nlock == true || tiles.get(new Pair(x,y)).slock == true) {
	        				  pattern &= ~0b0010;  
	        			  }
	        			  if (tiles.get(new Pair(x-1,y)).elock == true || tiles.get(new Pair(x,y)).wlock == true) {
	        				  pattern &= ~0b0100;
	        			  }
	        			  if (tiles.get(new Pair(x,y+1)).slock == true || tiles.get(new Pair(x,y)).nlock == true) {  
	        				  pattern &= ~0b1000;
	        			  }
	        		  } 	
	        		  
	        		  // These updateTiles cannot be converted to updateTypes. Investigate.
	        		  switch (pattern){
		        		  case 0b1010:
		        			  updateTile(x,y,Seed.VD);
		        			  break;
		        		  case 0b0101:
		        			  updateTile(x,y,Seed.HD);
		        			  break;
		        		  case 0b0011:
		        			  updateTile(x,y,Seed.NWD);
		        			  break;
		        		  case 0b1100:
		        			  updateTile(x,y,Seed.SED);
		        			  break;
		        		  case 0b1001:
		        			  updateTile(x,y,Seed.SWD);
		        			  break;
		        		  case 0b0110:
		        			  updateTile(x,y,Seed.NED);
		        			  break;
		        		  case 0b1101:
		        			  updateTile(x,y,Seed.STD);
		        			  break;
		        		  case 0b0111:
		        			  updateTile(x,y,Seed.NTD);
		        			  break;
		        		  case 0b1011:
		        			  updateTile(x,y,Seed.WTD);
		        			  break;
		        		  case 0b1110:
		        			  updateTile(x,y,Seed.ETD);
		        			  break;
		        		  case 0b1111:
		        			  updateTile(x,y,Seed.FWD);
		        			  break;
		        		  case 0b0001:
		        			  updateTile(x,y,Seed.HD);
		        			  break;
		        		  case 0b0100:
		        			  updateTile(x,y,Seed.HD);
		        			  break;
		        		  case 0b0010:
		        			  updateTile(x,y,Seed.VD);
		        			  break;
		        		  case 0b1000:
		        			  updateTile(x,y,Seed.VD);
		        			  break;
		        		  case 0b0000: //case of a stranded CALC cell
		        			  int tempLevel = walls.get(new Pair(x,y)).getLevel();
		        			  removeWall(x,y);
		        			  newBlock(x,y,1);
		        			  blocks.get(new Pair(x,y)).setLevel(tempLevel);
		        			  updateColor(x,y);
		        			  break;
		        		  default:
		        			  System.out.println("Leak in map");
	        		  }
	        		  Game.screen.refresh();	
				}
			}
		}
	} //end calcLevel
	
	/***************************************************************************************************************
	* Method      : detectCollision(int x, int y, Direction dir)
	*
	* Purpose     : determines if there is a collision in cell adjacent to the player in the direction passed
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction of the player passed
	*
	* Returns     : Returns a boolean of true if a collision is detected in the passed direction.
	*  
	***************************************************************************************************************/
	public boolean detectCollision(int x, int y, Direction dir) {	
		switch (dir) {
			case EAST:
				if (tiles.get(new Pair(x+1,y)).content.type == "waterd") Game.isRiver = true;
				return (tiles.get(new Pair(x+1,y)).getCollideState() ) ? true : false;
			case SOUTH:
				return (tiles.get(new Pair(x,y-1)).getCollideState()) ? true : false;
			case WEST:
				if (tiles.get(new Pair(x-1,y)).content.type == "waterd") Game.isRiver = true;
				return (tiles.get(new Pair(x-1,y)).getCollideState()) ? true : false;
			case NORTH:
				return (tiles.get(new Pair(x,y+1)).getCollideState()) ? true : false;
		}
		return false; //code should never hit this line
	} 
	
	/***************************************************************************************************************
	* Method      : digMap(int x, int y, Direction dir)
	*
	* Purpose     : A player can dig when facing any wall or floor tile that is not colored white. This method determines the
	* 				type of tile a player is facing, inspects its corresponding wall or floor object to determines its
	* 				current durability (level), decrements its durability, then changes its color to reflect its new 
	* 				durability, or removes the floor or wall tile if its durability reaches 0. When a wall tile reaches
	* 				0 durability its surrounding tiles are manipulated (via the caveWall() method). When a floor tile reaches
	* 				0 durability it is re-rendered as a pit.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction to carve
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
 	public void digMap(int x, int y, Direction dir){
		boolean isDestroyed;
 		switch(dir) {
 			//update this code for blocks as non-wall objects
			case EAST:
				if (tiles.get(new Pair(x+1,y)).getType() == "pit") {
					isDestroyed = pits.get(new Pair(x+1,y)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit maxed");
					}
					else {
						if (Game.log) System.out.println("Pit increased");
						updateColor(x+1,y);
					}
				}
				if (tiles.get(new Pair(x+1,y)).getType() == "floor" && floors.get(new Pair(x+1,y)).getLevel() != 4) {
					isDestroyed = floors.get(new Pair(x+1,y)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit dug");
						removeFloor(x+1,y);
						newPit(x+1,y,1);
					}
					else {
						if (Game.log) System.out.println("Digging east");
						updateColor(x+1,y);
					}
				}
				if (tiles.get(new Pair(x+1,y)).getType() == "wall"  && walls.get(new Pair(x+1,y)).getLevel() != 4) {
					isDestroyed = walls.get(new Pair(x+1,y)).cycleLevel();	
					if (isDestroyed) {
						carveWall(x,y,dir);
					}
					else {
						updateColor(x+1,y);
					}
				}
				if (tiles.get(new Pair(x+1,y)).getType() == "block" && blocks.get(new Pair(x+1,y)).getLevel() != 4) {
					isDestroyed = blocks.get(new Pair(x+1,y)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x+1,y);
					}
					else {
						updateColor(x+1,y);
					}
				}
				break;
			case SOUTH:
				if (tiles.get(new Pair(x,y-1)).getType() == "pit") {
					isDestroyed = pits.get(new Pair(x,y-1)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit maxed");
					}
					else {
						if (Game.log) System.out.println("Pit increased");
						updateColor(x,y-1);
					}
				}
				if (tiles.get(new Pair(x,y-1)).getType() == "floor" && floors.get(new Pair(x,y-1)).getLevel() != 4) {
					isDestroyed = floors.get(new Pair(x,y-1)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit dug");
						removeFloor(x,y-1);
						newPit(x,y-1,1);
					}
					else {
						if (Game.log) System.out.println("Digging south");
						updateColor(x,y-1);
					}
				}
				if (tiles.get(new Pair(x,y-1)).getType() == "wall" && walls.get(new Pair(x,y-1)).getLevel() != 4) {
					isDestroyed = walls.get(new Pair(x,y-1)).cycleLevel();
					if (isDestroyed) {
						carveWall(x,y,dir);
					}
					else {
						updateColor(x,y-1);
					}
				}
				if (tiles.get(new Pair(x,y-1)).getType() == "block" && blocks.get(new Pair(x,y-1)).getLevel() != 4) {
					isDestroyed = blocks.get(new Pair(x,y-1)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x,y-1);
					}
					else {
						updateColor(x,y-1);
					}
				}
				break;
			case WEST:
				if (tiles.get(new Pair(x-1,y)).getType() == "pit") {
					isDestroyed = pits.get(new Pair(x-1,y)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit maxed");
					}
					else {
						if (Game.log) System.out.println("Pit increased");
						updateColor(x-1,y);
					}
				}
				if (tiles.get(new Pair(x-1,y)).getType() == "floor" && floors.get(new Pair(x-1,y)).getLevel() != 4) {
					isDestroyed = floors.get(new Pair(x-1,y)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit dug");
						removeFloor(x-1,y);
						newPit(x-1,y,1);
					}
					else {
						if (Game.log) System.out.println("Digging west");
						updateColor(x-1,y);
					}
				}
				if (tiles.get(new Pair(x-1,y)).getType() == "wall" && walls.get(new Pair(x-1,y)).getLevel() != 4) {
					isDestroyed = walls.get(new Pair(x-1,y)).cycleLevel();
					if (isDestroyed) {
						carveWall(x,y,dir);
					}
					else {
						updateColor(x-1,y);
					}		
				}
				if (tiles.get(new Pair(x-1,y)).getType() == "block" && blocks.get(new Pair(x-1,y)).getLevel() != 4) {
					isDestroyed = blocks.get(new Pair(x-1,y)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x-1,y);
					}
					else {
						updateColor(x-1,y);
					}
				}
				break;
			case NORTH:
				if (tiles.get(new Pair(x,y+1)).getType() == "pit") {
					isDestroyed = pits.get(new Pair(x,y+1)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit maxed");
					}
					else {
						if (Game.log) System.out.println("Pit increased");
						updateColor(x,y+1);
					}
				}
				if (tiles.get(new Pair(x,y+1)).getType() == "floor" && floors.get(new Pair(x,y+1)).getLevel() != 4) {
					isDestroyed = floors.get(new Pair(x,y+1)).cycleLevel();
					if (isDestroyed) {
						if (Game.log) System.out.println("Pit dug");
						removeFloor(x,y+1);
						newPit(x,y+1,1);
					}
					else {
						if (Game.log) System.out.println("Digging north");
						updateColor(x,y+1);
					}
				}
				if (tiles.get(new Pair(x,y+1)).getType() == "wall" && walls.get(new Pair(x,y+1)).getLevel() != 4) {
					isDestroyed = walls.get(new Pair(x,y+1)).cycleLevel();
					if (isDestroyed) {
						carveWall(x,y,dir);
					}
					else {
						updateColor(x,y+1);
					}		
				}
				if (tiles.get(new Pair(x,y+1)).getType() == "block" && blocks.get(new Pair(x,y+1)).getLevel() != 4) {
					isDestroyed = blocks.get(new Pair(x,y+1)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x,y+1);
					}
					else {
						updateColor(x,y+1);
					}
				}
				break;
		}
 		Game.screen.refresh();
	}
 	
 	/**************************************************************************************************************	
 	* Method      : pushBlock(int x, int y, Direction dir)
	*
	* Purpose     : Determines if there is space behind a block for it to be pushed and moves it if true
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction to carve
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
 	public boolean pushBlock(int x, int y, Direction dir){
		boolean isBlocked;
 		switch(dir) {
			case EAST:
				if (tiles.get(new Pair(x+1,y)).getType() == "block" && blocks.get(new Pair(x+1,y)).getLevel() != 4) {
					isBlocked = detectCollision(x+1,y,dir);
					//get rid of this crap
					if (Game.isRiver) isBlocked = false;
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						System.out.println(tiles.get(new Pair(x+2,y)).getType());
						if (Game.log) System.out.println("Not Blocked");
						if (tiles.get(new Pair(x+2,y)).getType() == "pit") {
							removePit(x+2,y);
							removeBlock(x+1,y);
							//Display message that pit is covered
						}
						if (tiles.get(new Pair(x+2,y)).getType() == "waterd") {
							removeWater(x+2,y);
							removeBlock(x+1,y);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x+1,y)).getLevel();	
							bufferCell(x+2,y);
							removeBlock(x+1,y);
							newBlock(x+2,y,tempLevel);
		        			updateColor(x+2,y);
						}
						Game.screen.refresh();
						return true;
					}
					//else return true;
				}
				break;
			case SOUTH:
				if (tiles.get(new Pair(x,y-1)).getType() == "block" && blocks.get(new Pair(x,y-1)).getLevel() != 4) {
					isBlocked = detectCollision(x,y-1,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						if (Game.log) System.out.println("Not Blocked");
						if (tiles.get(new Pair(x,y-2)).getType() == "pit") {
							removePit(x,y-2);
							removeBlock(x,y-1);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x,y-1)).getLevel();	
							bufferCell(x,y-2);
							removeBlock(x,y-1);
							newBlock(x,y-2,tempLevel);
		        			updateColor(x,y-2);
						}
						Game.screen.refresh();
						return true;
					}
					//else return true;
				}
				break;
			case WEST:
				System.out.println(tiles.get(new Pair(x-2,y)).getType());
				if (tiles.get(new Pair(x-2,y)).getType() == "trigger") Game.overTrigger = true;
				if (tiles.get(new Pair(x-1,y)).getType() == "block" && blocks.get(new Pair(x-1,y)).getLevel() != 4) {
					isBlocked = detectCollision(x-1,y,dir);
					//get rid of this crap
					if (Game.isRiver) isBlocked = false;
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						if (Game.log) System.out.println("Not Blocked");
						if (tiles.get(new Pair(x-2,y)).getType() == "pit") {
							removePit(x-2,y);
							removeBlock(x-1,y);
							//Display message that pit is covered
						}
						if (tiles.get(new Pair(x-2,y)).getType() == "waterd") {
							removeWater(x-2,y);
							removeBlock(x-1,y);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x-1,y)).getLevel();	
							bufferCell(x-2,y);
							removeBlock(x-1,y);
							newBlock(x-2,y,tempLevel);
		        			updateColor(x-2,y);
						}
						Game.screen.refresh();
						return true;
					}
					//else return true;
				}
				break;
			case NORTH:
				if (tiles.get(new Pair(x,y+1)).getType() == "block" && blocks.get(new Pair(x,y+1)).getLevel() != 4) {
					isBlocked = detectCollision(x,y+1,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						if (Game.log) System.out.println("Not Blocked");
						if (tiles.get(new Pair(x,y+2)).getType() == "pit") {
							removePit(x,y+2);
							removeBlock(x,y+1);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x,y+1)).getLevel();	
							System.out.println("Pushing a level" + tempLevel + " block");
							bufferCell(x,y+2);
							removeBlock(x,y+1);
							//what is in buffer in x,y+1?
							newBlock(x,y+2,tempLevel);
		        			updateColor(x,y+2);
						}
						Game.screen.refresh();
						return true;
					}
					//else return true;
				}
				break;
		}
 		return false;
 		
	}
	
 	/***************************************************************************************************************
	* Method      : dropBlock
	*
	* Purpose     : Drop a block that becomes a Wall object
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Direction dir - the direction the player is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
 	public void dropBlock(int x, int y, Direction dir) {
		switch(dir) {
		case EAST:
			if (tiles.get(new Pair(x+1,y)).getType() == "floor") {
				removeFloor(x+1,y);
				newWall(x+1,y,1);
				overrideTile(x+1,y,Seed.CALC);
			}
			break;
		case SOUTH:
			if (tiles.get(new Pair(x,y-1)).getType() == "floor") {
				removeFloor(x,y-1);
				newWall(x,y-1,1);
				overrideTile(x,y-1,Seed.CALC);
			}
			break;
		case WEST:
			if (tiles.get(new Pair(x-1,y)).getType() == "floor") {
				removeFloor(x-1,y);
				newWall(x-1,y,1);
				overrideTile(x-1,y,Seed.CALC);
			}
			break;
		case NORTH:
			if (tiles.get(new Pair(x,y+1)).getType() == "floor") {
				removeFloor(x,y+1);
				newWall(x,y+1,1);
				overrideTile(x,y+1,Seed.CALC);
			}
			break;
		}
		calcLevel();
		Game.screen.refresh();
 	}
 	
 	/***************************************************************************************************************
	* Method      : bufferCell(int x, int y)
	*
	* Purpose     : Places the contents of the cell at coordinates x,y into a buffer so something new can occupy
	* 				this cell.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void bufferCell(int x, int y) {	
		tiles.get(new Pair(x,y)).pushBuffer(x,y);} 
	
	/***************************************************************************************************************
	* Method      : restoreBuffer(int x, int y)
	*
	* Purpose     : restores the buffered content at the specified x,y coordinate
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void restoreBuffer(int x, int y) {
		tiles.get(new Pair(x,y)).restoreBuffer(x, y);
		updateColor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : moveEntity(int x, int y, Direction dir, int lanternRadius)
	*
	* Purpose     : When a player moves into another cell, it restores the buffered contents of the tile he is leaving 
	* 				while buffers the contents of the cell he is moving into.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction the player is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void moveEntity(int x, int y, Direction dir, Seed symbol, int lanternRadius, EntityType entity) {
		switch(dir) {
			case EAST:
				if (tiles.get(new Pair(x+1,y)).content.type == "pit") {
					Game.isTrapped = true;
				}
				if (tiles.get(new Pair(x+1,y)).getSeed() == Seed.PC && entity == EntityType.PLAYER) {
					restoreBuffer(x+1,y);
				}
				bufferCell(x+1,y);
				tiles.get(new Pair(x+1,y)).updateType(symbol);
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				restoreBuffer(x,y);
				updateLantern(x+1,y,lanternRadius);
				break;
			case SOUTH:
				if (tiles.get(new Pair(x,y-1)).content.type == "pit") {
					Game.isTrapped = true;
				}
				if (tiles.get(new Pair(x,y-1)).getSeed() == Seed.PC && entity == EntityType.PLAYER) {
					restoreBuffer(x,y-1);
				}
				bufferCell(x,y-1);
				tiles.get(new Pair(x,y-1)).updateType(symbol);
				
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				restoreBuffer(x,y);
				updateLantern(x,y-1,lanternRadius);
				if (tiles.get(new Pair(x,y-1)).content.type == "pit") {
					Game.isTrapped = true;
				}
				break;
			case WEST:
				if (tiles.get(new Pair(x-1,y)).content.type == "pit") {
					Game.isTrapped = true;
				}
				if (tiles.get(new Pair(x-1,y)).getSeed() == Seed.PC && entity == EntityType.PLAYER) {
					restoreBuffer(x-1,y);
				}
				bufferCell(x-1,y);
				tiles.get(new Pair(x-1,y)).updateType(symbol);
				
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				restoreBuffer(x,y);
				updateLantern(x-1,y,lanternRadius);
				if (tiles.get(new Pair(x-1,y)).content.type == "pit") {
					Game.isTrapped = true;
				}
				break;
			case NORTH:
				if (tiles.get(new Pair(x,y+1)).content.type == "pit") {
					Game.isTrapped = true;
				}
				if (tiles.get(new Pair(x,y+1)).getSeed() == Seed.PC && entity == EntityType.PLAYER) {
					restoreBuffer(x,y+1);
				}
				bufferCell(x,y+1);
				tiles.get(new Pair(x,y+1)).updateType(symbol);
			
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				restoreBuffer(x,y);
				updateLantern(x,y+1,lanternRadius);
				if (tiles.get(new Pair(x,y+1)).content.type == "pit") {
					Game.isTrapped = true;
				}
				break;
			case NONE:
				;
				break;
		}
		tiles.get(new Pair(x,y)).setLight(true); //in the event that the lantern turns this bit off
		Game.screen.refresh();
	}
	
	public void avatarFollow(int playerX, int playerY, int pixieX, int pixieY, Seed pixieSymbol, int lanternRadius) {
		tiles.get(new Pair(playerX,playerY)).updateType(pixieSymbol);
		//bufferString(playerX,playerY);
		restoreBuffer(pixieX,pixieY);
		if (Game.lantern != Lantern.FOW) {
			blackLantern(pixieX,pixieY,lanternRadius);
		}
		updateLantern(playerX,playerY,lanternRadius);
	}

	/***************************************************************************************************************
	* Method      : updateSymbol(int x, int y, Seed symbol)
	*
	* Purpose     : Updates the Unicode character associated with the direction the avatar is facing when the cell
	* 				doesn't require a lantern update
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction the player is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateSymbol(int x, int y, Seed symbol) {
		tiles.get(new Pair(x,y)).updateType(symbol);
		bufferString(x,y);
		Game.screen.refresh();
	} 
	
	/***************************************************************************************************************
	* Method      : updateSymbol(int x, int y, Seed symbol, int lanternRadius)
	*
	* Purpose     : Updates the Unicode character associated with the direction the avatar is facing when the cell
	* 				DOES require a lantern update
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Seed symbol - the Unicode character to write to the buffer
	* 			  : int lanternRadius - the radius of the lantern
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateSymbol(int x, int y, Seed symbol, int lanternRadius) {
		tiles.get(new Pair(x,y)).updateType(symbol);
		bufferString(x,y);
		updateLantern(x,y,lanternRadius);
		Game.screen.refresh();
	} 
	
	/***************************************************************************************************************
	* Method      : carveWall(int x, int y, Direction dir)
	*
	* Purpose     : Recalculates walls upon the removal of a wall
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction the player is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void carveWall(int x, int y, Direction dir) {	
		switch(dir){
			case EAST:
				removeWall(x+1,y);
				if (tiles.get(new Pair(x+1,y-1)).getType() != "floor") {
					//need to call newWall if wall, otherwise need to get the ForeColor, which overrideTile does not do
					typeWall(x+1,y-1);
					if (tiles.get(new Pair(x+1,y-2)).content == Seed.HD || tiles.get(new Pair(x+1,y-2)).content == Seed.NED || tiles.get(new Pair(x+1,y-2)).content == Seed.NWD || tiles.get(new Pair(x+1,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y-2)).nlock = true;
		
					}
					if (tiles.get(new Pair(x,y-1)).content == Seed.VD || tiles.get(new Pair(x,y-1)).content == Seed.SED || tiles.get(new Pair(x,y-1)).content == Seed.NED || tiles.get(new Pair(x,y-1)).content == Seed.ETD) {
						tiles.get(new Pair(x,y-1)).elock = true;
					}
				}
				if (tiles.get(new Pair(x+1,y+1)).getType() != "floor") {
					typeWall(x+1,y+1);
					if (tiles.get(new Pair(x+1,y+2)).content == Seed.HD || tiles.get(new Pair(x+1,y+2)).content == Seed.SED || tiles.get(new Pair(x+1,y+2)).content == Seed.SWD || tiles.get(new Pair(x+1,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x+1,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x,y+1)).content == Seed.VD || tiles.get(new Pair(x,y+1)).content == Seed.SED || tiles.get(new Pair(x,y+1)).content == Seed.NED || tiles.get(new Pair(x,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x,y+1)).elock = true;
					}
				}
				if (tiles.get(new Pair(x+2,y-1)).getType() != "floor") {
					typeWall(x+2,y-1);
					if (tiles.get(new Pair(x+2,y-2)).content == Seed.HD || tiles.get(new Pair(x+2,y-2)).content == Seed.NED || tiles.get(new Pair(x+2,y-2)).content == Seed.NWD || tiles.get(new Pair(x+2,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x+2,y-2)).nlock = true;
					}
					if (tiles.get(new Pair(x+3,y-1)).content == Seed.VD || tiles.get(new Pair(x+3,y-1)).content == Seed.SWD || tiles.get(new Pair(x+3,y-1)).content == Seed.NWD || tiles.get(new Pair(x+3,y-1)).content == Seed.WTD) {
						tiles.get(new Pair(x+3,y-1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x+2,y+1)).getType() != "floor") {
					typeWall(x+2,y+1);
					if (tiles.get(new Pair(x+2,y+2)).content == Seed.HD || tiles.get(new Pair(x+2,y+2)).content == Seed.SED || tiles.get(new Pair(x+2,y+2)).content == Seed.SWD || tiles.get(new Pair(x+2,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x+2,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x+3,y+1)).content == Seed.VD || tiles.get(new Pair(x+3,y+1)).content == Seed.SWD || tiles.get(new Pair(x+3,y+1)).content == Seed.NWD || tiles.get(new Pair(x+3,y+1)).content == Seed.WTD) {
						tiles.get(new Pair(x+3,y+1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x+2,y)).getType() != "floor") {
					typeWall(x+2,y);
					if (tiles.get(new Pair(x+3,y)).content == Seed.VD || tiles.get(new Pair(x+3,y)).content == Seed.SWD || tiles.get(new Pair(x+3,y)).content == Seed.NWD || tiles.get(new Pair(x+3,y)).content == Seed.WTD) {
						tiles.get(new Pair(x+3,y)).wlock = true;
					}
				}
			
				break;	
			case SOUTH: 
				removeWall(x,y-1);
				if (tiles.get(new Pair(x-1,y-1)).getType() != "floor") {
					typeWall(x-1,y-1);
					if (tiles.get(new Pair(x-2,y-1)).content == Seed.VD || tiles.get(new Pair(x-2,y-1)).content == Seed.SED || tiles.get(new Pair(x-2,y-1)).content == Seed.NED || tiles.get(new Pair(x-2,y-1)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y-1)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y)).content == Seed.HD || tiles.get(new Pair(x-1,y)).content == Seed.SED || tiles.get(new Pair(x-1,y)).content == Seed.SWD || tiles.get(new Pair(x-1,y)).content == Seed.STD) {
						tiles.get(new Pair(x-1,y)).slock = true;
					}
				}
				if (tiles.get(new Pair(x+1,y-1)).getType() != "floor") {
					typeWall(x+1,y-1);
					if (tiles.get(new Pair(x+2,y-1)).content == Seed.VD || tiles.get(new Pair(x+2,y-1)).content == Seed.SWD || tiles.get(new Pair(x+2,y-1)).content == Seed.NWD || tiles.get(new Pair(x+2,y-1)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y-1)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y)).content == Seed.HD || tiles.get(new Pair(x+1,y)).content == Seed.SED || tiles.get(new Pair(x+1,y)).content == Seed.SWD || tiles.get(new Pair(x+1,y)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y)).slock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y-2)).getType() != "floor") {
					typeWall(x-1,y-2);
					if (tiles.get(new Pair(x-2,y-2)).content == Seed.VD || tiles.get(new Pair(x-2,y-2)).content == Seed.SED || tiles.get(new Pair(x-2,y-2)).content == Seed.NED || tiles.get(new Pair(x-2,y-2)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y-2)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y-3)).content == Seed.HD || tiles.get(new Pair(x-1,y-3)).content == Seed.NED || tiles.get(new Pair(x-1,y-3)).content == Seed.NWD || tiles.get(new Pair(x-1,y-3)).content == Seed.NTD) {
						tiles.get(new Pair(x-1,y-3)).nlock = true;
					}	
				}
				if (tiles.get(new Pair(x+1,y-2)).getType() != "floor") {
					typeWall(x+1,y-2);
					if (tiles.get(new Pair(x+2,y-2)).content == Seed.VD || tiles.get(new Pair(x+2,y-2)).content == Seed.SWD || tiles.get(new Pair(x+2,y-2)).content == Seed.NWD || tiles.get(new Pair(x+2,y-2)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y-2)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y-3)).content == Seed.HD || tiles.get(new Pair(x+1,y-3)).content == Seed.NED || tiles.get(new Pair(x+1,y-3)).content == Seed.NWD || tiles.get(new Pair(x+1,y-3)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y-3)).nlock = true;
					}
				}
				if (tiles.get(new Pair(x,y-2)).getType() != "floor") {
					typeWall(x,y-2);
					if (tiles.get(new Pair(x,y-3)).content == Seed.HD || tiles.get(new Pair(x,y-3)).content == Seed.NED || tiles.get(new Pair(x,y-3)).content == Seed.NWD || tiles.get(new Pair(x,y-3)).content == Seed.NTD) {
						tiles.get(new Pair(x,y-3)).nlock = true;
					}
				}
				
				break;
			case WEST:
				removeWall(x-1,y);
				if (tiles.get(new Pair(x-1,y-1)).getType() != "floor") {
					typeWall(x-1,y-1);
					if (tiles.get(new Pair(x-1,y-2)).content == Seed.HD || tiles.get(new Pair(x-1,y-2)).content == Seed.NED || tiles.get(new Pair(x-1,y-2)).content == Seed.NWD || tiles.get(new Pair(x-1,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x-1,y-2)).nlock = true;
					}
					if (tiles.get(new Pair(x,y-1)).content == Seed.VD || tiles.get(new Pair(x,y-1)).content == Seed.SWD || tiles.get(new Pair(x,y-1)).content == Seed.NWD || tiles.get(new Pair(x,y-1)).content == Seed.WTD) {
						tiles.get(new Pair(x,y-1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y+1)).getType() != "floor") {
					typeWall(x-1,y+1);
					if (tiles.get(new Pair(x-1,y+2)).content == Seed.HD || tiles.get(new Pair(x-1,y+2)).content == Seed.SED || tiles.get(new Pair(x-1,y+2)).content == Seed.SWD || tiles.get(new Pair(x-1,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x-1,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x,y+1)).content == Seed.VD || tiles.get(new Pair(x,y+1)).content == Seed.SWD || tiles.get(new Pair(x,y+1)).content == Seed.NWD || tiles.get(new Pair(x,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x,y+1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x-2,y-1)).getType() != "floor") {
					typeWall(x-2,y-1);
					if (tiles.get(new Pair(x-2,y-2)).content == Seed.HD || tiles.get(new Pair(x-2,y-2)).content == Seed.NED || tiles.get(new Pair(x-2,y-2)).content == Seed.NWD || tiles.get(new Pair(x-2,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x-2,y-2)).nlock = true;
					}
					if (tiles.get(new Pair(x-3,y-1)).content == Seed.VD || tiles.get(new Pair(x-3,y-1)).content == Seed.SED || tiles.get(new Pair(x-3,y-1)).content == Seed.NED || tiles.get(new Pair(x-3,y-1)).content == Seed.ETD) {
						tiles.get(new Pair(x-3,y-1)).elock = true;
					}
				}
				if (tiles.get(new Pair(x-2,y+1)).getType() != "floor") {
					typeWall(x-2,y+1);
					if (tiles.get(new Pair(x-2,y+2)).content == Seed.HD || tiles.get(new Pair(x-2,y+2)).content == Seed.SED || tiles.get(new Pair(x-2,y+2)).content == Seed.SWD || tiles.get(new Pair(x-2,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x-2,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x-3,y+1)).content == Seed.VD || tiles.get(new Pair(x-3,y+1)).content == Seed.SED || tiles.get(new Pair(x-3,y+1)).content == Seed.NED || tiles.get(new Pair(x-3,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x-3,y+1)).elock = true;				
					}
				}
				if (tiles.get(new Pair(x-2,y)).getType() != "floor") {
					typeWall(x-2,y);
					if (tiles.get(new Pair(x-3,y)).content == Seed.VD || tiles.get(new Pair(x-3,y)).content == Seed.SED || tiles.get(new Pair(x-3,y)).content == Seed.NED || tiles.get(new Pair(x-3,y)).content == Seed.ETD) {
						tiles.get(new Pair(x-3,y)).elock = true;
					}
				}
				break;
			case NORTH:
				removeWall(x,y+1);
				if (tiles.get(new Pair(x+1,y+1)).getType() != "floor") {
					typeWall(x+1,y+1);
					if (tiles.get(new Pair(x+2,y+1)).content == Seed.VD || tiles.get(new Pair(x+2,y+1)).content == Seed.SWD || tiles.get(new Pair(x+2,y+1)).content == Seed.NWD || tiles.get(new Pair(x+2,y+1)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y+1)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y)).content == Seed.HD || tiles.get(new Pair(x+1,y)).content == Seed.NED || tiles.get(new Pair(x+1,y)).content == Seed.NWD || tiles.get(new Pair(x+1,y)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y)).nlock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y+1)).getType() != "floor") {
					typeWall(x-1,y+1);
					if (tiles.get(new Pair(x-2,y+1)).content == Seed.VD || tiles.get(new Pair(x-2,y+1)).content == Seed.SED || tiles.get(new Pair(x-2,y+1)).content == Seed.NED || tiles.get(new Pair(x-2,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y+1)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y)).content == Seed.HD || tiles.get(new Pair(x-1,y)).content == Seed.NED || tiles.get(new Pair(x-1,y)).content == Seed.NWD || tiles.get(new Pair(x-1,y)).content == Seed.NTD) {
						tiles.get(new Pair(x-1,y)).nlock = true;
					}
				}
				if (tiles.get(new Pair(x+1,y+2)).getType() != "floor") {
					typeWall(x+1,y+2);
					if (tiles.get(new Pair(x+2,y+2)).content == Seed.VD || tiles.get(new Pair(x+2,y+2)).content == Seed.SWD || tiles.get(new Pair(x+2,y+2)).content == Seed.NWD || tiles.get(new Pair(x+2,y+2)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y+2)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y+3)).content == Seed.HD || tiles.get(new Pair(x+1,y+3)).content == Seed.SED || tiles.get(new Pair(x+1,y+3)).content == Seed.SWD || tiles.get(new Pair(x+1,y+3)).content == Seed.STD) {
						tiles.get(new Pair(x+1,y+3)).slock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y+2)).getType() != "floor") {
					typeWall(x-1,y+2);
					if (tiles.get(new Pair(x-2,y+2)).content == Seed.VD || tiles.get(new Pair(x-2,y+2)).content == Seed.SED || tiles.get(new Pair(x-2,y+2)).content == Seed.NED || tiles.get(new Pair(x-2,y+2)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y+2)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y+3)).content == Seed.HD || tiles.get(new Pair(x-1,y+3)).content == Seed.SED || tiles.get(new Pair(x-1,y+3)).content == Seed.SWD || tiles.get(new Pair(x-1,y+3)).content == Seed.STD) {
						tiles.get(new Pair(x-1,y+3)).slock = true;
					}
				}
				
				if (tiles.get(new Pair(x,y+2)).getType() != "floor") {
					typeWall(x,y+2);
					if (tiles.get(new Pair(x,y+3)).content == Seed.HD || tiles.get(new Pair(x,y+3)).content == Seed.SED || tiles.get(new Pair(x,y+3)).content == Seed.SWD || tiles.get(new Pair(x,y+3)).content == Seed.STD) {
						tiles.get(new Pair(x,y+3)).slock = true;
					}
				}
				break;
		} // end switch
		calcLevel();
		Game.screen.refresh();
	} // end removeWall
	
	/***************************************************************************************************************
	* Method      : typeWall(int x, int y, int dir)
	*
	* Purpose     : When new walls are generated from carving walls, this method routes to differing method depending
	* 				on whether the tile being converted to a wall was of type "earth" or type "wall."
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction the player is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void typeWall(int x, int y) {
		if (tiles.get(new Pair(x,y)).getType() == "wall") {
			overrideTile(x,y,Seed.CALC);
		}
		else if (tiles.get(new Pair(x,y)).getType() == "earth") {
			newWall(x,y,1);	
		}
		//Idea possible to break out if type = block
	}

	/***************************************************************************************************************
	* Method      : blackLantern(int x, int y, int radius)
	*
	* Purpose     : turns off the .light property of all cells within the passed radius
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int radius - the radius of the lantern passed
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void blackLantern (int a, int b, int radius) {
		for(int y=b-radius; y<=b+radius; y++){
	          for (int x=a-radius; x<=a+radius; x++) {
	        	  double r = Math.sqrt(Math.pow(x-a,2)+ Math.pow(y-b,2));
	        	  if (r < radius) {
	        		  if (x >= leftScreen && x <= rightScreen && y<= topScreen && y>=bottomScreen) {
		        		  if (!tiles.get(new Pair(x,y)).permaLight) tiles.get(new Pair(x,y)).setLight(false);
		        		  updateColor(x,y);
	        		  }
	        	  }
	          }
		}
	}
	
	/***************************************************************************************************************
	* Method      : updateLantern(int x, int y, int radius)
	*
	* Purpose     : turns on the .light property of all cells within the passed radius
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int radius - the radius of the lantern passed
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateLantern(int a, int b, int radius) {
		//when the lantern radius changes, have a separate routine that blacks out everything in radius first then recalculates it
		//might want to do this for movement as well. Before movement, blackout all cells, then recalculate after movement.
		//System.out.println(radius);
		for(int y=b-radius; y<=b+radius; y++){
			for (int x=a-radius; x<=a+radius; x++) {
				double r = Math.sqrt(Math.pow(x-a,2)+ Math.pow(y-b,2));
				if (r < radius) {  		 
					if (x >= leftScreen && x <= rightScreen && y<= topScreen && y>=bottomScreen) {
		    		  tiles.get(new Pair(x,y)).setLight(true);
		    		  updateColor(x,y);
					}
				}
			}
		}
		//updateColor(a,b);
		Game.screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : lightLevel()
	*
	* Purpose     : a debugging method to illuminate the entire level by disabling all lanterns
	*
	* Parameters  : none
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void lightLevel() {
		for(int y=topScreen; y>=bottomScreen; y--){
	          for (int x=leftScreen; x<=rightScreen; x++) {
	        	  tiles.get(new Pair(x,y)).bufferLight();
	        	  tiles.get(new Pair(x,y)).setLight(true);
	        	  updateColor(x,y);
	        	  Game.screen.refresh();
	          }
		}
	}
	
	/***************************************************************************************************************
	* Method      : drawTree
	*
	* Purpose     : Draws a tree at the specified coordinates 
	*
	* Parameters  : int x - x coordinate 
	* 		 	  : int y - y coordinate
	*
	* Returns     : None
	*  
	***************************************************************************************************************/
	public void drawTree(int x, int y) {
		drawTile(x+1,y,Seed.NWRT,false);
		drawTile(x+2,y,Seed.NERT,false);
		
		drawTile(x,y-1,Seed.NWRT,false);
		drawTile(x+1,y-1,Seed.SERT,false);
		drawTile(x+2,y-1,Seed.SWRT,false);	
		drawTile(x+3,y-1,Seed.NERT,false);
		
		drawTile(x,y-2,Seed.SWRT,false);
		drawTile(x+1,y-2,Seed.NERT,false);
		drawTile(x+2,y-2,Seed.NWRT,false);
		drawTile(x+3,y-2,Seed.SERT,false);
		
		drawTile(x+1,y-3,Seed.SWRT,false);
		drawTile(x+2,y-3,Seed.SERT,false);
	}

	/***************************************************************************************************************
	* Method      : drawColumn3
	*
	* Purpose     : Draws a 3x3 column
	*
	* Parameters  : int x - the x coordinate of the column center
	* 			  :	int y - the y coordinate of the column center
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void drawColumn3(int x, int y) {
		for (int j=0; j>-3; j--){
			for (int i=0; i<3; i++){
				if (i==1 && j==-1){
					continue;
				}
				newWall(x+i,y+j,4);
			}
		}
		calcLevel();
	}
	
	/***************************************************************************************************************
	* Method      : drawCow
	*
	* Purpose     : Draws a cow!
	*
	* Parameters  : int x - the x coordinate of the cow's head
	* 			  :	int y - the y coordinate of the cow's head
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void drawCow(int x,int y) {	
		drawTile(x,y,Seed.HEAD,false);
		drawTile(x+1,y,Seed.LHALF,false);
		drawTile(x+2,y,Seed.RHALF,false);
		drawTile(x+3,y,Seed.TAIL,false);
	}
	
	/***************************************************************************************************************
	* Method      : removeCow
	*
	* Purpose     : removes cow from screen
	*
	* Parameters  : int x - the x coordinate of the cow's head
	* 			  :	int y - the y coordinate of the cow's head
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void removeCow(int x,int y) {	
		restoreBuffer(x,y);
		updateColor(x,y);
		restoreBuffer(x+1,y);
		updateColor(x+1,y);
		restoreBuffer(x+2,y);
		updateColor(x+2,y);
		restoreBuffer(x+3,y);
		updateColor(x+3,y);
	}
	
	/***************************************************************************************************************
	* Method      : square
	*
	* Purpose     : An effect of eminating squares from location x,y
	*
	* Parameters  : int x - the x coordinate of the effect's center
	* 			  :	int y - the y coordinate of the effect's center
	* 			  : int qty - the number of rings
	* 			  : int pauseTime - the duration effect will last
	* 			  : Direction dir - the direct to exclude the effect
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void squares(int x, int y, int qty, int pauseTime, Direction dir) {
		int i = 1;
		for (int q=1; q<qty; ++q){
			System.out.println(q);
			for (int r=1; r<=i; ++r) {	
				drawTile(x-q+r,y+q,Seed.HS,true);
				drawTile(x-q+r,y-q,Seed.HS,true);
				drawTile(x-q,y+q-r,Seed.VS,true);
				if(q != 1 && y+q-r !=y && dir == Direction.EAST) drawTile(x+q,y+q-r,Seed.VS,true);
			}
			drawTile(x-q,y+q,Seed.NWS,true);
			drawTile(x+q,y+q,Seed.NES,true);
			drawTile(x-q,y-q,Seed.SWS,true);
			drawTile(x+q,y-q,Seed.SES,true);
			Game.screen.refresh();
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			i+=2;
		}
		try {
			Thread.sleep(pauseTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for (int b=0; b<qty; ++b) {
//			for (int a=0; a<=qty; ++a) {
//				restoreBuffer(a,b);
//			}
//		}
		Game.screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : clearLamp
	*
	* Purpose     : GET RID OF THIS CRAPOLA
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void clearLamp(){
//		tiles.get(new Pair(-21,1)).setLight(true);
		tiles.get(new Pair(-21,1)).permaLight = false;
//		updateColor(-21,1);
//		tiles.get(new Pair(-20,1)).setLight(true);
		tiles.get(new Pair(-20,1)).permaLight = false;
//		updateColor(-20,1);
//		tiles.get(new Pair(-19,1)).setLight(true);
		tiles.get(new Pair(-19,1)).permaLight = false;
//		updateColor(-19,1);
//		tiles.get(new Pair(-21,0)).setLight(true);
		tiles.get(new Pair(-21,0)).permaLight = false;
//		updateColor(-21,0);
//		tiles.get(new Pair(-19,0)).setLight(true);
		tiles.get(new Pair(-19,0)).permaLight = false;
//		updateColor(-19,0);
//		tiles.get(new Pair(-21,-1)).setLight(true);
		tiles.get(new Pair(-21,-1)).permaLight = false;
//		updateColor(-21,-1);
//		tiles.get(new Pair(-20,-1)).setLight(true);
		tiles.get(new Pair(-20,-1)).permaLight = false;
//		updateColor(-20,-1);
//		tiles.get(new Pair(-19,-1)).setLight(true);
		tiles.get(new Pair(-19,-1)).permaLight = false;
//		updateColor(-19,-1);
//		tiles.get(new Pair(-20,0)).setLight(true);
		tiles.get(new Pair(-20,0)).permaLight = false;
//		updateColor(-20,0);
//		tiles.get(new Pair(-20,0)).setLight(true);
		tiles.get(new Pair(-20,0)).permaLight = false;
//		updateColor(-20,0);
	}
	
	/***************************************************************************************************************
	* Method      : emote
	*
	* Purpose     : draws an emote bubble
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Direction origin - the direction the bubble appears relative to x,y
	* 			  : Boolean hasBorder - not implemented yet
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void emote(String emote, int x, int y, Direction origin, int pauseTime, boolean highlight) {
		int i;
		int xOff=0;
		int yOff=0;
		if (origin == Direction.LOADER) {
			emote = "                    ";
		}
		switch(origin) {
			case NE:
				xOff = x - 1;
				yOff = y + 1;
				break;
			case NW:
				xOff = x - emote.length() - 2;
				yOff = y + 1;
				break;
			case SOUTH:
				xOff = x - emote.length()/2 - 2;
				yOff = y - 3;
				break;
			case NORTH:
				xOff = x - emote.length()/2 - 2;
				yOff = y + 1;
				break;
			case WEST:
				xOff = x - emote.length() - 4;
				yOff = y - 1;
				break;
			case EAST:
				xOff = x - emote.length() + 7;
				yOff = y - 1;
				break;
			case SE:
				xOff = x - 1;
				yOff = y - 3;
				break;
			case SW:
				xOff = x - emote.length() - 2;
				yOff = y - 3;
				break;
			case NONE:
				x = 0;
				xOff = x - emote.length()/2 -2;
				yOff = y - 1;
				break;
			case LOADER:
				x = 0;
				xOff = x - emote.length()/2 -2;
				yOff = y - 1;
				break;
		}
		drawTile(xOff,yOff,Seed.SWS,true);
		for (i=1; i<=emote.length()+2; ++i) {
			drawTile(xOff+i,yOff,Seed.HS,true);
		}
		drawTile(xOff+i,yOff,Seed.SES,true);
		drawTile(xOff,yOff+1,Seed.VS,true);
		for (i=1; i<=emote.length()+2; ++i) {
			drawTile(xOff+i,yOff+1,Seed.SPACE,true);
		}
		drawTile(xOff+i,yOff+1,Seed.VS,true);
		drawTile(xOff,yOff+2,Seed.NWS,true);
		for (i=1; i<=emote.length()+2; ++i) {
			drawTile(xOff+i,yOff+2,Seed.HS,true);
		}
		drawTile(xOff+i,yOff+2,Seed.NES,true);
		Game.screen.refresh();
		
		if (origin != Direction.LOADER) {
			if (highlight) {
				Game.screen.putString((xOff+2 + widthFactor), (levelHeight - (yOff + heightFactor+1)),emote,Terminal.Color.WHITE, Terminal.Color.GREEN);		
			}
			else {
				Game.screen.putString((xOff+2 + widthFactor), (levelHeight - (yOff + heightFactor+1)),emote,Terminal.Color.WHITE, Terminal.Color.BLACK);	
			}
		}
		else {
			for (int j=0; j<emote.length(); ++j) {
				Game.screen.putString((xOff+2 + widthFactor+j), (levelHeight - (yOff + heightFactor+1))," ",Terminal.Color.GREEN, Terminal.Color.GREEN);		
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Game.screen.refresh();
			}
		}
		switch(origin) {
			case NE:
				restoreBuffer(xOff+1,yOff);
				drawTile(xOff+1,yOff,Seed.NES,true);
				break;
			case NW:
				restoreBuffer(xOff+emote.length()+2,yOff);
				drawTile(xOff+emote.length()+2,yOff,Seed.NWS,true);
				break;
			case NORTH:
				restoreBuffer(xOff+emote.length()/2+2,yOff);
				drawTile(xOff+emote.length()/2+2,yOff,Seed.NER,true);
				break;
			case SOUTH:
				restoreBuffer(xOff+emote.length()/2+2,yOff+2);
				drawTile(xOff+emote.length()/2+2,yOff+2,Seed.SER,true);
				break;
			case WEST:
				restoreBuffer(xOff+emote.length()+3,yOff+1);
				drawTile(xOff+emote.length()+3,yOff+1,Seed.SWR,true);
				break;
			case EAST:
				restoreBuffer(xOff+emote.length()-6,yOff+1);
				drawTile(xOff+emote.length()-6,yOff+1,Seed.SER,true);
				break;
			case SE:
				restoreBuffer(xOff+1,yOff+2);
				drawTile(xOff+1,yOff+2,Seed.SES,true);
				break;
			case SW:
				restoreBuffer(xOff+emote.length()+2,yOff+2);
				drawTile(xOff+emote.length()+2,yOff+2,Seed.SWS,true);
				break;
		}
		Game.screen.refresh();	
		if (pauseTime == 0) {
			boolean flag=true;
			while (flag) {
				flag = Game.enterToContinue();		
			}
		}
		else {
			Game.pause(pauseTime);
		}
		
		for (int b=0; b<3; ++b) {
			for (int a=0; a<=i; ++a) {
				//drawTile(a+xOff,b+yOff,Seed.WATER);
				restoreBuffer(a+xOff, b+yOff);
				//drawTile(a+xOff,b+yOff,Seed.PIT);
				//System.out.println(a+xOff + "," + (b+yOff));
			}
		}
		Game.screen.refresh();
	}
	
	
	/***************************************************************************************************************
	* Method      : fillMap()
	*
	* Purpose     : Fills a screen with whatever seed you pass in  
	*
	* Parameters  :	None.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void fillMap(int level, Seed seed) {
		for(int y=topScreen; y>=bottomScreen; y--){
	          for (int x=leftScreen; x<=rightScreen; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  newEarth(x,y,level,seed);
	          }
		}
	}
	
	
	/***************************************************************************************************************
	* Method      : newLevel0()
	*
	* Purpose     : Fills a new level with Cell objects and sets each Cell with EARTH. Draws a starting room.  
	*
	* Parameters  :	None.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newLevel0() {
		fillMap(0,Seed.SPACE);
	}
	

	/***************************************************************************************************************
	* Method      : newLevel1()
	*
	* Purpose     : Level 1 of game  
	*
	* Parameters  :	None.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newLevel1() {
		fillMap(1,Seed.GRASS);
		//path next to 2nd river
		boxFill(25,14, 29,-13,Seed.PATH,false);
		// deep bushes behind temple
		boxFill(8,13,25,-13,Seed.BUSH,false);
		boxFill(26,2,42,-2,Seed.BUSH,false);
		boxFill(29,7,38,-6,Seed.BUSH,false);
		// river behind temple
		boxFill(26,14, 28,-13,Seed.WATERD,false);
		//path next to trees
		boxFill(30,6,37,-5,Seed.FLOOR,false);
		// path next to cow
		boxFill(13,1,21,-1,Seed.FLOOR,false);
		boxFill(11,-5,11,-7,Seed.FLOOR,false);
		boxFill(9,-5,9,-7,Seed.FLOOR,false);
		boxFill(10,-5,10,-7,Seed.FLOOR,false);
		//blocks at end of path by stairs
		newBlock(43,2,4);
		newBlock(43,-2,4);
		//bottom path
		boxFill(9,-8,11,-12,Seed.FLOOR,false);
		boxFill(12,-11,24,-12,Seed.FLOOR,false);
		chainWall(43,1,Direction.EAST,4,4);
		chainWall(43,-1,Direction.EAST,4,4);
		chainWall(47,1,Direction.SOUTH,3,4);
		boxFill(43,0,46,0,Seed.VSTAIR,false);
		boxFill(22,11,24,-11,Seed.FLOOR,false);
		//top path
		boxFill(11,7,11,5,Seed.FLOOR,true);
		boxFill(9,7,9,5,Seed.FLOOR,true);
		boxFill(10,7,10,5,Seed.FLOOR,false);
		boxFill(9,12,11,8,Seed.FLOOR,false);
		boxFill(12,12,24,11,Seed.FLOOR,false);
		boxFill(25,1,42,-1,Seed.FLOOR,false);
		//2nd bridge
		chainWall(25,1,Direction.EAST,5,4);
		chainWall(25,-1,Direction.EAST,5,4);
		newBlock(25,2,4);
		newBlock(29,2,4);
		newBlock(25,-2,4);
		newBlock(29,-2,4);
		boxFill(-6,12,1,5,Seed.BUSH,false);
		boxFill(-6,-5,1,-12,Seed.BUSH,false);
		boxFill(-6,3,1,-3,Seed.BUSH,false);
		boxFill(-6,12,1,5,Seed.BUSH,false);
		boxFill(-9,2,3,-2,Seed.BUSH,false);
		boxFill(-4,4,-1,-4,Seed.BUSH,false);
		boxFill(-13,14,-10,-13,Seed.PATH,false);
		boxFill(-12,14,-11,-13,Seed.WATERD,false);

		//BARRIERS SO ENEMIES WON'T MOVE OFF SCREEN
		boxFill(-50,14,-14,14,Seed.GBARRIER, false);
		boxFill(-50,13,-50,-13,Seed.GBARRIER, false);
		boxFill(-49,-13,-14,-13,Seed.GBARRIER, false);
		drawTile(-13,14,Seed.PBARRIER,false);
		drawTile(-13,-13,Seed.PBARRIER,false);
		//
		
		boxFill(-5,2,0,-2,Seed.FLOOR,false);
//		chainWall(-4,1,Direction.EAST,4,4);
//		chainWall(-1,0,Direction.SOUTH,2,4);
//		chainWall(-2,-1,Direction.WEST,2,4);
//		chainWall(-4,-1,Direction.NORTH,2,4);
		boxFill(-4,1,-1,-1,Seed.WATERS,false);
		boxFill(-3,0,-2,0,Seed.WATERD,false);
		boxFill(-13,0,-10,0,Seed.FLOOR,false);
		boxFill(-9,1,-6,-1,Seed.FLOOR,false);
		boxFill(1,1,3,-1,Seed.FLOOR,false);
		boxFill(-5,11,0,6,Seed.FLOOR,false);
		boxFill(-5,-6,0,-11,Seed.FLOOR,false);
		boxFill(1,1,3,-1,Seed.FLOOR,false);
		boxFill(-3,5,-2,3,Seed.FLOOR,false);
		boxFill(-3,-3,-2,-5,Seed.FLOOR,false);
		newBlock(-13,2,4);
		newBlock(-10,2,4);
		chainWall(-13,1,Direction.EAST, 4,4);
		chainWall(-13,-1,Direction.EAST, 4,4);
		newFloor(10,4,4);
		newFloor(10,-4,4);
		newBlock(-13,-2,4);
		newBlock(-10,-2,4);
		drawTree(-4,10);
		drawTree(-4,-7);
		boxFill(3,1,3,-1,Seed.VSTAIR,false);
		boxFill(2,1,2,-1,Seed.VSTAIR,false);
		boxFill(1,1,1,-1,Seed.VSTAIR,false);
		chainWall(4,1,Direction.NORTH,4,4);
		chainWall(5,4,Direction.NORTH,2,4);
		chainWall(6,5,Direction.EAST,2,4);
		chainWall(8,5,Direction.SOUTH,2,4);
		newWall(9,4,4);
		newWall(11,4,4);
		newWall(12,4,4);
		newWall(11,-4,4);
		chainWall(12,3,Direction.SOUTH,3,4);
		chainWall(12,3,Direction.SOUTH,3,4);
		newWall(12,0,1);
		newWall(9,-4,4);
		chainWall(12,-1,Direction.SOUTH,3,4);
		newWall(12,-4,4);
		chainWall(8,-4,Direction.SOUTH,2,4);
		chainWall(7,-5,Direction.WEST,2,4);
		chainWall(5,-5,Direction.NORTH,2,4);
		chainWall(4,-4,Direction.NORTH,4,4);
		boxFill(5,3,8,-3,Seed.FLOOR,false);
		newBlock(7,4,4);
		newBlock(6,4,4);
		newBlock(7,2,4);
		newBlock(6,2,4);
		newBlock(6,-2,4);
		newBlock(7,-2,4);
		newBlock(6,-4,4);
		newBlock(7,-4,4);
		boxFill(9,3,11,-3,Seed.WATERS,false);
		boxFill(10,2,10,1,Seed.WATERD,false);
		boxFill(10,-1,10,-2,Seed.WATERD,false);
		drawTile(4,0,Seed.FLR,false);
		//columns to prevent enemies from getting trapped
		boxFill(32,1,32,0,Seed.BUSH,false);
		//boxFill()
		newFloor(4,0,4);
		
		// trees above/below the cow
		drawTree(13,9);
		drawTree(17,9);
		drawTree(13,5);
		drawTree(17,5);
		drawTree(13,-2);
		drawTree(17,-2);
		drawTree(13,-6);
		drawTree(17,-6);
		// trees in middle of path
		drawTree(32,4);
		drawTree(32,0);
		//trees past 2nd river
		drawTree(30,11);
		drawTree(34,11);
		drawTree(39,6);
		drawTree(43,6);
		drawTree(39,-3);
		drawTree(43,-3);
		drawTree(30,-7);
		drawTree(34,-7);
		
		drawCow(13,0);
		
		tiles.get(new Pair(-21,1)).setLight(true);
		tiles.get(new Pair(-21,1)).permaLight = true;
		updateColor(-21,1);
		tiles.get(new Pair(-20,1)).setLight(true);
		tiles.get(new Pair(-20,1)).permaLight = true;
		updateColor(-20,1);
		tiles.get(new Pair(-19,1)).setLight(true);
		tiles.get(new Pair(-19,1)).permaLight = true;
		updateColor(-19,1);
		tiles.get(new Pair(-21,0)).setLight(true);
		tiles.get(new Pair(-21,0)).permaLight = true;
		updateColor(-21,0);
		tiles.get(new Pair(-19,0)).setLight(true);
		tiles.get(new Pair(-19,0)).permaLight = true;
		updateColor(-19,0);
		tiles.get(new Pair(-21,-1)).setLight(true);
		tiles.get(new Pair(-21,-1)).permaLight = true;
		updateColor(-21,-1);
		tiles.get(new Pair(-20,-1)).setLight(true);
		tiles.get(new Pair(-20,-1)).permaLight = true;
		updateColor(-20,-1);
		tiles.get(new Pair(-19,-1)).setLight(true);
		tiles.get(new Pair(-19,-1)).permaLight = true;
		updateColor(-19,-1);
		tiles.get(new Pair(-20,0)).setLight(true);
		tiles.get(new Pair(-20,0)).permaLight = true;
		updateColor(-20,0);
		
		
		//Trigger(x,y,ID,Seed,isLit,endures)
		newTrigger(-33,0,	0,Seed.TRIGGERG, false, false);
		newTrigger(-20,0,	1,Seed.HELP, true, false);
		tiles.get(new Pair(-20,0)).setLight(true);
		tiles.get(new Pair(-20,0)).permaLight = true;
		updateColor(-20,0);
		newTrigger(-11,0,	2,Seed.TRIGGERF, false, false);
		newTrigger(-5,0,	3,Seed.HELP, false, false);
		newTrigger(-5,11,	4,Seed.HELP, false, false);
		newTrigger(0,11,	5,Seed.HELP, false, false);
		newTrigger(4,0,		6,Seed.TRIGGERF, false, false);
		//newTrigger(6,0,		7,Seed.TRIGGERF, false, false);
		newTrigger(46,0,  	8,Seed.TRIGGERV, false, false);	
		
		calcLevel();	
	}
	
	
	/***************************************************************************************************************
	* Method      : newLevel2()
	*
	* Purpose     : Level 2 of game  
	*
	* Parameters  :	None.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newLevel2() {
		// Fills the screen with empty Cells
		fillMap(2, Seed.EARTH);
		
		chainWall(-43,1,Direction.NORTH,2,4);
		chainWall(-43,-1,Direction.SOUTH,2,4);
		chainWall(-42,2,Direction.EAST,4,4);
		chainWall(-42,-2,Direction.EAST,4,4);
		chainWall(-38,-2,Direction.NORTH,2,4);
		chainWall(-38,2,Direction.SOUTH,2,4);
		chainWall(-47, 1, Direction.SOUTH, 3, 4);
		chainWall(-46, 1, Direction.EAST, 3, 4);
		chainWall(-46, -1, Direction.EAST, 3, 4);
		boxFill(-46,0,-44,0,Seed.VSTAIR,false);
		// green walls
		newWall(-38, 0, 1);
		newWall(-32, 0, 1);
		newWall(5, 0, 1);
		//boxFill(-46,1,-44,1,Seed.HS,true);
		//boxFill(-46,-1,-44,-1,Seed.HS,true);
		//drawTile(-47,1,Seed.NWS,false);
		//drawTile(-47,0,Seed.VS,false);
		//drawTile(-47,-1,Seed.SWS,false);
		
		boxFill(-42,1,-39,-1,Seed.FLOOR,false);
		boxFill(-43,0,-43,0,Seed.FLOOR,false);
		//drawTile(-43,0,Seed.FLOOR,false);
		
		//Cow Room
		chainWall(21,2,Direction.EAST,26,4);
		chainWall(21,-8,Direction.EAST,26,4);
		chainWall(46,2,Direction.SOUTH,10,4);
		chainWall(21,2,Direction.SOUTH,10,4);
		boxFill(22,1,45,-7,Seed.GRASS,false);
		newWall(27,-8,4);
		newWall(28,-8,4);
		newWall(21,-3,1);
		//Water
		boxFill(42,1,45,1,Seed.WATERD,false);
		boxFill(43,0,45,0,Seed.WATERD,false);
		boxFill(44,-1,45,-1,Seed.WATERD,false);
		boxFill(45,-2,45,-2,Seed.WATERD,false);
		//Bushes
		drawTile(22,-1,Seed.BUSH,false);
		drawTile(24,0,Seed.BUSH,false);
		drawTile(25,1,Seed.BUSH,false);
		drawTile(29,0,Seed.BUSH,false);
		drawTile(31,-1,Seed.BUSH,false);
		drawTile(33,-1,Seed.BUSH,false);
		drawTile(34,0,Seed.BUSH,false);
		drawTile(37,1,Seed.BUSH,false);
		drawTile(39,0,Seed.BUSH,false);
		drawTile(22,-6,Seed.BUSH,false);
		drawTile(25,-7,Seed.BUSH,false);
		drawTile(26,-6,Seed.BUSH,false);
		drawTile(29,-6,Seed.BUSH,false);
		drawTile(32,-5,Seed.BUSH,false);
		drawTile(35,-7,Seed.BUSH,false);
		drawTile(38,-6,Seed.BUSH,false);
		drawTile(39,-7,Seed.BUSH,false);
		drawTile(40,-5,Seed.BUSH,false);
		drawTile(43,-6,Seed.BUSH,false);
		drawTile(45,-5,Seed.BUSH,false);
		//Cows
		drawCow(24,-2);
		drawCow(24,-4);
		drawCow(29,-2);
		drawCow(29,-4);
		drawCow(34,-2);
		drawCow(34,-4);
		drawCow(39,-2);
		drawCow(39,-4);
		
		//Main Room
		chainWall(-32,1,Direction.EAST,2,4);
		chainWall(-31,2,Direction.EAST,3,4);
		chainWall(-29,1,Direction.EAST,3,4);
		chainWall(-27,4,Direction.SOUTH,3,4);
		chainWall(-26,7,Direction.SOUTH,4,4);
		chainWall(-26,8,Direction.EAST,3,4);
		chainWall(-24,7,Direction.EAST,4,4);
		chainWall(-21,8,Direction.EAST,3,4);
		chainWall(-19,7,Direction.EAST,4,4);
		chainWall(-16,8,Direction.EAST,3,4);
		chainWall(-14,7,Direction.EAST,4,4);
		chainWall(-11,8,Direction.EAST,3,4);
		chainWall(-9,7,Direction.EAST,4,4);
		chainWall(-6,8,Direction.EAST,3,4);
		chainWall(-4,7,Direction.EAST,3,4);
		//chainWall(-2,8,Direction.NORTH,7,4);
		//chainWall(1,7,Direction.NORTH,4,4);
		chainWall(2,7, Direction.WEST,2,4);
		chainWall(2,-7, Direction.WEST,2,4);
		//chainWall(-2,11,Direction.EAST,4,4);
		chainWall(2,3,Direction.NORTH,4,4);
		chainWall(2,2,Direction.EAST,3,4);
		chainWall(5,2,Direction.SOUTH,2,4);
		chainWall(-32,-1,Direction.EAST,2,4);
		chainWall(-31,-2,Direction.EAST,3,4);
		chainWall(-29,-1,Direction.EAST,3,4);
		chainWall(-27,-4,Direction.NORTH,3,4);
		chainWall(-26,-7,Direction.NORTH,4,4);
		chainWall(-26,-8,Direction.EAST,3,4);
		chainWall(-24,-7,Direction.EAST,4,4);
		chainWall(-21,-8,Direction.EAST,3,4);
		chainWall(-19,-7,Direction.EAST,4,4);
		chainWall(-16,-8,Direction.EAST,3,4);
		chainWall(-14,-7,Direction.EAST,4,4);
		chainWall(-11,-8,Direction.EAST,3,4);
		chainWall(-9,-7,Direction.EAST,4,4);
		chainWall(-6,-8,Direction.EAST,3,4);
		chainWall(-4,-7,Direction.EAST,3,4);
		//chainWall(-2,-8,Direction.SOUTH,6,4);
		//chainWall(1,-7,Direction.SOUTH,4,4);
		//chainWall(-2,-11,Direction.EAST,4,4);
		chainWall(2,-3,Direction.SOUTH,5,4);
		chainWall(2,-2,Direction.EAST,3,4);
		chainWall(5,-2,Direction.NORTH,2,4);
		
		boxFill(-25,6,1,-6,Seed.FLOOR,false);
		boxFill(-26,3,-26,-3,Seed.FLOOR,false);
		boxFill(-31,0,-27,0,Seed.FLOOR,false);
		boxFill(2,1,4,-1,Seed.FLOOR,false);
		boxFill(-1,14,0,-13,Seed.WATERD,false);
		
		drawColumn3(-22,5); // top left
		drawTile(-21,4,Seed.WATERD,false);
		drawColumn3(-6,5);// top right column
		drawTile(-5,4,Seed.WATERD, false);
		drawColumn3(-22,-3);//bottom left
		drawTile(-21,-4,Seed.WATERD, false);
		drawColumn3(-6,-3);// bottom right
		drawTile(-5,-4,Seed.WATERD, false);
		drawColumn3(-14,5); // north middle column
		drawTile(-13,4,Seed.WATERD, false);
		drawColumn3(-14,-3);// south middle column
		drawTile(-13,-4,Seed.WATERD,false);
		newBlock(-24,1,4);
		newBlock(-24,-1,1);
		newBlock(-17,1,4);
		newBlock(-17,-1,1);
		newBlock(-9,1,4);
		newBlock(-9,-1,4);
		newBlock(-30,1,3);
		newBlock(-30,-1,3);
		newBlock(2,0,1);
		
		// pits
		newFloor(-21,1,1);
		newFloor(-11,4,1);
		newFloor(-3,-3,1);
		
		newTrigger(-27,0,  	9,Seed.TRIGGERF, false, false);
		calcLevel();
	}
} //end class Level
