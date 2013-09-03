import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Checkbox.*;
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
Checkbox tbt = new Checkbox("3x3");
Checkbox ccd = new Checkbox("Checkerboard");
Checkbox ccdcd = new Checkbox("Check");
Checkbox ccdcf = new Checkbox("Check");
Checkbox rnd = new Checkbox("Random");
Checkbox rndcd = new Checkbox("Rand");
Checkbox rndcf = new Checkbox("Rand");
// state editing
Checkbox interact = new Checkbox("Interactive");

 // cell type selection
String[] cells = new String[]{"Cell", "offCell", "onCell", "BlinkCell", "sequenceCell","randomCell", "Life",
 "Seeds", "ParityCell", "Conveyor", "Wolfram","Symmetrical","Mirror"};
SpinnerListModel modelA = new SpinnerListModel(cells);
JSpinner cellpicker = new JSpinner( modelA);
SpinnerListModel modelAA = new SpinnerListModel(cells);
JSpinner Bcellpicker = new JSpinner(modelAA);

//cell maturity selection
String[] mats = new String[]{"1","2","3","4"};
SpinnerListModel modelC = new SpinnerListModel(mats);
JSpinner matpicker = new JSpinner(modelC);
SpinnerListModel modelCC = new SpinnerListModel(mats);
JSpinner Bmatpicker = new JSpinner(modelCC);

//inversion control
Checkbox invA = new Checkbox("Invert");
Checkbox invB = new Checkbox("Invert");

// direction selection
String[] dirs = new String[] {"DN", "LL","L","UL","UP","UR","R","LR"};
SpinnerListModel dirselA = new SpinnerListModel(dirs);
JSpinner condirA = new JSpinner(dirselA);
SpinnerListModel dirselB = new SpinnerListModel(dirs);
JSpinner condirB = new JSpinner(dirselB);

//speed control
String[] speed = new String[]{"Very Slow", "Slow", "Fast", "Very Fast"};
SpinnerListModel modelB = new SpinnerListModel(speed);
JSpinner scon = new JSpinner(modelB);

//wrap around
Checkbox hwrap = new Checkbox("Wrap-X");
Checkbox vwrap = new Checkbox("Wrap-Y");

cellBrain tray;
//int windowflag = 0;

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
	//cellborder = new JButton("Set Border");
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
	//add(cellborder);
	add(clear);
	add(rnd);
	add(ccd);
	add(tbt);
	add(interact);
	add(hwrap);
	add(vwrap);
	add(about);
	
	
	scon.addChangeListener(this);
	ss.addActionListener(this);
	step.addActionListener(this);
	newc.addActionListener(this);
	eds.addActionListener(this);
	stf.addActionListener(this);
	celledit.addActionListener(this);
	//cellborder.addActionListener(this);
	clear.addActionListener(this);
	rnd.addItemListener(this);
	ccd.addItemListener(this);
	tbt.addItemListener(this);
	interact.addItemListener(this);
	hwrap.addItemListener(this);
	vwrap.addItemListener(this);
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
	
	// Edit State button goes to/from state editing mode
	if(e.getSource() == eds){ if (tray.editflag == false){ pflag = true; tray.setPause(true);
	tray.setEdit(true);}
	else{tray.setEdit(false);}}
	
	// State Fill button set the on/off state of all cells
	if(e.getSource() == stf){int option = 1; if(ccd.getState()){option = 2;} if(rnd.getState()){option = 3;} 
	if(ccd.getState() && tbt.getState()){option = 4;}
			switch(option){
				case 1: if(tray.paused){tray.stateFill();}else{ tray.sfflag = true; tray.sfopt = 1;} break;
				case 2: if(tray.paused){tray.stateCheckFill();}else{tray.sfflag =true; tray.sfopt =3;} break;
				case 3: if(tray.paused){tray.stateRandFill();}else{tray.sfflag = true; tray.sfopt = 4;} break;
				case 4: if(tray.paused){tray.stateCheckFilltbt();}else{tray.sfflag = true; tray.sfopt = 5;} break;
				default:if(tray.paused){tray.stateCheckFill();}else{ tray.sfflag = true; tray.sfopt = 3;} break;}}
		
	//open the cell editor menu
	if(e.getSource() == celledit){if(cedit == null){makeMenu("CE");}else{ceframe.setVisible(true);}}
		
	// Edit Cell button goes to/from cell editing mode	
	if(e.getSource() == edc){ 
	if(tray.editcellflag == false){pflag = true;
	tray.setPause(true); tray.setCellEdit(true);}
		else{tray.setCellEdit(false);}}	
		
	//Cell Fill button sets all cells
	if(e.getSource() == cf){celleditoption = 1; if(tray.merlin.getCFO("Check")){celleditoption = 2;} 
		if (tray.merlin.getCFO("Rand")){ celleditoption = 3;} if(ccd.getState() && tbt.getState()){celleditoption = 4;} 
		if(tray.merlin.getCFO("Rand") && tray.merlin.getCFO("Check")){celleditoption = 5;} 
		if(tray.merlin.getCFO("Rand") && tray.merlin.getCFO("Check") && tray.merlin.getBrush() == "3x3"){celleditoption = 6;}
			switch (celleditoption){
				case 1: tray.cellFill(); break;
				case 2: tray.cellCheckFill(); break;
				case 3: tray.cellRandFill(); break;
				case 4: tray.cellCheckFilltbt(1,2); break;
				case 5: tray.cellRCFill(); break;
				case 6: tray.cellCheckFilltbt(1,4); break;
				default: tray.cellFill(); break; }}
	
	// the Set Border button sets the cells at the edge of the automaton
	if(e.getSource() == cellborder){celleditoption = 1; if(tray.merlin.getCFO("Check")){celleditoption = 3;} 
		if (tray.merlin.getCFO("Rand")){ celleditoption = 4;}
		if(tray.merlin.getCFO("Check") && tray.merlin.getCFO("Rand")){ celleditoption = 5;}
		tray.setBorder(celleditoption);}
	
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
	
	if(e.getItemSelectable() == tbt){if (tbt.getState()){tray.tbtflag = true;tray.merlin.setBrush("3x3");}
	else{tray.tbtflag = false;tray.merlin.setBrush("Normal");}}
	
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
	
	if(e.getItemSelectable() == ccdcd){if(ccdcd.getState()){tray.merlin.setCDO("Check",true);}else{tray.merlin.setCDO("Check", false);}}
	
	if(e.getItemSelectable() == rndcd){if(rndcd.getState()){tray.merlin.setCDO("Rand",true);}else{tray.merlin.setCDO("Rand", false);}}
	
	if(e.getItemSelectable() == ccdcf){if(ccdcf.getState()){tray.merlin.setCFO("Check", true);}else{tray.merlin.setCFO("Check",false);}}
	
	if(e.getItemSelectable() == rndcf){if(rndcf.getState()){tray.merlin.setCFO("Rand", true);}else{tray.merlin.setCFO("Rand",false);}}
	
	if(e.getItemSelectable() == invA){if(invA.getState()){tray.castor.setInvert(true);}else{tray.castor.setInvert(false);}}
	
	if(e.getItemSelectable() == invB){if(invB.getState()){tray.pollux.setInvert(true);}else{tray.pollux.setInvert(false);}}
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
		if(modelA.getValue()=="Life") {selVal = 6;}
		if(modelA.getValue()=="Seeds") {selVal = 7;}
		if(modelA.getValue()=="ParityCell") {selVal = 8;}
		if(modelA.getValue()=="Conveyor") {selVal = 9;}
		if(modelA.getValue()=="Wolfram"){selVal = 10;}
		if(modelA.getValue()=="Symmetrical"){selVal = 11;}
		if(modelA.getValue()=="Mirror"){selVal = 12;}
		
	tray.castor.setCT(selVal);
	selCelOptA(selVal);
}

public void setWCB(){
	int selVal = 0;
		if(modelAA.getValue()=="Cell") {selVal = 0;}
		if(modelAA.getValue()=="offCell") {selVal = 1;}
		if(modelAA.getValue()=="onCell") {selVal = 2;}
		if(modelAA.getValue()=="BlinkCell"){ selVal = 3;}
		if(modelAA.getValue()=="sequenceCell") {selVal = 4;} 
		if(modelAA.getValue()=="randomCell") {selVal = 5;}
		if(modelAA.getValue()=="Life") {selVal = 6;}
		if(modelAA.getValue()=="Seeds") {selVal = 7;}
		if(modelAA.getValue()=="ParityCell") {selVal = 8;}
		if(modelAA.getValue()=="Conveyor") {selVal = 9;}
		if(modelAA.getValue()=="Wolfram"){selVal = 10;}
		if(modelAA.getValue()=="Symmetrical"){selVal = 11;}
		if(modelAA.getValue()=="Mirror"){selVal = 12;}
		
	tray.pollux.setCT(selVal);
	selCelOptB(selVal);
}

public void setMat(){
	int selVal = 1;
	if(modelC.getValue() == "1") {selVal = 1;}
	if(modelC.getValue() == "2") {selVal = 2;}
	if(modelC.getValue() == "3") {selVal = 3;}
	if(modelC.getValue() == "4") {selVal = 4;}
	tray.castor.setMaturity(selVal);
}

public void setMatB(){
	int selVal = 1;
	if(modelCC.getValue() == "1"){selVal = 1;}
	if(modelCC.getValue() == "2"){selVal = 2;}
	if(modelCC.getValue() == "3"){selVal = 3;}
	if(modelCC.getValue() == "4"){selVal = 4;}
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
		rndcf.addItemListener(this);
		cellpicker.addChangeListener(this);
		matpicker.addChangeListener(this);
		condirA.addChangeListener(this);
		invA.addItemListener(this);
		Bcellpicker.addChangeListener(this);
		Bmatpicker.addChangeListener(this);
		condirB.addChangeListener(this);
		invB.addItemListener(this);
		cedit = new cellMenuComponent();
		// layout the cell editor
		GroupLayout celayout = new GroupLayout(cedit);
		celayout.setAutoCreateGaps(true);
		celayout.setAutoCreateContainerGaps(true);
		celayout.setHorizontalGroup(
				celayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(edc)
					.addGroup(celayout.createSequentialGroup()
						.addComponent(ccdcd)
						.addComponent(rndcd))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(cf)
						.addComponent(cellborder))
					.addGroup(celayout.createSequentialGroup()
						.addComponent(ccdcf)
						.addComponent(rndcf))
					.addComponent(cellpicker)
					.addGroup(celayout.createSequentialGroup()
						.addComponent(matpicker)
						.addComponent(condirA))
					.addComponent(invA)
					.addComponent(Bcellpicker)
					.addGroup(celayout.createSequentialGroup()
						.addComponent(Bmatpicker)
						.addComponent(condirB))
					.addComponent(invB)
				);
				
		celayout.setVerticalGroup(
				celayout.createSequentialGroup()
					.addComponent(edc)
					.addGroup(celayout.createParallelGroup()
						.addComponent(ccdcd)
						.addComponent(rndcd))
					.addGroup(celayout.createParallelGroup()
						.addComponent(cf)
						.addComponent(cellborder))
					.addGroup(celayout.createParallelGroup()
						.addComponent(ccdcf)
						.addComponent(rndcf))
					.addComponent(cellpicker)
					.addGroup(celayout.createParallelGroup()
							.addComponent(matpicker)
							.addComponent(condirA))
					.addComponent(invA)
					.addComponent(Bcellpicker)
					.addGroup(celayout.createParallelGroup()
							.addComponent(Bmatpicker)
							.addComponent(condirB))
					.addComponent(invB)
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
		setWCB();}
		
	}
	
	// brings up cell-specific parameters on the editing menu
	public void selCelOptA(int a){
		if(ceflag){
		switch(a){
			case 0: invA.setVisible(false); invA.setEnabled(false);ceframe.setVisible(true); break;
			case 1: invA.setVisible(false); invA.setEnabled(false);ceframe.setVisible(true); break;
			case 2: invA.setVisible(false); invA.setEnabled(false);ceframe.setVisible(true);break;
			case 3: invA.setVisible(false); invA.setEnabled(false);ceframe.setVisible(true); break;
			case 4: invA.setVisible(true); invA.setEnabled(true);ceframe.setVisible(true);break;
			case 5: invA.setVisible(false); invA.setEnabled(false);ceframe.setVisible(true); break;
			case 6: invA.setVisible(false); invA.setEnabled(false);ceframe.setVisible(true); break;
			case 7: invA.setVisible(false); invA.setEnabled(false); ceframe.setVisible(true);break;
			case 8:  invA.setVisible(true); invA.setEnabled(true);ceframe.setVisible(true);break;
			case 9:  invA.setVisible(true); invA.setEnabled(true);ceframe.setVisible(true);break;
			case 10:  invA.setVisible(true); invA.setEnabled(true);ceframe.setVisible(true);break;
			case 11:  invA.setVisible(true); invA.setEnabled(true);ceframe.setVisible(true);break;
			case 12:  invA.setVisible(true); invA.setEnabled(true);ceframe.setVisible(true);break;
		}}
	}
	
	public void selCelOptB(int a){
		if(ceflag){
		switch(a){
			case 0: invB.setVisible(false); invB.setEnabled(false);ceframe.setVisible(true); break;
			case 1: invB.setVisible(false); invB.setEnabled(false); ceframe.setVisible(true);break;
			case 2: invB.setVisible(false); invB.setEnabled(false); ceframe.setVisible(true);break;
			case 3: invB.setVisible(false); invB.setEnabled(false);ceframe.setVisible(true); break;
			case 4: invB.setVisible(true); invB.setEnabled(true);ceframe.setVisible(true);break;
			case 5: invB.setVisible(false); invB.setEnabled(false); ceframe.setVisible(true);break;
			case 6: invB.setVisible(false); invB.setEnabled(false);ceframe.setVisible(true); break;
			case 7: invB.setVisible(false); invB.setEnabled(false);ceframe.setVisible(true); break;
			case 8:  invB.setVisible(true); invB.setEnabled(true);ceframe.setVisible(true);break;
			case 9:  invB.setVisible(true); invB.setEnabled(true);ceframe.setVisible(true);break;
			case 10:  invB.setVisible(true); invB.setEnabled(true);ceframe.setVisible(true);break;
			case 11:  invB.setVisible(true); invB.setEnabled(true);ceframe.setVisible(true);break;
			case 12:  invB.setVisible(true); invB.setEnabled(true);ceframe.setVisible(true);break;
		}}
	}

}
