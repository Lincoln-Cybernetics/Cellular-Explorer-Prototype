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
public class cellOptionHandler{

int celltype = 0;
String mbotname = "2x2";
int maturity = 1;
int direction = 0;
boolean inver = false;
boolean parity = false;
boolean recursive = false;
int mirrorx = 0;
int mirrory = 0;
boolean[] rule = new boolean[8];
boolean[] sequence = new boolean[8];
public cellOptionHandler(){
}

//option setting methods
public void setCT(int a){
	celltype = a;}
	
public void setMBOT(String nameo){ mbotname = nameo;}	
	
public void setMaturity(int a){
	maturity = a;}
	
public void setDirection(int a){
	direction = a;}

public void setInvert(boolean a){
	inver = a;}

public void setInt(String a, int b){
	if(a == "MirrX"){ mirrorx = b;}
	if(a == "MirrY"){ mirrory = b;}
}

public void setBool(String a, boolean b){
	if(a == "Par"){parity = b;}
	if(a == "Rec"){recursive = b;}
}

public void setBoola( String a, boolean[] b){
	if(a == "Seq"){sequence = b;}
	if(a == "Rule"){rule = b;}
}

//option getting methods
public int getCT(){ return celltype;}

public cell getCell(){ cell marduk = generateCell(); return marduk;}


private cell generateCell(){
	cell tiamat;
	switch(celltype){
		case 0: tiamat = new cell();break;
		case 1: tiamat = new mbot(mbotname);break;
		default: tiamat = new cell();break;}
 return tiamat;
}

}

class randcellOptionHandler extends cellOptionHandler{
	int xsiz = 1;
	int ysiz = 1;
	String[] MBOTCell = new String[]{"2x2", "3/4 Life", "Amoeba", "Assimilation", "Coagulations", "Coral", "Day and Night", "Diamoeba", "Dot Life",
"Dry Life", "Fredkin", "Gnarl", "High Life", "Life", "Life without Death", "Live Free or Die", "Long Life", "Maze", "Mazectric",
"Move", "Pseudo-life", "Replicator", "Seeds", "Serviettes", "Stains", "Vote", "Vote 4/5", "Walled Cities"};
	Random shovel = new Random();
	//can not set parameters
	public void setCT(int a){}
	public void setMaturity(int a){}
	public void setDirection(int a){}
	public void setInvert(boolean a){}
	public void setInt( String a, int b){
		if(a == "Xsiz"){xsiz = b;}
		if(a == "Ysiz"){ysiz = b;}
	}
		
	// generate random cells
	private cell generateCell(){
	cell tiamat;
	switch(celltype){
		case 0: tiamat = new cell();break;
		case 1: tiamat = new mbot(mbotname);break;
		default: tiamat = new cell();break;}
		return tiamat;
		}
		
	public int getCT(){ return celltype;}
	
	public cell getCell(){celltype = shovel.nextInt(2); mbotname = MBOTCell[shovel.nextInt(MBOTCell.length)];
		cell marduk = generateCell(); return marduk;}
	
	public int getMaturity(){int ranmat =  shovel.nextInt(4); ranmat +=1; return ranmat;}
	public int getDirection(){return shovel.nextInt(8);}
	public boolean getInvert(){return shovel.nextBoolean();}
	public boolean getBool( String a){return shovel.nextBoolean();}
	public int getInt(String a){
		if(a == "MirrX"){return shovel.nextInt(xsiz);}
		if(a == "MirrY"){return shovel.nextInt(ysiz);}
		return 0;}
	public boolean getBoola(String a, int h){
	return shovel.nextBoolean();}

}
