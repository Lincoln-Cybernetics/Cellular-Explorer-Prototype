
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
class wolfram extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Dir", "Rule"};
	static int ccount = 0;
	//parameter for which Wolfram rule to apply
	boolean[] rule = new boolean[8];//{false,true,true,true,false,true,true,false};
	
	public wolfram(){
		neighbors = new boolean[3][1];
		hood = "Wolfram";
		maturity = 1;
		direction = 6;}
	
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
		if (a == "Dir"){direction = b; setDirHood();}
		if(a == "FadeRate"){faderate = b;}
		}
		
	public void setBool( String a, boolean b){
		if (a == "Inv"){invert = b;}
		if(a == "Fade"){fade = b;}
	} 
		
	public void setBoola( String a, int v, boolean b){
		if (a == "Rule"){
			
			rule[v] = b;}
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
	return rule[cellstate];
	

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
		if(a == "FadeRate"){faderate = b;}
	}
	
	public void setBool( String a, boolean b){
		if(a == "Inv"){invert = b;}
		if(a == "Fade"){fade = b;}
	}
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
	}
	
	protected boolean calculate(){
		int astate = 0; int bstate = 0; boolean state = false;
			// check for horizontal symmetry
			if (direction == 4 || direction == 0 || direction == 8 || direction == 9){
				if (neighbors[0][0]){astate += 1;} if (neighbors[2][0]){bstate += 1;}
				if (neighbors[0][1]){astate += 2;} if (neighbors[2][1]){bstate += 2;}
				if (neighbors[0][2]){astate += 4;} if (neighbors[2][2]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){state = true; return state;}}
				else{if (direction == 9){return false;}}}
				astate = 0; bstate = 0;
				
			// check for Upper Left- Lower Right symmetry	
			if (direction == 5 || direction == 1 || direction == 8 || direction == 9){
				if (neighbors[1][0]){astate += 1;} if (neighbors[2][1]){bstate += 1;}
				if (neighbors[0][0]){astate += 2;} if (neighbors[2][2]){bstate += 2;}
				if (neighbors[0][1]){astate += 4;} if (neighbors[1][2]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){state = true; return state;}}
				else{if (direction == 9){return false;}}}
				astate = 0; bstate = 0;
				
			// check for vertical symmetry	
			if (direction == 6 || direction == 2 || direction == 8 || direction == 9){
				if (neighbors[0][0]){astate += 1;} if (neighbors[0][2]){bstate += 1;}
				if (neighbors[1][0]){astate += 2;} if (neighbors[1][2]){bstate += 2;}
				if (neighbors[2][0]){astate += 4;} if (neighbors[2][2]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){state = true; return state;}}
				else{if (direction == 9){return false;}}}
				astate = 0; bstate = 0;

			// check for Lower Left- Upper Right symmetry
			if (direction == 3 || direction == 7 || direction == 8 || direction == 9){
				if (neighbors[0][1]){astate += 1;} if (neighbors[1][0]){bstate += 1;}
				if (neighbors[0][2]){astate += 2;} if (neighbors[2][0]){bstate += 2;}
				if (neighbors[1][2]){astate += 4;} if (neighbors[2][1]){bstate += 4;}
				if (astate == bstate){ if (direction != 9){state = true; return state;}}
				else{if (direction == 9){return false;}}}
				if(direction == 9){state = true;}
				return state;}
	}
	
class mirrorCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv", "Mir"};
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
		if(a == "FadeRate"){ faderate = b;}
	
		
	}
	
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
		if(a == "Fade"){fade = b;}
	}
	
	public int getInt(String a){
		if(a == "HX"){ return hoodX;}
		if(a == "HY"){ return hoodY;}
		if(a == "Age"){return age;}
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
		if(invert){return !neighbors[0][0];}else{ return neighbors[0][0];}}
	
}

class majorityCell extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv", "Rec"};
	static int ccount = 0;
	boolean invert;
	boolean recursive;
	boolean self;
	
	public majorityCell(){
		neighbors = new boolean[3][3];
		hood = "Moore";
		maturity = 1;
		invert = false;
		recursive = true;
	}
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
		}
		
	public void setInt(String a, int b){
		if(a == "Mat"){maturity = b;}
		if(a == "FadeRate"){faderate = b;}	
	}
	
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Fade"){fade = b;}
	}
	
	public int getInt(String a){
		if(a == "Mat"){ return maturity;}
		if(a == "Age"){return age;}
		else{ return counter;}
	}
	
	public boolean getBool( String a){
		if(a == "Inv"){return invert;}
		if(a == "Rec"){return recursive;}
		else{return self;}
	}
	
	protected boolean calculate(){
		int ons = 0; int offs = 0; active = false;
		for(int y = 0; y <= 2; y++){
			for(int x = 0; x <= 2; x++){
				if(x == 1 && y == 1){ if(recursive){if(neighbors[x][y]){ons += 1;}else{offs += 1;}}}
				else{if(neighbors[x][y]){ons += 1;}else{offs += 1;}}
			}}
			if(ons > offs){ return true;}
			if(offs > ons){ return false;}
			if(ons == offs){ return self;}
			return self;}		
}

class gnarl extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv", "Rec"};
	static int ccount = 0;
	
	public gnarl(){
		neighbors = new boolean[3][3];
		hood = "Moore";
		maturity = 1;
		invert = false;
		recursive = true;
	}
	
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
		}
		
	public void setInt(String a, int b){
		if(a == "Mat"){maturity = b;}
		if(a == "FadeRate"){faderate = b;}	
	}
	
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Fade"){fade = b;}
	}
	
	public int getInt(String a){
		if(a == "Mat"){ return maturity;}
		if(a == "Age"){return age;}
		else{ return counter;}
	}
	
	public boolean getBool( String a){
		if(a == "Inv"){return invert;}
		if(a == "Rec"){return recursive;}
		else{return self;}
	}	
	
	protected boolean calculate(){
		int cellstate = 0;
			for(int y = 0; y <=2; y++){
				for(int x = 0; x <= 2; x++){
					if(x == 1 && y == 1){if(recursive){if(neighbors[x][y]){cellstate += 1;}}}
					else{if(neighbors[x][y]){cellstate += 1;}}
				}}
			if(cellstate == 1){return true;}
			else{return false;}
						
		}
		
}


class amoeba extends cell{
	// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat", "Inv", "Rec"};
	static int ccount = 0;
	
	public amoeba(){
		neighbors = new boolean[3][3];
		hood = "Moore";
		maturity = 1;
		invert = false;
		recursive = true;
	}
	
	
	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
		}
		
	public void setInt(String a, int b){
		if(a == "Mat"){maturity = b;}
		if(a == "FadeRate"){faderate = b;}	
	}
	
	public void setBool(String a, boolean b){
		if(a == "Inv"){invert = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Fade"){fade = b;}
	}
	
	public int getInt(String a){
		if(a == "Mat"){ return maturity;}
		if(a == "Age"){return age;}
		else{ return counter;}
	}
	
	public boolean getBool( String a){
		if(a == "Inv"){return invert;}
		if(a == "Rec"){return recursive;}
		else{return self;}
	}	
	
	protected boolean calculate(){
		int cellstate = 0;
		boolean stim;
			for(int y = 0; y <=2; y++){
				for(int x = 0; x <= 2; x++){
					if(x == 1 && y == 1){if(recursive){if(neighbors[x][y]){cellstate += 1;}}}
					else{if(neighbors[x][y]){cellstate += 1;}}
				}}
			switch(cellstate){
			case 0: stim = false; break;
			case 1: if(self){stim = true;}else{stim = false;} break;
			case 2: stim = false; break;
			case 3: stim = true; break;
			case 4: stim = false; break;
			case 5: stim = true; break;
			case 6: stim = false; break;
			case 7: if(self == false){stim = true;}else{stim = false;}break;
			case 8: if(self){stim = true;}else{stim = false;} break;
			default: stim = false; break;
			}
			return stim;			
		}
		
}



class highlife extends cell{

// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat","Inv", "Rec"};
	static int ccount = 0;

public highlife(){
	neighbors = new boolean[3][3];
	hood = "Moore";
	maturity = 1;
	direction = 0;
	invert = false;
	recursive = false;	
	}


	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
		}

	// set parameters
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
		if (a == "Dir"){direction = b;}
		if(a == "FadeRate"){faderate = b;}
		}
		
	public void setBool( String a, boolean b){
		if (a == "Inv"){invert = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Fade"){fade = b;}
	} 

	protected boolean calculate(){
	int cellstate = 0;
			for(int y = 0; y <=2; y++){
				for(int x = 0; x <= 2; x++){
					if(x == 1 && y == 1){if(recursive){if(neighbors[x][y]){cellstate += 1;}}}
					else{if(neighbors[x][y]){cellstate += 1;}}
				}}
			if(self){if(cellstate == 2 || cellstate == 3){return true;}else {return false;}}
			else{if(cellstate == 3 || cellstate == 6){return true;}else{ return false;}}
	}

}



class prime extends cell{

// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat","Inv", "Rec"};
	static int ccount = 0;

public prime(){
	neighbors = new boolean[3][3];
	hood = "Moore";
	maturity = 1;
	direction = 0;
	invert = false;
	recursive = false;	
	}


	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
		}

	// set parameters
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
		if (a == "Dir"){direction = b;}
		if(a == "FadeRate"){faderate = b;}
		}
		
	public void setBool( String a, boolean b){
		if (a == "Inv"){invert = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Fade"){fade = b;}
	} 

	protected boolean calculate(){
	int cellstate = 0;
			for(int y = 0; y <=2; y++){
				for(int x = 0; x <= 2; x++){
					if(x == 1 && y == 1){if(recursive){if(neighbors[x][y]){cellstate += 1;}}}
					else{if(neighbors[x][y]){cellstate += 1;}}
				}}
		
			switch(cellstate){
				case 1: return false; 
				case 2: return true; 
				case 3: return true; 
				case 4: return false; 
				case 5: return true; 
				case 6: return false; 
				case 7: return true; 
				case 8: return false; 
				case 9: return  false;}
				return false;
				
			}
}



class dayNight extends cell{

// array that determines what parameters can be set for this type of cell
	static String[] controls = new String[]{"Mat","Inv", "Rec"};
	static int ccount = 0;

public dayNight(){
	neighbors = new boolean[3][3];
	hood = "Moore";
	maturity = 1;
	direction = 0;
	invert = false;
	recursive = false;	
	}


	// get settable parameters
	public static String getControl(){
		return controls[ccount];}
	
	public static void incControl(){
		ccount +=1;
		if(ccount == controls.length){ccount = 0;}
		}

	// set parameters
	public void setInt( String a, int b){
		if (a == "Mat"){maturity = b;}
		
		if(a == "FadeRate"){faderate = b;}
		}
		
	public void setBool( String a, boolean b){
		if (a == "Inv"){invert = b;}
		if(a == "Rec"){recursive = b;}
		if(a == "Fade"){fade = b;}
	} 

	protected boolean calculate(){
	int cellstate = 0;
			for(int y = 0; y <=2; y++){
				for(int x = 0; x <= 2; x++){
					if(x == 1 && y == 1){if(recursive){if(neighbors[x][y]){cellstate += 1;}}}
					else{if(neighbors[x][y]){cellstate += 1;}}
				}}
			switch(cellstate){
				case 3: return true;
				case 4: if(self == false){ return true;}else{return false;}
				case 6: return true;
				case 7: return true;
				case 8: return true;
				default: return false;
			}
	}

}

