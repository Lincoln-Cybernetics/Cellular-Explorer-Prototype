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

public int getMaturity(){ return maturity;}

public int getDirection(){ return direction;}

public boolean getInvert(){ return inver;}

public int getInt(String a){
	if(a == "MirrX"){return mirrorx;}
	if(a == "MirrY"){return mirrory;}
	return 0;
}

public boolean getBool(String a){
	if (a == "Par"){return parity;}
	if (a == "Rec"){return recursive;}
	return false;
}

public boolean getBoola(String a, int h){
	if(a == "Seq"){return sequence[h];}
	if(a == "Rule"){return rule[h];}
	return rule[h];}

}

class randcellOptionHandler extends cellOptionHandler{
	int xsiz = 1;
	int ysiz = 1;
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
	public int getCT(){ return shovel.nextInt(19);}
	public int getMaturity(){int ranmat =  shovel.nextInt(4); ranmat +=1; return ranmat;}
	public int getDirection(){return shovel.nextInt(8);}
	public boolean getInvert(){return shovel.nextBoolean();}
	public boolean getBool( String a){return shovel.nextBoolean();}
	public int getInt(String a){
		if(a == "MirrX"){return shovel.nextInt(xsiz);}
		if(a == "MirrY"){return shovel.nextInt(ysiz);}
		return 0;}
}
