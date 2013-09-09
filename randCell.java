import java.util.Random;

class randCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat"};
	static int ccount = 0;

	Random attractor = new Random();
	
	public randCell(){
		hood = "None";
		maturity = 1;}
		
	public void setInt(String a, int b){
		if(a == "Mat"){ maturity = b;}
	}
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}

	
	protected boolean calculate(){
		return attractor.nextBoolean();}
}

class conway extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat"};
	static int ccount = 0;
	public conway(){
		neighbors = new boolean[3][3];
		hood = "Moore";
		maturity = 1;}
		
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
	}
	
	public void setBool( String a, boolean b){
		if(a == "Inv"){ invert = b;}
		if(a == "Rec"){ recursive = b;}
	}
	
		// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}

	
	protected boolean calculate(){
		cellstate = 0;
		for(int y = 0; y<=2; y++){
			for(int x = 0; x<=2; x++){
				if (x == 1 && y == 1){if (recursive){if(neighbors[x][y]){cellstate += 1;}}}
				else {if(neighbors[x][y] == true){cellstate += 1;}}
				}}
				if(cellstate == 3){return true;}
				if(cellstate == 2){ return self;}
				return false;
			}
		}
		
class seeds extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat"};
	static int ccount = 0;
		public seeds(){
			neighbors = new boolean[3][3];
			hood = "Moore";
			maturity = 1;
		}
		
		public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
	}
		public void setBool( String a, boolean b){
			if(a == "Inv"){ invert = b;}
			if(a == "Rec"){ recursive = b;}
		}
		
		// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}
		
		protected boolean calculate(){
			cellstate = 0;
		for(int y = 0; y<=2; y++){
			for(int x = 0; x<=2; x++){
				if (x == 1 && y == 1){if (recursive){if(neighbors[x][y]){cellstate += 1;}}}
				else {if(neighbors[x][y] == true){cellstate += 1;}}
				}}
				if(cellstate == 2 && self == false){ return true;}
				else{return false;}
			}
		}

class parityCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv"};
	static int ccount = 0;
	// parameter
	// even parity = false, odd parity = true
	boolean parity;
	
	// default:  non-recursive, even parity
	public parityCell(){
		neighbors = new boolean[3][3];
		recursive = false;
		parity = false;
		hood = "Moore";
		maturity = 1;
	}
	
	public void setBool( String a, boolean b){
		if(a == "Parity"){parity = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Inv"){invert = b;}
	}
	
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
	}
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}
	
	protected boolean calculate(){
		cellstate = 0;
		for(int y=0; y<=2; y++){
			for(int x = 0; x<= 2; x++){
				if(x==1 && y==1){if (recursive == true){ if(neighbors[1][1]){cellstate += 1;}}}
				else{ if(neighbors[x][y]){cellstate += 1;}}
			}}
			if(parity && cellstate%2 == 1){return true;}
			if(!parity && cellstate%2 == 0){return true;}
			else{return false;}
		}
	}
	
class conveyorCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Dir", "Inv"};
	static int ccount = 0;
	public conveyorCell(){
		neighbors = new boolean[3][3];
		direction = 0;
		hood = "Moore";
		maturity = 0;
	}
		// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}
		
	public void setInt( String a, int b){
		if(a == "Mat"){ maturity = b;}
		if(a == "Dir"){ direction = b;}
	}
	
	public void setBool( String a, boolean b){
		if(a == "Inv"){invert = b;}
	}
	
	protected boolean calculate(){
				switch(direction){
					case 0: if (neighbors[1][0]){return true;} break;
					case 1: if (neighbors[2][0]){return true;} break;
					case 2: if (neighbors[2][1]){return true;} break;
					case 3: if (neighbors[2][2]){return true;} break;
					case 4: if (neighbors[1][2]){return true;} break;
					case 5: if (neighbors[0][2]){return true;} break;
					case 6: if (neighbors[0][1]){return true;} break;
					case 7: if (neighbors[0][0]){return true;} break;
				}
				return false;}
	}
		
	
