
public class cell{
	// main calculation variables
	boolean active;
	int cellstate;
	int counter;
	boolean[][] neighbors;
	boolean self;
	boolean recursive;
	boolean invert;
	// parameter variables
	int maturity;
	int direction;
	String hood;
	
	//constructors
	public cell(){
		hood = "Self";
		maturity = 1;
		invert = false;
		neighbors = new boolean[1][1];
	}
	
	public cell(int a){
		hood = "Self";
		maturity = 1;
		invert = false;
		neighbors = new boolean[1][1];
	}
	
	public cell(int a, int b){
		hood = "Self";
		maturity = 1;
		invert = false;
		neighbors = new boolean[1][1];
	}
	
	// get parameters
	public String getNeighborhood(){ return hood;}
	
	public int getMat(){return maturity;}
	
	public int getDir(){return direction;}
	
	public int getInt( String a ){return counter;}
	
	public boolean getBool( String a){return self;}
	
	// set parameters
	public void setBool( String a, boolean b){}
	public void setBoola( String a, boolean b[]){}
	public void setInt( String a, int b){}
	
	
		
	// set neighbors
	public void setNeighbors(boolean nhood[][]){ 
		if (hood == "None"){self = nhood[0][0];}
		
		if (hood == "Self"){
		neighbors[0][0] = nhood[0][0]; self = nhood[0][0];}
		
		if (hood == "Moore"){ 
			for(int y = 0; y <= 2; y++){
			for(int x = 0; x <= 2; x++){
				neighbors[x][y] = nhood[x][y];}}
				self = neighbors[1][1];}
				
		if(hood == "Wolfram" || hood == "WolframV" || hood == "WolframLL" || hood == "WolframUL"){
			neighbors[0][0] = nhood[0][0]; neighbors[1][0] = nhood[1][0]; neighbors[2][0] = nhood[2][0];
			self = nhood[1][0];}
			
		if(hood == "Mirror"){neighbors[0][0] = nhood[0][0];}
		}
		
	//iterate
	public boolean iterate(){
		counter+= 1;
		if(counter == maturity){counter = 0;if(invert){active = !calculate();}else{active = calculate();}}
		else{active = self;}
		return active;}
		
	// if maturity is reached, calculate the new state
	protected boolean calculate(){
		return self;}
}

class offCell extends cell{
	public offCell(){
		hood = "None";
		maturity = 1;
		self = false;
		}
	protected boolean calculate(){
		return false;}
}

class onCell extends cell{
	public onCell(){
		hood = "None";
		maturity = 1;
		self = true;
		}
	protected boolean calculate(){
		return true;}
}

class blinkCell extends cell{
	public blinkCell(){
		hood = "None";
		maturity = 1;
	}
	
	public void setBool(String a, boolean b){
		if(a == "Self"){self = b;}
	}
	
	public void setInt(String a, int b){
		if(a == "Mat"){ maturity = b;}
	}
		
	protected boolean calculate(){ return !self;}
	
}

class seqCell extends cell{
	//the array holds the sequence of states, the counter indexes the array
	boolean[] seq;
	int seqlen;
	int seqcounter;
	
	// constructs the cell with a default sequence length of 8
	public seqCell(){
		seqcounter = 0;
		hood = "Self";
		neighbors = new boolean[1][1];
		seq = new boolean[8];
		seqlen = 8;
		maturity = 1;}
	
	// defines the length of the state sequence in the constructor	
	public seqCell(int a){
		seqcounter = 0;
		hood = "Self";
		neighbors = new boolean[1][1];
		seq = new boolean[a];
		seqlen = a;
		maturity = 1;}

	// define the sequence its self
	public void setBoola(String a, boolean b[]){
		for (int c = 0; c<=seqlen-1; c++){
			if(b.length<= c){
		seq[c] = b[c];}}
	}
	// set maturity	
	public void setInt( String a, int b){
		if(a == "Mat"){ maturity = b;}
	}
	// set invert
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
	}	
		
	protected boolean calculate(){
		boolean temp = seq[seqcounter];
		seqcounter += 1;
		if(seqcounter == seq.length){seqcounter = 0;}
		return temp;}
		
}
