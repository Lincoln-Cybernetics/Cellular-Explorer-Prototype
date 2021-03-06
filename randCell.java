import java.util.Random;
/*Cellular Explorer Prototype proof of concept
 * Copyright(C) 02013 Matt Ahlschwede
 *  This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

public class randCell extends cell{
	// describe the cell's neighborhood
	int dim;//dimensionality
	int radius;
	
	// describe the current state of the cell
	boolean active;
	int state;
	String name;
	
	//neighborhood location variables
	int hoodx;
	int hoody;
	
	// neighborhood variables
	boolean self;
	boolean[] neighbors;
	boolean[][] neighborhood;
	boolean[][][] environment;
	
	int mystate;
	int[] neighborstate;
	int[][] hoodstate;
	int[][][] envirostate;
	
	// maturity setting
	int mat;
	int matcount;
	
	//age and fade rule variables
	int age;
	boolean ages;
	
	// Random number generator
	Random bombadil;
	
	
	//constructor
	public randCell(){
		dim = -1;
		radius = 0;
		active = false;
		state = 0;
		name = "Random";
		hoodx = -1;
		hoody = -1;
		self = false;
		mystate = 0;
		age = 0;
		ages = false;
		mat = 1;
		matcount = 0;
		bombadil = new Random();
		}
			//initilization
		public void setLocation(int x, int y){
			
		}
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Mat"){return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			return false;}
		
		public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			
			}
		
		public int getParameter(String paramname){ 
			if(paramname == "Dim"){ return dim;}
			if(paramname == "Rad"){ return radius;}
			if(paramname == "HoodSize"){return 0;}
			if(paramname == "NextX"){return -1;}
			if(paramname == "NextY"){return -1;}
			if(paramname == "Age"){ return age;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			}
		
		public void setRule(int a, boolean b){}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		public void iterate(){
			 matcount += 1;
			 if(matcount >= mat){matcount = 0;
			 calculate(); }
			 if(ages){ if(active){ age = bombadil.nextInt(256);}else{ age = 0;} state = age;}
			 else{if(active){state = 1;}else{state = 0;}}
			
			}
		
		private void calculate(){if(bombadil.nextBoolean()){active = true;}else{active = false;}}
		
		public void purgeState(){ active = false; state = 0;}
		
		public void activate(){ active = true; state = 1;}
		
		// current state returning methods
		public boolean getActive(){ return active;}
		
		public int getState(){ return state;}
		
		public String getName(){ return name;}
		
		// neighborhood setting methods
		public void setSelf(boolean b){ self = b;}
		
		public void setNeighbors( boolean[] truckdrivin){neighbors = truckdrivin;}
		
		public void setNeighborhood( boolean[][] spozak){neighborhood = spozak;}
		
		public void setEnvironment( boolean[][][] biome){environment = biome;}
		
		public void setState( int a){ state = a;}
		
		public void setNeighborState( int[] address){ neighborstate = address;}
		
		public void setHoodState( int[][] zipcode){ hoodstate = zipcode;}
		
		public void setEnvironmentState( int[][][] planet){envirostate = planet;}
		
		
		
		
}
