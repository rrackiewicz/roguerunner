public class Pixie extends Entity {
 	
	public Pixie(int x, int y, int dir) {
		coord = new Coord(x,y);
		//System.out.println(coord.getX() + "," + coord.getY());
		if (Game.log) System.out.println("Pixie created.");
//		this.x = x;
//		this.y = y;
		this.dir = dir;
		this.foreColor = Rating.WHITE;
		this.backColor = Rating.BLACK;
		this.lanternRadius = 7;
		this.moveRate = 1000;
	}
	
	public Seed getSymbol() {
		switch(this.dir) {
		case 0b001:
			if (moveCooldown)  
				return Seed.PE;
			else return Seed.PEC;
		case 0b0010:
			if (moveCooldown)  
				return Seed.PS;
			else return Seed.PSC;
		case 0b0100:
			if (moveCooldown)  
				return Seed.PW;
			else return Seed.PWC;
		case 01000:
			if (moveCooldown)  
				return Seed.PN;
			else return Seed.PNC;
		
		}
		return Seed.PC;
	}
	
	public void setDirection(int dir) {
		this.dir = dir;
	}
}
