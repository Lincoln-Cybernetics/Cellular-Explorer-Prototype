import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.event.*;

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
	selector harry;
	int celltype;	
	int maturity = 1;
	// settings (speed, primary cell selection, secondary cell selection, maturity, secondary maturity, cell size, and array initialization)
	public automatonOptionHandler merlin;
	public cellOptionHandler castor;
	public cellOptionHandler pollux;
	public cellOptionHandler eris;
	//display settings
	int magnify = 5;

	//general array counters Int x, and Int y are instantiated locally for each use
	
	//keep track of mouse position in edit mode
	int xlocal;
	int ylocal;
	// editing brush
	brush sigmund;
	// moore neighborhood brush
	threebrush ariadne;
	spinbrush andromeda;
	//changestate flag 
	boolean pauseflag = false;
	boolean paused = true;
	
	//general editing flags
	
	//for selecting a single cell
	boolean singleselflag = false;
	//rectangle selection
	int rectselflag = 0;
	boolean rectselfie = false;
	
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
		harry = new selector(xsiz, ysiz);
		harry.deselect();
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush(xsiz, ysiz);
		ariadne.setType(true);
		andromeda = new spinbrush(xsiz, ysiz);
		andromeda.setType(true);
		bigboard = new cellComponent(xsiz, ysiz);
		bigboard.addMouseMotionListener(this);
		bigboard.addMouseListener(this);
		makeWindow();
		//initialize the board
		setEditBrush();
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
		harry = new selector(xsiz, ysiz);
		harry.deselect();
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush(xsiz, ysiz);
		ariadne.setType(true);
		andromeda = new spinbrush(xsiz, ysiz);
		andromeda.setType(true);
		bigboard = new cellComponent(xsiz, ysiz);
		bigboard.addMouseMotionListener(this);
		bigboard.addMouseListener(this);
		makeWindow();
		//initialize the board
		setEditBrush();
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
				garden.setLocation(200,165);
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
					
					// general editing methods
					
					// brush setting method
					public void setEditBrush(){
						switch(merlin.getBrush()){
						case 1: sigmund = new brush(xsiz, ysiz); break;
						case 2: sigmund = new twobrush(xsiz, ysiz); break;
						case 3: sigmund = new threebrush(xsiz,ysiz); break;
						case 4: sigmund = new gliderbrush(xsiz, ysiz); sigmund.setOrientation(merlin.getBrushDir()); break;
						default : sigmund = new brush(xsiz, ysiz); break;}
					}
					
					//selection methods
					//tells the brain a selection is being made
					public void setSelection(int a){
						switch(a){
							case 1: hiliteflag = true; singleselflag = true;bigboard.setSelect(true);break;
							case 2: hiliteflag = false; singleselflag = false; break;
							case 3: hiliteflag = true; bigboard.setSelect(true); rectselflag = 1; break;
						}
					}
				
					// refreshes the selection in the display
					public void refreshSel(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){
							bigboard.setSelection(x,y,harry.getSelection(x,y));}}
							bigboard.repaint();
						}
						
				
					// cell editing methods	
					
				// enters/exits cell edit mode
				public void setCellEdit(boolean a){
					editcellflag = a; if(a){bigboard.setMode(3);}else{bigboard.setMode(merlin.getDisp());bigboard.remHilite();}}
				
				
				//makes the cells
				public void populate(int a, int b, int c){
					if(harry.getSelected() == false || harry.getSelection(a,b)){
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
								for(int g = 0; g <= 7; g++){
								culture[a][b].setBoola("Seq", g, decider.getBoola("Seq", g));
								}
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
								for(int g = 0; g <= 7; g++){
								culture[a][b].setBoola("Rule", g, decider.getBoola("Rule", g));
								}
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
								
						case 14: culture[a][b] = new gnarl();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
								
						case 15: culture[a][b] = new amoeba();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
								
						case 16: culture[a][b] = new highlife();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
								
						case 17: culture[a][b] = new prime();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;
								
						case 18: culture[a][b] = new dayNight();
								culture[a][b].setInt("Mat", decider.getMaturity());
								culture[a][b].setBool("Inv", decider.getInvert());
								culture[a][b].setBool("Rec", decider.getBool("Rec"));
								break;		
								
						default: culture[a][b] = new cell();
								break;}
						bigboard.setSpecies(a,b,decider.getCT());
						bigboard.setLifespan(a,b,decider.getMaturity()); 
					}}
					
					
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
					
					public void boolFill(String a, boolean b){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){
							if(harry.getSelected() == false || harry.getSelection(x,y)){
								if(a == "Fade"){culture[x][y].setBool("Fade",b);}
						}}}
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
					
				
					
					public void cellRandFill(){
						for(int y=0;y<=ysiz-1;y++){
						for(int x=0;x<=xsiz-1;x++){	
							cellRandDraw(x,y);
						}}
						if(editcellflag){bigboard.repaint();}
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
				
				// enters/exits state edit mode
				public void setEdit(boolean a){
					editflag = a; if(a){bigboard.setMode(2);} else{bigboard.setMode(merlin.getDisp());}}
				
				//changes the state array
				public void setCellState(int x, int y, boolean b){
					if(harry.getSelected() == false || harry.getSelection(x,y)){
						current[x][y] = b;}
				}
				
				//state fill methods
				public void stateFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateDraw(x,y);}}
					if(bigboard.getMode() != 3){bigboard.setState(current);bigboard.repaint();}
					} 
						
				public void stateRandFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						stateRandDraw(x,y);
					}} 
					if(bigboard.getMode() != 3){bigboard.setState(current);bigboard.repaint();}
					}
					
				public void stateClearFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						current[x][y] = false;culture[x][y].purgeState();}}
						if(bigboard.getMode() != 3){bigboard.setState(current);bigboard.repaint();}
						}
						
				
							
			
						
				public void stateCheckFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
					stateCheckDraw(x,y, true);
					}} 
					if(bigboard.getMode() != 3){bigboard.setState(current);bigboard.repaint();}
					}
					
				public void stateRCFill(){
					for(int y=0;y<=ysiz-1;y++){
					for(int x=0;x<=xsiz-1;x++){
						if( y % 2 == 1 ^ x % 2 == 1){stateDraw(x,y);}
						else{stateRandDraw(x,y);}}}
						if(bigboard.getMode() != 3){bigboard.setState(current);bigboard.repaint();}
					}
				
				public void stateInvert(){
					for(int y = 0; y<= ysiz-1; y++){
						for(int x=0; x<= xsiz-1; x++){
							setCellState(x,y,!current[x][y]);}}
					if(bigboard.getMode() != 3){bigboard.setState(current);bigboard.repaint();}
				}		
				
				//state drawing methods	
				public void stateDraw(int x,int y){
					setCellState(x,y,true);}
					
				public void stateAltDraw(int x,int y){
					setCellState(x,y,false);}
					
				public void stateCheckDraw(int x,int y, boolean fill){
					if( y % 2 == 1 ^ x % 2 == 1){ setCellState(x,y,fill);}
						else{if(fill){setCellState(x,y,false);}}}
					
				public void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					setCellState(x,y,foghorn.nextBoolean());}	
					
			
						
						
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
					ariadne.locate(x,y);
					for(int v = 0; v<=2; v++){
					for(int h = 0; h<=2; h++){
						int tempx = ariadne.getNextX();
						int tempy = ariadne.getNextY();
						if(tempx == -1 || tempy == -1){neighbors[h][v] = false;}
						else{ neighbors[h][v] = current[tempx][tempy];}}}
						return neighbors;}
		
		
		// neighborhood for one dimensional cells with one neighbor on each side
		public boolean[][] getWolfram(int x, int y, int o){
			boolean[][] wolfhood = new boolean[3][1];
			andromeda.locate(x,y);
			andromeda.setOrientation(o);
			for(int v = 0; v<=2; v++){
				int tempx = andromeda.getNextX();
				int tempy = andromeda.getNextY();
				if(tempx == -1 || tempy == -1){wolfhood[v][0] = false;}
				else{wolfhood[v][0] = current[tempx][tempy];}}
				return wolfhood;
			}
		
	
		
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
				   case 7: stateInvert(); break;
				   case 8: stateRCFill(); break;
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
							culture[x][y].setNeighbors(getWolfram(x,y, 0));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "WolframV"){
							culture[x][y].setNeighbors(getWolfram(x,y, 1));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "WolframUL"){
							culture[x][y].setNeighbors(getWolfram(x,y, 2));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "WolframLL"){
							culture[x][y].setNeighbors(getWolfram(x,y, 3));
							newstate[x][y] = culture[x][y].iterate();}
							
						if(culture[x][y].getNeighborhood() == "Mirror"){
							culture[x][y].setNeighbors(getSelf(culture[x][y].getInt("HX"), culture[x][y].getInt("HY")));
							newstate[x][y] = culture[x][y].iterate();}
							
						
					}}
					
					// cycles new values into current state
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
								// set age
						bigboard.setAge(x,y,culture[x][y].getAge());
				
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
						if(singleselflag){bigboard.setHiLite(xlocal,ylocal,1);}
						if(rectselflag == 1){bigboard.setHiLite(xlocal,ylocal,1);}
						 }
						 // draws rectangle during rectangle selection
						 if(rectselflag == 2){for(int y = 0; y <= ysiz-1; y++){
												for(int x = 0; x <= xsiz-1; x++){
													bigboard.setSelection(x,y,harry.getSelection(x,y));}}
							 bigboard.finishRect(xlocal,ylocal); rectselfie = false;}
						 
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
						
						if(merlin.isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
						//edit state
						if(merlin.getMAction() == "SDraw"){
						if(editflag == true || merlin.getInter()){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getSDO("Check")){option = 3;}
							 if(merlin.getSDO("Rand")){if(e.isMetaDown()){option = 2;}else{option = 4;}}
							if (merlin.getSDO("Check") && e.isMetaDown()){option = 5;}
							switch(option){
								case 1: stateDraw(thisx,thisy); break;
								case 2: stateAltDraw(thisx,thisy); break;
								case 3: stateCheckDraw(thisx,thisy, true); break;
								case 4: stateRandDraw(thisx,thisy); break;
								case 5: stateCheckDraw(thisx,thisy, false); break;
								default: stateDraw(thisx,thisy); break;}}
							}
						
						//editcell
						if(merlin.getMAction() == "CDraw"){
						if (editcellflag == true && mirselflag == false){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getCDO("Check")){option = 3;}
							if(merlin.getCDO("Rand")){option = 4;} if(merlin.getCDO("Check") && merlin.getCDO("Rand")){option = 5;}
							
							switch(option){
							case 1: cellDraw(thisx,thisy); break;
							case 2: cellAltDraw(thisx,thisy); break;
							case 3: cellCheckDraw(thisx,thisy); break;
							case 4: cellRandDraw(thisx,thisy); break;
							case 5: cellRCDraw(thisx,thisy); break;
							default: cellDraw(thisx,thisy); break;
							}}
							}
							
							// cell selection
						if(merlin.getMAction() == "SSel"){
						if(singleselflag){
							 if(e.isMetaDown()){harry.removeCell(thisx,thisy);}
							 else{harry.selectCell(thisx,thisy);}
							}
						}
						
					}
						
						refreshSel();
					}
					else{
						 //  rectangle selection
						 if(merlin.getMAction() == "SRect"){
						 if(rectselflag == 2){for(int y = 0; y <= ysiz-1; y++){
												for(int x = 0; x <= xsiz-1; x++){
													bigboard.setSelection(x,y,harry.getSelection(x,y));}}
							 bigboard.finishRect(xlocal,ylocal); rectselfie = false;}}}
							}
							
					public void mouseEntered(MouseEvent e){}
					
					public void mouseExited(MouseEvent e){}
					public void mousePressed(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						if (xlocal > xsiz-1){xlocal = xsiz-1;}
						if (ylocal > ysiz-1){ylocal = ysiz-1;}
						
								if(merlin.isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						//nothin' doin'
					}}
					else{
						
						//rectangle selection
						if(merlin.getMAction() == "SRect"){
						if(rectselflag == 1){harry.startRect(xlocal,ylocal, !e.isMetaDown());hiliteflag = false;
						 bigboard.remHilite();bigboard.beginRect(xlocal,ylocal,!e.isMetaDown()); rectselflag = 2;rectselfie = true;}}
						 
					 }
						}
					
					public void mouseReleased(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						if (xlocal > xsiz-1){xlocal = xsiz-1;}
						if (ylocal > ysiz-1){ylocal = ysiz-1;}
						
								if(merlin.isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						//nothin' doin'
					}}
					else{
						//rectangle selection
						if(merlin.getMAction() == "SRect"){
						if(rectselflag == 2&& rectselfie == false){harry.endRect(xlocal,ylocal); 
							for(int y = 0; y<= ysiz-1; y++){
							for(int x = 0; x<= xsiz-1; x++){
								bigboard.setSelection(x,y,harry.getSelection(x,y));}}
								bigboard.repaint();rectselflag = 0;
								switch(bigboard.getMode()){
									case 1: if(merlin.getInter()){merlin.setMAction("SDraw");}else{merlin.setMAction("None");}break;
									case 2: merlin.setMAction("SDraw"); break;
									case 3: merlin.setMAction("CDraw"); break;
									case 4: if(merlin.getInter()){merlin.setMAction("SDraw");}else{merlin.setMAction("None");}break;
									default: if(merlin.getInter()){merlin.setMAction("SDraw");}else{merlin.setMAction("None");}break;}
								}
							}
							
						}
						}
					
					public void mouseClicked(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/magnify;}
						if (xlocal > xsiz-1){xlocal = xsiz-1;}
						if (ylocal > ysiz-1){ylocal = ysiz-1;}
						
							if(merlin.isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
						//edit state
						if(merlin.getMAction() == "SDraw"){
						if(editflag == true || merlin.getInter()){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getSDO("Check")){option = 3;} 
							if(merlin.getSDO("Rand")){if(e.isMetaDown()){option = 2;}else{option = 4;}}
							if (merlin.getSDO("Check") && e.isMetaDown()){option = 5;}
							switch(option){
								case 1: stateDraw(thisx,thisy); break;
								case 2: stateAltDraw(thisx,thisy); break;
								case 3: stateCheckDraw(thisx,thisy, true); break;
								case 4: stateRandDraw(thisx,thisy); break;
								case 5: stateCheckDraw(thisx,thisy, false); break;
								default: stateDraw(thisx,thisy); break;}}
						 }
						 
						//edit cells
						if(merlin.getMAction() == "CDraw"){
						if (editcellflag == true && mirselflag == false){
							int option = 1;if(e.isMetaDown()){option = 2;} if (merlin.getCDO("Check")){option = 3;}
							if(merlin.getCDO("Rand")){option = 4;} if(merlin.getCDO("Check") && merlin.getCDO("Rand")){option = 5;}
							switch(option){
							case 1: cellDraw(thisx,thisy); break;
							case 2: cellAltDraw(thisx,thisy); break;
							case 3: cellCheckDraw(thisx,thisy); break;
							case 4: cellRandDraw(thisx,thisy); break;
							case 5: cellRCDraw(thisx,thisy); break;
							default: cellDraw(thisx,thisy); break;
							}} 
							}
							
						
						
						// cell selection
						if(merlin.getMAction() == "SSel"){
						if(singleselflag){
							if(e.isMetaDown()){harry.removeCell(thisx,thisy);}
							else{harry.selectCell(thisx,thisy);}	
						}}
					}
						
								refreshSel();
						}
						else{
						
						//rectangle selection
						if(merlin.getMAction() == "SRect"){
						if(rectselflag == 1){harry.startRect(xlocal,ylocal, !e.isMetaDown());hiliteflag = false; bigboard.remHilite();bigboard.beginRect(xlocal,ylocal,!e.isMetaDown()); rectselflag = 2;rectselfie = true;}
						if(rectselflag == 2 && rectselfie == false){harry.endRect(xlocal,ylocal); 
							for(int y = 0; y<= ysiz-1; y++){
							for(int x = 0; x<= xsiz-1; x++){
								bigboard.setSelection(x,y,harry.getSelection(x,y));}}
								bigboard.repaint();rectselflag = 0;
								switch(bigboard.getMode()){
									case 1: if(merlin.getInter()){merlin.setMAction("SDraw");}else{merlin.setMAction("None");}break;
									case 2: merlin.setMAction("SDraw"); break;
									case 3: merlin.setMAction("CDraw"); break;
									case 4: if(merlin.getInter()){merlin.setMAction("SDraw");}else{merlin.setMAction("None");}break;
									default: if(merlin.getInter()){merlin.setMAction("SDraw");}else{merlin.setMAction("None");}break;}
								}
						}
						
						//mirror selection
						if(merlin.getMAction() == "Mirsel"){
						if(editcellflag && mirselflag){
							if(prisec){castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal); mirselflag = false;hiliteflag = false;}
							else{pollux.setInt("MirrX", xlocal); pollux.setInt("MirrY", ylocal); mirselflag = false;hiliteflag = false;}
							bigboard.setHiLite(xlocal, ylocal, 3);merlin.setMAction("CDraw");
						}}
				}
					
				
		}
}
