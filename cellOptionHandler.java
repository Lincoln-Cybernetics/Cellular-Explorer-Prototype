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
public class cellOptionHandler implements ucListener{
cellPicker source;
int celltype = 0;
String mbotname = "Custom";
int maturity = 1;
int direction = 0;
boolean inver = false;
boolean parity = false;
boolean recursive = false;
int mirrorx = 0;
int mirrory = 0;
boolean[] rule = new boolean[8];
boolean[] bornon = new boolean[9];
boolean[] surv = new boolean[9];
boolean[] sequence = new boolean[8];
boolean doesage = false;
boolean doesfade = false;
int fadenum = 0;
public cellOptionHandler(){
}

//sets the source for the cell info
public void setCP( cellPicker lorenzo){
	source = lorenzo;}

//handles events from the cell info source
public void handleControl( ucEvent e){
	switch(e.getCommand()){
		case 1: setCT(source.getCT()); break;
		case 2: setMBOT(source.getMBOT()); break;
	}
	}

//option setting methods
private void setCT(int a){
	celltype = a;}
	
private void setMBOT(String nameo){ mbotname = nameo;}	
	


public void setInt(String a, int b){
	if(a == "MirrX"){ mirrorx = b;}
	if(a == "MirrY"){ mirrory = b;}
}

public void setBool(String a, boolean b){
	if(a == "Ages"){ doesage = b;}
	if(a == "Fades"){ doesfade = b; fadenum = 256;} 
	if(a == "B0"){ bornon[0] = b;}
	if(a == "B1"){ bornon[1] = b;}
	if(a == "B2"){ bornon[2] = b;}
	if(a == "B3"){ bornon[3] = b;}
	if(a == "B4"){ bornon[4] = b;}
	if(a == "B5"){ bornon[5] = b;}
	if(a == "B6"){ bornon[6] = b;}
	if(a == "B7"){ bornon[7] = b;}
	if(a == "B8"){ bornon[8] = b;}
	if(a == "S0"){ surv[0] = b;}
	if(a == "S1"){ surv[1] = b;}
	if(a == "S2"){ surv[2] = b;}
	if(a == "S3"){ surv[3] = b;}
	if(a == "S4"){ surv[4] = b;}
	if(a == "S5"){ surv[5] = b;}
	if(a == "S6"){ surv[6] = b;}
	if(a == "S7"){ surv[7] = b;}
	if(a == "S8"){ surv[8] = b;}
}

public void setBoola( String a, boolean[] b){
	if(a == "Seq"){sequence = b;}
	if(a == "Rule"){rule = b;}
}

//option getting methods
public int getCT(){ return celltype;}

public cell getCell(){ cell marduk = generateCell(); return marduk;}


public cell generateCell(){
	cell tiamat;
	// makes the right type of cell
	switch(celltype){
		case 0: tiamat = new cell();break;
		case 1: if(mbotname == "Custom"){tiamat = new mbot(); tiamat = setRules(tiamat);}else{tiamat = new mbot(mbotname);}break;
		default: tiamat = new cell();break;}
		// set options and parameters
		if(tiamat.getControls("Age")){ tiamat.setOption("Ages", doesage);}
		if(tiamat.getControls("Fade")){ tiamat.setOption("Fades", doesfade); tiamat.setParameter("Fade", fadenum);}
 return tiamat;
}

private cell setRules(cell isis){
	// sets custom rules for MBOT cells
	if(isis.getName() == "M.B.O.T."){ for(int n = 0; n<9; n++){ 
		String bstr = "B0"; String sstr = "S0"; 
		switch(n){
			case 0: bstr = "B0"; sstr = "S0"; break;
			case 1: bstr = "B1"; sstr = "S1"; break;
			case 2: bstr = "B2"; sstr = "S2"; break;
			case 3: bstr = "B3"; sstr = "S3"; break;
			case 4: bstr = "B4"; sstr = "S4"; break;
			case 5: bstr = "B5"; sstr = "S5"; break;
			case 6: bstr = "B6"; sstr = "S6"; break;
			case 7: bstr = "B7"; sstr = "S7"; break;
			case 8: bstr = "B8"; sstr = "S8"; break;
		}
	isis.setOption(bstr, bornon[n]);
	isis.setOption(sstr, surv[n]);}
									}
								
		return isis;
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
	public cell generateCell(){
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
