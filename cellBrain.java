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
	boolean[][] current; 	
	boolean[][] newstate; 	
	public cell[][] culture; 	
	
	int celltype;	
	int maturity = 1;
	// settings (speed, primary cell selection, secondary cell selection, maturity, secondary maturity, cell size, and array initialization)
	public automatonOptionHandler merlin;
	public cellOptionHandler castor;
	public cellOptionHandler pollux;
	public cellOptionHandler eris;
	
	int workcell = 0;
	
	int workmat = 1;
	
	int setdir = 0;
	
	int magnify = 5;
	
	
	
	//general array counters Int x, and Int y are instantiated locally for each use
	
	//keep track of mouse position in edit mode
	int xlocal;
	int ylocal;
	//changestate flag 
	boolean pauseflag = false;
	boolean paused = true;
	//state editing flags
	boolean editflag = false;
	boolean sfflag = false;
	int sfopt = 0;
	//cell editing flags
	boolean editcellflag = false;
	boolean prisec = true;//shunts settings info to castor(true) or pollux(false)
	boolean mirselflag = false;//toggles mirror selection mode
	
	// display flags
	boolean hiliteflag = false; //used to signal when to hilite a cell
	
	//automaton flags
	//none
	
	Thread t = new Thread(this);
	
	
	// constructors
	
	//default
	public cellBrain(){
		xsiz = 400;
		ysiz = 150;
		merlin = new automatonOptionHandler();
		castor = new cellOptionHandler();
		pollux = new cellOptionHandler();
		eris = new randcellOptionHandler();
		eris.setInt("Xsiz", xsiz); eris.setInt("Ysiz", ysiz);
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		bigboard = new cellComponent(xsiz, ysiz);
		bigboard.addMouseMotionListener(this);
		bigboard.addMouseListener(this);
		makeWindow();
		//initialize the board
		int cs = castor.getCT();
		int ms = castor.getMaturity();
		castor.setCT(6);
		castor.setMaturity(1);
		cellFill();
		castor.setCT(cs);	
		castor.setMaturity(ms);
			 
				
				
			bigboard.setState(current);
			}
	
	//set size		
	public cellBrain(int a, int b){
		xsiz = a;
		ysiz = b;
		merlin = new automatonOptionHandler();
		castor = new cellOptionHandler();
		pollux = new cellOptionHandler();
		eris = new randcellOptionHandler();
		eris.setInt("Xsiz", xsiz); eris.setInt("Ysiz", ysiz);
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		bigboard = new cellComponent(xsiz, ysiz);
		bigboard.addMouseMotionListener(this);
		bigboard.addMouseListener(this);
		makeWindow();
		//initialize the board
		int cs = castor.getCT();
		int ms = castor.getMaturity();
		castor.setCT(6);
		castor.setMaturity(1);
		cellFill();
		castor.setCT(cs);	
		castor.setMaturity(ms);
				
				
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
					editflag = a; if(a){bigboard.setMode(2);} else{bigboard.setMode(merlin.getDisp());}}
					
				// enters/exits cell edit mode
				public void setCellEdit(boolean a){
					editcellflag = a; if(a){bigboard.setMode(3);}else{bigboard.setMode(merlin.getDisp());bigboard.remHilite();}}
				
				
				//makes the cells
				public void populate(int a, int b, int c){
					cellOptionHandler decider;
					switch(c){
						case 1: decider = castor;break;
						case 2: decider = pollux;break;
						case 3: decider = eris; break;
						default: decider = castor;break;}
					switch (decider.getCT()){
						case 0: culture[a][b] = new cell();
								break;
						case 1: culture[a][b] = new offCell();
								break;
						case 2: culture[a][b] = new onCell();
								break;
						case 3: culture[a][b] = new blinkCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								break;
						case 4: culture[a][b] = new seqCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								break;
						case 5: culture[a][b] = new randCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								break;
						case 6: culture[a][b] = new conway();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
						case 7: culture[a][b] = new seeds();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
						case 8: culture[a][b] = new parityCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Par", decider.getBool("Par"));
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
						case 9: culture[a][b] = new conveyorCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setInt("Dir", decider.getDirection());
								culture[a][b].setBool("Inv", decider.getInvert());
								break;
						case 10: culture[a][b] = new wolfram();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setInt("Dir", decider.getDirection());
								culture[a][b].setBool("Inv", decider.getInvert());
								break;
						case 11: culture[a][b] = new symmetriCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setInt("Dir", decider.getDirection());
								culture[a][b].setBool("Inv", decider.getInvert());
								break;		
						case 12: culture[a][b] = new mirrorCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setInt("HX", decider.getInt("MirrX"));
								culture[a][b].setInt("HY", decider.getInt("MirrY"));
								break;	
						case 13: culture[a][b] = new majorityCell();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
						default: culture[a][b] = new cell();
								break;}
						bigboard.setSpecies(a,b,decider.getCT());
						bigboard.setLifespan(a,b,decider.getMaturity()); 
					}
					
					// cell editing methods
					
					// cell drawing methods
					public void cellDraw(int x, int y){
						populate(x,y,1);}
						
					public void cellAltDraw(int x, int y){
						populate(x,y,2);} 
					
					public void cellCheckDraw(int x, int y){
						if( y % 2 == 1 ^ x % 2 == 1){
						populate(x,y,1);}
						else{
						populate(x,y,2);}
						}
						
					public void cellRCDraw(int x, int y){
						if( y % 2 == 1 ^ x % 2 == 1){
						populate(x,y,1);}
						else{
						populate(x,y,3);}
						}
						
					public void cellRandDraw(int x, int y){
						populate(x,y,3);
					}
						
					// cell filling methods
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
					
					public void cellRCFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellRCDraw(x,y);
							}}
						if(editcellflag){bigboard.repaint();}
					}
					
					public void cellCheckFill2x2(int a, int b){
						for(int y=0; y<= ysiz-1; y+=2){
							for(int x=0; x<= xsiz-1; x+=2){
								if(y % 4 ==0 ^ x % 4 == 0){
									pop2x2(x,y,a);}
									else{pop2x2(x,y,b);}}}
									if(editcellflag){bigboard.repaint();}
								}
					
					public void cellCheckFilltbt(int a, int b){
						for(int y=1; y<= ysiz-1; y+=3){
						for(int x=1; x<= xsiz-1; x+=3){
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtPop(x,y,a);}
							else{tbtPop(x,y,b);}} 
							if(xsiz-1 % 3 != 1){if(y % 2 == 1 ^ xsiz-1 % 2 == 1){
							tbtPop(xsiz-1,y,a);}
							else{tbtPop(xsiz-1,y,b);}  }}
							if(ysiz-1 % 3 != 1){ for(int x = 1; x<= xsiz-1; x+= 3){int y = ysiz-1;
							if(y % 2 == 1 ^ x % 2 == 1){
							tbtPop(x,y,a);}
							else{tbtPop(x,y,b);}} 
							if(xsiz-1 % 3 != 1){if(ysiz-1 % 2 == 1 ^ xsiz-1 % 2 == 1){
							tbtPop(xsiz-1,ysiz-1,a);}
							else{tbtPop(xsiz-1,ysiz-1,b);}  }}
							if(editcellflag){bigboard.repaint();}}
					
					public void cellRandFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellRandDraw(x,y);
						}}
						if(editcellflag){bigboard.repaint();}
					}
					
					//sets the cells in a 2x2 area
					public void pop2x2(int x, int y, int opt){
						switch (opt){
							case 1: cellDraw(x,y); break;
							case 2: cellAltDraw(x,y); break;
							case 3: cellCheckDraw(x,y); break;
							case 4: cellRandDraw(x,y); break;
							case 5: cellRCDraw(x,y); break;
							default: cellDraw(x,y);break;}
						//x+1,y
							if(x==xsiz-1){}
							else{switch (opt){
							case 1: cellDraw(x+1,y); break;
							case 2: cellAltDraw(x+1,y); break;
							case 3: cellCheckDraw(x+1,y); break;
							case 4: cellRandDraw(x+1,y); break;
							case 5: cellRCDraw(x+1,y); break;
							default: cellDraw(x+1,y);break;}}	
						//x,y+1
							if(y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x,y+1); break;
							case 2: cellAltDraw(x,y+1); break;
							case 3: cellCheckDraw(x,y+1); break;
							case 4: cellRandDraw(x,y+1); break;
							case 5: cellRCDraw(x,y+1); break;
							default: cellDraw(x,y+1);break;}}		
						//x+1,y+1
							if(x==xsiz-1 || y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x+1,y+1); break;
							case 2: cellAltDraw(x+1,y+1); break;
							case 3: cellCheckDraw(x+1,y+1); break;
							case 4: cellRandDraw(x+1,y+1); break;
							case 5: cellRCDraw(x+1,y+1); break;
							default: cellDraw(x+1,y+1);break;}}		
						}
					
					//sets the cells in a 3x3 area
					public void tbtPop(int x,int y, int opt){
						switch (opt){
							case 1: cellDraw(x,y); break;
							case 2: cellAltDraw(x,y); break;
							case 3: cellCheckDraw(x,y); break;
							case 4: cellRandDraw(x,y); break;
							case 5: cellRCDraw(x,y); break;
							default: cellDraw(x,y);break;}
							// if the cell is on the border ignore the outside, otherwise populate 
							//x-1,y-1
							if(x==0 || y==0){}
							else{ switch (opt){
							case 1: cellDraw(x-1,y-1); break;
							case 2: cellAltDraw(x-1,y-1); break;
							case 3: cellCheckDraw(x-1,y-1); break;
							case 4: cellRandDraw(x-1,y-1); break;
							case 5: cellRCDraw(x-1,y-1); break;
							default: cellDraw(x-1,y-1);break;}}						
							//x-1,y
							if(x==0){}
							else{switch (opt){
							case 1: cellDraw(x-1,y); break;
							case 2: cellAltDraw(x-1,y); break;
							case 3: cellCheckDraw(x-1,y); break;
							case 4: cellRandDraw(x-1,y); break;
							case 5: cellRCDraw(x-1,y); break;
							default: cellDraw(x-1,y);break;}}		
							//x-1,y+1
							if(x==0 || y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x-1,y+1); break;
							case 2: cellAltDraw(x-1,y+1); break;
							case 3: cellCheckDraw(x-1,y+1); break;
							case 4: cellRandDraw(x-1,y+1); break;
							case 5: cellRCDraw(x-1,y+1); break;
							default: cellDraw(x-1,y+1);break;}}		
							//x,y-1
							if(y==0){}
							else{switch (opt){
							case 1: cellDraw(x,y-1); break;
							case 2: cellAltDraw(x,y-1); break;
							case 3: cellCheckDraw(x,y-1); break;
							case 4: cellRandDraw(x,y-1); break;
							case 5: cellRCDraw(x,y-1); break;
							default: cellDraw(x,y-1);break;}}		
							//x,y+1
							if(y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x,y+1); break;
							case 2: cellAltDraw(x,y+1); break;
							case 3: cellCheckDraw(x,y+1); break;
							case 4: cellRandDraw(x,y+1); break;
							case 5: cellRCDraw(x,y+1); break;
							default: cellDraw(x,y+1);break;}}		
							//x+1,y-1
							if(x==xsiz-1 || y==0){}
							else{switch (opt){
							case 1: cellDraw(x+1,y-1); break;
							case 2: cellAltDraw(x+1,y-1); break;
							case 3: cellCheckDraw(x+1,y-1); break;
							case 4: cellRandDraw(x+1,y-1); break;
							case 5: cellRCDraw(x+1,y-1); break;
							default: cellDraw(x+1,y-1);break;}}		
							//x+1,y
							if(x==xsiz-1){}
							else{switch (opt){
							case 1: cellDraw(x+1,y); break;
							case 2: cellAltDraw(x+1,y); break;
							case 3: cellCheckDraw(x+1,y); break;
							case 4: cellRandDraw(x+1,y); break;
							case 5: cellRCDraw(x+1,y); break;
							default: cellDraw(x+1,y);break;}}	
							//x+1,y+1
							if(x==xsiz-1 || y==ysiz-1){}
							else{switch (opt){
							case 1: cellDraw(x+1,y+1); break;
							case 2: cellAltDraw(x+1,y+1); break;
							case 3: cellCheckDraw(x+1,y+1); break;
							case 4: cellRandDraw(x+1,y+1); break;
							case 5: cellRCDraw(x+1,y+1); break;
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
								case 5: cellRCDraw(x,0); cellRCDraw(x,ysiz-1);break;
								default: cellDraw(x,0); cellDraw(x, ysiz-1); break;}}
						for(int y = 0; y<= ysiz-1; y++){
							switch(option){
								case 1: cellDraw(0,y); cellDraw(xsiz-1, y); break;
								case 2: cellAltDraw(0,y); cellAltDraw(xsiz-1, y); break;
								case 3: cellCheckDraw(0,y); cellCheckDraw(xsiz-1,y); break;
								case 4: cellRandDraw(0,y); cellRandDraw(xsiz-1, y); break;
								case 5: cellRCDraw(0,y); cellRCDraw(xsiz-1, y); break;
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
							
				public void stateCheckFill2x2(){
					for(int y=0; y <= ysiz-1; y+=2){
						for(int x=0; x <= xsiz-1; x+=2){
							if(y%4 == 0 ^ x % 4 == 0){
								state2x2(x,y,1);}
								else{state2x2(x,y,2);}}}
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
					
				//draws into the state 2x2
				public void state2x2(int x, int y, int opt){
						switch (opt){
							case 1: stateDraw(x,y); break;
							case 2: stateAltDraw(x,y); break;
							case 3: stateCheckDraw(x,y, true); break;
							case 4: stateRandDraw(x,y); break;
							case 5: stateCheckDraw(x,y, false); break;
							default: stateDraw(x,y);break;}
						//x+1,y
							if(x==xsiz-1){}
							else{switch (opt){
							case 1: stateDraw(x+1,y); break;
							case 2: stateAltDraw(x+1,y); break;
							case 3: stateCheckDraw(x+1,y, true); break;
							case 4: stateRandDraw(x+1,y); break;
							case 5: stateCheckDraw(x+1,y, false); break;
							default: stateDraw(x+1,y);break;}}	
						//x,y+1
							if(y==ysiz-1){}
							else{switch (opt){
							case 1: stateDraw(x,y+1); break;
							case 2: stateAltDraw(x,y+1); break;
							case 3: stateCheckDraw(x,y+1, true); break;
							case 4: stateRandDraw(x,y+1); break;
							case 5: stateCheckDraw(x,y+1, false); break;
							default: stateDraw(x,y+1);break;}}					
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
					
					//sets neighborhoods for mirror cells
					public void mirSel(boolean a){
						if(mirselflag == false){mirselflag = true; hiliteflag = true; prisec = a;}
						else{if(prisec != a){prisec = a; mirselflag = true; hiliteflag = true;}
							else{mirselflag = false; hiliteflag = false;}}
						}
					
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
							if(merlin.getWrap("X") ){if(x==0 && y != 0){neighbors[0][0] = current[xsiz-1][y-1];}
							else{neighbors[0][0] = false;}}
							if( merlin.getWrap("Y")){if(y == 0 && x != 0){neighbors[0][0] = current[x-1][ysiz-1];} else{neighbors[0][0] = false;}}
							if(merlin.getWrap("X") && merlin.getWrap("Y")){if(x==0 && y != 0){neighbors[0][0] = current[xsiz-1][y-1];}if(y == 0 && x != 0){neighbors[0][0] = current[x-1][ysiz-1];}if(x == 0 && y == 0){neighbors[0][0] = current[xsiz-1][ysiz-1];}}}
							//non-border
							else{ if(current[x-1][y-1]== true){neighbors[0][0]=true;}}
							
							//Left-center neighbor
							if(x==0){neighbors[0][1] = false; if(merlin.getWrap("X") ){neighbors[0][1] = current[xsiz-1][y];}}
							else{if(current[x-1][y] == true){neighbors[0][1] = true;}}
							
							//Lower-Left neighbor
							if(x==0 || y==ysiz-1){
							// normal border
							neighbors[0][2] = false;
							//wraparound borders 
							if(merlin.getWrap("X") ){if(x == 0 && y != ysiz-1){neighbors[0][2] = current[xsiz-1][y+1];} else{neighbors[0][2] = false;}}
							if( merlin.getWrap("Y")){if(y == ysiz-1 && x!= 0){neighbors[0][2] = current[x-1][0];}else{neighbors[0][2] = false;}}
							if(merlin.getWrap("X") && merlin.getWrap("Y")){if(x == 0 && y != ysiz-1){neighbors[0][2] = current[xsiz-1][y+1];}if(y == ysiz-1 && x!= 0){neighbors[0][2] = current[x-1][0];}if(x == 0 && y == ysiz-1){neighbors[0][2] = current[xsiz-1][0];}}}
							//non-border
							else{if(current[x-1][y+1] == true) {neighbors[0][2] = true;}}
							
							//upper-center neighbor
							if(y==0){neighbors[1][0] = false;if( merlin.getWrap("Y")){neighbors[1][0] = current[x][ysiz-1];}}
							else{if(current[x][y-1]==true){ neighbors[1][0] = true;}}
							
							//lower-center neighbor
							if(y==ysiz-1){neighbors[1][2] = false;if( merlin.getWrap("Y")){neighbors[1][2] = current[x][0];}}
							else{if(current[x][y+1]==true){ neighbors[1][2] = true;}}
							
							//upper-right neighbor
							if(x==xsiz-1 || y==0){
							//normal border	
							neighbors[2][0] = false;
							//wraparound border
							if(merlin.getWrap("X") ){if (x == xsiz-1 && y != 0){neighbors[2][0] = current[0][y-1];}else{neighbors[2][0] = false;}}
							if( merlin.getWrap("Y")){if(y == 0 && x != xsiz-1){neighbors[2][0] = current[x+1][ysiz-1];}else{neighbors[2][0] = false;}}
							if(merlin.getWrap("X") && merlin.getWrap("Y")){if (x == xsiz-1 && y != 0){neighbors[2][0] = current[0][y-1];}if(y == 0 && x != xsiz-1){neighbors[2][0] = current[x+1][ysiz-1];}if(x == xsiz-1 && y == 0){neighbors[2][0] = current[0][ysiz-1];}}}
							//non-border
							else{if(current[x+1][y-1]== true){neighbors[2][0] = true;}}
							
							//center-right neighbor
							if(x==xsiz-1){neighbors[2][1] = false; if(merlin.getWrap("X") ){neighbors[2][1] = current[0][y];}if (merlin.getWrap("X") && merlin.getWrap("Y")){neighbors[2][1] = current[0][y];}}
							else{if(current[x+1][y]==true){neighbors[2][1] = true;}}
							
							//lower-right neighbor
							if(x==xsiz-1 || y==ysiz-1){
							// normal border	
							neighbors[2][2] = false;
							//wraparound border
							if(merlin.getWrap("X")){if (x == xsiz-1 && y != ysiz-1){neighbors[2][2] = current[0][y+1];} else {neighbors[2][2] = false;}}
							if( merlin.getWrap("Y")){if(y == ysiz-1 && x != xsiz-1){neighbors[2][2] = current[x+1][0];}else{neighbors[2][2] = false;}}
							if(merlin.getWrap("X") && merlin.getWrap("Y")){if (x == xsiz-1 && y != ysiz-1){neighbors[2][2] = current[0][y+1];} if(y == ysiz-1 && x != xsiz-1){neighbors[2][2] = current[x+1][0];}if(x == xsiz-1 && y == ysiz-1){neighbors[2][2] = current[0][0];}}
							}
							//non-border
							else{if(current[x+1][y+1] == true){neighbors[2][2] = true;}}
						return neighbors;
		}
		
		
		// neighborhood for one dimensional, horizontal cells
		public boolean[][] getWolfram(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if (x == 0){wolfhood[0][0] = false; if(merlin.getWrap("X") ){wolfhood[0][0] = current[xsiz-1][y];}} else{wolfhood[0][0] = current[x-1][y];}
			wolfhood[1][0] = current[x][y];
			if(x == xsiz-1){wolfhood[2][0] = false;if(merlin.getWrap("X") ){wolfhood[2][0] = current[0][y];}} else{wolfhood[2][0] = current[x+1][y];}
			return wolfhood;}
		
		// neighborhood for one dimensional, vertical cells	
		public boolean[][] getWolframV(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if(y == 0){wolfhood[0][0] = false;if( merlin.getWrap("Y")){wolfhood[0][0] = current[x][ysiz-1];}} else{wolfhood[0][0] = current[x][y-1];}
			wolfhood[1][0] = current[x][y];
			if(y == ysiz-1){wolfhood[2][0] = false;if( merlin.getWrap("Y")){wolfhood[2][0] = current[x][0];}} else{wolfhood[2][0] = current[x][y+1];}
			return wolfhood;}
			
		// neighborhood for one dimensional cells starting with upper left
		public boolean[][] getWolframUL(int x, int y){
			boolean[][] wolfhood = new boolean[3][1];
			if (x == 0 || y == 0){ 
				//normal border
				wolfhood[0][0] = false;
				//wraparound border
				if(merlin.getWrap("X") ){if(x == 0 && y != 0){wolfhood[0][0] = current[xsiz-1][y-1];}else{wolfhood[0][0] = false;}}
			if ( merlin.getWrap("Y")){if(y == 0 && x != 0){wolfhood[0][0] = current[x-1][ysiz-1];} else{wolfhood[0][0] = false;}}
			if(merlin.getWrap("X") && merlin.getWrap("Y")){if(x == 0 && y != 0){wolfhood[0][0] = current[xsiz-1][y-1];}if(y == 0 && x != 0){wolfhood[0][0] = current[x-1][ysiz-1];}if(x == 0 && y == 0){wolfhood[0][0] = current[xsiz-1][ysiz-1];}}}
			//non-border
			 else{ wolfhood[0][0] = current[x-1][y-1];}
			 
			 // center cell
			wolfhood[1][0] = current[x][y];
			
			if (x == xsiz-1 || y == ysiz-1){ 
				// normal border
				wolfhood[2][0] = false;
				//wraparound border
				if(merlin.getWrap("X") ){if (x == xsiz-1 && y != ysiz-1){wolfhood[2][0] = current[0][y+1];} else {wolfhood[2][0] = false;}}
				if( merlin.getWrap("Y")){if(y == ysiz-1 && x != xsiz-1){wolfhood[2][0] = current[x+1][0];}else{wolfhood[2][0] = false;}}
				if(merlin.getWrap("X") && merlin.getWrap("Y")){if (x == xsiz-1 && y != ysiz-1){wolfhood[2][0] = current[0][y+1];}if(y == ysiz-1 && x != xsiz-1){wolfhood[2][0] = current[x+1][0];}if(x == xsiz-1 && y == ysiz-1){wolfhood[2][0] = current[0][0];}}} 
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
				if(merlin.getWrap("X")){if(x == 0 && y != ysiz-1){wolfhood[0][0] = current[xsiz-1][y+1];} else{wolfhood[0][0] = false;}}
				if(merlin.getWrap("Y")){if(y == ysiz-1 && x!= 0){wolfhood[0][0] = current[x-1][0];}else{wolfhood[0][0] = false;}}
				if(merlin.getWrap("X") && merlin.getWrap("Y")){if(x == 0 && y != ysiz-1){wolfhood[0][0] = current[xsiz-1][y+1];}if(y == ysiz-1 && x!= 0){wolfhood[0][0] = current[x-1][0];}if(x == 0 && y == ysiz-1){wolfhood[0][0] = current[xsiz-1][0];}}}
			//non-border
			else{wolfhood[0][0] = current[x-1][y+1];}
			
			// center cell
			wolfhood[1][0] = current[x][y];
			
			if(x == xsiz-1 || y == 0){
				// normal border
				wolfhood[2][0] = false;
				//wraparound border
				if(merlin.getWrap("X")){if (x == xsiz-1 && y != 0){wolfhood[2][0] = current[0][y-1];}else{wolfhood[2][0] = false;}}
				if(merlin.getWrap("Y")){if(y == 0 && x != xsiz-1){wolfhood[2][0] = current[x+1][ysiz-1];}else{wolfhood[2][0] = false;}}
				if(merlin.getWrap("X") && merlin.getWrap("Y")){if (x == xsiz-1 && y != 0){wolfhood[2][0] = current[0][y-1];}if(y == 0 && x != xsiz-1){wolfhood[2][0] = current[x+1][ysiz-1];}if(x == xsiz-1 && y == 0){wolfhood[2][0] = current[0][ysiz-1];}}} 
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
				   case 6: stateCheckFill2x2(); break;
				   default: stateCheckFill(); break;}
				   sfflag = false;}
				   
					//gets new values from the cells
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
						if (culture[x][y].getNeighborhood() == "None"){
							culture[x][y].setNeighbors(getSelf(x,y));
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
							
						//set age for multicolor mode
						if(bigboard.getMode() == 4){bigboard.setAge(x,y,newstate[x][y]);}
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
				Thread.sleep(merlin.getZT());
			} catch(InterruptedException ie){}
			
			}

				}
				
			
			// mouse interaction methods
					public void mouseMoved( MouseEvent e){
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						if (xlocal > xsiz-1){xlocal = xsiz-1;}
						if (ylocal > ysiz-1){ylocal = ysiz-1;}
						
						// hilight passed over cells
						if(hiliteflag){if(mirselflag && editcellflag){int colsel; if(prisec){colsel = 1;}
						else{colsel = 2;} bigboard.setHiLite(xlocal, ylocal, colsel);}
						 }
						// hilight a mirrorCell's targets as each mirrrorCell is moused over in the editor 
						 if(mirselflag == false && editcellflag){
						if(culture[xlocal][ylocal].hood == "Mirror"){bigboard.setHiLite(
							culture[xlocal][ylocal].getInt("HX"),culture[xlocal][ylocal].getInt("HY"), 1);}
						else{}	
						}
						
						}
						
					public void mouseDragged(MouseEvent e) {
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						if (xlocal > xsiz-1){xlocal = xsiz-1;}
						if (ylocal > ysiz-1){ylocal = ysiz-1;}
						
						//edit state
						if(editflag == true || merlin.getInter()){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getSDO("Check")){option = 3;} if(merlin.getSDO("Rand")){option = 4;}
							if (merlin.getSDO("Check") && e.isMetaDown()){option = 5;}
							if(merlin.getBrush() == 3){tbtState(xlocal, ylocal, option);}
							if(merlin.getBrush() == 2){state2x2(xlocal, ylocal, option);}
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
						if (editcellflag == true && mirselflag == false){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getCDO("Check")){option = 3;}
							if(merlin.getCDO("Rand")){option = 4;} if(merlin.getCDO("Check") && merlin.getCDO("Rand")){option = 5;}
							if (merlin.getBrush() == 3){tbtPop(xlocal,ylocal,option);}
							if(merlin.getBrush() == 2){pop2x2(xlocal, ylocal, option);}
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
						if (xlocal > xsiz-1){xlocal = xsiz-1;}
						if (ylocal > ysiz-1){ylocal = ysiz-1;}
						
						//edit state
						if(editflag == true || merlin.getInter()){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getSDO("Check")){option = 3;} if(merlin.getSDO("Rand")){option = 4;}
							if (merlin.getSDO("Check") && e.isMetaDown()){option = 5;}
							if(merlin.getBrush() == 3){tbtState(xlocal, ylocal, option);}
							if(merlin.getBrush() == 2){state2x2(xlocal, ylocal, option);}
							else{
							switch(option){
								case 1: stateDraw(xlocal,ylocal); break;
								case 2: stateAltDraw(xlocal,ylocal); break;
								case 3: stateCheckDraw(xlocal, ylocal, true); break;
								case 4: stateRandDraw(xlocal, ylocal); break;
								case 5: stateCheckDraw(xlocal, ylocal, false); break;
								default: stateDraw(xlocal, ylocal); break;}}
						 bigboard.repaint();}
						 
						//edit cells
						if (editcellflag == true && mirselflag == false){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getCDO("Check")){option = 3;}
							if(merlin.getCDO("Rand")){option = 4;} if(merlin.getCDO("Check") && merlin.getCDO("Rand")){option = 5;}
							if (merlin.getBrush() == 3){tbtPop(xlocal,ylocal,option);}
							if(merlin.getBrush() == 2){pop2x2(xlocal, ylocal, option);}
							else{
							switch(option){
							case 1: cellDraw(xlocal,ylocal); break;
							case 2: cellAltDraw(xlocal,ylocal); break;
							case 3: cellCheckDraw(xlocal,ylocal); break;
							case 4: cellRandDraw(xlocal,ylocal); break;
							default: cellDraw(xlocal, ylocal); break;
							}} 
							bigboard.repaint();}
							
						//mirror selection
						if(editcellflag && mirselflag){
							if(prisec){castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal); mirselflag = false;hiliteflag = false;}
							else{pollux.setInt("MirrX", xlocal); pollux.setInt("MirrY", ylocal); mirselflag = false;hiliteflag = false;}
							bigboard.setHiLite(xlocal, ylocal, 3);
						}
						
						}
					
				
		}
