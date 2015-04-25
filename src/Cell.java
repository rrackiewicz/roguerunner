import com.googlecode.lanterna.terminal.Terminal;

/**
 * Cell objects are responsible for drawing a Unicode character on a game tile
 * The current character being displayed is stored in the variable content
 * The previous character buffered is stored in the variable buffer 
 */
public class Cell {  
  
	public Seed content;
	public Seed bufferContent;
	private Terminal.Color foreColor; 
	private Terminal.Color bufferForeColor;
	private Terminal.Color backColor;
	private Terminal.Color bufferBackColor;
	private boolean light = false;
	private boolean bufferLight;
	//directional locks coerce cells to not render in the direction of the lock. 
	public boolean nlock;
	public boolean elock;
	public boolean slock; 
	public boolean wlock; 
	
	//Empty constructor
	public Cell() {
	}
	
	//Buffers the current content without updating the current content
	//The parameters x and y are only for debugging
	public void pushBuffer(int x, int y) {
		bufferContent = content;
		bufferForeColor = foreColor;
		bufferBackColor = backColor;
		bufferLight = light;
		//if (Game.log) System.out.println("Buffering the " + bufferForeColor + " " + bufferContent + ". Content is " + content + " at " + x + "," + y);
		
	}
	
	//Buffers the previous content and updates the new content. Preserves directional overrides.
	public void updateBuffer(Seed ID) {
		bufferContent = content;
		bufferForeColor = foreColor;
		bufferBackColor = backColor;
		bufferLight = light;
		content = ID;
//		if (Game.log) System.out.println("Buffering the " + bufferForeColor + " " + bufferContent);
//		if (Game.log) System.out.println("Content is now a " + foreColor + " " + content);
	}
	
	public void updateType(Seed ID) {
		content = ID;
		//System.out.println("I am a " + this.foreColor + " " + content);
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
		bufferLight = light;
		content = ID;
	}
	
	//Restores the content of the cell to the contents of the buffer
	//The parameters x and y are only for debugging
	public void restoreBuffer(int x, int y) {
		content = bufferContent;
		foreColor = bufferForeColor;
		backColor = bufferBackColor;
		light = bufferLight;
		//if (Game.log) System.out.println("Restoring the " + foreColor + " " + content + " from buffer at " + x + "," + y);
	}
	
	public String getType(){
		return content.type;
	}
	
	public void setType(String type){
		content.type = type;
	}
	
	public Seed getSeed() {
		return content;
	}
	
	public boolean getCollideState() {
		return content.canCollide;
	}
	
	public void setLight(boolean state) {
		this.light = state;
	}
	
	public boolean getLight() {
		return this.light;
	}
	
	public void bufferLight() {
		this.bufferLight = light;
	}
	
	public void restoreLight() {
		this.light = this.bufferLight;
	}
	
	public Terminal.Color getBufferForeColor() {
		return bufferForeColor;
	}
	
	public Seed getBufferContent() {
		return bufferContent;
	}
	
	public void setForeColor(Terminal.Color color) {	
		this.foreColor = color;
		//System.out.println("Rating " + color + " is translated to forecolor " + this.foreColor);
	}
	
	public Terminal.Color getForeColor() {
		return foreColor;
	}
	
	public void setBackColor(Terminal.Color color) {
		this.backColor = color; 
	}
	
	public Terminal.Color getBackColor() {
		return backColor;
	}
	
}
