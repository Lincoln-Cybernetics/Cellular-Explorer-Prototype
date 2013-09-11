import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.event.*;

public class cellComponent extends JComponent 
{
	int xdim;
	int ydim;
	int magnify;
	int mode = 1;
	boolean[][] cstate;
	int[][] species;
	int[][] lifespan;
	int[][] orientation;
	int hlx = 0;
	int hly = 0;
	int hlc = 0;
	boolean hiliteflag = false;
	
	public cellComponent(){
		xdim = 500;
		ydim = 300;
		magnify = 5;
		cstate = new boolean[xdim][ydim];
		species = new int[xdim][ydim];
		lifespan = new int[xdim][ydim];
		orientation = new int[xdim][ydim];
		setPreferredSize(new Dimension(xdim*magnify, ydim*magnify));
	}
	
	public cellComponent(int x, int y){
		xdim = x;
		ydim = y;
		magnify = 5;
		cstate = new boolean[xdim][ydim];
		species = new int[xdim][ydim];
		lifespan = new int[xdim][ydim];
		orientation = new int[xdim][ydim];
		setPreferredSize(new Dimension(xdim*magnify, ydim*magnify));

	}
	// set variables
	public void setState(boolean update[][]){
		cstate = update;
		repaint();}
		
	public void setMode(int a){
		mode = a; repaint();}
		
	public void setMag(int a){
		magnify = a; repaint();}
		
	public void setSpecies(int a, int b, int c){
		species[a][b] = c;}
		
	public void setLifespan(int a, int b, int c){
		lifespan[a][b] = c;}

//get variables

	public int getMode(){
		return mode;}

//hilights a cell
	public void setHiLite(int a, int b, int c){
		hiliteflag = true; hlx = a; hly = b; hlc = c; repaint();}

//main paint method
public void paintComponent( Graphics g){
					int x = 0;
					int y = 0;
					int schmagnify;
					
					
					for(y=0;y<=ydim-1;y++){
						for(x=0;x<=xdim-1;x++){
						
							//normal rendering
							if (mode == 1){
								g.setColor(cstate[x][y] ? Color.green : Color.black);
								g.fillRect(x*magnify,y*magnify,magnify,magnify);}
						
							//state editing
							if (mode == 2){
								if(magnify>4){schmagnify = magnify-1;}
								else{schmagnify = magnify;}
								g.setColor(cstate[x][y] ? Color.green : Color.black);
								g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);}
								
							// cell editing
							if(mode == 3){
								if(magnify>4){schmagnify = magnify-1;}
								else{schmagnify = magnify;}
								switch(species[x][y]){
									case 0: g.setColor(Color.gray);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 1: g.setColor(Color.black);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 2: g.setColor(Color.white);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);break;
									case 3: g.setColor(Color.red);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 4: g.setColor(Color.blue);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 5: g.setColor(Color.orange);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 6: g.setColor(Color.green);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 7: g.setColor(Color.cyan);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 8: g.setColor(Color.yellow);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 9: g.setColor(Color.pink);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
									case 10: g.setColor(Color.gray);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);
											 g.setColor(Color.black);g.fillRect(x*magnify+2,y*magnify+2,schmagnify-2,schmagnify-2); break;
									case 11: g.setColor(Color.gray);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);
											 g.setColor(Color.white);g.fillRect(x*magnify+2,y*magnify+2,schmagnify-2,schmagnify-2); break;
									case 12: g.setColor(Color.gray);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify);
											 g.setColor(Color.red);g.fillRect(x*magnify+2,y*magnify+2,schmagnify-2,schmagnify-2); break;
									
									default: g.setColor(Color.black);g.fillRect(x*magnify,y*magnify,schmagnify,schmagnify); break;
								}
								//outline each cell according to its maturity setting
								switch(lifespan[x][y]){
									case 1: g.setColor(Color.white); break;
									case 2: g.setColor(Color.red); break;
									case 3: g.setColor(Color.blue); break;
									case 4: g.setColor(Color.black); break;
									default: g.setColor(Color.green); break;
								}
								g.drawRect(x*magnify,y*magnify,magnify,magnify);
								
								// hilite a hilited cell
								if(hiliteflag){
									if(x == hlx && y == hly){
								switch(hlc){
									case 1: g.setColor(Color.red); break;
									case 2: g.setColor(Color.blue); break;
									default: g.setColor(Color.red); break;}
								g.drawRect(x*magnify, y*magnify, magnify, magnify);
									}}
									
								}
								
							
								
						}}
						}
}
