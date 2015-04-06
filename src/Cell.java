import com.googlecode.lanterna.terminal.Terminal;

/**
 * Cell objects are responsible for drawing a Unicode character on a game tile
 * The current character being displayed is stored in the variable content
 * The previous character buffered is stored in the variable buffer 
 */
public class Cell {  
  
	public Seed content;
	public Seed bufferContent;
	private Rating foreColor; 
	private Rating bufferForeColor;
	private Rating backColor;
	private Rating bufferBackColor;
	public int light;
	//directional locks coerce cells to not render in the direction of the lock. 
	public boolean nlock = false;
	public boolean elock = false;
	public boolean slock = false;
	public boolean wlock = false;
	
	//Empty constructor
	public Cell() {
		this.light = 0b0;
	}
	
	//Buffers the current content without updating the current content
	//The parameters x and y are only for debugging
	public void pushBuffer(int x, int y) {
		bufferContent = content;
		bufferForeColor = foreColor;
		bufferBackColor = backColor;
		if (Game.log) System.out.println("Buffering the " + bufferForeColor + " " + bufferContent + " without changing content " + content + " at " + x + "," + y);
		//System.out.println("Buffer color is " + bufferForeColor);
	}
	
	//Buffers the previous content and updates the new content. Preserves directional overrides.
	public void updateBuffer(Seed ID) {
		bufferContent = content;
		bufferForeColor = foreColor;
		bufferBackColor = backColor;
		content = ID;
		if (Game.log) System.out.println("Buffering the " + bufferForeColor + " " + bufferContent);
		if (Game.log) System.out.println("Content is now a " + foreColor + " " + content);
		if (Game.log) System.out.println("^----------------------------------^");
	}
	
	public void updateType(Seed ID) {
		content = ID;
	}
	
	//Buffers the previous content and updates the new content. Unlocks all directional overrides.
	public void overrideBuffer(Seed ID) {
		this.nlock = false;
		this.slock = false;
		this.elock = false;
		this.wlock = false;
		bufferContent = content;
		bufferForeColor = foreColor;
		bufferBackColor = backColor;
		content = ID;
	}
	
	//Restores the content of the cell to the contents of the buffer
	//The parameters x and y are only for debugging
	public void restoreBuffer(int x, int y) {
		content = bufferContent;
		foreColor = bufferForeColor;
		backColor = bufferBackColor;
		if (Game.log) System.out.println("Restoring the " + foreColor + " " + content + " from buffer at " + x + "," + y);
	}
	
	public String getType(){
		return content.type;
	}
	
	public void setForeColor(Rating color) {
		this.foreColor = color; 
	}
	
	public Terminal.Color getForeColor() {
		return foreColor.color;
	}
	
	public void setBackColor(Rating color) {
		this.backColor = color; 
	}
	
	public Terminal.Color getBackColor() {
		return backColor.color;
	}
	
}
