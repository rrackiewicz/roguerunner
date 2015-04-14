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
	//public HashMap<Pair, Enemies> enemies;
	//public HashMap<Pair, Resources> resources;
	//public HashMap<Pair, Traps> traps;
	
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
	//get rid of these color variables below
	public Rating playerForeColor;
	public Rating playerBackColor;
	public Rating pixieForeColor;
	public Rating pixieBackColor;

	/***************************************************************************************************************
	* Method      : Level()
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
		rightScreen = (levelWidth / 2) - 1; //+49
		topScreen = levelHeight / 2 - 2; //+13
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
	}
	
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
	* Method      : bufferString()
	*
	* Purpose     : Writes the Unicode character of a tile at coordinate x,y to the screen buffer at the foreground
	* 				and background color stored in that tile 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void bufferString(int x, int y) {
		Terminal.Color foreColor;
		Terminal.Color backColor;
		//if (Game.log) System.out.println("Drawing " + tiles.get(new Pair(x,y)).content + " at " + x + "," + y);
		if (tiles.get(new Pair(x,y)).getLight()) {
			foreColor = tiles.get(new Pair(x,y)).getForeColor();
			backColor = tiles.get(new Pair(x,y)).getBackColor();
		}
		else 
		{
			foreColor = Rating.BLACK.color;
			backColor = Rating.BLACK.color;
		}
		Game.screen.putString((x + widthFactor + xOffset), (levelHeight - (y + heightFactor + yOffset)), tiles.get(new Pair(x,y)).content.ID, foreColor, backColor);	
	}
	
	/***************************************************************************************************************
	* Method      : newPlayer()
	*
	* Purpose     : Places the character into the level 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newPlayer(int x, int y, Rating foreColor, Rating backColor, Seed symbol, int lanternRadius) {
		bufferCell(x,y);
		tiles.get(new Pair(x,y)).setLight(true);
		playerForeColor = foreColor; //errors without this
		playerBackColor = backColor;
		tiles.get(new Pair(x,y)).updateType(symbol);
		tiles.get(new Pair(x,y)).setForeColor(playerForeColor);
		tiles.get(new Pair(x,y)).setBackColor(playerBackColor);	
		bufferString(x,y);
		updateLantern(x,y,lanternRadius);
	}
	
	/***************************************************************************************************************
	* Method      : newPixie()
	*
	* Purpose     : Places the pixie into the level 
	*
	* Parameters  : None
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newPixie(int x, int y, Rating foreColor, Rating backColor, Seed symbol, int lanternRadius) {
		bufferCell(x,y);
		tiles.get(new Pair(x,y)).setLight(true);
		pixieForeColor = foreColor;
		pixieBackColor = backColor;
		tiles.get(new Pair(x,y)).updateType(symbol);
		tiles.get(new Pair(x,y)).setForeColor(pixieForeColor);
		tiles.get(new Pair(x,y)).setBackColor(pixieBackColor);
		bufferString(x,y);
		updateLantern(x,y,lanternRadius);
	}
	
	/***************************************************************************************************************
	* Method      : newEarth(int x, int y)
	*
	* Purpose     : Draws a new tile of type EARTH 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newEarth(int x, int y) {
		//if (Game.log) System.out.println("New earth added.");
		tiles.get(new Pair(x,y)).updateType(Seed.EARTH);
		tiles.get(new Pair(x,y)).setForeColor(Rating.BLACK);
		tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
		bufferString(x,y);		
	}
	
	/***************************************************************************************************************
	* Method      : newWall(int x, int y, int level)
	*
	* Purpose     : Adds a new Wall object. Draws a new tile of type CALC. All tiles of type CALC are converted 
	* 				into their respective Unicode characters in the calcLevel() method. 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
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
	}
	
	/***************************************************************************************************************
	* Method      : removeWall(int x, int y)
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
		newFloor(x,y,1);
	}
	
	/***************************************************************************************************************
	* Method      : newBlock(int x, int y, int level)
	*
	* Purpose     : Adds a new Block object. Blocks can be pushed onto floor tiles or into pits. 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
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
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeBlock(int x, int y)
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
		newFloor(x,y,1);
	}

	/***************************************************************************************************************
	* Method      : newFloor(int x, int y, int level)
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
		updateColor(x,y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removeFloor(int x, int y)
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
	* Method      : newPit(int x, int y, int level)
	*
	* Purpose     : Adds a new Pit object. Draws a new tile of type PIT.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newPit(int x, int y, int level) {
		bufferCell(x,y);
		pits.put(new Pair(x,y), new Pit());
		tiles.get(new Pair(x,y)).updateType(Seed.PIT);
		updateColor(x,y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : removePit(int x, int y)
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
		newFloor(x,y,1);
	}
	
	/***************************************************************************************************************
	* Method      : newWater(int x, int y)
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
		System.out.println(tiles.get(new Pair(x,y)).getBufferContent());
		bufferCell(x,y);
		System.out.println(tiles.get(new Pair(x,y)).getBufferContent());
		water.put(new Pair(x,y), new Water());
		System.out.println(tiles.get(new Pair(x,y)).getBufferContent());
		tiles.get(new Pair(x,y)).updateType(Seed.WATER);
		System.out.println(tiles.get(new Pair(x,y)).getBufferContent());
		updateColor(x,y);
		System.out.println(tiles.get(new Pair(x,y)).getBufferContent());
		bufferString(x,y);
		System.out.println(tiles.get(new Pair(x,y)).getBufferContent());
	}
	
	/***************************************************************************************************************
	* Method      : updateTile(int x, int y, Seed contents)
	*
	* Purpose     : Changes the enum type of a tile while RETAINING all directional locks. Only used when converting
	* 				CALCs to other wall types.
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : Seed contents - the enum value of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateTile(int x, int y, Seed contents) {
		tiles.get(new Pair(x,y)).updateBuffer(contents);
		bufferString(x,y);	
	}
	
	/***************************************************************************************************************
	* Method      : updateColor(int x, int y)
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
		//figure out a way to disable color unless player is the corresponding spirit
		if (tiles.get(new Pair(x,y)).getLight()) {
			switch (tiles.get(new Pair(x,y)).content.type) {
				case "wall":
					tiles.get(new Pair(x,y)).setForeColor(walls.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(walls.get(new Pair(x,y)).getBackColor());
					break;
				case "earth":
					tiles.get(new Pair(x,y)).setForeColor(Rating.BLACK);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
					break;
				case "pit":
					tiles.get(new Pair(x,y)).setForeColor(pits.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(pits.get(new Pair(x,y)).getBackColor());
					break;
				case "floor":
					tiles.get(new Pair(x,y)).setForeColor(floors.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(floors.get(new Pair(x,y)).getBackColor());
					break;
				case "block":
					tiles.get(new Pair(x,y)).setForeColor(blocks.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(blocks.get(new Pair(x,y)).getBackColor());
					break;
				case "water":
					tiles.get(new Pair(x,y)).setForeColor(water.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(water.get(new Pair(x,y)).getBackColor());
					break;
				case "emote":
					tiles.get(new Pair(x,y)).setForeColor(Rating.WHITE);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
					break;
				case "empty":
					tiles.get(new Pair(x,y)).setForeColor(Rating.BLACK);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
					break;
				case "grass":
					tiles.get(new Pair(x,y)).setForeColor(Rating.GREEN);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
					break;
				case "bush":
					tiles.get(new Pair(x,y)).setForeColor(Rating.GREEN);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
					break;
				case "me":
					tiles.get(new Pair(x,y)).setForeColor(Rating.WHITE);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
					break;
				default: 
					tiles.get(new Pair(x,y)).setForeColor(Rating.WHITE);
					tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
			}
		}
		//if (Game.log) System.out.println("Updating color at " + x + " " + y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : overrideTile(int x, int y, Seed content)
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
	
	public void drawTile(int x, int y, Seed content) {
		bufferCell(x,y);
		tiles.get(new Pair(x,y)).updateType(content);
		tiles.get(new Pair(x,y)).setLight(true);
		updateColor(x,y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : newLevel()
	*
	* Purpose     : Fills a new level with Cell objects and sets each Cell with EARTH. Draws a starting room.  
	*
	* Parameters  :	None.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newLevel() {
		// Fills the screen with empty Cells
		for(int y=topScreen; y>=bottomScreen; y--){
	          for (int x=leftScreen; x<=rightScreen; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  newEarth(x,y);
	          }
		}
		//would be nice to pull coordinates in from a text file
		

		newWall(0,3,4);
		newWall(-2,3,4);
		newWall(-3,3,4);
		newWall(-4,3,4);
		newWall(-6,3,4);
		newWall(6,3,4);
		newWall(5,3,4);
		newWall(4,3,4);
		newWall(2,3,4);
		newWall(1,3,4);
		
		newWall(0,4,4);
		newWall(-1,4,4);
		newWall(-2,4,4);
		newWall(-4,4,4);
		newWall(-5,4,4);
		newWall(-6,4,4);
		newWall(4,4,4);
		newWall(3,4,4);
		newWall(2,4,4);

		newWall(-6,2,4);
		newWall(6,2,4);
		newWall(7,2,4);
		newWall(8,2,4);
		newWall(9,2,4);
		
		newWall(9,1,4);
		newWall(-6,1,4);
		
		newWall(6,0,4);
		newWall(9,0,1);
		
		newWall(-6,-1,4);
		newWall(9,-1,4);
		
		newWall(-6,-2,4);
		newWall(9,-2,4);
		newWall(8,-2,4);
		newWall(7,-2,4);
		newWall(6,-2,4);
		
		newWall(-6,-3,4);
		newWall(-4,-3,4);
		newWall(-3,-3,4);
		newWall(-2,-3,4);
		newWall(0,-3,4);
		newWall(1,-3,4);
		newWall(2,-3,4);
		newWall(4,-3,4);
		newWall(5,-3,4);
		newWall(6,-3,4);
		
		newWall(-6,-4,4);
		newWall(-5,-4,4);
		newWall(-4,-4,4);
		newWall(-2,-4,4);
		newWall(-1,-4,4);
		newWall(0,-4,4);
		newWall(2,-4,4);
		newWall(3,-4,4);
		newWall(4,-4,4);
	
		newFloor(-5,2,4);
		newFloor(-4,2,4);
		newFloor(-3,2,4);
		newFloor(-2,2,4);
		newFloor(-1,2,4);
		newFloor(0,2,4);
		newFloor(1,2,4);
		newFloor(2,2,4);
		newFloor(3,2,4);
		newFloor(4,2,4);
		newFloor(5,2,4);
		
		newFloor(-5,1,4);
		newFloor(-3,1,4);
		newFloor(-1,1,4);
		newFloor(1,1,4);
		newFloor(3,1,4);
		newFloor(5,1,4);
		newFloor(6,1,4);
		newFloor(7,1,4);
		newFloor(8,1,4);
		
		newFloor(-6,0,4);
		newFloor(-5,0,4);
		newFloor(-4,0,4);
		newFloor(-3,0,4);
		newFloor(-2,0,4);
		newFloor(-1,0,4);
		newFloor(0,0,4);
		newFloor(1,0,4);
		newFloor(2,0,4);
		newFloor(3,0,4);
		newFloor(4,0,4);
		newFloor(5,0,4);
		newFloor(6,0,1);
		newFloor(7,0,4);
		newFloor(8,0,4);

		newFloor(-5,-1,4);
		newFloor(-3,-1,4);
		newFloor(-1,-1,4);
		newFloor(1,-1,4);
		newFloor(3,-1,4);
		newFloor(5,-1,4);
		newFloor(6,-1,4);
		newFloor(7,-1,4);
		newFloor(8,-1,4);
		
		newFloor(-5,-2,4);
		newFloor(-4,-2,4);
		newFloor(-3,-2,4);
		newFloor(-2,-2,4);
		newFloor(-1,-2,4);
		newFloor(0,-2,4);
		newFloor(1,-2,4);
		newFloor(2,-2,4);
		newFloor(3,-2,4);
		newFloor(4,-2,4);
		newFloor(5,-2,4);
		
		newWater(-5,3);
		newWater(-1,3);
		newWater(3,3);
		newWater(-5,-3);
		newWater(-1,-3);
		newWater(3,-3);
		newWater(-50,-13);
		newWater(+49,-13);
		newWater(-50,13);
		newWater(+49,13);
		
		newBlock(0,-1,4);
		newBlock(2,-1,4);
		newBlock(4,-1,4);
		newBlock(-2,-1,4);
		newBlock(-4,-1,4);
		
		newBlock(0,1,1);
		newBlock(2,1,1);
		newBlock(4,1,1);
		newBlock(-2,1,1);
		newBlock(-4,1,1);
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
				return (tiles.get(new Pair(x+1,y)).getCollideState()) ? true : false;
			case SOUTH:
				return (tiles.get(new Pair(x,y-1)).getCollideState()) ? true : false;
			case WEST:
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
				if (tiles.get(new Pair(x+1,y)).getType() == "block") {
					System.out.println("Is a block");
					isBlocked = detectCollision(x+1,y,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Not Blocked");
					}
					else {
						if (tiles.get(new Pair(x+2,y)).getType() == "pit") {
							removePit(x+2,y);
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
				if (tiles.get(new Pair(x,y-1)).getType() == "block") {
					System.out.println("Is a block");
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
				if (tiles.get(new Pair(x-1,y)).getType() == "block") {
					System.out.println("Is a block");
					isBlocked = detectCollision(x-1,y,dir);
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
				if (tiles.get(new Pair(x,y+1)).getType() == "block") {
					System.out.println("Is a block");
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
		tiles.get(new Pair(x,y)).pushBuffer(x,y);
	} 
	
	public void restoreBuffer(int x, int y) {
		tiles.get(new Pair(x,y)).restoreBuffer(x, y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : restoreTile(int x, int y, Direction dir, int lanternRadius)
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
	public void restoreTile(int x, int y, Direction dir, int lanternRadius) {
		switch(dir) {
			case EAST:
				if (tiles.get(new Pair(x+1,y)).getSeed() == Seed.PC) {
					restoreBuffer(x+1,y);
				}
				bufferCell(x+1,y);
				if (Game.playerControl) {
					tiles.get(new Pair(x+1,y)).updateType(Seed.PE);
				}
				else {
					tiles.get(new Pair(x+1,y)).updateType(Seed.PC);
				}
				if (Game.lantern != Lantern.FOW) {
					
					//verify this should be x+1 or just x
					blackLantern(x,y,lanternRadius);
				}
				updateLantern(x+1,y,lanternRadius);
				
				break;
			case SOUTH:
				if (tiles.get(new Pair(x,y-1)).getSeed() == Seed.PC) {
					restoreBuffer(x,y-1);
				}
				bufferCell(x,y-1);
				if (Game.playerControl) {
					tiles.get(new Pair(x,y-1)).updateType(Seed.PS);
				}
				else {
					tiles.get(new Pair(x,y-1)).updateType(Seed.PC);
				}
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				updateLantern(x,y-1,lanternRadius);
				break;
			case WEST:
				if (tiles.get(new Pair(x-1,y)).getSeed() == Seed.PC) {
					restoreBuffer(x-1,y);
				}
				bufferCell(x-1,y);
				if (Game.playerControl) {
					tiles.get(new Pair(x-1,y)).updateType(Seed.PW);
				}
				else {
					tiles.get(new Pair(x-1,y)).updateType(Seed.PC);
				}
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				updateLantern(x-1,y,lanternRadius);
				break;
			case NORTH:
				if (tiles.get(new Pair(x,y+1)).getSeed() == Seed.PC) {
					restoreBuffer(x,y+1);
				}
				bufferCell(x,y+1);
				if (Game.playerControl) {
					tiles.get(new Pair(x,y+1)).updateType(Seed.PN);
				}
				else {
					tiles.get(new Pair(x,y+1)).updateType(Seed.PC);
				}
				if (Game.lantern != Lantern.FOW) {
					blackLantern(x,y,lanternRadius);
				}
				updateLantern(x,y+1,lanternRadius);
				break;
		}
		tiles.get(new Pair(x,y)).setLight(true); //in the event that the lantern turns this bit off
		updateColor(x,y);
		restoreBuffer(x,y);
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
	* Method      : updateAvatar(int x, int y, int dir)
	*
	* Purpose     : Updates the Unicode character associated with the direction the avatar is facing. This code is
	* 				called when an entity turns
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	* 			  : int dir - the direction the player is facing
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void updateAvatar(int x, int y, Seed symbol) {
		tiles.get(new Pair(x,y)).updateType(symbol);
		bufferString(x,y);
		Game.screen.refresh();
	} 
	
	public void updateAvatar(int x, int y, Seed symbol, int lanternRadius) {
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

	public void blackLantern (int a, int b, int radius) {
		for(int y=b-radius; y<=b+radius; y++){
	          for (int x=a-radius; x<=a+radius; x++) {
	        	  double r = Math.sqrt(Math.pow(x-a,2)+ Math.pow(y-b,2));
	        	  if (r < radius) {
	        		  if (x >= leftScreen && x <= rightScreen && y<= topScreen && y>=bottomScreen) {
		        		  tiles.get(new Pair(x,y)).setLight(false);
		        		  updateColor(x,y);
	        		  }
	        	  }
	          }
		}	
	}
	
	public void updateLantern(int a, int b, int radius) {
		//when the lantern radius changes, have a separate routine that blacks out everything in radius first then recalculates it
		//might want to do this for movement as well. Before movement, blackout all cells, then recalculate after movement.
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
		tiles.get(new Pair(a,b)).setLight(true);
		updateColor(a,b);
	}
	
	public void refreshLantern() {
		for(int y=topScreen; y>=bottomScreen; y--){
	          for (int x=leftScreen; x<=rightScreen; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	          }
		}	
	}
	
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
	
	public void emote(String emote, int x, int y, Direction origin, int pauseTime) {
		int i;
		int xOff=0;
		int yOff=0;
		switch(origin) {
			case SW:
				xOff = x - 1;
				yOff = y + 1;
				break;
			case SE:
				xOff = x - emote.length() - 2;
				yOff = y + 1;
				break;
			case NORTH:
				xOff = x - emote.length()/2 - 2;
				yOff = y - 3;
				break;
			case SOUTH:
				xOff = x - emote.length()/2 - 2;
				yOff = y + 1;
				break;
			case WEST:
				break;
			case EAST:
				break;
			case NW:
				xOff = x - 1;
				yOff = y - 3;
				break;
			case NE:
				xOff = x - emote.length() - 2;
				yOff = y - 3;
				break;
		}
		//drawTile(-8,-3,Seed.WATER);
		drawTile(xOff,yOff,Seed.SWS);
		for (i=1; i<=emote.length()+2; ++i) {
			drawTile(xOff+i,yOff,Seed.HS);
		}
		drawTile(xOff+i,yOff,Seed.SES);
		drawTile(xOff,yOff+1,Seed.VS);
		for (i=1; i<=emote.length()+2; ++i) {
			drawTile(xOff+i,yOff+1,Seed.SPACE);
		}
		drawTile(xOff+i,yOff+1,Seed.VS);
		drawTile(xOff,yOff+2,Seed.NWS);
		for (i=1; i<=emote.length()+2; ++i) {
			drawTile(xOff+i,yOff+2,Seed.HS);
		}
		drawTile(xOff+i,yOff+2,Seed.NES);
		Game.screen.putString((xOff+2 + widthFactor), (levelHeight - (yOff + heightFactor+1)),emote,Terminal.Color.WHITE, Terminal.Color.BLACK);
		
		
		switch(origin) {
		case SW:
			restoreBuffer(xOff+1,yOff);
			drawTile(xOff+1,yOff,Seed.NES);
			restoreBuffer(xOff+2,yOff);
			drawTile(xOff+2,yOff,Seed.NWR);
			break;
		case SE:
			restoreBuffer(xOff+emote.length()+1,yOff);
			drawTile(xOff+emote.length()+1,yOff,Seed.NER);
			restoreBuffer(xOff+emote.length()+2,yOff);
			drawTile(xOff+emote.length()+2,yOff,Seed.NWS);
			break;
		case NORTH:
			break;
		case SOUTH:
			break;
		case WEST:
			break;
		case EAST:
			break;
		case NW:
			restoreBuffer(xOff+1,yOff+2);
			drawTile(xOff+1,yOff+2,Seed.SES);
			restoreBuffer(xOff+2,yOff+2);
			drawTile(xOff+2,yOff+2,Seed.SWR);
			break;
		case NE:
			restoreBuffer(xOff+emote.length()+1,yOff+2);
			drawTile(xOff+emote.length()+1,yOff+2,Seed.SER);
			restoreBuffer(xOff+emote.length()+2,yOff+2);
			drawTile(xOff+emote.length()+2,yOff+2,Seed.SWS);
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
		//drawTile(xOff,yOff,Seed.WATER);
		for (int b=0; b<3; ++b) {
			for (int a=0; a<=i; ++a) {
				//drawTile(a+xOff,b+yOff,Seed.WATER);
				restoreBuffer(a+xOff, b+yOff);
				//System.out.println(a+xOff + "," + (b+yOff));
			}
		}
		Game.screen.refresh();
	}
	
	/***************************************************************************************************************
	* Method      : drawOpening()
	*
	* Purpose     : Fills a new level with Cell objects and sets each Cell with EARTH. Draws a starting room.  
	*
	* Parameters  :	None.
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void drawOpening() {
		// Fills the screen with empty Cells
		for(int y=topScreen; y>=bottomScreen; y--){
	          for (int x=leftScreen; x<=rightScreen; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  newEarth(x,y);
	          }
		}
//		newBlock(1,-1,4);
//		newBlock(2,-1,4);
//		newBlock(3,-1,4);
//		newBlock(4,2,4);
//		newBlock(4,3,4);
//		newBlock(6,1,4);
//		newBlock(6,2,4);
//		newBlock(6,3,4);
//		drawTile(6,1,Seed.BUSH);
		
	}

} //end class Level
