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
	int[][] maturity = new int[xsiz][ysiz];
	int ztime = 2;
	//general array counters
	int x;
	int y;
	//keep track of mouse position in edit mode
	int xlocal;
	int ylocal;
	//changestate flag
	boolean csflag = false; 
	boolean fillflag = false;
	boolean randflag = false;
	boolean clearflag = false;
	boolean pauseflag = false;
	boolean editflag = false;
	boolean hiliteflag = false;
	boolean editcellflag = false;
	boolean firstrunflag = true;
	
	int workcell = 0;
	int magnify = 5;
	int demoflag = 0;
	
	Thread t = new Thread(this);
	
	public CellComponent(){
		addMouseMotionListener(this);
		addMouseListener(this);
		//initialize the board
	for(y=0;y<=ysiz-1;y++){
		for(x=0;x<=xsiz-1;x++){	
				 current[x][y] = false;
				 newstate[x][y] = false;
				 celltype[x][y] = 5;
				 maturity[x][y] = 1;
				 populate(x,y);
			 }}
				
				
			repaint();
			}
			
			
			
			 public boolean begin(){
				t.start(); return true;}
				
				public void populate(int a, int b){
					switch (celltype[a][b]){
						case 0: culture[a][b] = new Cell(maturity[a][b]);
						break;
						case 1: culture[a][b] = new onCell(maturity[a][b]);
						break;
						case 2: culture[a][b] = new blinkCell(maturity[a][b]);
						break;
						case 3: culture[a][b] = new blinkCell2(maturity[a][b]);
						break;
						case 4: culture[a][b] = new Randcell(maturity[a][b]);
						break;
						case 5: culture[a][b] = new Conway(maturity[a][b]);
						break;
						case 6: culture[a][b] = new Seeds(maturity[a][b]);
						break;
						default: culture[a][b] = new Cell(maturity[a][b]);
						break;}
					}
			
			
			
			public void run(){
				int x=0;
				int y=0;
				Random zibzob = new Random();
				//initialize the cell array for demos
				if (firstrunflag == true){
					 for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
					switch(demoflag){
					 case 0://normal
					celltype[x][y] =5;
					populate(x,y);
					break;
					
					case 1: // glider bomb
				  if(x==250 && y<195 &&y>5){celltype[x][y] =2;}
				  else{if (x<100 && x>50){celltype[x][y] = 6;}
				  else{celltype[x][y] = 5;}} populate(x,y);
				  break;
				  
					case 2:// assorted
					if (x<100 && y>100){celltype[x][y] = 6;}
					else{celltype[x][y] = 5;}
					if(x==xsiz-1){celltype[x][y] = 1;}
					if (y==0){celltype[x][y] = 2;}
					if (y==ysiz-1){celltype[x][y] = 4;}
				  populate(x,y); break;
				 
				default:
				celltype[x][y] = 1;
				populate(x,y); break;}}}
				firstrunflag = false;}
							
					
					
				while(true){
					//pauses the program
					try{	while (pauseflag ==true){
			  Thread.sleep(100);} 
			   }  catch(InterruptedException ie) {}
			   
			   // fills the current state array
			   if (fillflag == true){ 
				   for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
							if (randflag == true){current[x][y] = zibzob.nextBoolean();}
							if(clearflag == true){current[x][y] = false;}
						}}fillflag = false;randflag = false;clearflag = false; }
	
			   // get neighboring states for the cells
					boolean[][] neighbors = new boolean[3][3];
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
				
						//init neighbors WITHOUT resetting x and y
					neighbors[0][0]=false;neighbors[0][1]=false;neighbors[0][2]=false;
					neighbors[1][0]=false;neighbors[1][1]=current[x][y];neighbors[1][2]=false;
					neighbors[2][0]=false;neighbors[2][1]=false;neighbors[2][2]=false;
						
						
						// if the cell is on the border ignore outside, otherwise test neighbors
							if(x==0 || y==0){neighbors[0][0] = false;}
							else{ if(current[x-1][y-1]== true) neighbors[0][0]=true;}
							
							if(x==0){neighbors[0][1] = false;}
							else{if(current[x-1][y] == true) neighbors[0][1] = true;}
							
							if(x==0 || y==ysiz-1){neighbors[0][2] = false;}
							else{if(current[x-1][y+1] == true) neighbors[0][2] = true;}
							
							if(y==0){neighbors[1][0] = false;}
							else{if(current[x][y-1]==true) neighbors[1][0] = true;}
							
							if(y==ysiz-1){neighbors[1][2] = false;}
							else{if(current[x][y+1]==true) neighbors[1][2] = true;}
							
							if(x==xsiz-1 || y==0){neighbors[2][0] = false;}
							else{if(current[x+1][y-1]== true) neighbors[2][0] = true;}
							
							if(x==xsiz-1){neighbors[2][1] = false;}
							else{if(current[x+1][y]==true) neighbors[2][1] = true;}
							
							if(x==xsiz-1 || y==ysiz-1){neighbors[2][2] = false;}
							else{if(current[x+1][y+1] == true) neighbors[2][2] = true;}
						
							//gets new values from the cells
							newstate[x][y] =culture[x][y].iterate(neighbors);
						
						
						
				}}
				// cycles new values into current state
				for(y=0;y<=ysiz-1;y++){
					for(x=0;x<=xsiz-1;x++){
						current[x][y] = newstate[x][y];}}
						//update display
						repaint();
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
					if (editflag == true|| editcellflag == true){schmagnify = magnify-1;}
					else{schmagnify = magnify;}
					
					for(y=0;y<=ysiz-1;y++){
						for(x=0;x<=xsiz-1;x++){
							
							// normal color setting
							g.setColor(current[x][y] ? Color.green : Color.black);
							// cell editing colors
							if(editcellflag == true){
								switch(celltype[x][y]){
									case 0: g.setColor(Color.black); break;
									case 1: g.setColor(Color.white); break;
									case 2: g.setColor(Color.red); break;
									case 3: g.setColor(Color.blue); break;
									case 4: g.setColor(Color.orange); break;
									case 5: g.setColor(Color.green); break;
									case 6: g.setColor(Color.cyan); break;
									default: g.setColor(Color.black); break;
								}}
							g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);
							//if (hiliteflag == true && x==xlocal && y == ylocal){
							//g.setColor(Color.red);	g.drawRect(x*magnify,y*magnify,magnify,magnify);}
							
						}}
						}
					public void mouseMoved( MouseEvent e){
					//if(editflag == true || editcellflag == true){hiliteflag = true;
					//xlocal = e.getX()/magnify; ylocal = e.getY()/magnify;repaint();}
					//else{hiliteflag = false;repaint();}}
				}
					public void mouseDragged(MouseEvent e) {
						//edit state
						if(editflag == true){xlocal = e.getX()/magnify; ylocal = e.getY()/magnify;
						current[xlocal][ylocal] = !current[xlocal][ylocal]; repaint();}
						//editcell
						if (editcellflag == true){
					xlocal = e.getX()/magnify; ylocal = e.getY()/magnify; celltype[xlocal][ylocal] = workcell; 
					populate(xlocal, ylocal);repaint();}}
					public void mouseEntered(MouseEvent e){}
					public void mouseExited(MouseEvent e){}
					public void mousePressed(MouseEvent e){}
					public void mouseReleased(MouseEvent e){}
					public void mouseClicked(MouseEvent e){
						xlocal = e.getX()/magnify; ylocal = e.getY()/magnify;
						//edit state
						if(editflag == true){
						current[xlocal][ylocal] = !current[xlocal][ylocal];
						repaint();}
						//edit celltype
						if(editcellflag == true){
						celltype[xlocal][ylocal] = workcell;
						populate(xlocal,ylocal); repaint();}
						}
					
				
		}
