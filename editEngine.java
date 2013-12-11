import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class editEngine extends logicEngine implements ucListener, MouseInputListener{
int xlocal = 0;// used for mouse events
int ylocal = 0;// used for mouse events
int xmax;//x-dimension size of the automaton
int ymax;// y-dimension size of the automaton
int zt = 500;
brush sigmund;
int brushtype;
String maction;
int mode = 1;
int dmode = 1;
cellOptionHandler castor;
cellOptionHandler pollux;
cellOptionHandler eris;
boolean prisec = true;//shunts settings info to castor(true) or pollux(false)
selector sedna;
int sdo = 0; //state drawing option
boolean interactflag = false;
int sfo = 0; //state fill option
int cdo = 0; //cell drawing option
int cfo = 0; //cell fill option
boolean rcflag = false;//right click flag
boolean rectflag = false;//flag for making rectangles

public  editEngine(){
	pistons = new cellBrain[1][1];
	outputs = new cellComponent[1][1];
	sigmund = new brush();
	brushtype = 1;
	castor = new cellOptionHandler();
	pollux = new cellOptionHandler();
	eris = new randcellOptionHandler();
	sedna = new selector();
	}
	
public void initialize(){
	xmax = 400; ymax = 150; 
	pistons[0][0] = new cellBrain(400,150, this);
	 outputs[0][0] = new cellComponent(400,150);
	  outputs[0][0].addMouseMotionListener(this);	
							outputs[0][0].addMouseListener(this); pistons[0][0].setDisplay(outputs[0][0]); 
							setEditBrush(1);
						  JFrame disp = new JFrame("Cellular Explorer");
						  disp.getContentPane().add(new JScrollPane(outputs[0][0]));
						  disp.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
						  disp.setSize(1000,500);
						  disp.setLocation(200,165);
						  disp.setResizable(true);
						  disp.setVisible(true);
						  eris.setInt("Xsiz", 400);
						  eris.setInt("Ysiz", 150);
						  sedna = new selector(400,150);
						  setMode(1);
						}

public void handleControl(ucEvent e){}
	//start/stop the automaton
	public void playPause(){
		if(!pistons[0][0].mayIterate()){pistons[0][0].setOpMode(pistons[0][0].getDispType());
		outputs[0][0].setDispMode(pistons[0][0].getDispType());}
								pistons[0][0].pP(); }
								
	public void step(int a){
		for (int n = 0; n < a; n++){
			pistons[0][0].iterate();}
		}

	public void iterateInterrupt(int a){if (a == 1){stateFillSelect();}}
	
	//speed settings
	public void setMasterSpeed(int a){
		pistons[0][0].setZT(a); zt = a;}
		
	public int getMasterSpeed(){
		return zt;}
		
	public void setMouseAction(String action){
		maction = action;
	/*mouse actions:
	 * "BSel" select with the brush
	 * "CDraw" cell draw
	 * "SDraw" state draw
	 * "SRect" select rectangle
	 * "SRaction"  selecting rectangle
	 * "Mirsel" select mirror cell target
	 * "None" none
	 */
	}
	
	public boolean isMouseUsed(){
		if (maction == "SRect"){return false;}
		if (maction == "SRaction"){return false;}
		if (maction == "Mirsel"){return false;}
		if (maction == "None"){return false;}
		return true;}
	
	public void setSDO(int u){
		sdo = u;
		/* State Drawing Option
		 * 0 = Regular drawing 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 */
	 }
	 
	public void setInteract(boolean b){ interactflag = b;}
	 
	public void setSFO(int v){
		sfo = v;
		/* State Fill Option
		 * 0 = Regular fill 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 * 4 = Clear state
		 * 5 = Invert state
		 */
	 } 
	 
	 public void setCDO(int w){
		cdo = w;
		/* Cell Drawing Option
		 * 0 = Regular drawing 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 */
	 }
	 
	 public void setCFO(int t){
		cfo = t;
		/* Cell Fill Option
		 * 0 = Regular drawing 
		 * 1 = Checker Board pattern
		 * 2 = Randomized
		 * 3 = Randomized/Checker Board
		 */
	 }
	
	//general mode setting for display and automaton
	public void setMode(int m){
		/*Modes:
		 * 1 = Normal Running
		 * 2 = State Editing
		 * 3 = Cell Editing
		 * 4 = Multicolor
		 */
		 switch(m){
			 case 0: mode = dmode; pistons[0][0].setOpMode(dmode); outputs[0][0].setDispMode(dmode); setMouseAction("SDraw"); break;
			 case 1: mode = 1; dmode = 1; pistons[0][0].setOpMode(1); outputs[0][0].setDispMode(1); setMouseAction("SDraw"); break;
			 case 2: mode = 2; pistons[0][0].setOpMode(2); outputs[0][0].setDispMode(2); setMouseAction("SDraw"); break;
			 case 3: mode = 3; pistons[0][0].setOpMode(3); outputs[0][0].setDispMode(3); setMouseAction("CDraw"); break;
			 case 4: mode = 4; dmode = 4; pistons[0][0].setOpMode(4); outputs[0][0].setDispMode(4); setMouseAction("SDraw"); break;
			 default:  mode = dmode; pistons[0][0].setOpMode(dmode); outputs[0][0].setDispMode(dmode); setMouseAction("SDraw"); break;
		 }
		}
		
		public int getMode(){return mode;}
		

	// refreshes the selection in the display
					public void refreshSel(){
						sedna.detectSelection();
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){
							outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));
							}}
							outputs[0][0].repaint();
						}
						
							// brush setting method
					public void setEditBrush(int b){
						brushtype = b;
						switch(brushtype){
							// 1x1
						case 1: sigmund = new brush(pistons[0][0].xsiz, pistons[0][0].ysiz); break;
							//2x2
						case 2: sigmund = new twobrush(pistons[0][0].xsiz, pistons[0][0].ysiz); break;
							//3x3
						case 3: sigmund = new threebrush(pistons[0][0].xsiz,pistons[0][0].ysiz); break;
							//Glider
						case 4: sigmund = new gliderbrush(pistons[0][0].xsiz, pistons[0][0].ysiz); sigmund.setOrientation(0); break;
						
						default : sigmund = new brush(pistons[0][0].xsiz, pistons[0][0].ysiz); break;}
					}
					
					//makes the cells
				public void populate(int a, int b, int c){
					cell ed;
					if(sedna.getSelected() == false || sedna.getSelection(a,b)){
					cellOptionHandler decider;
					switch(c){
						case 1: decider = castor;break;
						case 2: decider = pollux;break;
						case 3: decider = eris; break;
						default: decider = castor;break;}
					switch (decider.getCT()){
						case 0: ed = new cell();
								break;
						case 1: ed = new offCell();
								break;
						case 2: ed = new onCell();
								break;
						case 3: ed = new blinkCell();
								ed.setInt("Mat", decider.getMaturity());
								break;
						case 4: ed = new seqCell();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								for(int g = 0; g <= 7; g++){
								ed.setBoola("Seq", g, decider.getBoola("Seq", g));
								}
								break;
						case 5: ed = new randCell();
								ed.setInt("Mat", decider.getMaturity());
								break;
						case 6: ed = new conway();
								ed.setInt("Mat", decider.getMaturity());
								break;
						case 7: ed = new seeds();
								ed.setInt("Mat", decider.getMaturity());
								break;
						case 8: ed = new parityCell();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								ed.setBool("Par", decider.getBool("Par"));
								ed.setBool("Rec", decider.getBool("Rec"));
								break;
						case 9: ed = new conveyorCell();
								ed.setInt("Mat", decider.getMaturity());
								ed.setInt("Dir", decider.getDirection());
								ed.setBool("Inv", decider.getInvert());
								break;
						case 10: ed = new wolfram();
								ed.setInt("Mat", decider.getMaturity());
								ed.setInt("Dir", decider.getDirection());
								ed.setBool("Inv", decider.getInvert());
								for(int g = 0; g <= 7; g++){
								ed.setBoola("Rule", g, decider.getBoola("Rule", g));
								}
								break;
						case 11: ed = new symmetriCell();
								ed.setInt("Mat", decider.getMaturity());
								ed.setInt("Dir", decider.getDirection());
								ed.setBool("Inv", decider.getInvert());
								break;		
						case 12: ed = new mirrorCell();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								ed.setInt("HX", decider.getInt("MirrX"));
								ed.setInt("HY", decider.getInt("MirrY"));
								break;	
						case 13: ed = new majorityCell();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								ed.setBool("Rec", decider.getBool("Rec"));
								break;
								
						case 14: ed = new gnarl();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								
								break;
								
						case 15: ed = new amoeba();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								
								break;
								
						case 16: ed = new highlife();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								
								break;
								
						case 17: ed = new prime();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								ed.setBool("Rec", decider.getBool("Rec"));
								break;
								
						case 18: ed = new dayNight();
								ed.setInt("Mat", decider.getMaturity());
								ed.setBool("Inv", decider.getInvert());
								
								break;		
								
						default: ed = new cell();
								break;}
								
								
								pistons[0][0].addCell(a,b,ed);
						outputs[0][0].setSpecies(a,b,decider.getCT());
						outputs[0][0].setLifespan(a,b,decider.getMaturity()); 
						
					}}
					
					
					// cell drawing methods
					
					//gateway method
					public void drawCell(int x, int y){
						/* Cell Drawing Option
						* 0 = Regular drawing 
						* 1 = Checker Board pattern
						* 2 = Randomized
						* 3 = Randomized/Checker Board
						*/
						if(rcflag){if(cdo == 1){ cellCheckDraw(x,y, true);}else{cellAltDraw(x,y);}}
						else{
							switch(cdo){
								case 0: cellDraw(x,y); break;
								case 1: cellCheckDraw(x,y, false); break;
								case 2: cellRandDraw(x,y); break;
								case 3: cellRCDraw(x,y); break;
								default: cellDraw(x,y); break;}}
						}
					
					public void cellDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
						
					public void cellAltDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,2);}} 
					
					public void cellCheckDraw(int x, int y, boolean b){
						if( y % 2 == 1 ^ x % 2 == 1){
							if(b){if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,2);}}
							else{if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
							}
						else{
							if(b){if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
							else{if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,2);}}
							}
						}
						
					public void cellRCDraw(int x, int y){
						if( y % 2 == 1 ^ x % 2 == 1){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,1);}}
						else{
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,3);}}
						}
						
					public void cellRandDraw(int x, int y){
						if(!sedna.getSelected() ||sedna.getSelection(x,y)){populate(x,y,3);}
					}
						
					// cell filling methods
					
					// gateway method
					public void fillCell(){
		
						switch(cfo){
							case 0: cellFill(); break;
							case 1: cellCheckFill(); break;
							case 2: cellRandFill(); break;
							case 3: cellRCFill(); break;
							default: cellFill(); break;}
							}
						
					private void cellFill(){
					for(int y=0;y<= pistons[0][0].ysiz-1;y++){
					for(int x=0;x<= pistons[0][0].xsiz-1;x++){
						cellDraw(x,y);}}
						if( mode == 3){outputs[0][0].repaint();}
						}
					
					
					private void cellCheckFill(){
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){	
							cellCheckDraw(x,y, false);
							}}
						if( mode == 3){outputs[0][0].repaint();}
						
					}
					
					private void cellRCFill(){
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){	
							cellRCDraw(x,y);
							}}
						if( mode == 3){outputs[0][0].repaint();}
						
					}
					
				
					
					private void cellRandFill(){
						for(int y=0;y<= pistons[0][0].ysiz-1;y++){
						for(int x=0;x<= pistons[0][0].xsiz-1;x++){	
							cellRandDraw(x,y);
						}}
						if( mode== 3){outputs[0][0].repaint();}
						
					}
					
					
						
						// sets the cells around the outside edge of the automaton 
						public void setBorder(){	
						for(int x = 0; x <= pistons[0][0].xsiz-1; x++){
							switch(cfo){
								case 0: cellDraw(x,0); cellDraw(x, pistons[0][0].ysiz-1); break;
								case 1: cellCheckDraw(x,0, false); cellCheckDraw(x,pistons[0][0].ysiz-1, false); break;
								case 2: cellRandDraw(x,0); cellRandDraw(x, pistons[0][0].ysiz-1); break;
								case 3: cellRCDraw(x,0); cellRCDraw(x, pistons[0][0].ysiz-1);break;
								default: cellDraw(x,0); cellDraw(x, pistons[0][0].ysiz-1); break;}}
						for(int y = 0; y<= pistons[0][0].ysiz-1; y++){
							switch(cfo){
								case 0: cellDraw(0,y); cellDraw(pistons[0][0].xsiz-1, y); break;
								case 1: cellCheckDraw(0,y, false); cellCheckDraw(pistons[0][0].xsiz-1,y, false); break;
								case 2: cellRandDraw(0,y); cellRandDraw(pistons[0][0].xsiz-1, y); break;
								case 3: cellRCDraw(0,y); cellRCDraw(pistons[0][0].xsiz-1, y); break;
								default: cellDraw(0,y); cellDraw(pistons[0][0].xsiz-1, y); break;}}
								if(mode == 3){outputs[0][0].repaint();}
								
					}
					//cell settings editing methods
					
					//fills boolean settings
					//public void boolFill(String a, boolean b){
					//	for(int y=0;y<=ysiz-1;y++){
					//	for(int x=0;x<=xsiz-1;x++){
						//	if(harry.getSelected() == false || harry.getSelection(x,y)){
								//culture[x][y].setBool(a,b);
					//	}}}
					//}
					
					//fills Int settings
					//public void intFill(String a, int b){
					//	for(int y=0;y<=ysiz-1;y++){
					//	for(int x=0;x<=xsiz-1;x++){
					//		if(harry.getSelected() == false || harry.getSelection(x,y)){
						//		culture[x][y].setInt(a,b);}}}
						//	}
								
	//State Editing Methods
	
						//state drawing methods
						
							//gateway method
				public void drawState(int x, int y){
						/* State Drawing Option
						* 0 = Regular drawing 
						* 1 = Checker Board pattern
						* 2 = Randomized
						* 3 = Randomized/Checker Board
						*/
						if(rcflag){if(sdo == 1){ stateCheckDraw(x,y, false);}else{stateAltDraw(x,y);}}
						else{
							switch(sdo){
								case 0: stateDraw(x,y); break;
								case 1: stateCheckDraw(x,y, true); break;
								case 2: stateRandDraw(x,y); break;
								case 3: stateRCDraw(x,y); break;
								default: stateDraw(x,y); break;}}
						}
					
				private void stateDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,true);}}
					
				private void stateAltDraw(int x,int y){
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,false);}}
					
				public void stateCheckDraw(int x,int y, boolean fill){
					if( y % 2 == 1 ^ x % 2 == 1){ if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,fill);}}
						else{if(fill){if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,false);}}}}
					
				private void stateRandDraw(int x, int y){
					Random foghorn = new Random();
					if(!sedna.getSelected() ||sedna.getSelection(x,y)){pistons[0][0].setCellState(x,y,foghorn.nextBoolean());}}	
					
				private void stateRCDraw(int x, int y){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}
								
				//state fill methods
				
				//gateway method
				public void fillState(){
					if(pistons[0][0].paused){stateFillSelect();}
					else{pistons[0][0].myopt = 1; pistons[0][0].sfflag = true;}
					}
				
					
					//selects the right state fill to do
					public void stateFillSelect(){
						/* State Fill Option
						* 0 = Regular fill 
						* 1 = Checker Board pattern
						* 2 = Randomized
						* 3 = Randomized/Checker Board
						* 4 = Clear state
						* 5 = Invert state
						*/
					
						switch(sfo){
							case 0: stateFill(); break;
							case 1: stateCheckFill(); break;
							case 2: stateRandFill(); break;
							case 3: stateRCFill(); break;
							case 4: stateClearFill(); break;
							case 5: stateInvert(); break;
							
							default: stateCheckFill(); break;}
						}
					
				private void stateFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateDraw(x,y);}}
						if(mode != 3){pistons[0][0].refreshState();}
					} 
						
				private void stateRandFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						stateRandDraw(x,y);
					}} 
					if( mode != 3){ pistons[0][0].refreshState();}
					}
					
				private void stateClearFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						pistons[0][0].current[x][y] = false; pistons[0][0].culture[x][y].purgeState();}}
						if(mode != 3){pistons[0][0].refreshState();}
						}
					
				private void stateCheckFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
					stateCheckDraw(x,y, true);
					}} 
					if(mode != 3){pistons[0][0].refreshState();}
					}
					
				private void stateRCFill(){
					for(int y=0;y<= ymax-1;y++){
					for(int x=0;x<= xmax-1;x++){
						if( y % 2 == 1 || x % 2 == 1){stateCheckDraw(x,y,true);}
						else{stateRandDraw(x,y);}}}
						if(mode != 3){pistons[0][0].refreshState();}
					}
				
				private void stateInvert(){
					for(int y = 0; y<= ymax-1; y++){
						for(int x=0; x<= xmax-1; x++){
							if(!sedna.getSelected() ||sedna.getSelection(x,y)){
								pistons[0][0].setCellState(x,y,!pistons[0][0].current[x][y]);}}}
					if(mode != 3){pistons[0][0].refreshState();}
				}		
				
			
						
							//sets neighborhoods for mirror cells
				//	public void mirSel(boolean a){
					//	if(mirselflag == false){mirselflag = true; hiliteflag = true; prisec = a;}
					//	else{if(prisec != a){prisec = a; mirselflag = true; hiliteflag = true;}
					//		else{mirselflag = false; hiliteflag = false;}}
					//	}

	// mouse interaction methods
					public void mouseMoved( MouseEvent e){
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						
						// hilight passed over cells
						//if(pistons[0][0].hiliteflag){if(pistons[0][0].mirselflag && mode == 3){int colsel; if(pistons[0][0].prisec){colsel = 1;}
						//else{colsel = 2;} outputs[0][0].setHiLite(xlocal, ylocal, colsel);}
						//if(pistons[0][0].singleselflag){outputs[0][0].setHiLite(xlocal,ylocal,1);}
						//if(pistons[0][0].rectselflag == 1){outputs[0][0].setHiLite(xlocal,ylocal,1);}
						 //}
						 // draws rectangle during rectangle selection
						 if(maction == "SRaction"){for(int y = 0; y <= pistons[0][0].ysiz-1; y++){
												for(int x = 0; x <= pistons[0][0].xsiz-1; x++){
													outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
							 outputs[0][0].finishRect(xlocal,ylocal); }
						 
						// hilight a mirrorCell's targets as each mirrrorCell is moused over in the editor 
						// if(pistons[0][0].mirselflag == false && pistons[0][0].merlin.getOpMode() == 3){
						//if(pistons[0][0].culture[xlocal][ylocal].hood == "Mirror"){outputs[0][0].setHiLite(
						//	pistons[0][0].culture[xlocal][ylocal].getInt("HX"),pistons[0][0].culture[xlocal][ylocal].getInt("HY"), 1);}
						//else{}	
						//}
						
						}
						
					public void mouseDragged(MouseEvent e) {
						if(e.getX() < 1){xlocal =0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if (e.getY() < 1){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
						//edit state
						if(maction == "SDraw"){
						if(mode == 2 || interactflag){
								drawState(thisx, thisy);
							}}
						
						//editcell
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
							
							// cell selection
						if(maction == "BSel"){
							outputs[0][0].setSelect(true);
							 if(rcflag){sedna.removeCell(thisx,thisy);}
							 else{sedna.selectCell(thisx,thisy);}
						}
						
					}
						
						refreshSel();
					}
					else{
						 //  rectangle selection
						 if(maction == "SRaction"){
						 for(int y = 0; y <= pistons[0][0].ysiz-1; y++){
							for(int x = 0; x <= pistons[0][0].xsiz-1; x++){
								outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
							 outputs[0][0].finishRect(xlocal,ylocal); }}
							}
							
					public void mouseEntered(MouseEvent e){}
					
					public void mouseExited(MouseEvent e){}
					public void mousePressed(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
							//edit state
						if(maction == "SDraw"){
						if(mode == 2 || interactflag){
								drawState(thisx, thisy);
							}}
						
						//editcell
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
						
						// cell selection
						if(maction == "BSel"){
							outputs[0][0].setSelect(true);
							 if(rcflag){sedna.removeCell(thisx,thisy);}
							 else{sedna.selectCell(thisx,thisy);}
						}
						
					}
					refreshSel();
					}
					else{
						
						//rectangle selection
						if(maction == "SRect"){
						sedna.startRect(xlocal,ylocal, !rcflag);
						 outputs[0][0].remHilite();outputs[0][0].beginRect(xlocal,ylocal,!rcflag); maction = "SRaction";}
						 
					 }
						}
					
					public void mouseReleased(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						//nothin' doin'
					}}
					else{
						//rectangle selection
						if(maction == "SRaction"){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= pistons[0][0].ysiz-1; y++){
							for(int x = 0; x<= pistons[0][0].xsiz-1; x++){
								outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
								outputs[0][0].repaint(); sedna.detectSelection();
								switch(mode){
									case 1: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									case 2: setMouseAction("SDraw"); break;
									case 3: setMouseAction("CDraw"); break;
									case 4: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									default: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;}
								}
							}
							
						}
						
					
					public void mouseClicked(MouseEvent e){
						if(e.getX() < 1){xlocal = 0;} else{xlocal = e.getX()/outputs[0][0].magnify;}
						if(e.getY() < 1 ){ylocal = 0;} else{ ylocal = e.getY()/outputs[0][0].magnify;}
						if (xlocal > pistons[0][0].xsiz-1){xlocal = pistons[0][0].xsiz-1;}
						if (ylocal > pistons[0][0].ysiz-1){ylocal = pistons[0][0].ysiz-1;}
						if(e.isMetaDown()){rcflag = true;}else{rcflag = false;}
						
						if(isMouseUsed()){
						sigmund.locate(xlocal, ylocal);
						int reps = sigmund.getBrushLength();
						for (int a = 0; a <= reps; a++){
						int thisx = sigmund.getNextX();
						int thisy = sigmund.getNextY();
						
						//edit state
						if(maction == "SDraw"){
						if(mode == 2 || interactflag){
								drawState(thisx, thisy);
						 }}
						 
						//edit cells
						if(maction == "CDraw"){
						if (mode == 3 ){
							drawCell(thisx, thisy);
							}
						}
							
						
						
						// cell selection
						if(maction == "BSel"){
							outputs[0][0].setSelect(true);
							if(rcflag){sedna.removeCell(thisx,thisy);}
							else{sedna.selectCell(thisx,thisy);}	
						}
					}
						
								refreshSel();
						}
						else{
						
						//rectangle selection
						if(maction == "SRect"){
							rectflag = true;
							maction = "SRaction";
							sedna.startRect(xlocal,ylocal, !rcflag);
							 outputs[0][0].remHilite();outputs[0][0].beginRect(xlocal,ylocal,!rcflag);
							  
						}
						
						if(maction == "SRaction" && rectflag == false){
						sedna.endRect(xlocal,ylocal); 
							for(int y = 0; y<= pistons[0][0].ysiz-1; y++){
							for(int x = 0; x<= pistons[0][0].xsiz-1; x++){
								outputs[0][0].setSelection(x,y,sedna.getSelection(x,y));}}
								outputs[0][0].repaint(); sedna.detectSelection();
								switch(mode){
									case 1: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									case 2: setMouseAction("SDraw"); break;
									case 3:setMouseAction("CDraw"); break;
									case 4: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									default: if(interactflag){setMouseAction("SDraw");}else{setMouseAction("None");}break;
									}
							}
						rectflag = false;
						
						//mirror selection
						if(maction == "Mirsel"){
						if(mode == 3){
							if(prisec){castor.setInt("MirrX", xlocal);castor.setInt("MirrY", ylocal);}
							else{pollux.setInt("MirrX", xlocal); pollux.setInt("MirrY", ylocal);}
							outputs[0][0].setHiLite(xlocal, ylocal, 3);setMouseAction("CDraw");
						}}
				}
					
				
		}
}
