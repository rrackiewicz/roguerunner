import java.util.HashMap;

//import com.googlecode.lanterna.TerminalFacade;
//import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

//import java.util.*;
//import java.nio.charset.Charset;

public class Level {
	
	public HashMap<Pair, Cell> tiles;
	public HashMap<Pair, Wall> walls;
	public HashMap<Pair, Floor> floors;
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
	//temporary colors until they can be passed from Things
	public Terminal.Color foreColor = Terminal.Color.WHITE;
	public Terminal.Color backColor = Terminal.Color.BLACK;

	public Level() {
		levelWidth = Game.screenSize.getColumns();
		levelHeight = Game.screenSize.getRows();
		leftLevel = levelWidth / 2 * -1;
		rightLevel = levelWidth / 2;
		topLevel = levelHeight / 2; 
		bottomLevel = levelHeight / 2 * -1; 
		widthFactor = levelWidth / 2;
		heightFactor = levelHeight / 2;
		
		// Creates a new HashMap container to store all the map cells for a given level
		// Each Pair object represents an x,y coordinate on the screen
		// Each Cell object contains an enumeration of Unicode characters that can be rendered to the screen
		tiles = new HashMap<Pair, Cell>();
		walls = new HashMap<Pair, Wall>();
		floors = new HashMap<Pair, Floor>();
	}
	
	public void updateTile(int x, int y, Seed contents) {
		tiles.get(new Pair(x,y)).updateBuffer(contents);
		//Need to verify that the tile doesn't already exist. Figure this out.
		switch (tiles.get(new Pair(x,y)).content.type) {
			case "wall":
				//walls.put(new Pair(x,y), new Wall(1));
				//tiles.get(new Pair(x,y)).rating = Rating.GREEN;
				//System.out.println(tiles.get(new Pair(x,y)).rating.color);
				break;
			case "pit":
				break;
			case "ground":
				break;
		}
		//Need to figure out how store an enum inside an enum
		Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, foreColor, backColor);
	}
	
	public void overrideTile(int x, int y, Seed contents) {
		tiles.get(new Pair(x,y)).overrideBuffer(contents);
		Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, foreColor, backColor);
	}
	
//	public void paintTile(int x, int y) {		
//		Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, foreColor, backColor);
//	}
	
	public void newLevel() {
		// Fills the screen with empty Cells
		for(int y=topLevel; y>=bottomLevel; y--){
	          for (int x=leftLevel; x<=rightLevel; x++) {
	        	  tiles.put(new Pair(x,y), new Cell());
	        	  updateTile(x,y, Seed.EARTH);
	          }
		}
		Game.screen.refresh();
		//draw a practice room
		//would be nice to pull coordinates in from a text file
		updateTile(-3,3,Seed.CALC);
		updateTile(-2,3,Seed.CALC);
		updateTile(-1,3,Seed.CALC);
		updateTile(1,3,Seed.CALC);
		updateTile(2,3,Seed.CALC);
		updateTile(3,3,Seed.CALC);
		
		updateTile(-3,2,Seed.CALC);
		updateTile(-1,2,Seed.CALC);
		updateTile(1,2,Seed.CALC);
		updateTile(3,2,Seed.CALC);
		
		updateTile(-3,1,Seed.CALC);
		updateTile(-1,1,Seed.CALC);
		updateTile(0,1,Seed.CALC);
		updateTile(1,1,Seed.CALC);
		updateTile(3,1,Seed.CALC);
	
		updateTile(-3,0,Seed.CALC);
		updateTile(3,0,Seed.CALC);
	 
		updateTile(-5,-1,Seed.CALC);
		updateTile(-4,-1,Seed.CALC);
		updateTile(-3,-1,Seed.CALC);
		updateTile(1,-1,Seed.CALC);
		updateTile(2,-1,Seed.CALC);
		updateTile(3,-1,Seed.CALC);
	  
		updateTile(-5,-2,Seed.CALC);
		updateTile(1,-2,Seed.CALC);
	
		updateTile(-5,-3,Seed.CALC);
		updateTile(-4,-3,Seed.CALC);
		updateTile(-3,-3,Seed.CALC);
		updateTile(-2,-3,Seed.CALC);
		updateTile(-1,-3,Seed.CALC);
		updateTile(0,-3,Seed.CALC);
		updateTile(1,-3,Seed.CALC);
	  
		updateTile(-2,2,Seed.EMPTY);
		updateTile(2,2,Seed.EMPTY);
	
		updateTile(-2,1,Seed.EMPTY);
		updateTile(2,1,Seed.EMPTY);
		
		updateTile(-2,0,Seed.EMPTY);
		updateTile(-1,0,Seed.EMPTY);
		updateTile(0,0,Seed.EMPTY);
		updateTile(1,0,Seed.EMPTY);
		updateTile(2,0,Seed.EMPTY);
		
		updateTile(-1,-1,Seed.EMPTY);
		updateTile(-2,-1,Seed.EMPTY);
		updateTile(0,-1,Seed.EMPTY);
		
		updateTile(-4,-2,Seed.EMPTY);
		updateTile(-3,-2,Seed.EMPTY);
		updateTile(-2,-2,Seed.EMPTY);
		updateTile(-1,-2,Seed.EMPTY);
		updateTile(-0,-2,Seed.EMPTY);
	}
	
	// This method is used to convert CALC walls into their double-wall equivalent
	public void calcLevel() {
		// We first assign each tile a 4-bit binary value by looking at surrounding tiles N, S, E and W of that tile
		// If there is a wall tile to the east we bit shift a 1 into the 0001 position
		// If there is a wall tile to the south we bit shift a 1 into the 0010 position
		// If there is a wall tile to the west we bit shift a 1 into the 0100 position
		// If there is a wall tile to the north we bit shift a 1 into the 1000 position
		for(int y=topLevel-1; y>bottomLevel+1; y--){
			for (int x = leftLevel + 1; x < rightLevel -1; x++) { 
				if (tiles.get(new Pair(x,y)).content.type == "wall") {
	        		  //create a 4-bit binary number to store the on/off state of cardinal tiles
	        		  int pattern = 0b0000;
	        		  if (tiles.get(new Pair(x+1,y)).content.type == "wall") {
	        			  //bitshift 1 into position 0. NOTE: Bitwise | (or) won't overwrite current bits
	        			  pattern = pattern | (0x1 << 0);
	        		  }
	        		  if (tiles.get(new Pair(x,y-1)).content.type == "wall") {
	        			  //bitshift 1 into position 1
	        			  pattern = pattern | (0x1 << 1);
	        		  }
	        		  if (tiles.get(new Pair(x-1,y)).content.type == "wall") {
	        			  //bitshift 1 into position 2
	        			  pattern = pattern | (0x1 << 2);
	        		  }
	        		  if (tiles.get(new Pair(x,y+1)).content.type == "wall") {
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
	        		  
	        		  // An enum is assigned based on a cell's bit configuration
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
		        			  updateTile(x,y,Seed.BLK);
		        			  break;
		        		  default:
		        			  System.out.println("Leak in map");
	        		  }
	        		  // Writes everything in the screen buffer to the terminal
	        		  Game.screen.refresh();	
				}
			}
		}
	} //end calcLevel
	
	public boolean detectCollision(int x, int y, int dir) {	
		switch (dir){
			case 0b0001:
				// Using ternary conditional ? for brevity
				return (tiles.get(new Pair(x+1,y)).content.type == "wall" || tiles.get(new Pair(x+1,y)).content.type == "block") ? false : true;
			case 0b0010:
				return (tiles.get(new Pair(x,y-1)).content.type == "wall" || tiles.get(new Pair(x,y-1)).content.type == "block") ? false : true;
			case 0b0100:
				return (tiles.get(new Pair(x-1,y)).content.type == "wall" || tiles.get(new Pair(x-1,y)).content.type == "block") ? false : true;
			case 0b1000:
				return (tiles.get(new Pair(x,y+1)).content.type == "wall" || tiles.get(new Pair(x,y+1)).content.type == "block") ? false : true;
		}
		return false; //code should never hit this line
	} //end detect Collision
	
	// This method buffers the current content of the cell that the player is standing on
	public void bufferCell(int x, int y) {	
		tiles.get(new Pair(x,y)).pushBuffer();
	} //end bufferCell
	
	// Each time a player moves this method buffers the contents of the cell that the player is moving into while restoring the contents of the cell the player is leaving
	public void updateBuffer(int x, int y, int dir) {
		//Pushes the contents of the current x,y cell into the buffer before its contents are updated
		tiles.get(new Pair(x,y)).pushBuffer();
		switch(dir) {
			case 0b0001:
				tiles.get(new Pair(x-1,y)).restoreBuffer();
				Game.screen.putString((x + widthFactor - 1), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, Terminal.Color.WHITE, Terminal.Color.BLACK);
				break;
			case 0b0010:
				tiles.get(new Pair(x,y+1)).restoreBuffer();
				Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor + 1)), tiles.get(new Pair(x,y)).content.ID, Terminal.Color.WHITE, Terminal.Color.BLACK);
				break;
			case 0b0100:
				tiles.get(new Pair(x+1,y)).restoreBuffer();
				Game.screen.putString((x + widthFactor + 1), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, Terminal.Color.WHITE, Terminal.Color.BLACK);
				break;
			case 0b1000:
				tiles.get(new Pair(x,y-1)).restoreBuffer();
				Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor - 1)), tiles.get(new Pair(x,y)).content.ID, Terminal.Color.WHITE, Terminal.Color.BLACK);
				break;
		}
		Game.screen.refresh();
	}
	
	// This method assigns an enumeration to the cell the player occupies based on the direction he is facing
	public void updatePlayer(int x, int y, int dir) {	
		switch(dir){
			case 0b0001:
				tiles.get(new Pair(x,y)).content = Seed.PE;
				break;	
			case 0b0010:
				tiles.get(new Pair(x,y)).content = Seed.PS;
				break;
			case 0b0100:
				tiles.get(new Pair(x,y)).content = Seed.PW;
				break;
			case 0b1000:
				tiles.get(new Pair(x,y)).content = Seed.PN;
				break;
			//case 0b0000:
				//updateTile(x,y,Seed.PC);
				//break;
		}
		// Renders the screen to the buffer, without buffering the contents of the cell 
		Game.screen.putString((x + widthFactor), (levelHeight - (y + heightFactor)), tiles.get(new Pair(x,y)).content.ID, Terminal.Color.WHITE, Terminal.Color.BLACK);
		// Draw the contents of the buffer to the terminal
		Game.screen.refresh();
	} //end paintPlayer
	
	// The code that follows will prevent cells from rendering branches in specific directions by flagging their directional bits to be ignore
	public void removeWall(int x, int y, int dir) {	
		switch(dir){
			case 0b0001:
				overrideTile(x+1,y,Seed.EMPTY);
				if (tiles.get(new Pair(x+1,y-1)).content.type != "floor") {
					overrideTile(x+1,y-1,Seed.CALC);
					if (tiles.get(new Pair(x+1,y-2)).content == Seed.HD || tiles.get(new Pair(x+1,y-2)).content == Seed.NED || tiles.get(new Pair(x+1,y-2)).content == Seed.NWD || tiles.get(new Pair(x+1,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y-2)).nlock = true;
		
					}
					if (tiles.get(new Pair(x,y-1)).content == Seed.VD || tiles.get(new Pair(x,y-1)).content == Seed.SED || tiles.get(new Pair(x,y-1)).content == Seed.NED || tiles.get(new Pair(x,y-1)).content == Seed.ETD) {
						tiles.get(new Pair(x,y-1)).elock = true;
					}
				}
				if (tiles.get(new Pair(x+1,y+1)).content.type != "floor") {
					overrideTile(x+1,y+1,Seed.CALC);
					if (tiles.get(new Pair(x+1,y+2)).content == Seed.HD || tiles.get(new Pair(x+1,y+2)).content == Seed.SED || tiles.get(new Pair(x+1,y+2)).content == Seed.SWD || tiles.get(new Pair(x+1,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x+1,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x,y+1)).content == Seed.VD || tiles.get(new Pair(x,y+1)).content == Seed.SED || tiles.get(new Pair(x,y+1)).content == Seed.NED || tiles.get(new Pair(x,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x,y+1)).elock = true;
					}
				}
				if (tiles.get(new Pair(x+2,y-1)).content.type != "floor") {
					overrideTile(x+2,y-1,Seed.CALC);
					if (tiles.get(new Pair(x+2,y-2)).content == Seed.HD || tiles.get(new Pair(x+2,y-2)).content == Seed.NED || tiles.get(new Pair(x+2,y-2)).content == Seed.NWD || tiles.get(new Pair(x+2,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x+2,y-2)).nlock = true;
					}
					if (tiles.get(new Pair(x+3,y-1)).content == Seed.VD || tiles.get(new Pair(x+3,y-1)).content == Seed.SWD || tiles.get(new Pair(x+3,y-1)).content == Seed.NWD || tiles.get(new Pair(x+3,y-1)).content == Seed.WTD) {
						tiles.get(new Pair(x+3,y-1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x+2,y+1)).content.type != "floor") {
					overrideTile(x+2,y+1,Seed.CALC);
					if (tiles.get(new Pair(x+2,y+2)).content == Seed.HD || tiles.get(new Pair(x+2,y+2)).content == Seed.SED || tiles.get(new Pair(x+2,y+2)).content == Seed.SWD || tiles.get(new Pair(x+2,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x+2,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x+3,y+1)).content == Seed.VD || tiles.get(new Pair(x+3,y+1)).content == Seed.SWD || tiles.get(new Pair(x+3,y+1)).content == Seed.NWD || tiles.get(new Pair(x+3,y+1)).content == Seed.WTD) {
						tiles.get(new Pair(x+3,y+1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x+2,y)).content.type != "floor") {
					overrideTile(x+2,y,Seed.CALC);
					if (tiles.get(new Pair(x+3,y)).content == Seed.VD || tiles.get(new Pair(x+3,y)).content == Seed.SWD || tiles.get(new Pair(x+3,y)).content == Seed.NWD || tiles.get(new Pair(x+3,y)).content == Seed.WTD) {
						tiles.get(new Pair(x+3,y)).wlock = true;
					}
				}
			
				break;	
			case 0b0010: 
				overrideTile(x,y-1,Seed.EMPTY);
				if (tiles.get(new Pair(x-1,y-1)).content.type != "floor") {
					overrideTile(x-1,y-1,Seed.CALC);
					if (tiles.get(new Pair(x-2,y-1)).content == Seed.VD || tiles.get(new Pair(x-2,y-1)).content == Seed.SED || tiles.get(new Pair(x-2,y-1)).content == Seed.NED || tiles.get(new Pair(x-2,y-1)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y-1)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y)).content == Seed.HD || tiles.get(new Pair(x-1,y)).content == Seed.SED || tiles.get(new Pair(x-1,y)).content == Seed.SWD || tiles.get(new Pair(x-1,y)).content == Seed.STD) {
						tiles.get(new Pair(x-1,y)).slock = true;
					}
				}
				if (tiles.get(new Pair(x+1,y-1)).content.type != "floor") {
					overrideTile(x+1,y-1,Seed.CALC);
					if (tiles.get(new Pair(x+2,y-1)).content == Seed.VD || tiles.get(new Pair(x+2,y-1)).content == Seed.SWD || tiles.get(new Pair(x+2,y-1)).content == Seed.NWD || tiles.get(new Pair(x+2,y-1)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y-1)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y)).content == Seed.HD || tiles.get(new Pair(x+1,y)).content == Seed.SED || tiles.get(new Pair(x+1,y)).content == Seed.SWD || tiles.get(new Pair(x+1,y)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y)).slock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y-2)).content.type != "floor") {
					overrideTile(x-1,y-2,Seed.CALC);
					if (tiles.get(new Pair(x-2,y-2)).content == Seed.VD || tiles.get(new Pair(x-2,y-2)).content == Seed.SED || tiles.get(new Pair(x-2,y-2)).content == Seed.NED || tiles.get(new Pair(x-2,y-2)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y-2)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y-3)).content == Seed.HD || tiles.get(new Pair(x-1,y-3)).content == Seed.NED || tiles.get(new Pair(x-1,y-3)).content == Seed.NWD || tiles.get(new Pair(x-1,y-3)).content == Seed.NTD) {
						tiles.get(new Pair(x-1,y-3)).nlock = true;
					}	
				}
				if (tiles.get(new Pair(x+1,y-2)).content.type != "floor") {
					overrideTile(x+1,y-2,Seed.CALC);
					if (tiles.get(new Pair(x+2,y-2)).content == Seed.VD || tiles.get(new Pair(x+2,y-2)).content == Seed.SWD || tiles.get(new Pair(x+2,y-2)).content == Seed.NWD || tiles.get(new Pair(x+2,y-2)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y-2)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y-3)).content == Seed.HD || tiles.get(new Pair(x+1,y-3)).content == Seed.NED || tiles.get(new Pair(x+1,y-3)).content == Seed.NWD || tiles.get(new Pair(x+1,y-3)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y-3)).nlock = true;
					}
				}
				if (tiles.get(new Pair(x,y-2)).content.type != "floor") {
					overrideTile(x,y-2,Seed.CALC);
					if (tiles.get(new Pair(x,y-3)).content == Seed.HD || tiles.get(new Pair(x,y-3)).content == Seed.NED || tiles.get(new Pair(x,y-3)).content == Seed.NWD || tiles.get(new Pair(x,y-3)).content == Seed.NTD) {
						tiles.get(new Pair(x,y-3)).nlock = true;
					}
				}
				
				break;
			case 0b0100:
				overrideTile(x-1,y,Seed.EMPTY);
				if (tiles.get(new Pair(x-1,y-1)).content.type != "floor") {
					overrideTile(x-1,y-1,Seed.CALC);
					if (tiles.get(new Pair(x-1,y-2)).content == Seed.HD || tiles.get(new Pair(x-1,y-2)).content == Seed.NED || tiles.get(new Pair(x-1,y-2)).content == Seed.NWD || tiles.get(new Pair(x-1,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x-1,y-2)).nlock = true;
					}
					if (tiles.get(new Pair(x,y-1)).content == Seed.VD || tiles.get(new Pair(x,y-1)).content == Seed.SWD || tiles.get(new Pair(x,y-1)).content == Seed.NWD || tiles.get(new Pair(x,y-1)).content == Seed.WTD) {
						tiles.get(new Pair(x,y-1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y+1)).content.type != "floor") {
					overrideTile(x-1,y+1,Seed.CALC);
					if (tiles.get(new Pair(x-1,y+2)).content == Seed.HD || tiles.get(new Pair(x-1,y+2)).content == Seed.SED || tiles.get(new Pair(x-1,y+2)).content == Seed.SWD || tiles.get(new Pair(x-1,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x-1,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x,y+1)).content == Seed.VD || tiles.get(new Pair(x,y+1)).content == Seed.SWD || tiles.get(new Pair(x,y+1)).content == Seed.NWD || tiles.get(new Pair(x,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x,y+1)).wlock = true;
					}
				}
				if (tiles.get(new Pair(x-2,y-1)).content.type != "floor") {
					overrideTile(x-2,y-1,Seed.CALC);
					if (tiles.get(new Pair(x-2,y-2)).content == Seed.HD || tiles.get(new Pair(x-2,y-2)).content == Seed.NED || tiles.get(new Pair(x-2,y-2)).content == Seed.NWD || tiles.get(new Pair(x-2,y-2)).content == Seed.NTD) {
						tiles.get(new Pair(x-2,y-2)).nlock = true;
					}
					if (tiles.get(new Pair(x-3,y-1)).content == Seed.VD || tiles.get(new Pair(x-3,y-1)).content == Seed.SED || tiles.get(new Pair(x-3,y-1)).content == Seed.NED || tiles.get(new Pair(x-3,y-1)).content == Seed.ETD) {
						tiles.get(new Pair(x-3,y-1)).elock = true;
					}
				}
				if (tiles.get(new Pair(x-2,y+1)).content.type != "floor") {
					overrideTile(x-2,y+1,Seed.CALC);
					if (tiles.get(new Pair(x-2,y+2)).content == Seed.HD || tiles.get(new Pair(x-2,y+2)).content == Seed.SED || tiles.get(new Pair(x-2,y+2)).content == Seed.SWD || tiles.get(new Pair(x-2,y+2)).content == Seed.STD) {
						tiles.get(new Pair(x-2,y+2)).slock = true;
					}
					if (tiles.get(new Pair(x-3,y+1)).content == Seed.VD || tiles.get(new Pair(x-3,y+1)).content == Seed.SED || tiles.get(new Pair(x-3,y+1)).content == Seed.NED || tiles.get(new Pair(x-3,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x-3,y+1)).elock = true;
						
					}
				}
				if (tiles.get(new Pair(x-2,y)).content.type != "floor") {
					overrideTile(x-2,y,Seed.CALC);
					if (tiles.get(new Pair(x-3,y)).content == Seed.VD || tiles.get(new Pair(x-3,y)).content == Seed.SED || tiles.get(new Pair(x-3,y)).content == Seed.NED || tiles.get(new Pair(x-3,y)).content == Seed.ETD) {
						tiles.get(new Pair(x-3,y)).elock = true;
					}
				}
				break;
			case 0b1000:
				overrideTile(x,y+1,Seed.EMPTY);
				if (tiles.get(new Pair(x+1,y+1)).content.type != "floor") {
					overrideTile(x+1,y+1,Seed.CALC);
					if (tiles.get(new Pair(x+2,y+1)).content == Seed.VD || tiles.get(new Pair(x+2,y+1)).content == Seed.SWD || tiles.get(new Pair(x+2,y+1)).content == Seed.NWD || tiles.get(new Pair(x+2,y+1)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y+1)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y)).content == Seed.HD || tiles.get(new Pair(x+1,y)).content == Seed.NED || tiles.get(new Pair(x+1,y)).content == Seed.NWD || tiles.get(new Pair(x+1,y)).content == Seed.NTD) {
						tiles.get(new Pair(x+1,y)).nlock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y+1)).content.type != "floor") {
					overrideTile(x-1,y+1,Seed.CALC);
					if (tiles.get(new Pair(x-2,y+1)).content == Seed.VD || tiles.get(new Pair(x-2,y+1)).content == Seed.SED || tiles.get(new Pair(x-2,y+1)).content == Seed.NED || tiles.get(new Pair(x-2,y+1)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y+1)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y)).content == Seed.HD || tiles.get(new Pair(x-1,y)).content == Seed.NED || tiles.get(new Pair(x-1,y)).content == Seed.NWD || tiles.get(new Pair(x-1,y)).content == Seed.NTD) {
						tiles.get(new Pair(x-1,y)).nlock = true;
					}
				}
				if (tiles.get(new Pair(x+1,y+2)).content.type != "floor") {
					overrideTile(x+1,y+2,Seed.CALC);
					if (tiles.get(new Pair(x+2,y+2)).content == Seed.VD || tiles.get(new Pair(x+2,y+2)).content == Seed.SWD || tiles.get(new Pair(x+2,y+2)).content == Seed.NWD || tiles.get(new Pair(x+2,y+2)).content == Seed.WTD) {
						tiles.get(new Pair(x+2,y+2)).wlock = true;
					}
					if (tiles.get(new Pair(x+1,y+3)).content == Seed.HD || tiles.get(new Pair(x+1,y+3)).content == Seed.SED || tiles.get(new Pair(x+1,y+3)).content == Seed.SWD || tiles.get(new Pair(x+1,y+3)).content == Seed.STD) {
						tiles.get(new Pair(x+1,y+3)).slock = true;
					}
				}
				if (tiles.get(new Pair(x-1,y+2)).content.type != "floor") {
					overrideTile(x-1,y+2,Seed.CALC);
					if (tiles.get(new Pair(x-2,y+2)).content == Seed.VD || tiles.get(new Pair(x-2,y+2)).content == Seed.SED || tiles.get(new Pair(x-2,y+2)).content == Seed.NED || tiles.get(new Pair(x-2,y+2)).content == Seed.ETD) {
						tiles.get(new Pair(x-2,y+2)).elock = true;
					}
					if (tiles.get(new Pair(x-1,y+3)).content == Seed.HD || tiles.get(new Pair(x-1,y+3)).content == Seed.SED || tiles.get(new Pair(x-1,y+3)).content == Seed.SWD || tiles.get(new Pair(x-1,y+3)).content == Seed.STD) {
						tiles.get(new Pair(x-1,y+3)).slock = true;
					}
				}
				
				if (tiles.get(new Pair(x,y+2)).content.type != "floor") {
					overrideTile(x,y+2,Seed.CALC);
					if (tiles.get(new Pair(x,y+3)).content == Seed.HD || tiles.get(new Pair(x,y+3)).content == Seed.SED || tiles.get(new Pair(x,y+3)).content == Seed.SWD || tiles.get(new Pair(x,y+3)).content == Seed.STD) {
						tiles.get(new Pair(x,y+3)).slock = true;
					}
				}
				break;
		} // end switch
		Game.screen.refresh();
	} // end removeWall
	
} //end class Level
