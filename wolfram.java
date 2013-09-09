

class wolfram extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Dir"};
	static int ccount = 0;
	//parameter for which Wolfram rule to apply
	boolean[] rule = new boolean[]{false,true,true,true,false,true,true,false};
	
	public wolfram(){
		neighbors = new boolean[3][1];
		hood = "Wolfram";
		maturity = 1;
		direction = 6;}
	
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
		if (a == "Dir"){direction = b; setDirHood();}
		}
		
	public void setBool( String a, boolean b){
		if (a == "Inv"){invert = b;}
	} 
		
	public void setBoola( String a, boolean b[]){
		if (a == "Rule"){for(int c = 0; c<=7; c++){
			rule[c] = b[c];}}
		}
	protected void setDirHood(){
		if(direction == 0 || direction == 4){hood = "WolframV";}
		if(direction == 1 || direction == 5){hood = "WolframLL";}
		if(direction == 2 || direction == 6){hood = "Wolfram";}
		if(direction == 3 || direction == 7){hood = "WolframUL";}
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
		 //assign a value to cellstate based on the cell's neighbors
		if(neighbors[0][0]){if(direction > 0 && direction < 5){cellstate += 4;} else{cellstate += 1;}} 
		if(neighbors[1][0]){cellstate += 2;}
		if(neighbors[2][0]){if(direction > 0 && direction < 5){cellstate += 1;} else{cellstate += 4;}}
	
	// determine the on/off state of the cell
	active = rule[cellstate];
	return active;

}
}

class symmetriCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Dir", "Inv"};
	static int ccount = 0;
	public symmetriCell(){
		neighbors = new boolean[3][3];
		direction = 0;
		maturity = 1;
		hood = "Moore";
	}
	
	public void setInt( String a, int b){
		if(a == "Mat"){maturity = b;}
		if(a == "Dir"){direction = b;}
		// additional direction options for this cell include
		// direction = 8: returns true if any symmetry is present
		// direction = 9: returns true if all symmetries are present
	}
	
	public void setBool( String a, boolean b){
		if(a == "Inv"){invert = b;}
	}
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}
	
	protected boolean calculate(){
		int astate = 0; int bstate = 0; active = false;
			// check for horizontal symmetry
			if (direction == 4 || direction == 0 || direction == 8 || direction == 9){
				if (neighbors[0][0]){astate += 1;} if (neighbors[2][0]){bstate += 1;}
				if (neighbors[0][1]){astate += 2;} if (neighbors[2][1]){bstate += 2;}
				if (neighbors[0][2]){astate += 4;} if (neighbors[2][2]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){active = true; return active;}}
				else{if (direction == 9){return false;}}}
				astate = 0; bstate = 0;
				
			// check for Upper Left- Lower Right symmetry	
			if (direction == 5 || direction == 1 || direction == 8 || direction == 9){
				if (neighbors[1][0]){astate += 1;} if (neighbors[2][1]){bstate += 1;}
				if (neighbors[0][0]){astate += 2;} if (neighbors[2][2]){bstate += 2;}
				if (neighbors[0][1]){astate += 4;} if (neighbors[1][2]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){active = true; return active;}}
				else{if (direction == 9){return false;}}}
				astate = 0; bstate = 0;
				
			// check for vertical symmetry	
			if (direction == 6 || direction == 2 || direction == 8 || direction == 9){
				if (neighbors[0][0]){astate += 1;} if (neighbors[0][2]){bstate += 1;}
				if (neighbors[1][0]){astate += 2;} if (neighbors[1][2]){bstate += 2;}
				if (neighbors[2][0]){astate += 4;} if (neighbors[2][2]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){active = true; return active;}}
				else{if (direction == 9){return false;}}}
				astate = 0; bstate = 0;

			// check for Lower Left- Upper Right symmetry
			if (direction == 3 || direction == 7 || direction == 8 || direction == 9){
				if (neighbors[0][1]){astate += 1;} if (neighbors[1][0]){bstate += 1;}
				if (neighbors[0][2]){astate += 2;} if (neighbors[2][0]){bstate += 2;}
				if (neighbors[1][2]){astate += 4;} if (neighbors[2][1]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){active = true; return active;}}
				else{if (direction == 9){return false;}}}
				if(direction == 9){active = true;}
				return active;}
	}
	
class mirrorCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv"};
	static int ccount = 0;
	int hoodX;
	int hoodY;
	boolean invert;
	
	public mirrorCell(){
		hood = "Mirror";
		direction = 0;
		maturity = 1;
		hoodX = 0;
		hoodY = 0;
		invert = false;
		}
		
	public mirrorCell(int a, int b){
		hood = "Mirror";
		direction = 0;
		maturity = 1;
		hoodX = a;
		hoodY = b;
		invert = false;
	}
		
	public void setInt(String a, int b){
		if(a == "Mat"){maturity = b;}
		if(a == "HX"){hoodX = b;}
		if(a == "HY"){hoodY = b;}
	}
	
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
	}
	
	public int getInt(String a){
		if(a == "HX"){ return hoodX;}
		if(a == "HY"){ return hoodY;}
		else{ return counter;}
	}
	
	public boolean getBool( String a){
		if(a == "Inv"){return invert;}
		else{return self;}
	}
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}
	
	protected boolean calculate(){
		 return neighbors[0][0];}
	
}
