
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

public class mbot extends cell{
	// describe the cell's neighborhood
	int dim = 2;//dimensionality
	int radius = 1;
	
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
	
	// born on
	boolean[] born;
	
	//survives on
	boolean[] survives;
	
	//age and fade rule variables
	int age;
	boolean ages;
	int fade;
	boolean fades;
	
	//constructors
	public mbot(){
		//dim = 2;
		//radius = 1;
		active = false;
		state = 0;
		name = "M.B.O.T.";
		hoodx = -1;
		hoody = -1;
		self = false;
		mystate = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		matcount = 0;
		born = new boolean[9];
		survives = new boolean[9];
		neighborhood = new boolean[3][3];
		}
		
		public mbot(String type){
		//dim = 2;
		//radius = 1;
		active = false;
		state = 0;
		name = type;
		hoodx = -1;
		hoody = -1;
		self = false;
		mystate = 0;
		age = 0;
		ages = false;
		fade = -1;
		fades = false;
		matcount = 0;
		born = new boolean[9];
		survives = new boolean[9];
		neighborhood = new boolean[3][3];
		
		for(int n = 0; n < 9; n++){
			born[n] = false; survives[n] = false;
		if( type == "2x2"){ if(n == 3 || n == 6){born[n] = true;} if(n == 1|| n == 2|| n == 5){survives[n] = true;}}
		if( type == "34Life"){ if(n == 3 || n == 4){born[n] = true; survives[n] = true;} }
		if( type == "Amoeba"){ if(n == 3 || n == 5 || n == 7){born[n] = true;} if(n == 1 || n == 3||  n == 5|| n == 8){survives[n] = true;}}
		if( type == "Assimilation"){ if(n == 3 || n==4 || n==5){born[n] = true;} if(n == 4 || n == 5 || n==6 || n==7 ){survives[n] = true;}}
		if( type == "Coagulations"){ if(n == 3 ||  n == 7 || n == 8){born[n] = true;} if( n == 2|| n == 3||n == 5|| n == 6||n == 7 || n == 8){survives[n] = true;}}
		if( type == "Coral"){ if(n == 3){born[n] = true;} if(n == 4 || n == 5 || n==6 || n==7 || n == 8){survives[n] = true;}}
		if( type == "DayNight"){ if(n == 3 ||  n == 6 || n == 7 || n == 8){born[n] = true;} if( n == 3|| n == 4 || n == 6|| n == 7|| n == 8){survives[n] = true;}}
		if( type == "Diamoeba"){ if(n == 3 || n == 5 || n == 6 || n == 7 || n == 8){born[n] = true;} if( n == 5|| n == 6 || n == 7 || n == 8){survives[n] = true;}}
		if( type == "DotLife"){ if(n == 3){born[n] = true;} if(n == 0|| n == 2|| n == 3){survives[n] = true;}}
		if( type == "DryLife"){ if(n == 3 ||  n == 7){born[n] = true;} if( n == 2|| n == 3){survives[n] = true;}}
		if(type == "Fredkin"){if(n == 1 || n == 3 || n == 5 || n == 7){born[n] = true;}else{ survives[n] = true;} }
		if(type == "Gnarl"){if(n == 1){born[n] = true;} if (n == 1){survives[n] = true;}}
		if( type == "HighLife"){ if(n == 3 ||  n == 6){born[n] = true;} if( n == 2|| n == 3){survives[n] = true;}}
		if(type == "Life"){if(n == 3){born[n] = true;} if (n == 3 || n == 2){survives[n] = true;}}
		if( type == "LifeWODeath"){ if(n == 3){born[n] = true;} survives[n] = true;}
		if( type == "LFOD"){ if(n == 2){born[n] = true;} if(n == 0){survives[n] = true;}}
		if( type == "LongLife"){ if(n == 3 || n == 4 || n == 5){born[n] = true;} if( n == 5){survives[n] = true;}}
		if( type == "Maze"){ if(n == 3){born[n] = true;} if(n == 1 || n == 2 || n==3 || n==4 || n == 5){survives[n] = true;}}
		if( type == "Mazectric"){ if(n == 3){born[n] = true;} if(n == 1 || n == 2 || n==3 || n==4 ){survives[n] = true;}}
		if( type == "Move"){ if(n == 3 || n == 6 || n == 8){born[n] = true;} if(n == 2|| n == 4|| n == 5){survives[n] = true;}}
		if( type == "PseudoLife"){ if(n == 3 || n == 5 || n == 7){born[n] = true;} if( n == 2 || n == 3 || n == 8){survives[n] = true;}}
		if(type == "Replicator"){if(n == 1 || n == 3 || n == 5 || n == 7){born[n] = true; survives[n] = true;} }
		if(type == "Seeds"){if(n == 2){born[n] = true;}}
		if( type == "Serviettes"){ if(n == 2 || n == 3 || n == 4){born[n] = true;}}
		if( type == "Stains"){ if(n == 3 || n == 6 || n == 7|| n == 8){born[n] = true;} if( n == 2|| n == 3|| n == 5|| n == 6|| n == 7|| n == 8){survives[n] = true;}}
		if( type == "Vote"){ if(n > 4){born[n] = true;} if(n >3){survives[n] = true;}}
		if( type == "Vote4/5"){ if(n ==4 || n == 6 || n == 7|| n ==8){born[n] = true;} if( n == 3|| n == 5|| n == 6|| n == 7|| n == 8){survives[n] = true;}}
		if( type == "WalledCities"){ if(n > 3){born[n] = true;} if( n == 2|| n == 3|| n == 4|| n == 5){survives[n] = true;}}
		}
		
	}
		
		//Get and set controls and options
		
		public boolean getControls(String control){
			if(control == "Age"){ return true;}
			if(control == "Fade"){ return true;}
			if(control == "Mat"){return true;}
			if(control == "Born"){ return true;}
			if(control == "Survives"){return true;}
			 return false;}
		
		public boolean getOption(String opname){ 
			if(opname == "Ages"){ return ages;}
			if(opname == "Fades"){ return fades;}
			if(opname == "B0"){return born[0];}if(opname == "B1"){return born[1];}
			if(opname == "B2"){return born[2];}if(opname == "B3"){return born[3];}
			if(opname == "B4"){return born[4];}if(opname == "B5"){return born[5];}
			if(opname == "B6"){return born[6];}if(opname == "B7"){return born[7];}
			if(opname == "B8"){return born[8];}
			
			if(opname == "S0"){return survives[0];}if(opname == "S1"){return survives[1];}
			if(opname == "S2"){return survives[2];}if(opname == "S3"){return survives[3];}
			if(opname == "S4"){return survives[4];}if(opname == "S5"){return survives[5];}
			if(opname == "S6"){return survives[6];}if(opname == "S7"){return survives[7];}
			if(opname == "S8"){return survives[8];}
			
			return false;}
		
		public void setOption(String opname, boolean b){
			if(opname == "Ages"){ages = b;if(b == false){if(active){age = 1;}else{age = 0;}}}
			if(opname == "Fades"){fades = b; if(b){ages = true;}}
			if(opname == "B0"){ born[0]=b;}if(opname == "B1"){ born[1]=b;}
			if(opname == "B2"){ born[2]=b;}if(opname == "B3"){ born[3]=b;}
			if(opname == "B4"){ born[4]=b;}if(opname == "B5"){ born[5]=b;}
			if(opname == "B6"){ born[6]=b;}if(opname == "B7"){ born[7]=b;}
			if(opname == "B8"){ born[8]=b;}
			
			if(opname == "S0"){ survives[0]=b;}if(opname == "S1"){ survives[1]=b;}
			if(opname == "S2"){ survives[2]=b;}if(opname == "S3"){ survives[3]=b;}
			if(opname == "S4"){ survives[4]=b;}if(opname == "S5"){ survives[5]=b;}
			if(opname == "S6"){survives[6]=b;}if(opname == "S7"){ survives[7]=b;}
			if(opname == "S8"){ survives[8]=b;}
			}
		
		public int getParameter(String paramname){ 
			if(paramname == "Dim"){ return dim;}
			if(paramname == "Rad"){ return radius;}
			if(paramname == "Age"){ return age;}
			if(paramname == "Fade"){ return fade;}
			if(paramname == "Mat"){ return mat;}
			if(paramname == "Matcount"){ return matcount;}
			return -1;}
		
		public void setParameter(String paramname, int a){
			if(paramname == "Age"){ age = a;}
			if(paramname == "Fade"){fade = a;}
			if(paramname == "Mat"){ mat = a;}
			if(paramname == "Matcount"){ matcount = a;}
			}
		
		public int getHoodX(){ return hoodx;}
		
		public int getHoodY(){ return hoody;}
		
		//main logic methods
		
		public void iterate(){
			  matcount += 1;
			 if(matcount == mat){matcount = 0;
			 calculate(); }
			 if(ages){ if(active){ if(age == 0){age = 1;} else{age += 1;}}else{ age = 0;} state = age;}
			 if(fades){ if( age >= fade){ purgeState(); age = 0;}}
			}
		
		private void calculate(){
			int cellstate = 0;
			for (int y = 0; y < 3; y++){
				for(int x = 0; x < 3; x++){
					if(x == 1 && y == 1){self = neighborhood[x][y];}
					else{if (neighborhood[x][y]){cellstate += 1;}}
				}}
				if(self){active = survives[cellstate];} else{active = born[cellstate];}
			}
		
		
		
		public void purgeState(){ active = false; state = 0;}
		
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
