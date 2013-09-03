import java.util.Random;

public class cellOptionHandler{

int celltype = 0;
int maturity = 1;
int direction = 0;
boolean inver = false;
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


//option getting methods
public int getCT(){ return celltype;}

public int getMaturity(){ return maturity;}

public int getDirection(){ return direction;}

public boolean getInvert(){ return inver;}


}

class randcellOptionHandler extends cellOptionHandler{
	Random shovel = new Random();
	//can not set parameters
	public void setCT(int a){}
	public void setMaturity(int a){}
	public void setDirection(int a){}
	public void setInvert(boolean a){}
	// generate random cells
	public int getCT(){ return shovel.nextInt(13);}
	public int getMaturity(){int ranmat =  shovel.nextInt(4); ranmat +=1; return ranmat;}
	public int getDirection(){return shovel.nextInt(8);}
	public boolean getInvert(){return shovel.nextBoolean();}
}
