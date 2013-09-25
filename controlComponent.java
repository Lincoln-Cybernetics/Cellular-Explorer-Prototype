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


// other buttons
JButton about;

//cell/state editing
Checkbox ccd = new Checkbox("Checkerboard");
Checkbox rnd = new Checkbox("Random");

// Brush selection

JRadioButton oneby = new JRadioButton("1x1", true);
JRadioButton twoby = new JRadioButton("2x2", false);
JRadioButton threeby = new JRadioButton("3x3", false);
ButtonGroup brushes = new ButtonGroup();


// cell editing
Checkbox ccdcd = new Checkbox("Check");
Checkbox ccdcf = new Checkbox("Check");
Checkbox rndcd = new Checkbox("Rand");
Checkbox rndcf = new Checkbox("Rand");
// state editing
Checkbox interact = new Checkbox("Interactive");

 // cell type selection
String[] cells = new String[]{"Cell", "offCell", "onCell", "BlinkCell", "sequenceCell","randomCell", "Life",
 "Seeds", "ParityCell", "Conveyor", "Wolfram","Symmetrical","Mirror", "Majority"};
SpinnerListModel modelA = new SpinnerListModel(cells);
JSpinner cellpicker = new JSpinner( modelA);
SpinnerListModel modelAA = new SpinnerListModel(cells);
JSpinner Bcellpicker = new JSpinner(modelAA);

//cell maturity selection
String[] mats = new String[]{"1","2","3","4", "8", "16", "32"};
SpinnerListModel modelC = new SpinnerListModel(mats);
JSpinner matpicker = new JSpinner(modelC);
SpinnerListModel modelCC = new SpinnerListModel(mats);
JSpinner Bmatpicker = new JSpinner(modelCC);

//inversion control (true to invert)
Checkbox invA = new Checkbox("Invert");
Checkbox invB = new Checkbox("Invert");

// direction selection
String[] dirs = new String[] {"DN", "LL","L","UL","UP","UR","R","LR"};
SpinnerListModel dirselA = new SpinnerListModel(dirs);
JSpinner condirA = new JSpinner(dirselA);
SpinnerListModel dirselB = new SpinnerListModel(dirs);
JSpinner condirB = new JSpinner(dirselB);

// mirror selector
JButton mirsel;
JButton mirselB;

//parity control (false = even, true = odd)
Checkbox parA = new Checkbox("Even");
Checkbox parB = new Checkbox("Even");

// recursive setting
Checkbox recA = new Checkbox("Recursive");
Checkbox recB = new Checkbox("Recursive");

//automaton rules
//speed control
String[] speed = new String[]{"Very Slow", "Slow", "Fast", "Very Fast"};
SpinnerListModel modelB = new SpinnerListModel(speed);
JSpinner scon = new JSpinner(modelB);

//wrap around
Checkbox hwrap = new Checkbox("Wrap-X");
Checkbox vwrap = new Checkbox("Wrap-Y");

//"Fade" rule
Checkbox fade = new Checkbox("Fade");

// multicolor mode
Checkbox multiC = new Checkbox("Multicolor");

cellBrain tray;


boolean firstflag;
boolean pflag;
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
	celledit = new JButton("Cell Editor");
	clear = new JButton("Clear");
	about = new JButton("About");
	setLayout( new FlowLayout() );
	
	add( newc );
	
	add(ss);
	add(scon);
	add(step);
	add(eds);
	add(stf);
	add(celledit);
	add(clear);
	add(rnd);
	add(ccd);
	add(oneby);
	add(twoby);
	add(threeby);
	add(interact);
	add(hwrap);
	add(vwrap);
	add(fade);
	add(multiC);
	add(about);
	
	brushes.add(oneby);
	brushes.add(twoby);
	brushes.add(threeby);
	
	scon.addChangeListener(this);
	ss.addActionListener(this);
	step.addActionListener(this);
	newc.addActionListener(this);
	eds.addActionListener(this);
	stf.addActionListener(this);
	celledit.addActionListener(this);
	clear.addActionListener(this);
	rnd.addItemListener(this);
	ccd.addItemListener(this);
	oneby.addActionListener(this);
	twoby.addActionListener(this);
	threeby.addActionListener(this);
	interact.addItemListener(this);
	hwrap.addItemListener(this);
	vwrap.addItemListener(this);
	fade.addItemListener(this);
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
		firstflag = true;setWC();setWCB();setMat();setMatB();setZT();}
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
	if(e.getSource() == oneby){ tray.merlin.setBrush(1);}
	// 2x2 brush
	if(e.getSource() == twoby){ tray.merlin.setBrush(2);}
	//3x3 brush
	if(e.getSource() == threeby){ tray.merlin.setBrush(3);}
	
	// Edit State button goes to/from state editing mode
	if(e.getSource() == eds){ if (tray.editflag == false){ pflag = true; tray.setPause(true);
	tray.setEdit(true);}
	else{tray.setEdit(false);}}
	
	// State Fill button set the on/off state of all cells
	if(e.getSource() == stf){int option = 1; if(ccd.getState()){option = 2;} if(rnd.getState()){option = 3;} 
	if(ccd.getState() && tray.merlin.getBrush() == 3){option = 4;}
	if(ccd.getState() && tray.merlin.getBrush() == 2){option = 5;}
			switch(option){
				case 1: if(tray.paused){tray.stateFill();}else{ tray.sfflag = true; tray.sfopt = 1;} break;
				case 2: if(tray.paused){tray.stateCheckFill();}else{tray.sfflag =true; tray.sfopt =3;} break;
				case 3: if(tray.paused){tray.stateRandFill();}else{tray.sfflag = true; tray.sfopt = 4;} break;
				case 4: if(tray.paused){tray.stateCheckFilltbt();}else{tray.sfflag = true; tray.sfopt = 5;} break;
				case 5: if(tray.paused){tray.stateCheckFill2x2();}else{tray.sfflag = true; tray.sfopt = 6;} break;
				default:if(tray.paused){tray.stateCheckFill();}else{ tray.sfflag = true; tray.sfopt = 3;} break;}}
		
	//open the cell editor menu
	if(e.getSource() == celledit){if(cedit == null){makeMenu("CE");}else{ceframe.setVisible(true);}}
		
	// Edit Cell button goes to/from cell editing mode	
	if(e.getSource() == edc){ 
	if(tray.editcellflag == false){pflag = true;
	tray.setPause(true); tray.setCellEdit(true);}
		else{tray.setCellEdit(false);}}	
		
	//Cell Fill button sets all cells
	if(e.getSource() == cf){celleditoption = 1; 
		if(tray.merlin.getCFO("Check")){celleditoption = 2;} 
		if (tray.merlin.getCFO("Rand")){ celleditoption = 3;} 
		if(tray.merlin.getCFO("Check") && tray.merlin.getCFO("Rand") == false && tray.merlin.getBrush() == 3){celleditoption = 4;} 
		if(tray.merlin.getCFO("Rand") && tray.merlin.getCFO("Check")){celleditoption = 5;} 
		if(tray.merlin.getCFO("Rand") && tray.merlin.getCFO("Check") && tray.merlin.getBrush() == 3){celleditoption = 6;}
		if(tray.merlin.getCFO("Check") && tray.merlin.getCFO("Rand") == false && tray.merlin.getBrush() == 2){celleditoption = 7;}
		if(tray.merlin.getCFO("Check") && tray.merlin.getCFO("Rand") && tray.merlin.getBrush() == 2){celleditoption = 8;}
			switch (celleditoption){
				case 1: tray.cellFill(); break;
				case 2: tray.cellCheckFill(); break;
				case 3: tray.cellRandFill(); break;
				case 4: tray.cellCheckFilltbt(1,2); break;
				case 5: tray.cellRCFill(); break;
				case 6: tray.cellCheckFilltbt(1,4); break;
				case 7: tray.cellCheckFill2x2(1,2); break;
				case 8: tray.cellCheckFill2x2(1,4); break;
				default: tray.cellFill(); break; }}
	
	// the Set Border button sets the cells at the edge of the automaton
	if(e.getSource() == cellborder){celleditoption = 1; if(tray.merlin.getCFO("Check")){celleditoption = 3;} 
		if (tray.merlin.getCFO("Rand")){ celleditoption = 4;}
		if(tray.merlin.getCFO("Check") && tray.merlin.getCFO("Rand")){ celleditoption = 5;}
		tray.setBorder(celleditoption);}
		
	// mirror cell neighborhood selectors
	if(e.getSource() == mirsel){
	tray.mirSel(true);
	}
	if(e.getSource() == mirselB){
		tray.mirSel(false);
	}
}
	
	
	if(e.getSource() == about){
		JFrame cpanel = new JFrame("About");
		String noticea = "Cellular Explorer Prototype proof of concept\n";
  String noticeb ="Copyright(C) 02013 Matt Ahlschwede\n";
  String noticec = "This program is free software: you can redistribute it and/or modify\n";
  String noticed ="  it under the terms of the GNU General Public License as published by\n";
  String noticee=  "the Free Software Foundation, either version 3 of the License, or\n";
  String noticef =  "(at your option) any later version.\n";
  String noticeg =  "This program is distributed in the hope that it will be useful,\n";
  String noticeh =  "but WITHOUT ANY WARRANTY; without even the implied warranty of\n";
  String noticei =  "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n";
  String noticej =  "GNU General Public License for more details.\n";
  String noticek =  "You should have received a copy of the GNU General Public License\n";
  String noticel =  "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n";
  String noticem = "The GPL is appended to the end of the file Prototype.java";
  String  notice = noticea+noticeb+noticec+noticed+noticee+noticef+noticeg+noticeh+noticei+noticej+noticek+noticel+noticem;
		JTextPane sign = new JTextPane();
		sign.setText(notice);
		cpanel.getContentPane().add(sign);

		cpanel.setSize(400,500);
		cpanel.setVisible(true);
	}
}

public void itemStateChanged(ItemEvent e){
	
	if (cflag){
	
	if(e.getItemSelectable() == ccd){if (ccd.getState()){tray.merlin.setSDO("Check", true);tray.merlin.setSFO("Check", true);}
	else{tray.merlin.setSDO("Check", false);tray.merlin.setSFO("Check", false);}}
	
	if(e.getItemSelectable() == rnd){if (rnd.getState()){tray.merlin.setSDO("Rand", true);tray.merlin.setSFO("Rand", true);}
	else{tray.merlin.setSDO("Rand", false);tray.merlin.setSFO("Rand", false);}}
	
	if(e.getItemSelectable() == interact){if(interact.getState()){tray.merlin.setSDO("Interactive", true);}
	else{tray.merlin.setSDO("Interactive", false);}}
	
	if(e.getItemSelectable() == hwrap){if(hwrap.getState()){tray.merlin.setWrap("X", true);}
	else{tray.merlin.setWrap("Y", false);}}
	
	if(e.getItemSelectable() == vwrap){if(vwrap.getState()){tray.merlin.setWrap("Y", true);}
	else{tray.merlin.setWrap("Y", false);}}
	
	if(e.getItemSelectable() == fade){if(fade.getState()){tray.merlin.setDisp(5);tray.merlin.setBool("Fade", true);tray.bigboard.setMode(5);}
	else{tray.merlin.setBool("Fade",false);tray.bigboard.setMode(1);}}
	
	if(e.getItemSelectable() == multiC){if(multiC.getState()){tray.merlin.setDisp(4);if(tray.bigboard.getMode() == 1){tray.bigboard.setMode(4);}}
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
}
}
	

public void setWC(){
	int selVal = 0;
		if(modelA.getValue()=="Cell") {selVal = 0;}
		if(modelA.getValue()=="offCell") {selVal = 1;}
		if(modelA.getValue()=="onCell") {selVal = 2;}
		if(modelA.getValue()=="BlinkCell"){ selVal = 3;}
		if(modelA.getValue()=="sequenceCell") {selVal = 4;} 
		if(modelA.getValue()=="randomCell") {selVal = 5;}
		if(modelA.getValue()=="Life") {selVal = 6;recA.setState(false);}
		if(modelA.getValue()=="Seeds") {selVal = 7;recA.setState(false);}
		if(modelA.getValue()=="ParityCell") {selVal = 8;recA.setState(true);}
		if(modelA.getValue()=="Conveyor") {selVal = 9;}
		if(modelA.getValue()=="Wolfram"){selVal = 10;}
		if(modelA.getValue()=="Symmetrical"){selVal = 11;}
		if(modelA.getValue()=="Mirror"){selVal = 12;}
		if(modelA.getValue()=="Majority"){selVal = 13; recA.setState(true);}
		
	tray.castor.setCT(selVal);
	selCelOpt(selVal,true);
}

public void setWCB(){
	int selVal = 0;
		if(modelAA.getValue()=="Cell") {selVal = 0;}
		if(modelAA.getValue()=="offCell") {selVal = 1;}
		if(modelAA.getValue()=="onCell") {selVal = 2;}
		if(modelAA.getValue()=="BlinkCell"){ selVal = 3;}
		if(modelAA.getValue()=="sequenceCell") {selVal = 4;} 
		if(modelAA.getValue()=="randomCell") {selVal = 5;}
		if(modelAA.getValue()=="Life") {selVal = 6;recB.setState(false);}
		if(modelAA.getValue()=="Seeds") {selVal = 7;recB.setState(false);}
		if(modelAA.getValue()=="ParityCell") {selVal = 8;recB.setState(true);}
		if(modelAA.getValue()=="Conveyor") {selVal = 9;}
		if(modelAA.getValue()=="Wolfram"){selVal = 10;}
		if(modelAA.getValue()=="Symmetrical"){selVal = 11;}
		if(modelAA.getValue()=="Mirror"){selVal = 12;}
		if(modelAA.getValue()=="Majority"){selVal = 13; recB.setState(true);}
		
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
		//components for primary cell
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
		mirsel = new JButton("SelectMirror");mirsel.addActionListener(this);
		parA.addItemListener(this);
		recA.addItemListener(this);
		
		//components for secondary cell
		Bcellpicker.addChangeListener(this);
		Bcellpicker.setMaximumSize(new Dimension(100,10));
		Bmatpicker.addChangeListener(this);
		Bmatpicker.setMaximumSize(new Dimension(40,25));
		Bmatpicker.setMinimumSize(new Dimension(40, 25));
		condirB.addChangeListener(this);
		condirB.setMaximumSize(new Dimension(40,25));
		condirB.setMinimumSize(new Dimension(40,25));
		invB.addItemListener(this);
		mirselB = new JButton("Select Mirror");mirselB.addActionListener(this);
		parB.addItemListener(this);
		recB.addItemListener(this);
		
		//menu component holds all the components
		cedit = new cellMenuComponent();
		// layout the cell editor
		GroupLayout celayout = new GroupLayout(cedit);
		celayout.setAutoCreateGaps(true);
		celayout.setAutoCreateContainerGaps(true);
		celayout.setHorizontalGroup(
				celayout.createParallelGroup(GroupLayout.Alignment.CENTER)
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
				);
		
		ceframe = new JFrame("Cell Editor");
		ceframe.getContentPane().add(cedit);
		ceframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ceframe.setSize(200,500);
		ceframe.setResizable(false);
		ceframe.setLocation(0,150);
		cedit.setLayout(celayout);
		ceframe.setVisible(true);
		setWC();
		setWCB();
		}
		
	}
	
	// brings up cell-specific parameters on the editing menu
	public void selCelOpt(int a, boolean b){
		Component[] cellcontrolA = new Component[]{matpicker, condirA, invA, mirsel, parA, recA};
		Component[] cellcontrolB = new Component[]{Bmatpicker, condirB, invB, mirselB, parB, recB};
		Component[] cellcontrolC;
		boolean cpresent = false;
		if(b){cellcontrolC = cellcontrolA;}else{cellcontrolC = cellcontrolB;}
		if(ceflag){
		for (int cntr = 0; cntr < cellcontrolC.length; cntr++){
		cpresent = tray.merlin.getCellOpt(a,cntr);
		if(cpresent){cellcontrolC[cntr].setVisible(true);cellcontrolC[cntr].setEnabled(true);}
		else{cellcontrolC[cntr].setVisible(false);cellcontrolC[cntr].setEnabled(false);}
		}
		cedit.repaint();
		}
	}
	
	

}
