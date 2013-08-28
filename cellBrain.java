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

class cellBrain extends JComponent implements Runnable, MouseInputListener
{
	//main variables
	cellComponent bigboard;
	JFrame garden;
	int xsiz; //= 500;
	int ysiz; // = 300;
	boolean[][] current; //= new boolean[xsiz][ysiz] ;
	boolean[][] newstate; //= new boolean[xsiz][ysiz];
	public cell[][] culture; //= new Cell[xsiz][ysiz];
	
	int[][] celltype; //= new int[xsiz][ysiz];
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
	
	
	// constructors
	
	//default
	public cellBrain(){
		xsiz = 400;
		ysiz = 150;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		celltype = new int[xsiz][ysiz];
		bigboard = new cellComponent(xsiz, ysiz);
		bigboard.addMouseMotionListener(this);
		bigboard.addMouseListener(this);
		makeWindow();
		//initialize the board
	for(int y=0;y<=ysiz-1;y++){
		for(int x=0;x<=xsiz-1;x++){	
				 current[x][y] = false;
				 newstate[x][y] = false;
				 celltype[x][y] = 6;
				 maturity = 1;
				 populate(x,y);
			
			 }}
				
				
			bigboard.setState(current);
			}
	
	//set size		
	public cellBrain(int a, int b){
		xsiz = a;
		ysiz = b;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		celltype = new int[xsiz][ysiz];
		bigboard = new cellComponent(xsiz, ysiz);
		bigboard.addMouseMotionListener(this);
		bigboard.addMouseListener(this);
		makeWindow();
		//initialize the board
	for(int y=0;y<=ysiz-1;y++){
		for(int x=0;x<=xsiz-1;x++){	
				 current[x][y] = false;
				 newstate[x][y] = false;
				 celltype[x][y] = 6;
				 maturity = 1;
				 populate(x,y);
			
			 }}
				
				
			bigboard.setState(current);
			}
			
			// creates the display
			public void makeWindow(){
				garden = new JFrame("Cellular Explorer");
				garden.getContentPane().add( new JScrollPane(bigboard) );
				garden.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				garden.setSize(1000,500);
				garden.setLocation(200,150);
				garden.setResizable(true);
				garden.setVisible( true );}
				
			// paints the display
			public void dispRefresh(){
				bigboard.repaint();}
			
			// starts the thread
			 public boolean begin(){
				t.start(); return true;}
				
				// mode setting methods
				
				//pause/unpause
				public void setPause(boolean a){
					pauseflag = a;}
				
				// enters/exits state edit mode
				public void setEdit(boolean a){
					editflag = a; if(a){bigboard.setMode(2);} else{bigboard.setMode(1);}}
					
				// enters/exits cell edit mode
				public void setCellEdit(boolean a){
					editcellflag = a; if(a){bigboard.setMode(3);}else{bigboard.setMode(1);}}
				
				
				//makes the cells
				public void populate(int a, int b){
					switch (celltype[a][b]){
						case 0: culture[a][b] = new cell();
								break;
						case 1: culture[a][b] = new offCell();
								break;
						case 2: culture[a][b] = new onCell();
								break;
						case 3: culture[a][b] = new blinkCell();
								culture[a][b].setInt("Mat", maturity);
								break;
						case 4: culture[a][b] = new seqCell();
								culture[a][b].setInt("Mat", maturity);
								break;
						case 5: culture[a][b] = new randCell();
								culture[a][b].setInt("Mat", maturity);
								break;
						case 6: culture[a][b] = new conway();
								culture[a][b].setInt("Mat", maturity);
								break;
						case 7: culture[a][b] = new seeds();
								culture[a][b].setInt("Mat", maturity);
								break;
						case 8: culture[a][b] = new parityCell();
								culture[a][b].setInt("Mat", maturity);
								break;
						case 9: culture[a][b] = new conveyorCell();
								culture[a][b].setInt("Mat", maturity);
								culture[a][b].setInt("Dir", setdir);
								break;
						case 10: culture[a][b] = new wolfram();
								culture[a][b].setInt("Mat", maturity);
								culture[a][b].setInt("Dir", setdir);
								break;
						case 11: culture[a][b] = new symmetriCell();
								culture[a][b].setInt("Mat", maturity);
								culture[a][b].setInt("Dir", setdir);
								break;		
						case 12: culture[a][b] = new mirrorCell();
								culture[a][b].setInt("Mat", maturity);
								break;		
						default: culture[a][b] = new cell();
								break;}
						bigboard.setSpecies(a,b,celltype[a][b]);
						bigboard.setLifespan(a,b,maturity); 
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
						celltype[x][y] = Iguana.nextInt(13);
						setdir = Iguana.nextInt(8);
						maturity = Iguana.nextInt(4);
						maturity +=1;
						populate(x,y);
					}
						
					
					public void cellFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						cellDraw(x,y);}}
						if(editcellflag){bigboard.repaint();}
					}
					
					public void cellCheckFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellCheckDraw(x,y);
							}}
						if(editcellflag){bigboard.repaint();}
					}
					
					public void cellCheckFilltbt(){
						for(int y=1; y<= ysiz-1; y+=3){
						for(int x=1; x<= xsiz-1; x+=3){
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtPop(x,y,1);}
							else{tbtPop(x,y,2);}} 
							if(xsiz-1 % 3 != 1){if(y % 2 == 1 ^ xsiz-1 % 2 == 1){
							tbtPop(xsiz-1,y,1);}
							else{tbtPop(xsiz-1,y,2);}  }}
							if(ysiz-1 % 3 != 1){ for(int x = 1; x<= xsiz-1; x+= 3){int y = ysiz-1;
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtPop(x,y,1);}
							else{tbtPop(x,y,2);}} 
							if(xsiz-1 % 3 != 1){if(ysiz-1 % 2 == 1 ^ xsiz-1 % 2 == 1){
							tbtPop(xsiz-1,ysiz-1,1);}
							else{tbtPop(xsiz-1,ysiz-1,2);}  }}
							if(editcellflag){bigboard.repaint();}}
					
					public void cellRandFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellRandDraw(x,y);
						}}
						if(editcellflag){bigboard.repaint();}
					}
					
					//sets the cells in a 3x3 area
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
						
						// sets the cells around the outside edge of the automaton 
						public void setBorder(int option){
						for(int x = 0; x <= xsiz-1; x++){
							switch(option){
								case 1: cellDraw(x,0); cellDraw(x, ysiz-1); break;
								case 2: cellAltDraw(x,0); cellAltDraw(x, ysiz-1); break;
								case 3: cellCheckDraw(x,0); cellCheckDraw(x,ysiz-1); break;
								case 4: cellRandDraw(x,0); cellRandDraw(x, ysiz-1); break;
								default: cellDraw(x,0); cellDraw(x, ysiz-1); break;}}
						for(int y = 0; y<= ysiz-1; y++){
							switch(option){
								case 1: cellDraw(0,y); cellDraw(xsiz-1, y); break;
								case 2: cellAltDraw(0,y); cellAltDraw(xsiz-1, y); break;
								case 3: cellCheckDraw(0,y); cellCheckDraw(xsiz-1,y); break;
								case 4: cellRandDraw(0,y); cellRandDraw(xsiz-1, y); break;
								default: cellDraw(0,y); cellDraw(xsiz-1, y); break;}}
								if(editcellflag){bigboard.repaint();}
					}
				
				// State editing methods
				
				//state fill methods
				public void stateFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateDraw(x,y);}}
					if(bigboard.getMode() == 1 || bigboard.getMode() == 2){bigboard.repaint();}
					} 
						
				public void stateRandFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateRandDraw(x,y);
					}} 
					if(bigboard.getMode() == 1 || bigboard.getMode() == 2){bigboard.repaint();}
					}
					
				public void stateClearFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateAltDraw(x,y);}}
						if(bigboard.getMode() == 1 || bigboard.getMode() == 2){bigboard.repaint();}
						}
						
				public void stateCheckFilltbt(){
						for(int y=1; y<= ysiz-1; y+=3){
						for(int x=1; x<= xsiz-1; x+=3){
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtState(x,y,1);}
							else{tbtState(x,y,2);}}}
							if(bigboard.getMode() == 1 || bigboard.getMode() == 2){bigboard.repaint();}
							}
						
				public void stateCheckFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
					stateCheckDraw(x,y, true);
					}} 
					if(bigboard.getMode() == 1 || bigboard.getMode() == 2){bigboard.repaint();}
					}
				
				//state drawing methods	
				public void stateDraw(int x,int y){
					current[x][y] = true;}
					
				public void stateAltDraw(int x,int y){
					current[x][y] = false;}
					
				public void stateCheckDraw(int x,int y, boolean fill){
					if( y % 2 == 1 ^ x % 2 == 1){ current[x][y] = fill;}
						else{if(fill){current[x][y] = false;}}}
					
				public void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					current[x][y] = foghorn.nextBoolean();}	
				
				//draws into the state 3x3	
				public void tbtState(int x,int y, int opt){
						switch (opt){
							case 1: stateDraw(x,y); break;
							case 2: stateAltDraw(x,y); break;
							case 3: stateCheckDraw(x,y, true); break;
							case 4: stateRandDraw(x,y); break;
							case 5: stateCheckDraw(x,y, false); break;
							default: stateDraw(x,y);break;}
						
						// if the cell is on the border ignore the outside, otherwise populate 
							//x-1,y-1
							if(x==0 || y==0){}
							else{ switch (opt){
							case 1: stateDraw(x-1,y-1); break;
							case 2: stateAltDraw(x-1,y-1); break;
							case 3: stateCheckDraw(x-1,y-1, true); break;
							case 4: stateRandDraw(x-1,y-1); break;
							case 5: stateCheckDraw(x-1,y-1, false); break;
							default: stateDraw(x-1,y-1);break;}}
							
							//x-1,y
							if(x==0){}
							else{switch (opt){
							case 1: stateDraw(x-1,y); break;
							case 2: stateAltDraw(x-1,y); break;
							case 3: stateCheckDraw(x-1,y, true); break;
							case 4: stateRandDraw(x-1,y); break;
							case 5: stateCheckDraw(x-1,y, false); break;
							default: stateDraw(x-1,y);break;}}
							
							//x-1,y+1
							if(x==0 || y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x-1,y+1); break;
							case 2: stateAltDraw(x-1,y+1); break;
							case 3: stateCheckDraw(x-1,y+1, true); break;
							case 4: stateRandDraw(x-1,y+1); break;
							case 5: stateCheckDraw(x-1,y+1, false); break;
							default: stateDraw(x-1,y+1);break;}}
							
							//x,y-1
							if(y==0){}
							else{switch (opt){
							case 1: stateDraw(x,y-1); break;
							case 2: stateAltDraw(x,y-1); break;
							case 3: stateCheckDraw(x,y-1, true); break;
							case 4: stateRandDraw(x,y-1); break;
							case 5: stateCheckDraw(x,y-1, false); break;
							default: stateDraw(x,y-1);break;}}
							
							//x,y+1
							if(y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x,y+1); break;
							case 2: stateAltDraw(x,y+1); break;
							case 3: stateCheckDraw(x,y+1, true); break;
							case 4: stateRandDraw(x,y+1); break;
							case 5: stateCheckDraw(x,y+1, false); break;
							default: stateDraw(x,y+1);break;}}
							
							//x+1,y-1
							if(x==xsiz-1 || y==0){}
							else{switch (opt){
							case 1: stateDraw(x+1,y-1); break;
							case 2: stateAltDraw(x+1,y-1); break;
							case 3: stateCheckDraw(x+1,y-1, true); break;
							case 4: stateRandDraw(x+1,y-1); break;
							case 5: stateCheckDraw(x+1,y-1, false); break;
							default: stateDraw(x+1,y-1);break;}}
							
							//x+1,y
							if(x==xsiz-1){}
							else{switch (opt){
							case 1: stateDraw(x+1,y); break;
							case 2: stateAltDraw(x+1,y); break;
							case 3: stateCheckDraw(x+1,y, true); break;
							case 4: stateRandDraw(x+1,y); break;
							case 5: stateCheckDraw(x+1,y, false); break;
							default: stateDraw(x+1,y);break;}}
							
							//x+1,y+1
							if(x==xsiz-1 || y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x+1,y+1); break;
							case 2: stateAltDraw(x+1,y+1); break;
							case 3: stateCheckDraw(x+1,y+1, true); break;
							case 4: stateRandDraw(x+1,y+1); break;
							case 5: stateCheckDraw(x+1, y+1, false); break;
							default: stateDraw(x+1,y+1);break;}}
							}
				
						
						
					//neighborhood methods
					
					//returns the "self" neighborhood
					public boolean[][] getSelf(int x, int y){
						boolean[][] selfie =new boolean[1][1];
						selfie[0][0] = current[x][y];
						return selfie;}
					
					// returns the states of a cell's Moore Neighborhood
					public boolean[][] getMoore(int x, int y){
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
		public boolean[][] getWolfram(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if (x == 0){wolfhood[0][0] = false; if(hwrapflag){wolfhood[0][0] = current[xsiz-1][y];}} else{wolfhood[0][0] = current[x-1][y];}
			wolfhood[1][0] = current[x][y];
			if(x == xsiz-1){wolfhood[2][0] = false;if(hwrapflag){wolfhood[2][0] = current[0][y];}} else{wolfhood[2][0] = current[x+1][y];}
			return wolfhood;}
		
		// neighborhood for one dimensional, vertical cells	
		public boolean[][] getWolframV(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if(y == 0){wolfhood[0][0] = false;if(vwrapflag){wolfhood[0][0] = current[x][ysiz-1];}} else{wolfhood[0][0] = current[x][y-1];}
			wolfhood[1][0] = current[x][y];
			if(y == ysiz-1){wolfhood[2][0] = false;if(vwrapflag){wolfhood[2][0] = current[x][0];}} else{wolfhood[2][0] = current[x][y+1];}
			return wolfhood;}
			
		// neighborhood for one dimensional cells starting with upper left
		public boolean[][] getWolframUL(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if (x == 0 || y == 0){ 
				//normal border
				wolfhood[0][0] = false;
				//wraparound border
				if(hwrapflag){if(x == 0 && y != 0){wolfhood[0][0] = current[xsiz-1][y-1];}else{wolfhood[0][0] = false;}}
			if (vwrapflag){if(y == 0 && x != 0){wolfhood[0][0] = current[x-1][ysiz-1];} else{wolfhood[0][0] = false;}}
			if(vwrapflag && hwrapflag){if(x == 0 && y != 0){wolfhood[0][0] = current[xsiz-1][y-1];}if(y == 0 && x != 0){wolfhood[0][0] = current[x-1][ysiz-1];}if(x == 0 && y == 0){wolfhood[0][0] = current[xsiz-1][ysiz-1];}}}
			//non-border
			 else{ wolfhood[0][0] = current[x-1][y-1];}
			 
			 // center cell
			wolfhood[1][0] = current[x][y];
			
			if (x == xsiz-1 || y == ysiz-1){ 
				// normal border
				wolfhood[2][0] = false;
				//wraparound border
				if(hwrapflag){if (x == xsiz-1 && y != ysiz-1){wolfhood[2][0] = current[0][y+1];} else {wolfhood[2][0] = false;}}
				if(vwrapflag){if(y == ysiz-1 && x != xsiz-1){wolfhood[2][0] = current[x+1][0];}else{wolfhood[2][0] = false;}}
				if(hwrapflag && vwrapflag){if (x == xsiz-1 && y != ysiz-1){wolfhood[2][0] = current[0][y+1];}if(y == ysiz-1 && x != xsiz-1){wolfhood[2][0] = current[x+1][0];}if(x == xsiz-1 && y == ysiz-1){wolfhood[2][0] = current[0][0];}}} 
				//non-border
				else{wolfhood[2][0] = current[x+1][y+1];}
			return wolfhood;}
			
		// neighborhood for one dimensional cells starting in lower left
		public boolean[][] getWolframLL(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if(x==0 || y== ysiz-1){
				// normal border
				wolfhood[0][0] = false;
				//wraparound borders 
				if(hwrapflag){if(x == 0 && y != ysiz-1){wolfhood[0][0] = current[xsiz-1][y+1];} else{wolfhood[0][0] = false;}}
				if(vwrapflag){if(y == ysiz-1 && x!= 0){wolfhood[0][0] = current[x-1][0];}else{wolfhood[0][0] = false;}}
				if(hwrapflag && vwrapflag){if(x == 0 && y != ysiz-1){wolfhood[0][0] = current[xsiz-1][y+1];}if(y == ysiz-1 && x!= 0){wolfhood[0][0] = current[x-1][0];}if(x == 0 && y == ysiz-1){wolfhood[0][0] = current[xsiz-1][0];}}}
			//non-border
			else{wolfhood[0][0] = current[x-1][y+1];}
			
			// center cell
			wolfhood[1][0] = current[x][y];
			
			if(x == xsiz-1 || y == 0){
				// normal border
				wolfhood[2][0] = false;
				//wraparound border
				if(hwrapflag){if (x == xsiz-1 && y != 0){wolfhood[2][0] = current[0][y-1];}else{wolfhood[2][0] = false;}}
				if(vwrapflag){if(y == 0 && x != xsiz-1){wolfhood[2][0] = current[x+1][ysiz-1];}else{wolfhood[2][0] = false;}}
				if(hwrapflag && vwrapflag){if (x == xsiz-1 && y != 0){wolfhood[2][0] = current[0][y-1];}if(y == 0 && x != xsiz-1){wolfhood[2][0] = current[x+1][ysiz-1];}if(x == xsiz-1 && y == 0){wolfhood[2][0] = current[0][ysiz-1];}}} 
				//non-border
				else{wolfhood[2][0] = current[x+1][y-1];}
			return wolfhood;}
		
		//logic methods
		// iterate the array
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
							culture[x][y].setNeighbors(getMoore(x,y));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "Self"){
							culture[x][y].setNeighbors(getSelf(x,y));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "Wolfram"){
							culture[x][y].setNeighbors(getWolfram(x,y));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "WolframV"){
							culture[x][y].setNeighbors(getWolframV(x,y));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "WolframUL"){
							culture[x][y].setNeighbors(getWolframUL(x,y));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "WolframLL"){
							culture[x][y].setNeighbors(getWolframLL(x,y));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "Mirror"){
							culture[x][y].setNeighbors(getSelf(culture[x][y].getInt("HX"), culture[x][y].getInt("HY")));
							newstate[x][y] = culture[x][y].iterate();}
					}}
					
					// cycles new values into current state
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						current[x][y] = newstate[x][y];}}
				bigboard.setState(current);}
			
			// runs the main thread
			public void run(){
				int x=0;
				int y=0;
				paused = false;
			
					
					
				while(true){
					//update display
						bigboard.repaint();
					//pauses the program
					try{	while (pauseflag ==true){
				paused = true;
			  Thread.sleep(1);} 
			   }  catch(InterruptedException ie) {}
			   paused = false;
			 
			 iterate();
			 
						//timeout between grid-wide iterations
						try{
				Thread.sleep(ztime);
			} catch(InterruptedException ie){}
			
			}

				}
				
			
					public void mouseMoved( MouseEvent e){
					
				}
					public void mouseDragged(MouseEvent e) {
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						
						//edit state
						if(editflag == true || interactive == true){
							int option = 1;if(e.isMetaDown()){option = 2;} if (checkdrawflag){option = 3;} if(randoflag){option = 4;}
							if (checkdrawflag && e.isMetaDown()){option = 5;}
							if(tbtflag){tbtState(xlocal, ylocal, option);}
							else{
							switch(option){
								case 1: stateDraw(xlocal,ylocal); break;
								case 2: stateAltDraw(xlocal,ylocal); break;
								case 3: stateCheckDraw(xlocal, ylocal, true); break;
								case 4: stateRandDraw(xlocal, ylocal); break;
								case 5: stateCheckDraw(xlocal, ylocal, false); break;
								default: stateDraw(xlocal, ylocal); break;}}
							bigboard.repaint();}
						
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
							bigboard.repaint();}}
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
							if (checkdrawflag && e.isMetaDown()){option = 5;}
							if(tbtflag){tbtState(xlocal, ylocal, option);}
							else{
							switch(option){
								case 1: stateDraw(xlocal,ylocal); break;
								case 2: stateAltDraw(xlocal,ylocal); break;
								case 3: stateCheckDraw(xlocal, ylocal, true); break;
								case 4: stateRandDraw(xlocal, ylocal); break;
								case 5: stateCheckDraw(xlocal, ylocal, false); break;
								default: stateDraw(xlocal, ylocal); break;}}
						 bigboard.repaint();}
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
							bigboard.repaint();}
						}
					
				
		}
