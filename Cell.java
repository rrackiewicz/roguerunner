import com.googlecode.lanterna.terminal.Terminal;

/**
 * Cell objects are responsible for drawing a Unicode character on a game tile
 * The current character being displayed is stored in the variable content
 * The previous character buffered is stored in the variable buffer 
 */
public class Cell {  
  
	public Seed content;
	public Seed buffer;
	public Rating rating; 
	//directional locks coerce cells to not render in the direction of the lock. 
	public boolean nlock = false;
	public boolean elock = false;
	public boolean slock = false;
	public boolean wlock = false;
	
	//Empty constructor
	public Cell() {
	}
	
	//Buffers the current content without updating the current content
	public void pushBuffer() {
		buffer = content;
		//System.out.println("Pushing " + content + " into buffer. Buffer is now " + buffer);
	}
	
	//Buffers the previous content and updates the new content. Preserves directional overrides.
	public void updateBuffer(Seed ID) {
		buffer = content;
		content = ID;
	}
	
	//Buffers the previous content and updates the new content. Unlocks all directional overrides.
	public void overrideBuffer(Seed ID) {
		this.nlock = false;
		this.slock = false;
		this.elock = false;
		this.wlock = false;
		buffer = content;
		content = ID;
	}
	
	//Restores the content of the cell to the contents of the buffer
	public void restoreBuffer() {
		//System.out.println("Restoring " + buffer + " from buffer.");
		content = buffer;
	}
}
