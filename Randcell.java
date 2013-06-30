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
	
	Random wiggler = new Random();
	public boolean iterate(boolean neighborhood[][]){
		return wiggler.nextBoolean();}
		public Randcell(){}
		public Randcell(int a){}
	}
	
	class OddCell extends Cell{
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int cellstate;
	int a;
	int b;
	boolean active;
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
	boolean[][] neighborhood = new boolean[3][3];
	int counter = 0;
	int cellstate;
	int a;
	int b;
	boolean active;
	public EvenCell(){}
	public EvenCell(int a){
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
			if(cellstate%2 == 0){active = true;}
			else{active = false;}
		return active;}
		else{return neighborhood[1][1];}
	}
}

