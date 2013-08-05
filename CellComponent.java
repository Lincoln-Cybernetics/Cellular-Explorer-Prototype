import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.event.*;
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

class CellComponent extends JComponent implements Runnable, MouseInputListener
{
	//main variables
	int xsiz = 500;
	int ysiz = 300;
	boolean[][] current = new boolean[xsiz][ysiz] ;
	boolean[][] newstate = new boolean[xsiz][ysiz];
	Cell[][] culture = new Cell[xsiz][ysiz];
	int[][] celltype = new int[xsiz][ysiz];
	int maturity = 1;
	// settings (speed, primary cell selection, secondary cell selection, maturity, secondary maturity, cell size, and array initialization)
	int ztime = 20;
	int workcell = 0;
	int workcellB = 0;
	int workmat = 1;
	int workmatB = 1;
	int setdir = 0;
	int workdirA = 0;
	int workdirB = 0;
	int magnify = 5;
	int demoflag = 0;
	
	
	//general array counters Int x, and Int y are instantiated locally for each use
	
	//keep track of mouse position in edit mode
	int xlocal;
	int ylocal;
	//changestate flag 
	boolean pauseflag = false;
	boolean paused = true;
	boolean interactive = false;
	//state editing flags
	boolean editflag = false;
	boolean sfflag = false;
	int sfopt = 0;
	//cell editing flags
	boolean editcellflag = false;
	boolean checkdrawflag = false;
	boolean oboflag = false;
	boolean tbtflag = false;
	boolean randoflag = false;
	
	//automaton flags
	boolean hwrapflag = false;
	boolean vwrapflag = false;
	
	Thread t = new Thread(this);
	
	public CellComponent(){
		addMouseMotionListener(this);
		addMouseListener(this);
		//initialize the board
	for(int y=0;y<=ysiz-1;y++){
		for(int x=0;x<=xsiz-1;x++){	
				 current[x][y] = false;
				 newstate[x][y] = false;
				 celltype[x][y] = 5;
				 maturity = 1;
				 populate(x,y);
			
			 }}
				
				
			repaint();
			}
			/*public void create(){
				for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
					switch(demoflag){
					 case 0://normal
					celltype[x][y] =5;
					populate(x,y);
					break;
					
					case 1: // demo
				 celltype[x][y] = 5;
					if (x>200 && x<245){if (y>100 && y<200){celltype[x][y] = 9;}}
				  populate(x,y);
				  break;
				  
					case 2:// assorted
					if (x<100 && y>100){celltype[x][y] = 6;}
					else{celltype[x][y] = 5;}
					if(x==xsiz-1){celltype[x][y] = 1;}
					if (y==0){celltype[x][y] = 2;}
					if (y==ysiz-1){celltype[x][y] = 4;}
				  populate(x,y); break;
				 
				default:
				celltype[x][y] = 0;
				populate(x,y); break;}		
						
				}}}*/
			
			
			 public boolean begin(){
				t.start(); return true;}
				
				public void populate(int a, int b){
					switch (celltype[a][b]){
						case 0: culture[a][b] = new Cell();
						break;
						case 1: culture[a][b] = new onCell();
						break;
						case 2: culture[a][b] = new blinkCell(maturity);
						break;
						case 3: culture[a][b] = new blinkCell2(maturity);
						break;
						case 4: culture[a][b] = new Randcell(maturity);
						break;
						case 5: culture[a][b] = new Conway(maturity);
						break;
						case 6: culture[a][b] = new Seeds(maturity);
						break;
						case 7: culture[a][b] = new OddCell(maturity);
						break;
						case 8: culture[a][b] = new EvenCell(maturity);
						break;
						case 9: culture[a][b] = new ConveyorCell(maturity, setdir);
						break;
						case 10: culture[a][b] = new Wolfram(maturity, setdir);
						break;
						default: culture[a][b] = new Cell();
						break;}
					}
					
					// cell editing methods
					public void cellDraw(int x, int y){
						setdir = workdirA;
						celltype[x][y] = workcell; maturity = workmat;
						populate(x,y);}
						
					public void cellAltDraw(int x, int y){
						setdir = workdirB;
						celltype[x][y] = workcellB; maturity = workmatB;
						populate(x,y);} 
					
					public void cellCheckDraw(int x, int y){
						if( y % 2 == 1 ^ x % 2 == 1){
							setdir = workdirA;	
						celltype[x][y] = workcell; maturity = workmat;}
						else{setdir = workdirB; celltype[x][y] = workcellB; maturity = workmatB;}
						populate(x,y);}
						
					public void cellRandDraw(int x, int y){
						Random Iguana = new Random();
						celltype[x][y] = Iguana.nextInt(10);
						setdir = Iguana.nextInt(8);
						maturity = Iguana.nextInt(4);
						maturity +=1;
						populate(x,y);
					}
						
					
					public void cellFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						cellDraw(x,y);}}
						repaint();
					}
					
					public void cellCheckFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellCheckDraw(x,y);
							}}
						repaint();
					}
					
					public void cellCheckFilltbt(){
						for(int y=1; y<= ysiz-1; y+=3){
						for(int x=1; x<= xsiz-1; x+=3){
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtPop(x,y,1);}
							else{tbtPop(x,y,2);}}}
							repaint();}
					
					public void cellRandFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellRandDraw(x,y);
						}}
						repaint();
					}
					
					public void tbtPop(int x,int y, int opt){
						switch (opt){
							case 1: cellDraw(x,y); break;
							case 2: cellAltDraw(x,y); break;
							case 3: cellCheckDraw(x,y); break;
							case 4: cellRandDraw(x,y); break;
							default: cellDraw(x,y);break;}
						
						// if the cell is on the border ignore the outside, otherwise populate 
							//x-1,y-1
							if(x==0 || y==0){}
							else{ switch (opt){
							case 1: cellDraw(x-1,y-1); break;
							case 2: cellAltDraw(x-1,y-1); break;
							case 3: cellCheckDraw(x-1,y-1); break;
							case 4: cellRandDraw(x-1,y-1); break;
							default: cellDraw(x-1,y-1);break;}}
							
							//x-1,y
							if(x==0){}
							else{switch (opt){
							case 1: cellDraw(x-1,y); break;
							case 2: cellAltDraw(x-1,y); break;
							case 3: cellCheckDraw(x-1,y); break;
							case 4: cellRandDraw(x-1,y); break;
							default: cellDraw(x-1,y);break;}}
							
							//x-1,y+1
							if(x==0 || y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x-1,y+1); break;
							case 2: cellAltDraw(x-1,y+1); break;
							case 3: cellCheckDraw(x-1,y+1); break;
							case 4: cellRandDraw(x-1,y+1); break;
							default: cellDraw(x-1,y+1);break;}}
							
							//x,y-1
							if(y==0){}
							else{switch (opt){
							case 1: cellDraw(x,y-1); break;
							case 2: cellAltDraw(x,y-1); break;
							case 3: cellCheckDraw(x,y-1); break;
							case 4: cellRandDraw(x,y-1); break;
							default: cellDraw(x,y-1);break;}}
							
							//x,y+1
							if(y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x,y+1); break;
							case 2: cellAltDraw(x,y+1); break;
							case 3: cellCheckDraw(x,y+1); break;
							case 4: cellRandDraw(x,y+1); break;
							default: cellDraw(x,y+1);break;}}
							
							//x+1,y-1
							if(x==xsiz-1 || y==0){}
							else{switch (opt){
							case 1: cellDraw(x+1,y-1); break;
							case 2: cellAltDraw(x+1,y-1); break;
							case 3: cellCheckDraw(x+1,y-1); break;
							case 4: cellRandDraw(x+1,y-1); break;
							default: cellDraw(x+1,y-1);break;}}
							
							//x+1,y
							if(x==xsiz-1){}
							else{switch (opt){
							case 1: cellDraw(x+1,y); break;
							case 2: cellAltDraw(x+1,y); break;
							case 3: cellCheckDraw(x+1,y); break;
							case 4: cellRandDraw(x+1,y); break;
							default: cellDraw(x+1,y);break;}}
							
							//x+1,y+1
							if(x==xsiz-1 || y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x+1,y+1); break;
							case 2: cellAltDraw(x+1,y+1); break;
							case 3: cellCheckDraw(x+1,y+1); break;
							case 4: cellRandDraw(x+1,y+1); break;
							default: cellDraw(x+1,y+1);break;}}
							
						}
				
				// State editing methods
				
				public void stateFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateDraw(x,y);}}
						repaint();} 
						
				public void stateRandFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateRandDraw(x,y);
					}} repaint();}
					
				public void stateClearFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateAltDraw(x,y);}}
						repaint();}
						
				public void stateCheckFilltbt(){
						for(int y=1; y<= ysiz-1; y+=3){
						for(int x=1; x<= xsiz-1; x+=3){
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtState(x,y,1);}
							else{tbtState(x,y,2);}}}
							repaint();}
						
				public void stateCheckFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
					stateCheckDraw(x,y);
					}} repaint();}
					
				public void stateDraw(int x,int y){
					current[x][y] = true;}
					
				public void stateAltDraw(int x,int y){
					current[x][y] = false;}
					
				public void stateCheckDraw(int x,int y){
					if( y % 2 == 1 ^ x % 2 == 1){ current[x][y] = true;}
						else{current[x][y] = false;}}
					
				public void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					current[x][y] = foghorn.nextBoolean();}	
					
				public void tbtState(int x,int y, int opt){
						switch (opt){
							case 1: stateDraw(x,y); break;
							case 2: stateAltDraw(x,y); break;
							case 3: stateCheckDraw(x,y); break;
							case 4: stateRandDraw(x,y); break;
							default: stateDraw(x,y);break;}
						
						// if the cell is on the border ignore the outside, otherwise populate 
							//x-1,y-1
							if(x==0 || y==0){}
							else{ switch (opt){
							case 1: stateDraw(x-1,y-1); break;
							case 2: stateAltDraw(x-1,y-1); break;
							case 3: stateCheckDraw(x-1,y-1); break;
							case 4: stateRandDraw(x-1,y-1); break;
							default: stateDraw(x-1,y-1);break;}}
							
							//x-1,y
							if(x==0){}
							else{switch (opt){
							case 1: stateDraw(x-1,y); break;
							case 2: stateAltDraw(x-1,y); break;
							case 3: stateCheckDraw(x-1,y); break;
							case 4: stateRandDraw(x-1,y); break;
							default: stateDraw(x-1,y);break;}}
							
							//x-1,y+1
							if(x==0 || y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x-1,y+1); break;
							case 2: stateAltDraw(x-1,y+1); break;
							case 3: stateCheckDraw(x-1,y+1); break;
							case 4: stateRandDraw(x-1,y+1); break;
							default: stateDraw(x-1,y+1);break;}}
							
							//x,y-1
							if(y==0){}
							else{switch (opt){
							case 1: stateDraw(x,y-1); break;
							case 2: stateAltDraw(x,y-1); break;
							case 3: stateCheckDraw(x,y-1); break;
							case 4: stateRandDraw(x,y-1); break;
							default: stateDraw(x,y-1);break;}}
							
							//x,y+1
							if(y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x,y+1); break;
							case 2: stateAltDraw(x,y+1); break;
							case 3: stateCheckDraw(x,y+1); break;
							case 4: stateRandDraw(x,y+1); break;
							default: stateDraw(x,y+1);break;}}
							
							//x+1,y-1
							if(x==xsiz-1 || y==0){}
							else{switch (opt){
							case 1: stateDraw(x+1,y-1); break;
							case 2: stateAltDraw(x+1,y-1); break;
							case 3: stateCheckDraw(x+1,y-1); break;
							case 4: stateRandDraw(x+1,y-1); break;
							default: stateDraw(x+1,y-1);break;}}
							
							//x+1,y
							if(x==xsiz-1){}
							else{switch (opt){
							case 1: stateDraw(x+1,y); break;
							case 2: stateAltDraw(x+1,y); break;
							case 3: stateCheckDraw(x+1,y); break;
							case 4: stateRandDraw(x+1,y); break;
							default: stateDraw(x+1,y);break;}}
							
							//x+1,y+1
							if(x==xsiz-1 || y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x+1,y+1); break;
							case 2: stateAltDraw(x+1,y+1); break;
							case 3: stateCheckDraw(x+1,y+1); break;
							case 4: stateRandDraw(x+1,y+1); break;
							default: stateDraw(x+1,y+1);break;}}
							}
				
						
						
					//neighborhood methods
					
						public boolean[][] getMoore(int x, int y){
						// returns the states of a cell's Moore Neighborhood
					boolean[][] neighbors = new boolean[3][3];
						//init neighbors WITHOUT resetting x and y
					neighbors[0][0]=false;neighbors[0][1]=false;neighbors[0][2]=false;
					neighbors[1][0]=false;neighbors[1][1]=current[x][y];neighbors[1][2]=false;
					neighbors[2][0]=false;neighbors[2][1]=false;neighbors[2][2]=false;
						
						
						// if the cell is on the border ignore outside, otherwise test neighbors
							//upper-left neighbor
							if(x==0 || y==0){
							// normal borders	
							neighbors[0][0] = false;
							//wraparound borders
							if(hwrapflag){if(x==0 && y != 0){neighbors[0][0] = current[xsiz-1][y-1];}
							else{neighbors[0][0] = false;}}
							if(vwrapflag){if(y == 0 && x != 0){neighbors[0][0] = current[x-1][ysiz-1];} else{neighbors[0][0] = false;}}
							if(vwrapflag == true && hwrapflag == true){if(x==0 && y != 0){neighbors[0][0] = current[xsiz-1][y-1];}if(y == 0 && x != 0){neighbors[0][0] = current[x-1][ysiz-1];}if(x == 0 && y == 0){neighbors[0][0] = current[xsiz-1][ysiz-1];}}}
							//non-border
							else{ if(current[x-1][y-1]== true){neighbors[0][0]=true;}}
							
							//Left-center neighbor
							if(x==0){neighbors[0][1] = false; if(hwrapflag){neighbors[0][1] = current[xsiz-1][y];}}
							else{if(current[x-1][y] == true){neighbors[0][1] = true;}}
							
							//Lower-Left neighbor
							if(x==0 || y==ysiz-1){
							// normal border
							neighbors[0][2] = false;
							//wraparound borders 
							if(hwrapflag){if(x == 0 && y != ysiz-1){neighbors[0][2] = current[xsiz-1][y+1];} else{neighbors[0][2] = false;}}
							if(vwrapflag){if(y == ysiz-1 && x!= 0){neighbors[0][2] = current[x-1][0];}else{neighbors[0][2] = false;}}
							if(hwrapflag == true && vwrapflag == true){if(x == 0 && y != ysiz-1){neighbors[0][2] = current[xsiz-1][y+1];}if(y == ysiz-1 && x!= 0){neighbors[0][2] = current[x-1][0];}if(x == 0 && y == ysiz-1){neighbors[0][2] = current[xsiz-1][0];}}}
							//non-border
							else{if(current[x-1][y+1] == true) {neighbors[0][2] = true;}}
							
							//upper-center neighbor
							if(y==0){neighbors[1][0] = false;if(vwrapflag){neighbors[1][0] = current[x][ysiz-1];}}
							else{if(current[x][y-1]==true){ neighbors[1][0] = true;}}
							
							//lower-center neighbor
							if(y==ysiz-1){neighbors[1][2] = false;if(vwrapflag){neighbors[1][2] = current[x][0];}}
							else{if(current[x][y+1]==true){ neighbors[1][2] = true;}}
							
							//upper-right neighbor
							if(x==xsiz-1 || y==0){
							//normal border	
							neighbors[2][0] = false;
							//wraparound border
							if(hwrapflag){if (x == xsiz-1 && y != 0){neighbors[2][0] = current[0][y-1];}else{neighbors[2][0] = false;}}
							if(vwrapflag){if(y == 0 && x != xsiz-1){neighbors[2][0] = current[x+1][ysiz-1];}else{neighbors[2][0] = false;}}
							if(hwrapflag == true && vwrapflag == true){if (x == xsiz-1 && y != 0){neighbors[2][0] = current[0][y-1];}if(y == 0 && x != xsiz-1){neighbors[2][0] = current[x+1][ysiz-1];}if(x == xsiz-1 && y == 0){neighbors[2][0] = current[0][ysiz-1];}}}
							//non-border
							else{if(current[x+1][y-1]== true){neighbors[2][0] = true;}}
							
							//center-right neighbor
							if(x==xsiz-1){neighbors[2][1] = false; if(hwrapflag){neighbors[2][1] = current[0][y];}if (hwrapflag && vwrapflag){neighbors[2][1] = current[0][y];}}
							else{if(current[x+1][y]==true){neighbors[2][1] = true;}}
							
							//lower-right neighbor
							if(x==xsiz-1 || y==ysiz-1){
							// normal border	
							neighbors[2][2] = false;
							//wraparound border
							if(hwrapflag){if (x == xsiz-1 && y != ysiz-1){neighbors[2][2] = current[0][y+1];} else {neighbors[2][2] = false;}}
							if(vwrapflag){if(y == ysiz-1 && x != xsiz-1){neighbors[2][2] = current[x+1][0];}else{neighbors[2][2] = false;}}
							if(hwrapflag == true && vwrapflag == true){if (x == xsiz-1 && y != ysiz-1){neighbors[2][2] = current[0][y+1];} if(y == ysiz-1 && x != xsiz-1){neighbors[2][2] = current[x+1][0];}if(x == xsiz-1 && y == ysiz-1){neighbors[2][2] = current[0][0];}}
							}
							//non-border
							else{if(current[x+1][y+1] == true){neighbors[2][2] = true;}}
						return neighbors;
		}
		
		
		// neighborhood for one dimensional, horizontal cells
		public boolean[] getWolfram(int x, int y){
			boolean[] wolfhood = new boolean[3];
			if (x == 0){wolfhood[0] = false; if(hwrapflag){wolfhood[0] = current[xsiz-1][y];}} else{wolfhood[0] = current[x-1][y];}
			wolfhood[1] = current[x][y];
			if(x == xsiz-1){wolfhood[2] = false;if(hwrapflag){wolfhood[2] = current[0][y];}} else{wolfhood[2] = current[x+1][y];}
			return wolfhood;}
		
		// neighborhood for one dimensional, vertical cells	
		public boolean[] getWolframV(int x, int y){
			boolean[] wolfhood = new boolean[3];
			if(y == 0){wolfhood[0] = false;if(vwrapflag){wolfhood[0] = current[x][ysiz-1];}} else{wolfhood[0] = current[x][y-1];}
			wolfhood[1] = current[x][y];
			if(y == ysiz-1){wolfhood[2] = false;if(vwrapflag){wolfhood[2] = current[x][0];}} else{wolfhood[2] = current[x][y+1];}
			return wolfhood;}
			
		// neighborhood for one dimensional cells starting with upper left
		public boolean[] getWolframUL(int x, int y){
			boolean[] wolfhood = new boolean[3];
			if (x == 0 || y == 0){ 
				//normal border
				wolfhood[0] = false;
				//wraparound border
				if(hwrapflag){if(x == 0 && y != 0){wolfhood[0] = current[xsiz-1][y-1];}else{wolfhood[0] = false;}}
			if (vwrapflag){if(y == 0 && x != 0){wolfhood[0] = current[x-1][ysiz-1];} else{wolfhood[0] = false;}}
			if(vwrapflag && hwrapflag){if(x == 0 && y == 0){wolfhood[0] = current[xsiz-1][ysiz-1];}}}
			//non-border
			 else{ wolfhood[0] = current[x-1][y-1];}
			wolfhood[1] = current[x][y];
			if (x == xsiz-1 || y == ysiz-1){ 
				// normal border
				wolfhood[2] = false;
				//wraparound border
				if(hwrapflag){if (x == xsiz-1 && y != ysiz-1){wolfhood[2] = current[0][y+1];} else {wolfhood[2] = false;}}
				if(vwrapflag){if(y == ysiz-1 && x != xsiz-1){wolfhood[2] = current[x+1][0];}else{wolfhood[2] = false;}}
				if(hwrapflag && vwrapflag){if(x == xsiz-1 && y == ysiz-1){wolfhood[2] = current[0][0];}}} 
				//non-border
				else{wolfhood[2] = current[x+1][y+1];}
			return wolfhood;}
			
		// neighborhood for one dimensional cells starting in lower left
		public boolean[] getWolframLL(int x, int y){
			boolean[] wolfhood = new boolean[3];
			if(x==0 || y== ysiz-1){
				// normal border
				wolfhood[0] = false;
				//wraparound borders 
				if(hwrapflag){if(x == 0 && y != ysiz-1){wolfhood[0] = current[xsiz-1][y+1];} else{wolfhood[0] = false;}}
				if(vwrapflag){if(y == ysiz-1 && x!= 0){wolfhood[0] = current[x-1][0];}else{wolfhood[0] = false;}}
				if(hwrapflag && vwrapflag){if(x == 0 && y == ysiz-1){wolfhood[0] = current[xsiz-1][0];}}}
			//non-border
			else{wolfhood[0] = current[x-1][y+1];}
			wolfhood[1] = current[x][y];
			if(x == xsiz-1 || y == 0){
				// normal border
				wolfhood[2] = false;
				//wraparound border
				if(hwrapflag){if (x == xsiz-1 && y != 0){wolfhood[2] = current[0][y-1];}else{wolfhood[2] = false;}}
				if(vwrapflag){if(y == 0 && x != xsiz-1){wolfhood[2] = current[x+1][ysiz-1];}else{wolfhood[2] = false;}}
				if(hwrapflag && vwrapflag){if(x == xsiz-1 && y == 0){wolfhood[2] = current[0][ysiz-1];}}} 
				//non-border
				else{wolfhood[2] = current[x+1][y-1];}
			return wolfhood;}
		
		public void iterate(){
			editcellflag = false; editflag = false;
			int x; int y;
			 if (sfflag){ switch(sfopt){
				   case 1: stateFill(); break;
				   case 2: stateClearFill(); break;
				   case 3: stateCheckFill(); break;
				   case 4: stateRandFill(); break;
				   case 5: stateCheckFilltbt(); break;
				   default: stateCheckFill(); break;}
				   sfflag = false;}
				   
					//gets new values from the cells
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						if (culture[x][y].getNeighborhood() == "None"){
							newstate[x][y] = culture[x][y].iterate();}
					
						if(culture[x][y].getNeighborhood() == "Moore"){
							newstate[x][y] =culture[x][y].iterate(getMoore(x,y));}
							
						if(culture[x][y].getNeighborhood() == "Self"){
							newstate[x][y] = culture[x][y].iterate(current[x][y]);}
							
						if(culture[x][y].getNeighborhood() == "Wolfram"){
							newstate[x][y] = culture[x][y].iterate(getWolfram(x,y));}
							
						if(culture[x][y].getNeighborhood() == "WolframV"){
							newstate[x][y] = culture[x][y].iterate(getWolframV(x,y));}
							
						if(culture[x][y].getNeighborhood() == "WolframUL"){
							newstate[x][y] = culture[x][y].iterate(getWolframUL(x,y));}
							
						if(culture[x][y].getNeighborhood() == "WolframLL"){
							newstate[x][y] = culture[x][y].iterate(getWolframLL(x,y));}
					}}
					
					// cycles new values into current state
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						current[x][y] = newstate[x][y];}}
				repaint();}
			
			
			public void run(){
				int x=0;
				int y=0;
				paused = false;
			
					
					
				while(true){
					//update display
						repaint();
					//pauses the program
					try{	while (pauseflag ==true){
				paused = true;
			  Thread.sleep(1);} 
			   }  catch(InterruptedException ie) {}
			   paused = false;
			   if (sfflag){ switch(sfopt){
				   case 1: stateFill(); break;
				   case 2: stateClearFill(); break;
				   case 3: stateCheckFill(); break;
				   case 4: stateRandFill(); break;
				   case 5: stateCheckFilltbt(); break;
				   default: stateCheckFill(); break;}
				   sfflag = false;}
			 
		
						//gets new values from the cells
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						if (culture[x][y].getNeighborhood() == "None"){
							newstate[x][y] = culture[x][y].iterate();}
					
						if(culture[x][y].getNeighborhood() == "Moore"){
							newstate[x][y] =culture[x][y].iterate(getMoore(x,y));}
							
						if(culture[x][y].getNeighborhood() == "Self"){
							newstate[x][y] = culture[x][y].iterate(current[x][y]);}
						
						if(culture[x][y].getNeighborhood() == "Wolfram"){
							newstate[x][y] = culture[x][y].iterate(getWolfram(x,y));}
							
						if(culture[x][y].getNeighborhood() == "WolframV"){
							newstate[x][y] = culture[x][y].iterate(getWolframV(x,y));}
							
						if(culture[x][y].getNeighborhood() == "WolframUL"){
							newstate[x][y] = culture[x][y].iterate(getWolframUL(x,y));}
							
						if(culture[x][y].getNeighborhood() == "WolframLL"){
							newstate[x][y] = culture[x][y].iterate(getWolframLL(x,y));}
					}}
					
					
				// cycles new values into current state
				for(y=0;y<=ysiz-1;y++){
					for(x=0;x<=xsiz-1;x++){
						current[x][y] = newstate[x][y];}}
						
						//timeout between grid-wide iterations
						try{
				Thread.sleep(ztime);
			} catch(InterruptedException ie){}
			
			}

				}
				
			public void paintComponent( Graphics g){
					int x = 0;
					int y = 0;
					int schmagnify;
					
					
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						
							//state editing
							if (editflag == true){
								if(magnify>4){schmagnify = magnify-1;}
								else{schmagnify = magnify;}
								g.setColor(current[x][y] ? Color.green : Color.black);
								g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);}
								
							// cell editing
							if(editcellflag == true){
								if(magnify>4){schmagnify = magnify-1;}
								else{schmagnify = magnify;}
								switch(celltype[x][y]){
									case 0: g.setColor(Color.black);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 1: g.setColor(Color.white);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 2: g.setColor(Color.red);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);break;
									case 3: g.setColor(Color.blue);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 4: g.setColor(Color.orange);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 5: g.setColor(Color.green);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 6: g.setColor(Color.cyan);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 7: g.setColor(Color.pink);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 8: g.setColor(Color.yellow);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 9: g.setColor(Color.gray);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 10: g.setColor(Color.black);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);
											 g.setColor(Color.white);g.fillRect(x*magnify+2,y*magnify+2,schmagnify-2,schmagnify-2); break;
									default: g.setColor(Color.black);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
								}
								//outline each cell according to its maturity setting
								switch(culture[x][y].maturity){
									case 1: g.setColor(Color.white); break;
									case 2: g.setColor(Color.red); break;
									case 3: g.setColor(Color.blue); break;
									case 4: g.setColor(Color.black); break;
									default: g.setColor(Color.green); break;
								}
								g.drawRect(x*magnify,y*magnify,magnify,magnify);}
								
								//normal rendering
								if (editflag == false && editcellflag == false){
										g.setColor(current[x][y] ? Color.green : Color.black);
							g.fillRect(x*magnify,y*magnify,magnify,magnify);}
								
						}}
						}
					public void mouseMoved( MouseEvent e){
					
				}
					public void mouseDragged(MouseEvent e) {
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						
						//edit state
						if(editflag == true || interactive == true){
							int option = 1;if(e.isMetaDown()){option = 2;} if (checkdrawflag){option = 3;} if(randoflag){option = 4;}
							if(tbtflag){tbtState(xlocal, ylocal, option);}
							else{
							switch(option){
								case 1: stateDraw(xlocal,ylocal); break;
								case 2: stateAltDraw(xlocal,ylocal); break;
								case 3: stateCheckDraw(xlocal, ylocal); break;
								case 4: stateRandDraw(xlocal, ylocal); break;
								default: stateDraw(xlocal, ylocal); break;}}
							repaint();}
						
						//editcell
						if (editcellflag == true){
							int option = 1;if(e.isMetaDown()){option = 2;} if (checkdrawflag){option = 3;} if(randoflag){option = 4;}
							if (tbtflag){tbtPop(xlocal,ylocal,option);}
							else{
							switch(option){
							case 1: cellDraw(xlocal,ylocal); break;
							case 2: cellAltDraw(xlocal,ylocal); break;
							case 3: cellCheckDraw(xlocal,ylocal); break;
							case 4: cellRandDraw(xlocal,ylocal); break;
							default: cellDraw(xlocal, ylocal); break;
							}}
							repaint();}}
					public void mouseEntered(MouseEvent e){}
					public void mouseExited(MouseEvent e){}
					public void mousePressed(MouseEvent e){}
					public void mouseReleased(MouseEvent e){}
					public void mouseClicked(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						//edit state
						if(editflag == true || interactive == true){
							int option = 1;if(e.isMetaDown()){option = 2;} if (checkdrawflag){option = 3;} if(randoflag){option = 4;}
							if(tbtflag){tbtState(xlocal, ylocal, option);}
							else{
							switch(option){
								case 1: stateDraw(xlocal,ylocal); break;
								case 2: stateAltDraw(xlocal,ylocal); break;
								case 3: stateCheckDraw(xlocal, ylocal); break;
								case 4: stateRandDraw(xlocal, ylocal); break;
								default: stateDraw(xlocal, ylocal); break;}}
						 repaint();}
						//edit celltype
						if (editcellflag == true){
							int option = 1;if(e.isMetaDown()){option = 2;} if (checkdrawflag){option = 3;} if(randoflag){option = 4;}
							if (tbtflag){tbtPop(xlocal,ylocal,option);}
							else{
							switch(option){
							case 1: cellDraw(xlocal,ylocal); break;
							case 2: cellAltDraw(xlocal,ylocal); break;
							case 3: cellCheckDraw(xlocal,ylocal); break;
							case 4: cellRandDraw(xlocal,ylocal); break;
							default: cellDraw(xlocal, ylocal); break;
							}} 
							repaint();}
						}
					
				
		}
