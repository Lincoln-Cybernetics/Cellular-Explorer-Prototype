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
class Cell{
		boolean[][] neighborhood = new boolean[3][3];
		int maturity = 1;
		int cellstate;
		boolean active;
		String hood = "None";
		public Cell(int a){}
		public Cell(){}
		public String getNeighborhood(){ return hood;}
		public boolean iterate(){return false;}
		public boolean iterate(boolean s){return false;}
		public boolean iterate( boolean wolfhood[]){return false;}
		public boolean iterate(boolean neighborhood[][]){
			return false;}
	}	

class blinkCell extends Cell{
	boolean blinkstate = false;
	int counter = 0;
	String hood = "Self";
	public String getNeighborhood(){ return hood;}
	public blinkCell(){}
	public blinkCell(int a){maturity = a;}
	public boolean iterate(boolean s){ 
		counter +=1;
		if (counter == maturity){
			counter = 0;
		if (blinkstate == false){ blinkstate = true; return blinkstate;}
		else{blinkstate = false; return blinkstate;}}
		else{return s;}
	}
}

class onCell extends Cell{
	String hood = "None";
	public onCell(){}
	public onCell(int a){}
	public String getNeighborhood(){ return hood;}
	public boolean iterate(){return true;}
}

class blinkCell2 extends Cell{
	String hood = "Self";
	int counter = 0;
	boolean blinkstate = true;
	public String getNeighborhood(){ return hood;}
	public blinkCell2(int a){maturity = a;}
	public blinkCell2(){}
	public boolean iterate(boolean s){
		counter += 1;
		if (counter == maturity){ 
		counter = 0;
		if (blinkstate == false){ blinkstate = true; return blinkstate;}
		else{blinkstate = false; return blinkstate;}}
		else{return s;}
	}
}

class Conway extends Cell{
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int cellstate;
	int a;
	int b;
	boolean active;
	String hood = "Moore";
	public String getNeighborhood(){ return hood;}
	public Conway(){}
	public Conway(int a){
		maturity = a;}
		
	public boolean iterate(boolean neighborhood[][]){
		counter += 1;
		if (counter == maturity){ counter = 0;
		cellstate = 0;
		for(b=0;b<=2;b++){
			for(a=0;a<=2;a++){
				if (neighborhood[a][b] == true){ cellstate+=1;}
			}}
			if(neighborhood[1][1] == true){cellstate-=1;}
			switch(cellstate){
					case 0: active = false;
					break;
					case 1: active = false;
					break;
					case 2: active =  neighborhood[1][1];
					break;
					case 3: active = true;
					break;
					case 4: active = false;
					break;
					case 5: active = false;
					break;
					case 6: active = false;
					break;
					case 7: active = false;
					break;
					case 8: active = false;
					break;
					default: active = false;
					break;
				}
		return active;}
		else{return neighborhood[1][1];}
	}
}

class Seeds extends Cell{
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int cellstate;
	int a;
	int b;
	boolean active;
	String hood = "Moore";
	public String getNeighborhood(){ return hood;}
	public Seeds(){}
	public Seeds(int a){ maturity = a;}
	public boolean iterate(boolean neighborhood[][]){
		counter += 1;
		if (counter == maturity){ counter = 0;
		cellstate = 0;
		for(b=0;b<=2;b++){
			for(a=0;a<=2;a++){
				if (neighborhood[a][b] == true){ cellstate+=1;}
			}}
			if(neighborhood[1][1] == true){cellstate-=1;}
			switch(cellstate){
					case 0: active = false;
					break;
					case 1: active = false;
					break;
					case 2: active =  true;
					break;
					case 3: active = false;
					break;
					case 4: active = false;
					break;
					case 5: active = false;
					break;
					case 6: active = false;
					break;
					case 7: active = false;
					break;
					case 8: active = false;
					break;
					default: active = false;
					break;
				}
		return active;}
		else{return neighborhood[1][1];}
	}
}
