
public class cell{
	// main calculation variables
	boolean active;
	int cellstate;
	int counter;
	boolean[][] neighbors;
	boolean self;
	boolean recursive;
	boolean invert;
	int age = 0;
	// parameter variables
	int maturity;
	int direction;
	String hood;
	boolean fade = false;
	int faderate = 255;
	// array that determines what parameters can be set for this type of cell
	// order:  maturity, direction, mirrorX, mirrorY, invert
	static String[] controls = new String[]{"None"};
	static int ccount = 0;
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
	
	// get settable parameters
	
	
	public int getMat(){return maturity;}
	
	public int getDir(){return direction;}
	
	public int getInt( String a ){return counter;}
	
	public int getAge(){return age;}
	
	public boolean getBool( String a){return self;}
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];
		}
		
	public static  void incControl(){
		ccount += 1;
		if(ccount == controls.length){ccount = 0;}
	}
	// get neighborhood
	public String getNeighborhood(){ return hood;}
	
	// set parameters
	public void setBool( String a, boolean b){
		if(a == "Fade"){fade = b;}}
	public void setBoola( String a, int v, boolean b){}
	public void setInt( String a, int b){
		if(a == "FadeRate"){faderate = b;}}
	
	
		
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
		
	//clears the state of the cell
	public void purgeState(){
		active = false;
		age = 0;
	}
		
	//iterate
	public boolean iterate(){
		counter+= 1;
		if(counter == maturity){counter = 0;if(invert){active = !calculate();}else{active = calculate();}}
		else{active = self;}
		if(active){age += 1;} 
		if(active == false){age = 0;}
		if(fade){if(age >= faderate){age = 0; active = false;}}
		if(age >= 2000000000){age = 0;}
		return active;}
		
	// if maturity is reached, calculate the new state
	protected boolean calculate(){
		return self;}
}

class offCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"None"};
	//determines which cells' states are in this cell's neighborhood
	
	public offCell(){
		hood = "None";
		maturity = 1;
		self = false;
		}
	
	protected boolean calculate(){
		return false;}
}

class onCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"None"};
	public onCell(){
		hood = "None";
		maturity = 1;
		self = true;
		}
			
	protected boolean calculate(){
		return true;}
}

class blinkCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat"};
	public blinkCell(){
		hood = "None";
		maturity = 1;
	}
	
	public void setBool(String a, boolean b){
		if(a == "Self"){self = b;}
		if(a == "Fade"){fade = b;}
	}
	
	public void setInt(String a, int b){
		if(a == "Mat"){ maturity = b;}
		if(a == "FadeRate"){ faderate = b;}
	}
	// get settable parameters
	public static String getControl(){
		return controls[ccount];
		}
		

	//logic	
	protected boolean calculate(){ if(active){return false;}else{return true;}}
	
}

class seqCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv", "Rule"};
	static int ccount = 0;
	//the array holds the sequence of states, the counter indexes the array
	boolean[] seq;
	//int seqlen;
	int seqcounter;
	
	// constructs the cell with a default sequence length of 8
	public seqCell(){
		seqcounter = 0;
		hood = "Self";
		neighbors = new boolean[1][1];
		seq = new boolean[8];
		//seqlen = 8;
		maturity = 1;}
	
	// defines the length of the state sequence in the constructor	
	public seqCell(int a){
		seqcounter = 0;
		hood = "Self";
		neighbors = new boolean[1][1];
		seq = new boolean[a];
		//seqlen = a;
		maturity = 1;}

	// define the sequence its self
	public void setBoola(String a, int v, boolean b){
		if(a == "Seq"){seq[v] = b;}
	}
	// set ints
	public void setInt( String a, int b){
		if(a == "Mat"){ maturity = b;}
		if(a == "FadeRate"){faderate = b;}
	}
	// set invert
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
		if(a == "Fade"){fade = b;}
	}	
		
	// get settable parameters
	public static String getControl(){
		return controls[ccount];
		}
	public static  void incControl(){
		ccount += 1;
		if(ccount == controls.length){ccount = 0;}
	}
		

		
	protected boolean calculate(){
		boolean temp = seq[seqcounter];
		seqcounter += 1;
		if(seqcounter == seq.length){seqcounter = 0;}
		return temp;}
		
}
