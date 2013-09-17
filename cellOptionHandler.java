import java.util.Random;

public class cellOptionHandler{

int celltype = 0;
int maturity = 1;
int direction = 0;
boolean inver = false;
boolean parity = false;
boolean recursive = false;
int mirrorx = 0;
int mirrory = 0;
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
	public int getCT(){ return shovel.nextInt(14);}
	public int getMaturity(){int ranmat =  shovel.nextInt(4); ranmat +=1; return ranmat;}
	public int getDirection(){return shovel.nextInt(8);}
	public boolean getInvert(){return shovel.nextBoolean();}
	public boolean getBool( String a){return shovel.nextBoolean();}
	public int getInt(String a){
		if(a == "MirrX"){return shovel.nextInt(xsiz);}
		if(a == "MirrY"){return shovel.nextInt(ysiz);}
		return 0;}
}
