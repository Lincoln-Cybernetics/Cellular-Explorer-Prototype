
public class automatonOptionHandler{

//automaton options
boolean xwrap = false;
boolean ywrap = false;
int ztime = 2000;
// brush options	
String brush = "Normal";
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

// drawing brush
public void setBrush(String a){
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
public String getBrush(){
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

}
