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
public class automatonOptionHandler{

//automaton options
boolean xwrap = false;
boolean ywrap = false;
int ztime = 2000;
int opMode;
//automaton rules
boolean fadeflag = false;
//mouse actions
String maction = "SDraw";
// brush options	
int Brush = 1;
int brushor = 0;
// cell drawing options
int cdo;
boolean cdcheck = false;
boolean cdrand = false;
// cell fill options
int cfo;
boolean cfcheck = false;
boolean cfrand = false;
boolean cfborder = false;
// state drawing options
int sdo;
boolean interactive = false;
boolean sdcheck = false;
boolean sdrand = false;
// state fill options
int sfo;
boolean sfcheck = false;
boolean sfrand = false;
// display options
int dispopt = 1;
int disptype = 1;

public automatonOptionHandler(){

}
//option setting methods

//automaton
public void setWrap(String a, boolean b){
	if (a == "X"){if(b){xwrap = true;}else{xwrap = false;}}
	if (a == "Y"){if(b){ywrap = true;}else{ywrap = false;}}
}

public void setZT(int a){
	ztime = a;}
	
// mouse action
public void setMAction(String a){
	maction = a;
	/*mouse actions:
	 * "SSel" select single cell
	 * "CDraw" cell draw
	 * "SDraw" state draw
	 * "SRect" select rectangle
	 * "Mirsel" select mirror cell target
	 * "None" none
	 */ }
	 
// does the current mouse action use a brush?
public boolean isMouseUsed(){
	if (maction == "SSel"){return true;}
	if (maction == "CDraw"){return true;}
	if (maction == "SDraw"){return true;}
	if (maction == "SRect"){return false;}
	if (maction == "Mirsel"){return false;}
	if (maction == "None"){return false;}
	return false;
}
	

// drawing brush
public void setBrush(int a){
	Brush = a;
	/*brushes
	 * 1 = 1x1
	 * 2 = 2x2
	 * 3 = 3x3
	 * 4 = glider brush
	 * */}
	 
public void setBrushDir(int a){
	brushor = a;
}
	
//celldrawing	
public void setCDO(String a, boolean b){
	if(a == "Check"){if(b){cdcheck = true;}else{cdcheck = false;}}
	if(a == "Rand"){if(b){cdrand = true;}else{cdrand = false;}}
	}
//cell fill	
public void setCFO(String a, boolean b){
	if(a == "Check"){if(b){cfcheck = true;}else{cfcheck = false;}}
	if(a == "Rand"){if(b){cfrand = true;}else{cfrand = false;}}
	if(a == "Border"){if(b){cfborder = true;}else{cfborder = false;}}
}	
//state drawing	
public void setSDO(String a, boolean b){
	if(a == "Check"){sdcheck = b;}
	if(a == "Rand"){sdrand = b;}
	if(a == "Interactive"){interactive = b;}
}
//state fill
public void setSFO(String a, boolean b){
	if(a == "Check"){if(b){sfcheck = true;}else{sfcheck = false;}}
	if(a == "Rand"){if(b){sfrand = true;}else{sfrand = false;}}
}



// set general boolean options
public void setBool(String a, boolean b){
	if(a == "Fade"){fadeflag = b;}
}

// set general Ints
public void setInt(String s, int n){
	if(s == "opM"){opMode = n;
	switch(n){
		case 1: setMAction("SDraw"); setDisp(1); disptype = 1; break;// normal running
		case 2: setMAction("SDraw"); setDisp(2); break;// state editing
		case 3: setMAction("CDraw"); setDisp(3); break;// cell editing
		case 4: setMAction("SDraw"); setDisp(4); disptype = 4;break;// multicolor mode
	}
	}
}

// set display options
public void setDisp(int a){
	dispopt = a;}
		

		
//option getting methods
// automaton
public boolean getWrap(String a){
	if(a == "X"){return xwrap;}
	if(a == "Y"){return ywrap;}
	return false;
}

public int getZT(){
	return ztime;}
	
//get mouse action
public String getMAction(){
	return maction;}

// brush
public int getBrush(){
	return Brush;}
	
public int getBrushDir(){
	return brushor;}
	
//cell drawing
public boolean getCDO(String a){
	if(a == "Check"){return cdcheck;}
	if(a == "Rand"){return cdrand;}
	return false;
	}
//cell fill
public boolean getCFO(String a){
	if(a == "Check"){return cfcheck;}
	if(a == "Rand"){return cfrand;}
	return false;
	}
//state drawing
public boolean getSDO(String a){
	if(a == "Check"){return sdcheck;}
	if(a == "Rand"){return sdrand;}
	return false;
	}
//interactive	
public boolean getInter(){
	return interactive;}

//state fill
public boolean getSFO(String a){
	if(a == "Check"){return sfcheck;}
	if(a == "Rand"){return sfrand;}
	return false;
	}



// display options
//display mode
public int getDisp(){
	return dispopt;}
// type of rendering
public int getDispType(){
	/* Display types
	 * 1 = normal
	 * 4 = multicolor
	 * */
	return disptype;}
	
// get booleans
public boolean getBool(String a){
	if(a == "Fade"){return fadeflag;}
	return false;
}

// get Ints
public int getInt(String s){
	if (s == "opM"){return opMode;}
	return 0;}
	
//get operating mode
public int getOpMode(){
	return opMode;}
	
//checks for situations where the automaton shouldn't iterate
public boolean mayIterate(){
	if(opMode == 3 || opMode == 2){return false;}
	else{return true;}}

}
