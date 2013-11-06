import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Checkbox.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

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


class controlComponent extends JComponent implements ActionListener, ChangeListener, ItemListener{
// all the controls	
JButton newc;
JButton ss;
JButton step;

// state editing 
JButton eds;
JButton stf;
JButton clear;
JButton invstate;

// cell editing 
JButton celledit;
JButton edc;
JButton cf;
JButton cellborder;

//cellediting parameters
int celleditoption =1;

// menus
JFrame ceframe;
cellMenuComponent cedit;
boolean ceflag = false;
JFrame selframe;
cellMenuComponent selcomp;


// other buttons
JButton about;

// Brush selection

JRadioButton oneby = new JRadioButton("1x1", true);
JRadioButton twoby = new JRadioButton("2x2", false);
JRadioButton threeby = new JRadioButton("3x3", false);
JRadioButton gliderb = new JRadioButton("Glider", false);
ButtonGroup brushes = new ButtonGroup();

// selection tools
JButton selwin;
JButton selall;
JButton selcel;
JButton desel;
JButton invsel;
Checkbox hidsel = new Checkbox("Hide Selection");
JButton rectsel;

// cell editing
Checkbox ccdcd = new Checkbox("Check");
Checkbox ccdcf = new Checkbox("Check");
Checkbox rndcd = new Checkbox("Rand");
Checkbox rndcf = new Checkbox("Rand");

// state editing
Checkbox interact = new Checkbox("Interactive");
Checkbox ccd = new Checkbox("Checkerboard");
Checkbox rnd = new Checkbox("Random");

 // cell type selection
String[] cells = new String[]{"Cell", "offCell", "onCell", "BlinkCell", "sequenceCell",
"randomCell", "Life", "Seeds", "ParityCell", "Conveyor", "Wolfram","Symmetrical","Mirror",
 "Majority", "Gnarl", "Amoeba", "HighLife", "Prime", "Day and Night"};
SpinnerListModel modelA = new SpinnerListModel(cells);
JSpinner cellpicker;
SpinnerListModel modelAA = new SpinnerListModel(cells);
JSpinner Bcellpicker;

//cell options
//cell maturity selection
String[] mats = new String[]{"1","2","3","4", "8", "16", "32"};
SpinnerListModel modelC = new SpinnerListModel(mats);
JSpinner matpicker;
SpinnerListModel modelCC = new SpinnerListModel(mats);
JSpinner Bmatpicker;

//inversion control (true to invert)
Checkbox invA;
Checkbox invB;

// direction selection
String[] dirs = new String[] {"DN", "LL","L","UL","UP","UR","R","LR"};
SpinnerListModel dirselA = new SpinnerListModel(dirs);
JSpinner condirA;
SpinnerListModel dirselB = new SpinnerListModel(dirs);
JSpinner condirB;

// mirror selector
JButton mirsel;
JButton mirselB;

//parity control (false = even, true = odd)
Checkbox parA;
Checkbox parB;

// recursive setting
Checkbox recA;
Checkbox recB;

//rule/sequence checkboxes
Checkbox[] juno = new Checkbox[8];
boolean[] rulea = new boolean[8];
String arrayname = "Seq";
Checkbox[] hera = new Checkbox[8];
boolean[] ruleb = new boolean[8];
String arraynameb = "Seq";

// array of cell options
Component[] cellcontrolA; 
Component[] cellcontrolB; 

//automaton rules
//speed control
String[] speed = new String[]{"Very Slow", "Slow", "Fast", "Very Fast"};
SpinnerListModel modelB = new SpinnerListModel(speed);
JSpinner scon = new JSpinner(modelB);

//wrap around
Checkbox hwrap = new Checkbox("Wrap-X");
Checkbox vwrap = new Checkbox("Wrap-Y");

//"Fade" rule
JButton fade = new JButton("Fade");
JButton unfade = new JButton("Un-Fade");

// multicolor mode
Checkbox multiC = new Checkbox("Multicolor");

cellBrain tray;


boolean firstflag;
boolean pflag;
boolean selflag = false;
boolean cflag = false;
int xx = 0;
int yy = 0;



public controlComponent(){
		
		firstflag = true;
		pflag = false;
			
	ss = new JButton("Play/Pause");
	newc = new JButton("New Culture");
	step = new JButton("Step");
	eds= new JButton("Edit State");
	stf = new JButton ("State Fill");
	invstate = new JButton("Invert State");
	celledit = new JButton("Cell Editor");
	clear = new JButton("Clear");
	selwin = new JButton("Selection menu");
	about = new JButton("About");
	setLayout( new FlowLayout() );

	
	//add controls
	add( newc );
	add(ss);
	add(scon);
	add(step);
	add(eds);
	add(stf);
	add(invstate);
	add(celledit);
	add(clear);
	add(rnd);
	add(ccd);
	add(oneby);
	add(twoby);
	add(threeby);
	add(gliderb);
	add(selwin);
	add(interact);
	add(hwrap);
	add(vwrap);
	add(fade);
	add(unfade);
	add(multiC);
	add(about);
	
	brushes.add(oneby);
	brushes.add(twoby);
	brushes.add(threeby);
	brushes.add(gliderb);
	
	scon.addChangeListener(this);
	ss.addActionListener(this);
	step.addActionListener(this);
	newc.addActionListener(this);
	eds.addActionListener(this);
	stf.addActionListener(this);
	invstate.addActionListener(this);
	celledit.addActionListener(this);
	clear.addActionListener(this);
	rnd.addItemListener(this);
	ccd.addItemListener(this);
	oneby.addActionListener(this);
	twoby.addActionListener(this);
	threeby.addActionListener(this);
	gliderb.addActionListener(this);
	selwin.addActionListener(this);
	interact.addItemListener(this);
	hwrap.addItemListener(this);
	vwrap.addItemListener(this);
	fade.addActionListener(this);
	unfade.addActionListener(this);
	multiC.addItemListener(this);
	about.addActionListener(this);
	
	}

public void actionPerformed(ActionEvent e){
	
	// "New Culture button makes a new automaton
	if(e.getSource() == newc){
		//creates a new automaton
		if(!cflag){
		tray = new cellBrain();
		cflag = true; 
		firstflag = true;//setWC();setWCB();setMat();setMatB();
		setZT();}
	}
	
	
	if(cflag){
	
	
	//Play/Pause button starts/ stops the main thread
	if(e.getSource() == ss){ if(firstflag == true){ boolean nonsense;
	nonsense = tray.begin(); firstflag = false;}
	else{ if (pflag == false){ pflag = true;tray.setPause(true);}
		else{ pflag = false; tray.setPause(false); if(tray.editflag == true){tray.setEdit(false);
		} if(tray.editcellflag == true){tray.setEdit(false);}
		}}}
		
	//step button	iterates the automaton once
	if(e.getSource() == step){ if(tray.paused){tray.iterate();}}
	
	//Clear button clears the state
	if(e.getSource() == clear){ if(tray.paused){tray.stateClearFill();}
	else{tray.sfflag = true; tray.sfopt = 2;}
	}
	
	//brush selection
	//basic 1x1
	if(e.getSource() == oneby){ tray.merlin.setBrush(1); tray.setEditBrush();}
	// 2x2 brush
	if(e.getSource() == twoby){ tray.merlin.setBrush(2); tray.setEditBrush();}
	//3x3 brush
	if(e.getSource() == threeby){ tray.merlin.setBrush(3); tray.setEditBrush();}
	//Glider brush
	if(e.getSource() == gliderb){ tray.merlin.setBrush(4); tray.setEditBrush();}
	
	// selection tools
	
	// selection menu
	if(e.getSource() == selwin){if(selframe == null){makeMenu("Sel");}else{selframe.setVisible(true);}}
	
	// select all
	if(e.getSource() == selall){selflag = true;tray.harry.selectAll(); tray.refreshSel();}
	// select a cell
	if(e.getSource() == selcel){if(selflag == false){tray.setSelection(1);selflag = true;tray.merlin.setMAction("SSel");selcel.setText("Selection done");}
	else{tray.setSelection(2);selflag = false;selcel.setText("Select Cells");tray.bigboard.remHilite();if(tray.bigboard.getMode() == 3){tray.merlin.setMAction("CDraw");}
	else{tray.merlin.setMAction("SDraw");}}}
	
	//deselect
	if(e.getSource() == desel){selflag = false;tray.harry.deselect();tray.refreshSel();}
	
	//invert selection
	if(e.getSource() == invsel){tray.harry.invertSel();tray.refreshSel();}
	
	//select rectangle
	if(e.getSource() == rectsel){tray.setSelection(3); tray.merlin.setMAction("SRect");}
	
	//rules
	// Fade
	if(e.getSource() == fade){tray.boolFill("Fade",true);}
	
	//unFade
	if(e.getSource() == unfade){tray.boolFill("Fade",false);}
	
	// Edit State button goes to/from state editing mode
	if(e.getSource() == eds){ if (tray.editflag == false){ pflag = true; tray.setPause(true);
	tray.setEdit(true);tray.merlin.setMAction("SDraw");}
	else{tray.setEdit(false);}}
	
	// State Fill button set the on/off state of all cells
	if(e.getSource() == stf){int option = 1; if(ccd.getState()){option = 2;} if(rnd.getState()){option = 3;} 
	if(ccd.getState() && rnd.getState()){option = 4;}
			switch(option){
				case 1: if(tray.paused){tray.stateFill();}else{ tray.sfflag = true; tray.sfopt = 1;} break;
				case 2: if(tray.paused){tray.stateCheckFill();}else{tray.sfflag =true; tray.sfopt =3;} break;
				case 3: if(tray.paused){tray.stateRandFill();}else{tray.sfflag = true; tray.sfopt = 4;} break;
				case 4: if(tray.paused){tray.stateRCFill();}else{tray.sfflag = true; tray.sfopt = 8;} break;
				default:if(tray.paused){tray.stateCheckFill();}else{ tray.sfflag = true; tray.sfopt = 3;} break;}}
				
	//state inversion button
	if(e.getSource() == invstate){
		if(tray.paused){tray.stateInvert();}else{ tray.sfflag = true; tray.sfopt = 7;}
	}
		
	//open the cell editor menu
	if(e.getSource() == celledit){if(cedit == null){makeMenu("CE");}else{ceframe.setVisible(true);}}
		
	// Edit Cell button goes to/from cell editing mode	
	if(e.getSource() == edc){ 
	if(tray.editcellflag == false){pflag = true;
	tray.setPause(true); tray.setCellEdit(true);tray.merlin.setMAction("CDraw");}
		else{tray.setCellEdit(false);tray.merlin.setMAction("SDraw");}}	
		
	//Cell Fill button sets all cells
	if(e.getSource() == cf){celleditoption = 1; 
		if(tray.merlin.getCFO("Check")){celleditoption = 2;} 
		if (tray.merlin.getCFO("Rand")){ celleditoption = 3;} 
		if(tray.merlin.getCFO("Rand") && tray.merlin.getCFO("Check")){celleditoption = 4;} 
		
			switch (celleditoption){
				case 1: tray.cellFill(); break;
				case 2: tray.cellCheckFill(); break;
				case 3: tray.cellRandFill(); break;
				case 4: tray.cellRCFill(); break;
				default: tray.cellFill(); break; }}
	
	// the Set Border button sets the cells at the edge of the automaton
	if(e.getSource() == cellborder){celleditoption = 1; if(tray.merlin.getCFO("Check")){celleditoption = 3;} 
		if (tray.merlin.getCFO("Rand")){ celleditoption = 4;}
		if(tray.merlin.getCFO("Check") && tray.merlin.getCFO("Rand")){ celleditoption = 5;}
		tray.setBorder(celleditoption);}
		
	// mirror cell neighborhood selectors
	if(e.getSource() == mirsel){
	tray.mirSel(true);tray.merlin.setMAction("Mirsel");
	}
	if(e.getSource() == mirselB){
		tray.mirSel(false);tray.merlin.setMAction("Mirsel");
	}
}
	
	
	if(e.getSource() == about){
		JFrame cpanel = new JFrame("About");
		String noticea = "Cellular Explorer Prototype v. 0.0.2\nPowered by Lincoln Cybernetics.\nLincolnCybernetics.com\n";
  String noticeb ="Copyright(C) 02013 Matt Ahlschwede\n\n";
  String noticec = " This program is free software: you can redistribute it and/or\nmodify";
  String noticed ="  it under the terms of the GNU General Public\nLicense as published by";
  String noticee=  "the Free Software Foundation,\neither version 3 of the License, or";
  String noticef =  "(at your option) any later version.\n\n";
  String noticeg =  " This program is distributed in the hope that it will be useful,\n";
  String noticeh =  "but WITHOUT ANY WARRANTY; without even the implied warranty of";
  String noticei =  " MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the";
  String noticej =  "GNU General Public License for more details.\n\n";
  String noticek =  " You should have received a copy of the GNU General Public License";
  String noticel =  "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n";
  String noticem = "The GPL is appended to the end of the file Prototype.java";
  String  notice = noticea+noticeb+noticec+noticed+noticee+noticef+noticeg+noticeh+noticei+noticej+noticek+noticel+noticem;
		JTextPane sign = new JTextPane();
		sign.setEditable(false);
		sign.setText(notice);
		cpanel.getContentPane().add(sign);
		cpanel.setLocation(675,0);
		cpanel.setSize(400,350);
		cpanel.setVisible(true);
	}
}

public void itemStateChanged(ItemEvent e){
	
	if (cflag){
	
	if(e.getItemSelectable() == ccd){if (ccd.getState()){tray.merlin.setSDO("Check", true);tray.merlin.setSFO("Check", true);}
	else{tray.merlin.setSDO("Check", false);tray.merlin.setSFO("Check", false);}}
	
	if(e.getItemSelectable() == rnd){if (rnd.getState()){tray.merlin.setSDO("Rand", true);tray.merlin.setSFO("Rand", true);}
	else{tray.merlin.setSDO("Rand", false);tray.merlin.setSFO("Rand", false);}}
	
	if(e.getItemSelectable() == hidsel){if(hidsel.getState() == true){tray.bigboard.setSelect(false);tray.bigboard.repaint();}
	else{tray.bigboard.setSelect(true);tray.bigboard.repaint();}}
	
	if(e.getItemSelectable() == interact){if(interact.getState()){tray.merlin.setSDO("Interactive", true);tray.merlin.setMAction("SDraw");}
	else{tray.merlin.setSDO("Interactive", false);tray.merlin.setMAction("None");}}
	
	if(e.getItemSelectable() == hwrap){if(hwrap.getState()){tray.merlin.setWrap("X", true);}
	else{tray.merlin.setWrap("X", false);} tray.ariadne.setWrap(hwrap.getState(),vwrap.getState());
	tray.andromeda.setWrap(hwrap.getState(),vwrap.getState());}
	
	if(e.getItemSelectable() == vwrap){if(vwrap.getState()){tray.merlin.setWrap("Y", true);}
	else{tray.merlin.setWrap("Y", false);}tray.ariadne.setWrap(hwrap.getState(),vwrap.getState());
	tray.andromeda.setWrap(hwrap.getState(),vwrap.getState());}
	
	if(e.getItemSelectable() == multiC){if(multiC.getState()){tray.merlin.setDisp(4);if(tray.bigboard.getMode() == 1 || tray.bigboard.getMode() == 5){tray.bigboard.setMode(4);}}
	else{tray.merlin.setDisp(1);if(tray.bigboard.getMode() == 4){tray.bigboard.setMode(1);}}}
	
	if(e.getItemSelectable() == ccdcd){if(ccdcd.getState()){tray.merlin.setCDO("Check",true);}else{tray.merlin.setCDO("Check", false);}}
	
	if(e.getItemSelectable() == rndcd){if(rndcd.getState()){tray.merlin.setCDO("Rand",true);}else{tray.merlin.setCDO("Rand", false);}}
	
	if(e.getItemSelectable() == ccdcf){if(ccdcf.getState()){tray.merlin.setCFO("Check", true);}else{tray.merlin.setCFO("Check",false);}}
	
	if(e.getItemSelectable() == rndcf){if(rndcf.getState()){tray.merlin.setCFO("Rand", true);}else{tray.merlin.setCFO("Rand",false);}}
	
	if(e.getItemSelectable() == invA){if(invA.getState()){tray.castor.setInvert(true);}else{tray.castor.setInvert(false);}}
	
	if(e.getItemSelectable() == invB){if(invB.getState()){tray.pollux.setInvert(true);}else{tray.pollux.setInvert(false);}}
	
	if(e.getItemSelectable() == parA){tray.castor.setBool("Par", parA.getState());if(parA.getState()){parA.setLabel("Odd");}else{parA.setLabel("Even");}}
	
	if(e.getItemSelectable() == parB){tray.pollux.setBool("Par", parB.getState());if(parB.getState()){parB.setLabel("Odd");}else{parB.setLabel("Even");}}
	
	if(e.getItemSelectable() == recA){tray.castor.setBool("Rec", recA.getState());}
	
	if(e.getItemSelectable() == recA){tray.pollux.setBool("Rec", recB.getState());}
	
	// set rule and sequence arrays
	for(int z = 0; z<= 7; z++){
	if(e.getItemSelectable() == juno[z]){rulea[z] = juno[z].getState(); tray.castor.setBoola(arrayname, rulea);}
	if(e.getItemSelectable() == hera[z]){ruleb[z] = hera[z].getState(); tray.pollux.setBoola(arraynameb, ruleb);}	
	}
}
}
	

public void setWC(){
	clearOpts(cellcontrolA);
	int selVal = 0;
		if(modelA.getValue()=="Cell") {selVal = 0;}
		if(modelA.getValue()=="offCell") {selVal = 1;}
		if(modelA.getValue()=="onCell") {selVal = 2;}
		if(modelA.getValue()=="BlinkCell"){ selVal = 3;}
		if(modelA.getValue()=="sequenceCell") {selVal = 4;arrayname = "Seq";} 
		if(modelA.getValue()=="randomCell") {selVal = 5;}
		if(modelA.getValue()=="Life") {selVal = 6;recA.setState(false);}
		if(modelA.getValue()=="Seeds") {selVal = 7;recA.setState(false);}
		if(modelA.getValue()=="ParityCell") {selVal = 8;recA.setState(true);}
		if(modelA.getValue()=="Conveyor") {selVal = 9;}
		if(modelA.getValue()=="Wolfram"){selVal = 10;arrayname = "Rule";}
		if(modelA.getValue()=="Symmetrical"){selVal = 11;}
		if(modelA.getValue()=="Mirror"){selVal = 12;}
		if(modelA.getValue()=="Majority"){selVal = 13; recA.setState(true);}
		if(modelA.getValue()=="Gnarl"){selVal = 14; recA.setState(false);}
		if(modelA.getValue()=="Amoeba"){selVal = 15; recA.setState(false);}
		if(modelA.getValue()=="HighLife"){selVal = 16; recA.setState(false);}
		if(modelA.getValue()=="Prime"){selVal = 17; recA.setState(false);}
		if(modelA.getValue()=="Day and Night"){selVal = 18; recA.setState(false);}
		
	tray.castor.setCT(selVal);
	selCelOpt(selVal,true);
}

public void setWCB(){
	clearOpts(cellcontrolB);
	int selVal = 0;
		if(modelAA.getValue()=="Cell") {selVal = 0;}
		if(modelAA.getValue()=="offCell") {selVal = 1;}
		if(modelAA.getValue()=="onCell") {selVal = 2;}
		if(modelAA.getValue()=="BlinkCell"){ selVal = 3;}
		if(modelAA.getValue()=="sequenceCell") {selVal = 4;arraynameb = "Seq";} 
		if(modelAA.getValue()=="randomCell") {selVal = 5;}
		if(modelAA.getValue()=="Life") {selVal = 6;recB.setState(false);}
		if(modelAA.getValue()=="Seeds") {selVal = 7;recB.setState(false);}
		if(modelAA.getValue()=="ParityCell") {selVal = 8;recB.setState(true);}
		if(modelAA.getValue()=="Conveyor") {selVal = 9;}
		if(modelAA.getValue()=="Wolfram"){selVal = 10;arraynameb = "Rule";}
		if(modelAA.getValue()=="Symmetrical"){selVal = 11;}
		if(modelAA.getValue()=="Mirror"){selVal = 12;}
		if(modelAA.getValue()=="Majority"){selVal = 13; recB.setState(true);}
		if(modelAA.getValue()=="Gnarl"){selVal = 14; recB.setState(false);}
		if(modelAA.getValue()=="Amoeba"){selVal = 15; recB.setState(false);}
		if(modelAA.getValue()=="HighLife"){selVal = 16; recB.setState(false);}
		if(modelAA.getValue()=="Prime"){selVal = 17; recB.setState(false);}
		if(modelAA.getValue()=="Day and Night"){selVal = 18; recB.setState(false);}
		
	tray.pollux.setCT(selVal);
	selCelOpt(selVal, false);
}

public void setMat(){
	int selVal = 1;
	if(modelC.getValue() == "1") {selVal = 1;}
	if(modelC.getValue() == "2") {selVal = 2;}
	if(modelC.getValue() == "3") {selVal = 3;}
	if(modelC.getValue() == "4") {selVal = 4;}
	if(modelC.getValue() == "8") {selVal = 8;}
	if(modelC.getValue() == "16") {selVal = 16;}
	if(modelC.getValue() == "32") {selVal = 32;}
	tray.castor.setMaturity(selVal);
}

public void setMatB(){
	int selVal = 1;
	if(modelCC.getValue() == "1"){selVal = 1;}
	if(modelCC.getValue() == "2"){selVal = 2;}
	if(modelCC.getValue() == "3"){selVal = 3;}
	if(modelCC.getValue() == "4"){selVal = 4;}
	if(modelCC.getValue() == "8"){selVal = 8;}
	if(modelCC.getValue() == "16"){selVal = 16;}
	if(modelCC.getValue() == "32"){selVal = 32;}
	tray.pollux.setMaturity(selVal);
}

public void setDirA(){
		int selVal = 0;
		if (dirselA.getValue() == "DN"){selVal = 0;}
		if (dirselA.getValue() == "LL"){selVal = 1;}
		if (dirselA.getValue() == "L") {selVal = 2;}
		if (dirselA.getValue() == "UL"){selVal = 3;}
		if (dirselA.getValue() == "UP"){selVal = 4;}
		if (dirselA.getValue() == "UR"){selVal = 5;}
		if (dirselA.getValue() == "R") {selVal = 6;}
		if (dirselA.getValue() == "LR"){selVal = 7;}
		tray.castor.setDirection(selVal);
}
	
public void setDirB(){
		int selVal = 0;
		if (dirselB.getValue() == "DN"){selVal = 0;}
		if (dirselB.getValue() == "LL"){selVal = 1;}
		if (dirselB.getValue() == "L") {selVal = 2;}
		if (dirselB.getValue() == "UL"){selVal = 3;}
		if (dirselB.getValue() == "UP"){selVal = 4;}
		if (dirselB.getValue() == "UR"){selVal = 5;}
		if (dirselB.getValue() == "R") {selVal = 6;}
		if (dirselB.getValue() == "LR"){selVal = 7;}
		tray.pollux.setDirection(selVal);
}

public void setZT(){
	int selVal = 2000;
	if(modelB.getValue() == "Very Slow") {selVal = 2000;}
	if(modelB.getValue() == "Slow") {selVal = 200;}
	if(modelB.getValue() == "Fast") {selVal = 20;}
	if(modelB.getValue() == "Very Fast") {selVal = 2;}
	tray.merlin.setZT(selVal);
}

public void stateChanged(ChangeEvent e){
	if (cflag){
	
	if (e.getSource() == cellpicker){
		
		setWC();
		}
		
	if(e.getSource() == Bcellpicker){
		
		setWCB();
	}
	
	if(e.getSource() == matpicker){
		
		setMat();
	}
	
	if(e.getSource() == Bmatpicker){
		
		setMatB();
	}
	
	if(e.getSource() == condirA){ 
		setDirA();
	}
		
	if(e.getSource() == condirB){ 
		setDirB();
	}	
		 
	if (e.getSource() == scon){
		
		setZT();
	}
	
	}
	}
	

	

	
public void makeMenu(String a){
	if(a == "CE"){
		ceflag = true;
		edc = new JButton("Edit Cells");
		edc.addActionListener(this);
		ccdcd.addItemListener(this);
		rndcd.addItemListener(this);
		cf = new JButton("Fill");
		cf.addActionListener(this);
		cellborder = new JButton("Border");
		cellborder.addActionListener(this);
		ccdcf.addItemListener(this);
		for(int d = 0; d <= juno.length-1; d++){juno[d] = new Checkbox();}
		
		//components for primary cell
		cellpicker = new JSpinner( modelA);
		matpicker = new JSpinner(modelC);
		invA = new Checkbox("Invert");
		condirA = new JSpinner(dirselA);
		mirsel = new JButton("SelectMirror");
		parA = new Checkbox("Even");
		recA = new Checkbox("Recursive");
		for(int d = 0; d <= juno.length-1; d++){ juno[d] = new Checkbox();}
		cellcontrolA = new Component[]{matpicker, condirA, invA, mirsel, parA, recA,
		juno[0], juno[1], juno[2], juno[3], juno[4], juno[5], juno[6], juno[7]};

		rndcf.addItemListener(this);
		cellpicker.addChangeListener(this);
		cellpicker.setMaximumSize(new Dimension(100,10));
		matpicker.addChangeListener(this);
		matpicker.setMaximumSize(new Dimension(40, 25));
		matpicker.setMinimumSize(new Dimension(40, 25));
		condirA.addChangeListener(this);
		condirA.setMaximumSize(new Dimension(40,25));
		condirA.setMinimumSize(new Dimension(40, 25));
		invA.addItemListener(this);
		mirsel.addActionListener(this);
		parA.addItemListener(this);
		recA.addItemListener(this);
		juno[0].addItemListener(this);
		juno[1].addItemListener(this);
		juno[2].addItemListener(this);
		juno[3].addItemListener(this);
		juno[4].addItemListener(this);
		juno[5].addItemListener(this);
		juno[6].addItemListener(this);
		juno[7].addItemListener(this);
		
		//components for secondary cell
		Bcellpicker = new JSpinner(modelAA);
		Bmatpicker = new JSpinner(modelCC);
		invB = new Checkbox("Invert");
		condirB = new JSpinner(dirselB);
		mirselB = new JButton("Select Mirror");
		parB = new Checkbox("Even");
		recB = new Checkbox("Recursive");
		for(int d = 0; d <= hera.length-1; d++){ hera[d] = new Checkbox();}
		cellcontrolB = new Component[]{Bmatpicker, condirB, invB, mirselB, parB, recB,
		hera[0], hera[1], hera[2], hera[3], hera[4], hera[5], hera[6], hera[7]};	
		
		Bcellpicker.addChangeListener(this);
		Bcellpicker.setMaximumSize(new Dimension(100,10));
		Bmatpicker.addChangeListener(this);
		Bmatpicker.setMaximumSize(new Dimension(40,25));
		Bmatpicker.setMinimumSize(new Dimension(40, 25));
		condirB.addChangeListener(this);
		condirB.setMaximumSize(new Dimension(40,25));
		condirB.setMinimumSize(new Dimension(40,25));
		invB.addItemListener(this);
		mirselB.addActionListener(this);
		parB.addItemListener(this);
		recB.addItemListener(this);
		hera[0].addItemListener(this);
		hera[1].addItemListener(this);
		hera[2].addItemListener(this);
		hera[3].addItemListener(this);
		hera[4].addItemListener(this);
		hera[5].addItemListener(this);
		hera[6].addItemListener(this);
		hera[7].addItemListener(this);
		
		
		
		//menu component holds all the components
		cedit = new cellMenuComponent();
		// layout the cell editor
		GroupLayout celayout = new GroupLayout(cedit);
		celayout.setAutoCreateGaps(false);
		celayout.setAutoCreateContainerGaps(true);
		celayout.setHorizontalGroup(
				celayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					//cell edit mode
					.addComponent(edc)
					.addGroup(celayout.createSequentialGroup()
						.addComponent(ccdcd)
						.addComponent(rndcd))	
					// cell fill
					.addGroup(celayout.createSequentialGroup()
						.addComponent(cf)
						.addComponent(cellborder))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(ccdcf)
						.addComponent(rndcf))	
					//primary cell
					.addComponent(cellpicker)
					.addGroup(celayout.createSequentialGroup()
						.addComponent(matpicker)
						.addComponent(condirA))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(invA)
						.addComponent(mirsel)
						.addComponent(parA)
						.addComponent(recA))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(juno[0])
						.addComponent(juno[1])
						.addComponent(juno[2])
						.addComponent(juno[3])
						.addComponent(juno[4])
						.addComponent(juno[5])
						.addComponent(juno[6])
						.addComponent(juno[7]))		
					//secondary cell
					.addComponent(Bcellpicker)
					.addGroup(celayout.createSequentialGroup()
						.addComponent(Bmatpicker)
						.addComponent(condirB))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(invB)
						.addComponent(mirselB)
						.addComponent(parB)
						.addComponent(recB))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(hera[0])
						.addComponent(hera[1])
						.addComponent(hera[2])
						.addComponent(hera[3])
						.addComponent(hera[4])
						.addComponent(hera[5])
						.addComponent(hera[6])
						.addComponent(hera[7]))		
				);
				
		celayout.setVerticalGroup(
				celayout.createSequentialGroup()
					//cell edit mode
					.addComponent(edc)
					.addGroup(celayout.createParallelGroup()
						.addComponent(ccdcd)
						.addComponent(rndcd))
					//cell fill
					.addGroup(celayout.createParallelGroup()
						.addComponent(cf)
						.addComponent(cellborder))
					.addGroup(celayout.createParallelGroup()
						.addComponent(ccdcf)
						.addComponent(rndcf))
					//primary cell
					.addComponent(cellpicker)
					.addGroup(celayout.createParallelGroup()
							.addComponent(matpicker)
							.addComponent(condirA))
					.addGroup(celayout.createParallelGroup()
							.addComponent(invA)
							.addComponent(mirsel)
							.addComponent(parA)
							.addComponent(recA))
					.addGroup(celayout.createParallelGroup()
							.addComponent(juno[0])
							.addComponent(juno[1])
							.addComponent(juno[2])
							.addComponent(juno[3])
							.addComponent(juno[4])
							.addComponent(juno[5])
							.addComponent(juno[6])
							.addComponent(juno[7]))
					//secondary cell
					.addComponent(Bcellpicker)
					.addGroup(celayout.createParallelGroup()
							.addComponent(Bmatpicker)
							.addComponent(condirB))
					.addGroup(celayout.createParallelGroup()
							.addComponent(invB)
							.addComponent(mirselB)
							.addComponent(parB)
							.addComponent(recB))
					.addGroup(celayout.createParallelGroup()
							.addComponent(hera[0])
							.addComponent(hera[1])
							.addComponent(hera[2])
							.addComponent(hera[3])
							.addComponent(hera[4])
							.addComponent(hera[5])
							.addComponent(hera[6])
							.addComponent(hera[7]))
				);
		
		ceframe = new JFrame("Cell Editor");
		ceframe.getContentPane().add(cedit);
		ceframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ceframe.setSize(200,500);
		ceframe.setResizable(false);
		ceframe.setLocation(0,165);
		cedit.setLayout(celayout);
		ceframe.setVisible(true);
		setWC();
		setWCB();
		}
		
		if(a == "Sel"){
			//make window
			selframe = new JFrame("Selection tools");
			selframe.setLocation(675,0);
			// make controls
			selall = new JButton("Select All");
			selcel = new JButton("Select");
			desel = new JButton("Deselect");
			invsel = new JButton("Invert Selection");
			rectsel = new JButton("Select Rectangle");
			selall.addActionListener(this);
			selcel.addActionListener(this);
			desel.addActionListener(this);
			invsel.addActionListener(this);
			hidsel.addItemListener(this);
			rectsel.addActionListener(this);
			//make the menu
			selcomp = new cellMenuComponent();
			selcomp.setLayout(new FlowLayout());
			selcomp.add(selall);
			selcomp.add(selcel);
			selcomp.add(desel);
			selcomp.add(invsel);
			selcomp.add(hidsel);
			selcomp.add(rectsel);
			//set up window
			selframe.getContentPane().add(selcomp);
			selframe.setSize(675,165);
			selframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			selframe.setResizable(false);
		}
	}
	
	// brings up cell-specific parameters on the editing menu
	public void selCelOpt(int a, boolean b){
		Component[] optio;
		if(b){optio = cellcontrolA;}else{optio = cellcontrolB;}
		/*
		 *0= matpicker
		 * 1= direction
		 * 2= invert
		 * 3= mirror selection
		 * 4= parity
		 * 5= recursive
		 * 6-13 = rule/sequence
		 */
		switch(a){
			case 0: break;
			case 1: break;
			case 2: break;
			case 3: showOpt(optio[0]); break;
			case 4: showOpt(optio[0]); showOpt(optio[2]); for(int w = 6; w<= 13; w++){showOpt(optio[w]);} break;
			case 5: showOpt(optio[0]); break;
			case 6: showOpt(optio[0]); showOpt(optio[5]); break;
			case 7: showOpt(optio[0]); showOpt(optio[5]); break;
			case 8: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[4]); showOpt(optio[5]); break;
			case 9: showOpt(optio[0]); showOpt(optio[1]); showOpt(optio[2]); break;
			case 10: showOpt(optio[0]); showOpt(optio[1]); for(int w = 6; w<= 13; w++){showOpt(optio[w]);} break;
			case 11: showOpt(optio[0]); showOpt(optio[1]); showOpt(optio[2]); break;
			case 12: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[3]); break;
			case 13: showOpt(optio[0]); showOpt(optio[1]); showOpt(optio[5]); break;
			case 14: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[5]); break;
			case 15: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[5]); break; 
			case 16: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[5]); break;
			case 17: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[5]); break;
			case 18: showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[5]); break;
			default:  showOpt(optio[0]); showOpt(optio[2]); showOpt(optio[5]); break;}
		
		cedit.repaint();
		}
	
	
	//clears all cell options
	public void clearOpts(Component[] cellopts){
		
			for (int na = 0; na <= cellopts.length-1; na++){
				cellopts[na].setVisible(false); cellopts[na].setEnabled(false);	
				}
				cedit.repaint();
			
		}
			
	public void showOpt(Component cellcont){
		cellcont.setVisible(true);
		cellcont.setEnabled(true);
	}	
	

}
