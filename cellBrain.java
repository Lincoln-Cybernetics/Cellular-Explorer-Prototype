import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.event.*;
import java.util.*;

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

class cellBrain extends JComponent implements Runnable{ 

	//main variables
	
	logicEngine controller;
	cellComponent display;
	int myopt = 1;
	int xsiz;
	int ysiz; 
	boolean[][] current; 	
	boolean[][] newstate; 
	
	public cell[][] culture; 	
	//selector harry;
	int celltype;	
	int maturity = 1;
	// settings (speed, primary cell selection, secondary cell selection, maturity, secondary maturity, cell size, and array initialization)
	

	//general array counters Int x, and Int y are instantiated locally for each use
	
	//keep track of mouse position in edit mode
	int xlocal;
	int ylocal;
	int ztime;
	// moore neighborhood brush
	threebrush ariadne;
	//Wolfram neighborhood brush
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
	int sdopt = 0;
	boolean rcflag = false;
	//cell editing flags
	boolean editcellflag = false;
	boolean mirselflag = false;//toggles mirror selection mode
	
	// display flags
	boolean hiliteflag = false; //used to signal when to hilite a cell
	
	//automaton flags
	boolean firstflag = true;
	int opmode = 1;
	/*operation modes:
	 * 1 = normal running
	 * 2 = state editing
	 * 3 = cell editing
	 * */
	int dt = 1;//display type
	boolean xwrap = false;//edge wrapping x-axis
	boolean ywrap = false;// edge wrapping y-axis
	Thread t = new Thread(this);
	
	
	// constructors
	
	//default
	public cellBrain(){
		controller = new logicEngine();
		xsiz = 400;
		ysiz = 150;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush(xsiz, ysiz);
		ariadne.setType(true);
		andromeda = new spinbrush(xsiz, ysiz);
		andromeda.setType(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		//initialize the board
		for(int y=0;y<= ysiz-1;y++){
		for(int x=0;x<= xsiz-1;x++){
				addCell(x,y, new conway());}} 
			//display.setState(current);	
			//fireupdateEvent();
			
			}
	
	//set size		
	public cellBrain(int a, int b, logicEngine vostok){
		controller = vostok;
		xsiz = a;
		ysiz = b;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush(xsiz, ysiz);
		ariadne.setType(true);
		andromeda = new spinbrush(xsiz, ysiz);
		andromeda.setType(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		//initialize the board
		for(int y=0;y<= ysiz-1;y++){
		for(int x=0;x<= xsiz-1;x++){
				addCell(x,y, new conway());}}
			//display.setState(current);	
			//fireupdateEvent();	
			
			}
		
		// use Strings to create	
		public cellBrain(String option){
			controller = new logicEngine();
		xsiz = 400;
		ysiz = 150;
		current = new boolean[xsiz][ysiz];
		newstate = new boolean[xsiz][ysiz];
		culture = new cell[xsiz][ysiz];
		ariadne = new threebrush(xsiz, ysiz);
		ariadne.setType(true);
		andromeda = new spinbrush(xsiz, ysiz);
		andromeda.setType(true);
		setXYwrap(false,false);
		ztime = controller.getMasterSpeed();
		//initialize the board
		if (option == "Rnd"){ 	
		for(int y=0;y<= ysiz-1;y++){
		for(int x=0;x<= xsiz-1;x++){
				addCell(x,y, new randCell());}}}
		else{for(int y=0;y<= ysiz-1;y++){
		for(int x=0;x<= xsiz-1;x++){
				addCell(x,y, new conway());}} }
			}
			
		
			// sets display	object
		public void setDisplay(cellComponent bigboard){
			display = bigboard;
			display.setState(current);
			}
			// sets iteration Speed
			public void setZT(int z){
				ztime = z;}
			
			//Play/Pause	
				
			public void pP(){
				if (firstflag == true){firstflag = false;setPause(false); t.start();}
				else{ setPause(!pauseflag);}
			}
			
			//pause/unpause
				public void setPause(boolean a){
					if(opmode == 2 || opmode == 3){pauseflag = true;}
					else{pauseflag = a;}}
			
			public boolean getFirst(){
				return firstflag;}
				
				// mode setting methods
				
				//set operational mode
				public void setOpMode(int mode){
					switch(mode){
						case 1: opmode = 1; dt = 1; break;// normal running
						case 2: setPause(true); opmode = 2; break;// state editing
						case 3: setPause(true); opmode = 3; break;// cell editing
						case 4: opmode = 4; dt = 4; break;// multicolor
					}
					}
					
				//tells whether the automaton may iterate(not allowed during editing)
				public boolean mayIterate(){
					if(opmode == 2 || opmode == 3){return false;}
					else{return true;}
				}
				
				// set edge-wrapping
				public void setXYwrap(boolean xwr, boolean ywr){xwrap = xwr; ywrap = ywr; 
				 ariadne.setWrap(xwrap,ywrap); andromeda.setWrap(xwrap, ywrap);}
				
				
				//stores the type of display while editing	
				public int getDispType(){
					return dt;}
				
				//sends the current state to the display
				public void refreshState(){
					display.setState(current);}
					
					// general editing methods
					
					//selection methods
					//tells the brain a selection is being made
					public void setSelection(int a){
						switch(a){
							case 1: hiliteflag = true; singleselflag = true;
									break;
							case 2: hiliteflag = false; singleselflag = false; break;
							case 3: hiliteflag = true; 
								rectselflag = 1; break;
						}
					}
				
					
						
				
					// cell editing methods	
				public void addCell(int x, int y, cell wilbur){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					culture[x][y] = wilbur;}
				
				
					
								
								
				
				// State editing methods
				
				//changes the state array
				public void setCellState(int x, int y, boolean b){
					if(x >= xsiz){x = xsiz-1;}
					if(x <= 0){x=0;}
					if(y >= ysiz){y = ysiz-1;}
					if(y <= 0){y=0;}
					current[x][y] = b;
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
		
	
		
		//main logic methods
		// iterate the array
		public void iterate(){
			if(mayIterate()){
			int x; int y;
			 if (sfflag){ controller.iterateInterrupt(myopt);
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
								// set age for multicolor
						if(opmode == 4){display.setAge(x,y,culture[x][y].getAge());}
				
						current[x][y] = newstate[x][y]; 
						}}
					display.setState(current);	
				
				}}
			
			// runs the main thread
			public void run(){
				int x=0;
				int y=0;
				paused = false;
			
					
					
				while(true){
					//update display
					display.setState(current);
						
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
				


			
			
		
}
