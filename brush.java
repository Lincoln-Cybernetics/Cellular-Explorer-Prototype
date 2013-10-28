
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

public class brush{
	int xsiz;
	int ysiz;
	int xloc;
	int yloc;
	int xcount = 0;
	int ycount = 0;
	int bristles;
	public brush(){}
	public brush(int x, int y){
		xsiz = x;
		ysiz = y;
		bristles = 1;
	}
		public int getBrushLength(){
			return bristles;}
			
		public void locate(int x, int y){
			xloc = x;
			yloc = y;}
			
		public int getNextX(){
			int currentx;
			xcount += 1;
			currentx = calculateX();
			if(xcount >= bristles){xcount = 0;}
			if(currentx < 0){currentx=0;} 
			if(currentx > xsiz-1){currentx = xsiz-1;}
			return currentx;
		}
		
		protected int calculateX(){
			int x;
			switch(xcount){
				case 1: x = xloc; break;
				default: x = xloc; break;
			}
			return x;}
		
		public int getNextY(){
			int currenty;
			ycount += 1;
			currenty = calculateY();
			if(ycount >= bristles){ycount = 0;}
			if(currenty < 0){currenty = 0;}
			if(currenty > ysiz-1){currenty = ysiz-1;}
			return currenty;
		}
		
		protected int calculateY(){
			int y;
			switch(ycount){
				case 1: y = yloc; break;
				default: y = yloc; break;
			}
			return y;}
			

}

class twobrush extends brush{
	
	public twobrush(int x, int y){
		xsiz = x;
		ysiz = y;
		bristles = 4;}
		
		protected int calculateX(){
			int x;
			switch(xcount){
				case 1: x = xloc; break;
				case 2: x = xloc+1; break;
				case 3: x = xloc; break;
				case 4: x = xloc + 1; break;
				default: x = xloc;break;}
				return x;}
				
		protected int calculateY(){
			int y;
			switch(ycount){
				case 1: y = yloc; break;
				case 2: y = yloc; break;
				case 3: y = yloc +1; break;
				case 4: y = yloc + 1; break;
				default: y = yloc; break;}
				return y;}
}

class threebrush extends brush{
	
	public threebrush(int x, int y){
		xsiz = x;
		ysiz = y;
		bristles = 9;}
		
		protected int calculateX(){
			int x;
			switch(xcount){
				case 1: x = xloc - 1; break;
				case 2: x = xloc; break;
				case 3: x = xloc + 1; break;
				case 4: x = xloc - 1; break;
				case 5: x = xloc; break;
				case 6: x = xloc + 1; break;
				case 7: x = xloc - 1; break;
				case 8: x = xloc; break;
				case 9: x = xloc + 1; break;
				default: x = xloc;}
				return x;}
				
		protected int calculateY(){
			int y;
			switch(ycount){
				case 1: y = yloc-1; break;
				case 2: y = yloc-1; break;
				case 3: y = yloc-1; break;
				case 4: y = yloc; break;
				case 5: y = yloc; break;
				case 6: y = yloc; break;
				case 7: y = yloc+1; break;
				case 8: y = yloc+1; break;
				case 9: y = yloc+1; break;
				default: y = yloc; break;}
				return y;}
}
		
