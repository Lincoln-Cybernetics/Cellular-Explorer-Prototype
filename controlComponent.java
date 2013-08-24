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
// all the buttons	
JButton newc;
JButton ss;
JButton step;
// state editing buttons
JButton eds;
JButton stf;
JButton clear;
// cell editing buttons
JButton edc;
JButton cf;
JButton cellborder;
int celleditoption =1;

// other buttons
JButton about;

//cell/state editing
Checkbox tbt = new Checkbox("3x3");
Checkbox ccd = new Checkbox("Checkerboard");
Checkbox rnd = new Checkbox("Random");

// state editing
Checkbox interact = new Checkbox("Interactive");

 // cell type selection
String[] cells = new String[]{"Cell", "onCell", "Blinkcell", "Blinkcell2", "Random cell", "Life",
 "Seeds", "OddCell", "EvenCell", "Conveyor", "Wolfram","Passive","Symmetrical"};
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
	edc = new JButton("Edit Cells");
	cf = new JButton("Cell Fill");
	cellborder = new JButton("Set Border");
	clear = new JButton("Clear");
	about = new JButton("About");
	setLayout( new FlowLayout() );
	
	add( newc );
	
	add(ss);
	add(scon);
	add(step);
	add(eds);
	add(stf);
	add(edc);
	add(cellpicker);
	add(matpicker);
	add(condirA);
	add(cf);
	add(Bcellpicker);
	add(Bmatpicker);
	add(condirB);
	add(cellborder);
	add(clear);
	add(rnd);
	add(ccd);
	add(tbt);
	add(interact);
	add(hwrap);
	add(vwrap);
	add(about);
	
	cellpicker.addChangeListener(this);
	Bcellpicker.addChangeListener(this);
	matpicker.addChangeListener(this);
	Bmatpicker.addChangeListener(this);
	condirA.addChangeListener(this);
	condirB.addChangeListener(this);
	scon.addChangeListener(this);
	ss.addActionListener(this);
	step.addActionListener(this);
	newc.addActionListener(this);
	eds.addActionListener(this);
	stf.addActionListener(this);
	edc.addActionListener(this);
	cf.addActionListener(this);
	cellborder.addActionListener(this);
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
	if(e.getSource() == clear){ 
	tray.sfflag = true; tray.sfopt = 2;}
	
	// Edit State button goes to/from state editing mode
	if(e.getSource() == eds){ if (tray.editflag == false){ pflag = true; tray.setPause(true);
	tray.setEdit(true);}
	else{tray.setEdit(false);}}
	
	// State Fill button set the on/off state of all cells
	if(e.getSource() == stf){int option = 1; if(ccd.getState()){option = 2;} if(rnd.getState()){option = 3;} 
	if(ccd.getState() && tbt.getState()){option = 4;}
			switch(option){
				case 1: tray.sfflag = true; tray.sfopt = 1; break;
				case 2: tray.sfflag =true; tray.sfopt =3; break;
				case 3: tray.sfflag = true; tray.sfopt = 4; break;
				case 4: tray.sfflag = true; tray.sfopt = 5; break;
				default: tray.sfflag = true; tray.sfopt = 3; break;}}
		
	// Edit Cell button goes to/from cell editing mode	
	if(e.getSource() == edc){ 
	if(tray.editcellflag == false){pflag = true;
	tray.setPause(true); tray.setCellEdit(true);}
		else{tray.setCellEdit(false);}}	
		
	//Cell Fill button sets all cells
	if(e.getSource() == cf){celleditoption = 1; if(ccd.getState()){celleditoption = 2;} 
		if (rnd.getState()){ celleditoption = 3;} if(ccd.getState() && tbt.getState()){celleditoption = 4;}
			switch (celleditoption){
				case 1: tray.cellFill(); break;
				case 2: tray.cellCheckFill(); break;
				case 3: tray.cellRandFill(); break;
				case 4: tray.cellCheckFilltbt(); break;
				default: tray.cellFill(); break; }}
	
	// the Set Border button sets the cells at the edge of the automaton
	if(e.getSource() == cellborder){celleditoption = 1; if(ccd.getState()){celleditoption = 3;} 
		if (rnd.getState()){ celleditoption = 4;}
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
	
	if(e.getItemSelectable() == tbt){if (tbt.getState()){tray.tbtflag = true;}
	else{tray.tbtflag = false;}}
	
	if(e.getItemSelectable() == ccd){if (ccd.getState()){tray.checkdrawflag = true;}
	else{tray.checkdrawflag = false;}}
	
	if(e.getItemSelectable() == rnd){if (rnd.getState()){tray.randoflag = true;}
	else{tray.randoflag = false;}}
	
	if(e.getItemSelectable() == interact){if(interact.getState()){tray.interactive = true;}
	else{tray.interactive = false;}}
	
	if(e.getItemSelectable() == hwrap){if(hwrap.getState()){tray.hwrapflag = true;}
	else{tray.hwrapflag = false;}}
	
	if(e.getItemSelectable() == vwrap){if(vwrap.getState()){tray.vwrapflag = true;}
	else{tray.vwrapflag = false;}}
}
}
	

public void setWC(){
	if(modelA.getValue()=="Cell") {tray.workcell = 0;}
		if(modelA.getValue()=="onCell") {tray.workcell = 1;}
		if(modelA.getValue()=="Blinkcell") {tray.workcell = 2;}
		if(modelA.getValue()=="Blinkcell2"){ tray.workcell = 3;}
		if(modelA.getValue()=="Random cell") {tray.workcell = 4;} 
		if(modelA.getValue()=="Life") {tray.workcell = 5;}
		if(modelA.getValue()=="Seeds") {tray.workcell = 6;}
		if(modelA.getValue()=="OddCell") {tray.workcell = 7;}
		if(modelA.getValue()=="EvenCell") {tray.workcell = 8;}
		if(modelA.getValue()=="Conveyor") {tray.workcell = 9;}
		if(modelA.getValue()=="Wolfram"){tray.workcell = 10;}
		if(modelA.getValue()=="Passive"){tray.workcell = 11;}
		if(modelA.getValue()=="Symmetrical"){tray.workcell = 12;}
}

public void setWCB(){
	if(modelAA.getValue()=="Cell") {tray.workcellB = 0;}
		if(modelAA.getValue()=="onCell") {tray.workcellB = 1;}
		if(modelAA.getValue()=="Blinkcell") {tray.workcellB = 2;}
		if(modelAA.getValue()=="Blinkcell2"){ tray.workcellB = 3;}
		if(modelAA.getValue()=="Random cell") {tray.workcellB = 4;} 
		if(modelAA.getValue()=="Life") {tray.workcellB = 5;}
		if(modelAA.getValue()=="Seeds") {tray.workcellB = 6;}
		if(modelAA.getValue()=="OddCell") {tray.workcellB = 7;}
		if(modelAA.getValue()=="EvenCell") {tray.workcellB = 8;}
		if(modelAA.getValue()=="Conveyor") {tray.workcellB = 9;}
		if(modelAA.getValue()=="Wolfram"){tray.workcellB = 10;}
		if(modelAA.getValue()=="Passive"){tray.workcellB = 11;}
		if(modelAA.getValue()=="Symmetrical"){tray.workcellB = 12;}
}

public void setMat(){
	if(modelC.getValue() == "1") {tray.workmat = 1;}
	if(modelC.getValue() == "2") {tray.workmat = 2;}
	if(modelC.getValue() == "3") {tray.workmat = 3;}
	if(modelC.getValue() == "4") {tray.workmat = 4;}
}

public void setMatB(){
	if(modelCC.getValue() == "1"){tray.workmatB = 1;}
	if(modelCC.getValue() == "2"){tray.workmatB = 2;}
	if(modelCC.getValue() == "3"){tray.workmatB = 3;}
	if(modelCC.getValue() == "4"){tray.workmatB = 4;}
}

public void setDirA(){
		if (dirselA.getValue() == "DN"){tray.workdirA = 4;}
		if (dirselA.getValue() == "LL"){tray.workdirA = 5;}
		if (dirselA.getValue() == "L") {tray.workdirA = 6;}
		if (dirselA.getValue() == "UL"){tray.workdirA = 7;}
		if (dirselA.getValue() == "UP"){tray.workdirA = 0;}
		if (dirselA.getValue() == "UR"){tray.workdirA = 1;}
		if (dirselA.getValue() == "R") {tray.workdirA = 2;}
		if (dirselA.getValue() == "LR"){tray.workdirA = 3;}
}
	
public void setDirB(){
		if (dirselB.getValue() == "DN"){tray.workdirB = 4;}
		if (dirselB.getValue() == "LL"){tray.workdirB = 5;}
		if (dirselB.getValue() == "L") {tray.workdirB = 6;}
		if (dirselB.getValue() == "UL"){tray.workdirB = 7;}
		if (dirselB.getValue() == "UP"){tray.workdirB = 0;}
		if (dirselB.getValue() == "UR"){tray.workdirB = 1;}
		if (dirselB.getValue() == "R") {tray.workdirB = 2;}
		if (dirselB.getValue() == "LR"){tray.workdirB = 3;}
	
}

public void setZT(){
	if(modelB.getValue() == "Very Slow") {tray.ztime = 2000;}
	if(modelB.getValue() == "Slow") {tray.ztime = 200;}
	if(modelB.getValue() == "Fast") {tray.ztime = 20;}
	if(modelB.getValue() == "Very Fast") {tray.ztime = 2;}
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

}
