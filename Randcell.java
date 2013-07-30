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

class Randcell extends Cell{
	String hood = "Self";
	int counter = 0;
	Random wiggler = new Random();
	public String getNeighborhood(){ return hood;}
	public boolean iterate(boolean s){
		counter += 1;
		if (counter == maturity){
			counter = 0;
		return wiggler.nextBoolean();}
		else{return s;}}
		public Randcell(){}
		public Randcell(int a){maturity = a;}
	}
	
	class OddCell extends Cell{
	String hood = "Moore";	
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int cellstate;
	int a;
	int b;
	boolean active;
	public String getNeighborhood(){ return hood;}
	public OddCell(){}
	public OddCell(int a){
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
			if(cellstate%2 == 1){active = true;}
			else{active = false;}
		return active;}
		else{return neighborhood[1][1];}
	}
}

class EvenCell extends Cell{
	String hood = "Moore";
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int cellstate;
	int a;
	int b;
	boolean active;
	public EvenCell(){}
	public EvenCell(int a){
		maturity = a;}
		public String getNeighborhood(){ return hood;}
	public boolean iterate(boolean neighborhood[][]){
		counter += 1;
		if (counter == maturity){ counter = 0;
		cellstate = 0;
		for(b=0;b<=2;b++){
			for(a=0;a<=2;a++){
				if (neighborhood[a][b] == true){ cellstate+=1;}
			}}
			if(neighborhood[1][1] == true){cellstate-=1;}
			if(cellstate%2 == 0){active = true;}
			else{active = false;}
		return active;}
		else{return neighborhood[1][1];}
	}
}

class ConveyorCell extends Cell{
	String hood = "Moore";
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int direction = 0;
	int a;
	int b;
	boolean active;
	public ConveyorCell(){}
	public ConveyorCell(int a){
		maturity = a;}
	public ConveyorCell(int a, int b){
		maturity = a; direction = b;}
	public void setDir(int a){ direction = a; return;}
	public String getNeighborhood(){ return hood;}
	public boolean iterate(boolean neighborhood[][]){
		counter += 1;
		if (counter == maturity){ counter = 0;
		switch (direction){
			case 0: active = neighborhood[1][2]; break;
			case 1: active = neighborhood[0][2]; break;
			case 2: active = neighborhood[0][1]; break;
			case 3: active = neighborhood[0][0]; break;
			case 4: active = neighborhood[1][0]; break;
			case 5: active = neighborhood[2][0]; break;
			case 6: active = neighborhood[2][1]; break;
			case 7: active = neighborhood[2][2]; break;
			default:  active = neighborhood[1][2]; break;}
		return active;}
		else{return neighborhood[1][1];}
	}
}


class Wolfram extends Cell{
	String hood = "Moore";
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int direction = 0;
	boolean active;
	public Wolfram(){}
	public Wolfram(int a){
		maturity = a;}
	public Wolfram(int a, int b){
		maturity = a; direction = b;}
		public String getNeighborhood(){ return hood;}
	public boolean iterate(boolean neighborhood[][]){
		counter+=1; if(counter == maturity){counter = 0;
		 cellstate = 0;
		if(neighborhood[1][1]){cellstate += 2;}
	if (direction == 0 || direction == 4){if(neighborhood[1][0]){cellstate += 4;} if (neighborhood[1][2]){cellstate += 1;}}
	if (direction == 1 || direction == 5){if(neighborhood[0][2]){cellstate += 4;} if (neighborhood[2][0]){cellstate += 1;}}
	if (direction == 2 || direction == 6){if(neighborhood[0][1]){cellstate += 4;} if (neighborhood[2][1]){cellstate += 1;}}
	if (direction == 3 || direction == 7){if(neighborhood[0][0]){cellstate += 4;} if (neighborhood[2][2]){cellstate += 1;}}
	switch(cellstate){
		case 0: active = false; break;
		case 1: active = true; break;
		case 2: active = true; break;
		case 3: active = true; break;
		case 4: active = false; break;
		case 5: active = true; break;
		case 6: active = true; break;
		case 7: active = false; break;
	}
	return active; }
	else{return neighborhood[1][1];}
}
}
