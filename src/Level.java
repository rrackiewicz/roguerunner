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
	
	public int levelWidth;
	public int levelHeight;
	public int leftLevel;
	public int rightLevel;
	public int topLevel; 
	public int bottomLevel; 
	public int widthFactor;
	public int heightFactor;
	public Rating playerForeColor;
	public Rating playerBackColor;
	//These pixie variables are sloppy. I could always pass in copies of player and pixie then return them with new coords
	public Rating pixieForeColor;
	public Rating pixieBackColor;
	public boolean pixieFollow;
	public int pixieX;
	public int pixieY;
	public int pixieRadius;
	public int playerRadius;

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
	public Level(int playerRadius, int pixieRadius) {
		this.playerRadius = playerRadius;
		this.pixieRadius = pixieRadius;
		//Mask playerMask = new Mask(playerRadius);
		//Mask pixieMask = new Mask(pixieRadius);
		levelWidth = Game.screenSize.getColumns();
		levelHeight = Game.screenSize.getRows();
		leftLevel = levelWidth / 2 * -1;
		rightLevel = levelWidth / 2;
		topLevel = levelHeight / 2; 
		bottomLevel = levelHeight / 2 * -1; 
		widthFactor = levelWidth / 2;
		heightFactor = levelHeight / 2;
		tiles = new HashMap<Pair, Cell>();
		walls = new HashMap<Pair, Wall>();
		floors = new HashMap<Pair, Floor>();
		pits = new HashMap<Pair, Pit>();
		blocks = new HashMap<Pair, Block>();
		water = new HashMap<Pair, Water>();
		pixieFollow = true;
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
		if (Game.log) System.out.println("Drawing " + tiles.get(new Pair(x,y)).content + " at " + x + "," + y);
		if (tiles.get(new Pair(x,y)).light == 0b1) {
			foreColor = tiles.get(new Pair(x,y)).getForeColor();
			backColor = tiles.get(new Pair(x,y)).getBackColor();
		}
		else 
		{
			foreColor = Rating.BLACK.color;
			backColor = Rating.BLACK.color;
		}
		Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, foreColor, backColor);	
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
	public void newPlayer(int x, int y, Rating foreColor, Rating backColor) {
		//tiles.get(new Pair(x,y)).updateBuffer(Seed.PN);
		tiles.get(new Pair(x,y)).light = 0b1;
		playerForeColor = foreColor;
		playerBackColor = backColor;
		//UPDATE THIS LINE
		tiles.get(new Pair(x,y)).updateType(Seed.PN);
		tiles.get(new Pair(x,y)).setForeColor(playerForeColor);
		tiles.get(new Pair(x,y)).setBackColor(playerBackColor);	
		bufferString(x,y);
		updateLantern(x,y,playerRadius);
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
	public void newPixie(int x, int y, Rating foreColor, Rating backColor) {
		tiles.get(new Pair(x,y)).light = 0b1;
		pixieForeColor = foreColor;
		pixieBackColor = backColor;
		pixieX = x;
		pixieY = y;
		//UPDATE THIS LINE
		tiles.get(new Pair(x,y)).updateType(Seed.P1);
		tiles.get(new Pair(x,y)).setForeColor(pixieForeColor);
		tiles.get(new Pair(x,y)).setBackColor(pixieBackColor);
		bufferString(x,y);
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
		if (Game.log) System.out.println("New earth added.");
		tiles.get(new Pair(x,y)).updateType(Seed.EARTH);
		tiles.get(new Pair(x,y)).setForeColor(Rating.WHITE);
		tiles.get(new Pair(x,y)).setBackColor(Rating.BLACK);
		bufferString(x,y);		
	}
	
	/***************************************************************************************************************
	* Method      : newWall(int x, int y)
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
	public void newWall(int x, int y) {
		walls.put(new Pair(x,y), new Wall());
		tiles.get(new Pair(x,y)).updateType(Seed.CALC);
		tiles.get(new Pair(x,y)).setForeColor(walls.get(new Pair(x,y)).getForeColor());
		tiles.get(new Pair(x,y)).setBackColor(walls.get(new Pair(x,y)).getBackColor());
		//System.out.println(walls.get(new Pair(x,y)).getForeColor());

		//There is no need to buffer new walls as they will be drawn after they are passed through calcLevel().
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
		newFloor(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : newBlock(int x, int y)
	*
	* Purpose     : Adds a new Block object. Blocks can be pushed onto floor tiles or into pits. 
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newBlock(int x, int y) {
		removeFloor(x,y);
		blocks.put(new Pair(x,y), new Block());

		tiles.get(new Pair(x,y)).updateType(Seed.BLOCK);
		tiles.get(new Pair(x,y)).setForeColor(blocks.get(new Pair(x,y)).getForeColor());
		tiles.get(new Pair(x,y)).setBackColor(blocks.get(new Pair(x,y)).getBackColor());
		//System.out.println(blocks.get(new Pair(x,y)).getForeColor());

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
		newFloor(x,y);
	}

	/***************************************************************************************************************
	* Method      : newFloor(int x, int y)
	*
	* Purpose     : Adds a new Floor object. Draws a new tile of type EMPTY.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newFloor(int x, int y) {
		if (Game.log) System.out.println("New floor created at " + x + "," + y);
		floors.put(new Pair(x,y), new Floor());
		tiles.get(new Pair(x,y)).updateType(Seed.FLOOR);
		tiles.get(new Pair(x,y)).setForeColor(floors.get(new Pair(x,y)).getForeColor());
		tiles.get(new Pair(x,y)).setBackColor(floors.get(new Pair(x,y)).getBackColor());
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
	* Method      : newPit(int x, int y)
	*
	* Purpose     : Adds a new Pit object. Draws a new tile of type PIT.  
	*
	* Parameters  : int x - the x coordinate of the tile
	* 			  :	int y - the y coordinate of the tile
	*
	* Returns     : This method does not return a value.
	*  
	***************************************************************************************************************/
	public void newPit(int x, int y) {
		pits.put(new Pair(x,y), new Pit());

		tiles.get(new Pair(x,y)).updateType(Seed.PIT);
		tiles.get(new Pair(x,y)).setForeColor(pits.get(new Pair(x,y)).getForeColor());
		tiles.get(new Pair(x,y)).setBackColor(pits.get(new Pair(x,y)).getBackColor());
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
		newFloor(x,y);
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
		water.put(new Pair(x,y), new Water());

		tiles.get(new Pair(x,y)).updateType(Seed.WATER);
		tiles.get(new Pair(x,y)).setForeColor(water.get(new Pair(x,y)).getForeColor());
		tiles.get(new Pair(x,y)).setBackColor(water.get(new Pair(x,y)).getBackColor());
		bufferString(x,y);
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
		if (tiles.get(new Pair(x,y)).light == 0b1) {
			switch (tiles.get(new Pair(x,y)).content.type) {
				case "wall":
					tiles.get(new Pair(x,y)).setForeColor(walls.get(new Pair(x,y)).getForeColor());
					tiles.get(new Pair(x,y)).setBackColor(walls.get(new Pair(x,y)).getBackColor());
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
				case "me":
					tiles.get(new Pair(x,y)).setForeColor(playerForeColor);
					tiles.get(new Pair(x,y)).setBackColor(playerBackColor);
					break;
				case "pixie":
					tiles.get(new Pair(x,y)).setForeColor(pixieForeColor);
					tiles.get(new Pair(x,y)).setBackColor(pixieBackColor);
					break;
			}
		}
		if (Game.log) System.out.println("Updating color at " + x + " " + y);
		bufferString(x,y);
	}
	
	/***************************************************************************************************************
	* Method      : overrideTile(int x, int y, Seed contents)
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
	public void overrideTile(int x, int y, Seed contents) {
		tiles.get(new Pair(x,y)).overrideBuffer(contents);
		//This isn't working because no Walls exist in the new locations yet. When I add that code in carve, then I can uncomment this
		//tiles.get(new Pair(x,y)).setForeColor(walls.get(new Pair(x,y)).getForeColor());
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
		for(int y=topLevel; y>=bottomLevel; y--){
	          for (int x=leftLevel; x<=rightLevel; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  newEarth(x,y);
	          }
		}
		//would be nice to pull coordinates in from a text file
		
		newWall(0,4);
		newWall(-2,4);
		newWall(-3,4);
		newWall(-4,4);
		newWall(-6,4);
		newWall(6,4);
		newWall(5,4);
		newWall(4,4);
		newWall(2,4);
		newWall(1,4);
		
		newWall(0,5);
		newWall(-1,5);
		newWall(-2,5);
		newWall(-4,5);
		newWall(-5,5);
		newWall(-6,5);
		newWall(4,5);
		newWall(3,5);
		newWall(2,5);
		
		newWall(-6,3);
		newWall(6,3);
		
		newWall(-6,2);
		newWall(6,2);
		newWall(7,2);
		newWall(8,2);
		newWall(9,2);
		
		newWall(9,1);
		newWall(-6,1);
		
		newWall(6,0);
		newWall(9,0);
		
		newWall(-6,-1);
		newWall(9,-1);
		
		
		newWall(-6,-2);
		newWall(9,-2);
		newWall(8,-2);
		newWall(7,-2);
		newWall(6,-2);
		
		newWall(-6,-3);
		newWall(6,-3);
		
		newWall(-6,-4);
		newWall(-4,-4);
		newWall(-3,-4);
		newWall(-2,-4);
		newWall(0,-4);
		newWall(1,-4);
		newWall(2,-4);
		newWall(4,-4);
		newWall(5,-4);
		newWall(6,-4);
		
		newWall(-6,-5);
		newWall(-5,-5);
		newWall(-4,-5);
		newWall(-2,-5);
		newWall(-1,-5);
		newWall(0,-5);
		newWall(2,-5);
		newWall(3,-5);
		newWall(4,-5);
		
		newFloor(-5,3);
		newFloor(-4,3);
		newFloor(-3,3);
		newFloor(-2,3);
		newFloor(-1,3);
		newFloor(0,3);
		newFloor(1,3);
		newFloor(2,3);
		newFloor(3,3);
		newFloor(4,3);
		newFloor(5,3);
		
		newFloor(-5,2);
		newFloor(-4,2);
		newFloor(-3,2);
		newFloor(-2,2);
		newFloor(-1,2);
		newFloor(0,2);
		newFloor(1,2);
		newFloor(2,2);
		newFloor(3,2);
		newFloor(4,2);
		newFloor(5,2);
		
		newFloor(-5,1);
		newFloor(-4,1);
		newFloor(-3,1);
		newFloor(-2,1);
		newFloor(-1,1);
		newFloor(0,1);
		newFloor(1,1);
		newFloor(2,1);
		newFloor(3,1);
		newFloor(4,1);
		newFloor(5,1);
		newFloor(6,1);
		newFloor(7,1);
		newFloor(8,1);
		
		newFloor(-6,0);
		newFloor(-5,0);
		newFloor(-4,0);
		newFloor(-3,0);
		newFloor(-2,0);
		newFloor(-1,0);
		newFloor(0,0);
		newFloor(1,0);
		newFloor(2,0);
		newFloor(3,0);
		newFloor(4,0);
		newFloor(5,0);
		newFloor(6,0);
		newFloor(7,0);
		newFloor(8,0);
		
		newFloor(-5,-1);
		newFloor(-4,-1);
		newFloor(-3,-1);
		newFloor(-2,-1);
		newFloor(-1,-1);
		newFloor(0,-1);
		newFloor(1,-1);
		newFloor(2,-1);
		newFloor(3,-1);
		newFloor(4,-1);
		newFloor(5,-1);
		newFloor(6,-1);
		newFloor(7,-1);
		newFloor(8,-1);
		
		newFloor(-5,-2);
		newFloor(-4,-2);
		newFloor(-3,-2);
		newFloor(-2,-2);
		newFloor(-1,-2);
		newFloor(0,-2);
		newFloor(1,-2);
		newFloor(2,-2);
		newFloor(3,-2);
		newFloor(4,-2);
		newFloor(5,-2);
		
		newFloor(-5,-3);
		newFloor(-4,-3);
		newFloor(-3,-3);
		newFloor(-2,-3);
		newFloor(-1,-3);
		newFloor(0,-3);
		newFloor(1,-3);
		newFloor(2,-3);
		newFloor(3,-3);
		newFloor(4,-3);
		newFloor(5,-3);
		
		newWater(-5,4);
		newWater(-1,4);
		newWater(3,4);
		newWater(-5,-4);
		newWater(-1,-4);
		newWater(3,-4);
		
		
		newBlock(0,-2);
		newBlock(2,-2);
		newBlock(4,-2);
		newBlock(-2,-2);
		newBlock(-4,-2);
		
		newBlock(0,2);
		newBlock(2,2);
		newBlock(4,2);
		newBlock(-2,2);
		newBlock(-4,2);
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
		// We first assign each tile a 4-bit binary value by looking at surrounding tiles N, S, E and W of that tile
		// If there is a wall tile to the east we bit shift a 1 into the 0001 position
		// If there is a wall tile to the south we bit shift a 1 into the 0010 position
		// If there is a wall tile to the west we bit shift a 1 into the 0100 position
		// If there is a wall tile to the north we bit shift a 1 into the 1000 position
		for(int y=topLevel-1; y>bottomLevel+1; y--){
			for (int x = leftLevel + 1; x < rightLevel -1; x++) { 
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
		        			  newBlock(x,y);
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
	* Method      : detectCollision(int x, int y, int dir)
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
	public boolean detectCollision(int x, int y, int dir) {	
		switch (dir){
			case 0b0001:
				return (tiles.get(new Pair(x+1,y)).getType() == "wall" || tiles.get(new Pair(x+1,y)).getType() == "block") ? true : false;
			case 0b0010:
				return (tiles.get(new Pair(x,y-1)).getType() == "wall" || tiles.get(new Pair(x,y-1)).getType() == "block") ? true : false;
			case 0b0100:
				return (tiles.get(new Pair(x-1,y)).getType() == "wall" || tiles.get(new Pair(x-1,y)).getType() == "block") ? true : false;
			case 0b1000:
				return (tiles.get(new Pair(x,y+1)).getType() == "wall" || tiles.get(new Pair(x,y+1)).getType() == "block") ? true : false;
		}
		return false; //code should never hit this line
	} 
	
	/***************************************************************************************************************
	* Method      : digMap(int x, int y, int dir)
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
 	public void digMap(int x, int y, int dir){
		boolean isDestroyed;
 		switch(dir) {
 			//update this code for blocks as non-wall objects
			case 0b0001:
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
						newPit(x+1,y);
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
				if (tiles.get(new Pair(x+1,y)).getType() == "block") {
					isDestroyed = blocks.get(new Pair(x+1,y)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x+1,y);
					}
					else {
						updateColor(x+1,y);
					}
				}
				break;
			case 0b0010:
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
						newPit(x,y-1);
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
				if (tiles.get(new Pair(x,y-1)).getType() == "block") {
					isDestroyed = blocks.get(new Pair(x,y-1)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x,y-1);
					}
					else {
						updateColor(x,y-1);
					}
				}
				break;
			case 0b0100:
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
						newPit(x-1,y);
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
				if (tiles.get(new Pair(x-1,y)).getType() == "block") {
					isDestroyed = blocks.get(new Pair(x-1,y)).cycleLevel();	
					if (isDestroyed) {
						removeBlock(x-1,y);
					}
					else {
						updateColor(x-1,y);
					}
				}
				break;
			case 0b1000:
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
						newPit(x,y+1);
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
				if (tiles.get(new Pair(x,y+1)).getType() == "block") {
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
 	* Method      : pushBlock(int x, int y, int dir)
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
 	public boolean pushBlock(int x, int y, int dir){
		boolean isBlocked;
 		switch(dir) {
			case 0b0001:
				if (tiles.get(new Pair(x+1,y)).getType() == "block") {					
					isBlocked = detectCollision(x+1,y,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
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
							newBlock(x+2,y);
							blocks.get(new Pair(x+2,y)).setLevel(tempLevel);
		        			updateColor(x+2,y);
						}
						return true;
					}
					//else return true;
				}
				break;
			case 0b0010:
				if (tiles.get(new Pair(x,y-1)).getType() == "block") {
					isBlocked = detectCollision(x,y-1,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						if (tiles.get(new Pair(x,y-2)).getType() == "pit") {
							removePit(x,y-2);
							removeBlock(x,y-1);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x,y-1)).getLevel();	
							bufferCell(x,y-2);
							removeBlock(x,y-1);
							newBlock(x,y-2);
							blocks.get(new Pair(x,y-2)).setLevel(tempLevel);
		        			updateColor(x,y-2);
						}
						return true;
					}
					//else return true;
				}
				break;
			case 0b0100:
				if (tiles.get(new Pair(x-1,y)).getType() == "block") {	
					isBlocked = detectCollision(x-1,y,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						if (tiles.get(new Pair(x-2,y)).getType() == "pit") {
							removePit(x-2,y);
							removeBlock(x-1,y);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x-1,y)).getLevel();	
							bufferCell(x-2,y);
							removeBlock(x-1,y);
							newBlock(x-2,y);
							blocks.get(new Pair(x-2,y)).setLevel(tempLevel);
		        			updateColor(x-2,y);
						}
						return true;
					}
					//else return true;
				}
				break;
			case 0b1000:
				if (tiles.get(new Pair(x,y+1)).getType() == "block") {
					isBlocked = detectCollision(x,y+1,dir);
					if (isBlocked) {
						if (Game.log) System.out.println("Blocked");
					}
					else {
						if (tiles.get(new Pair(x,y+2)).getType() == "pit") {
							removePit(x,y+2);
							removeBlock(x,y+1);
							//Display message that pit is covered
						}
						else {
							int tempLevel = blocks.get(new Pair(x,y+1)).getLevel();	
							bufferCell(x,y+2);
							removeBlock(x,y+1);
							newBlock(x,y+2);
							blocks.get(new Pair(x,y+2)).setLevel(tempLevel);
		        			updateColor(x,y+2);
						}
						return true;
					}
					//else return true;
				}
				break;
		}
 		return false;
 		//Game.screen.refresh();
	}
	
 	public void dropBlock(int x, int y, int dir) {
		switch(dir) {
		case 0b0001:
			if (tiles.get(new Pair(x+1,y)).getType() == "floor") {
				removeFloor(x+1,y);
				newWall(x+1,y);
				overrideTile(x+1,y,Seed.CALC);
			}
			break;
		case 0b0010:
			if (tiles.get(new Pair(x,y-1)).getType() == "floor") {
				removeFloor(x,y-1);
				newWall(x,y-1);
				overrideTile(x,y-1,Seed.CALC);
			}
			break;
		case 0b0100:
			if (tiles.get(new Pair(x-1,y)).getType() == "floor") {
				removeFloor(x-1,y);
				newWall(x-1,y);
				overrideTile(x-1,y,Seed.CALC);
			}
			break;
		case 0b1000:
			if (tiles.get(new Pair(x,y+1)).getType() == "floor") {
				removeFloor(x,y+1);
				newWall(x,y+1);
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
	
	/***************************************************************************************************************
	* Method      : restoreTile(int x, int y, int dir)
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
	public Coord restoreTile(int x, int y, int dir) {
		boolean facingPixie = false;
		if (Game.log) System.out.println("Updating tile " + x + "," + y);
		switch(dir) {
			case 0b0001:
				if (tiles.get(new Pair(x+1,y)).getType() == "pixie") {
					facingPixie = true;
					tiles.get(new Pair(x+1,y)).restoreBuffer(x+1,y);
				}
				bufferCell(x+1,y);
				if (Game.playerControl) {
					tiles.get(new Pair(x+1,y)).updateType(Seed.PE);
				}
				else tiles.get(new Pair(x+1,y)).updateType(Seed.PC);
				if (!Game.fogOfWar) blackLantern(x,y,playerRadius);
				updateLantern(x+1,y,playerRadius);
				break;
			case 0b0010:
				if (tiles.get(new Pair(x,y-1)).getType() == "pixie") {
					facingPixie = true;
					tiles.get(new Pair(x,y-1)).restoreBuffer(x,y-1);
				}
				bufferCell(x,y-1);
				if (Game.playerControl) {
					tiles.get(new Pair(x,y-1)).updateType(Seed.PS);
				}
				else tiles.get(new Pair(x,y-1)).updateType(Seed.PC);
				if (!Game.fogOfWar) blackLantern(x,y,playerRadius);
				updateLantern(x,y-1,playerRadius);
				break;
			case 0b0100:
				if (tiles.get(new Pair(x-1,y)).getType() == "pixie") {
					facingPixie = true;
					tiles.get(new Pair(x-1,y)).restoreBuffer(x-1,y);
				}
				bufferCell(x-1,y);
				if (Game.playerControl) {
					tiles.get(new Pair(x-1,y)).updateType(Seed.PW);
				}
				else tiles.get(new Pair(x-1,y)).updateType(Seed.PC);
				if (!Game.fogOfWar) blackLantern(x,y,playerRadius);
				updateLantern(x-1,y,playerRadius);
				break;
			case 0b1000:
				if (tiles.get(new Pair(x,y+1)).getType() == "pixie") {
					facingPixie = true;
					tiles.get(new Pair(x,y+1)).restoreBuffer(x,y+1);
				}
				bufferCell(x,y+1);
				if (Game.playerControl) {
					tiles.get(new Pair(x,y+1)).updateType(Seed.PN);
				}
				else tiles.get(new Pair(x,y+1)).updateType(Seed.PC);
				if (!Game.fogOfWar) blackLantern(x,y,playerRadius);
				updateLantern(x,y+1,playerRadius);
				break;
		}
		if (pixieFollow) {
			if (!facingPixie) {
				tiles.get(new Pair(pixieX, pixieY)).restoreBuffer(pixieX, pixieY);
				bufferString(pixieX,pixieY);
			}
			
			tiles.get(new Pair(x,y)).updateType(Seed.P1);
			pixieX = x;
			pixieY = y;
			updateColor(x,y);

		}
		//Puts pixie in space previously occupied by player
		else {
			//What happens if !pixFollow but facingPixie?
			tiles.get(new Pair(x,y)).restoreBuffer(x,y);
			bufferString(x,y);
		}
		
		Game.screen.refresh();
		if (Game.log) System.out.println("^-----end update--------^");
		Coord tempCoord = new Coord(pixieX, pixieY);
		return tempCoord; //returns the coordinate of the pixie
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
		//System.out.println("Type is " + tiles.get(new Pair(x,y)).getType() + " at coordinates " + x + "," + y + " before update.");
		System.out.println(symbol);
		tiles.get(new Pair(x,y)).updateType(symbol);
		bufferString(x,y);
		//System.out.println("Type is " + tiles.get(new Pair(x,y)).getType() + " at coordinates " + x + "," + y + " after update.");
		Game.screen.refresh();
	} //end paintPlayer
	
	/***************************************************************************************************************
	* Method      : carveWall(int x, int y, int dir)
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
	public void carveWall(int x, int y, int dir) {	
		switch(dir){
			case 0b0001:
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
			case 0b0010: 
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
			case 0b0100:
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
			case 0b1000:
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
			newWall(x,y);	
		}
		//Idea possible to break out if type = block
	}

	public void blackLantern (int a, int b, int radius) {
		for(int y=b-radius; y<=b+radius; y++){
	          for (int x=a-radius; x<=a+radius; x++) {
	        	  double r = Math.sqrt(Math.pow(x-a,2)+ Math.pow(y-b,2));
	        	  if (r < radius) {
	        		  tiles.get(new Pair(x,y)).light = 0;
	        		  updateColor(x,y);
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
	        		  tiles.get(new Pair(x,y)).light = 1;
	        		  updateColor(x,y);
	        	  }
	          }
		}
	}
	
	public void displayLog() {
		//Displays keys associated with walls, tiles, pits etc for debuffing
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
		for(int y=topLevel; y>=bottomLevel; y--){
	          for (int x=leftLevel; x<=rightLevel; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  newEarth(x,y);
	          }
		}
	}
} //end class Level
