import java.util.HashMap;

public class Mask {
	public HashMap<Pair, Switch> lantern;
	
	public Mask(int radius) {
		lantern = new HashMap<Pair, Switch>();
		int h = radius;
		int k = radius;
		for(int y=0; y<=2*radius; y++){
	          for (int x=0; x<=2*radius; x++) {
	        	  lantern.put(new Pair(x,y), new Switch());
	        	  //Next, use distance formula to calculate if r lies inside radius. If so, flip the bit to 1
	        	  double r = Math.sqrt(Math.pow(x-h,2)+ Math.pow(y-k,2));
	        	  if (r < radius) {
	        		  lantern.get(new Pair(x,y)).setBit(1);
	        	  }
	        	  System.out.print(lantern.get(new Pair(x,y)).getBit());
	        	 
	          }
	          System.out.println();
		}
	}
}
