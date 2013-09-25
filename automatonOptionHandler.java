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
//automaton rules
boolean fadeflag = false;
// brush options	
int brush = 1;
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
// cell control options
int cellcount = 14;
int controlcount = 6;
String[] conts = new String[]{"Mat","Dir","Inv", "Mir", "Par", "Rec"};
boolean[][] cellopts = new boolean[cellcount][controlcount];
// display options
int dispopt;

public automatonOptionHandler(){
	setCellOpts();
}
//option setting methods

//automaton
public void setWrap(String a, boolean b){
	if (a == "X"){if(b){xwrap = true;}else{xwrap = false;}}
	if (a == "Y"){if(b){ywrap = true;}else{ywrap = false;}}
}

public void setZT(int a){
	ztime = a;}

// drawing brush
public void setBrush(int a){
	brush = a;}
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
	if(a == "Check"){if(b){sdcheck = true;}else{sdcheck = false;}}
	if(a == "Rand"){if(b){sdrand = true;}else{sdrand = false;}}
	if(a == "Interactive"){if(b){interactive = true;}else{interactive = false;}}
}
//state fill
public void setSFO(String a, boolean b){
	if(a == "Check"){if(b){sfcheck = true;}else{sfcheck = false;}}
	if(a == "Rand"){if(b){sfrand = true;}else{sfrand = false;}}
}

// cell control options
public void setCellOpts(){
	for (int a = 0; a < controlcount; a++){
		if(cell.getControl() == conts[a]){cellopts[0][a] = true; cell.incControl();}else{cellopts[0][a] = false;}
		if(offCell.getControl() == conts[a]){cellopts[1][a] = true; offCell.incControl();}else{cellopts[1][a] = false;}
		if(onCell.getControl() == conts[a]){cellopts[2][a] = true; onCell.incControl();}else{cellopts[2][a] = false;}
		if(blinkCell.getControl() == conts[a]){cellopts[3][a] = true; blinkCell.incControl();}else{cellopts[3][a] = false;}
		if(seqCell.getControl() == conts[a]){cellopts[4][a] = true; seqCell.incControl();}else{cellopts[4][a] = false;}
		if(randCell.getControl() == conts[a]){cellopts[5][a] = true; randCell.incControl();}else{cellopts[5][a] = false;}
		if(conway.getControl() == conts[a]){cellopts[6][a] = true; conway.incControl();}else{cellopts[6][a] = false;}
		if(seeds.getControl() == conts[a]){cellopts[7][a] = true; seeds.incControl();}else{cellopts[7][a] = false;}
		if(parityCell.getControl() == conts[a]){cellopts[8][a] = true; parityCell.incControl();}else{cellopts[8][a] = false;}
		if(conveyorCell.getControl() == conts[a]){cellopts[9][a] = true; conveyorCell.incControl();}else{cellopts[9][a] = false;}
		if(wolfram.getControl() == conts[a]){cellopts[10][a] = true; wolfram.incControl();}else{cellopts[10][a] = false;}
		if(symmetriCell.getControl() == conts[a]){cellopts[11][a] = true; symmetriCell.incControl();}else{cellopts[11][a] = false;}
		if(mirrorCell.getControl() == conts[a]){cellopts[12][a] = true; mirrorCell.incControl();}else{cellopts[12][a] = false;}
		if(majorityCell.getControl() == conts[a]){cellopts[13][a] = true; majorityCell.incControl();}else{cellopts[13][a] = false;}
	}
}

// set general boolean options
public void setBool(String a, boolean b){
	if(a == "Fade"){fadeflag = b;}
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

// brush
public int getBrush(){
	return brush;}
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
	
public boolean getInter(){
	return interactive;}

//state fill
public boolean getSFO(String a){
	if(a == "Check"){return sfcheck;}
	if(a == "Rand"){return sfrand;}
	return false;
	}

//cell controll options
public boolean getCellOpt(int a, int b){
	return cellopts[a][b];
}

// display options
public int getDisp(){
	return dispopt;}
	
// get general booleans
public boolean getBool(String a){
	if(a == "Fade"){return fadeflag;}
	return false;
}
}
