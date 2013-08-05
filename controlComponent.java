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
int celleditoption =1;
JButton about;

//cell/state editing
Checkbox tbt = new Checkbox("3x3");
Checkbox ccd = new Checkbox("Checkerboard");
Checkbox rnd = new Checkbox("Random");

// state editing
Checkbox interact = new Checkbox("Interactive");

 // cell type selection
String[] cells = new String[]{"Cell", "onCell", "Blinkcell", "Blinkcell2", "Random cell", "Life", "Seeds", "OddCell", "EvenCell", "Conveyor", "Wolfram"};
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

CellComponent[] tray = new CellComponent[3];
int windowflag = 0;

boolean[] firstflag = new boolean[3];
boolean[] pflag = new boolean[3];
boolean cflag = false;
int xx = 0;
int yy = 0;



public controlComponent(){
	
	for(windowflag=0;windowflag<=2;windowflag++){
		
		firstflag[windowflag] = true;
		pflag[windowflag] = false;
		}
		windowflag=0;
	ss = new JButton("Play/Pause");
	newc = new JButton("New Culture");
	step = new JButton("Step");
	eds= new JButton("Edit State");
	stf = new JButton ("State Fill");
	edc = new JButton("Edit Cells");
	cf = new JButton("Cell Fill");
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
	
	if(e.getSource() == newc){
		//sets up the window, adds the cell component
		tray[0] = new CellComponent();
		JFrame garden = new JFrame("Cellular Explorer");
		garden.getContentPane().add( new JScrollPane(tray[0]) );
		garden.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tray[0].xsiz = 400; tray[0].ysiz = 150;tray[0].magnify = 5;
		garden.setSize(tray[0].xsiz*tray[0].magnify, tray[0].ysiz*tray[0].magnify);
		garden.setVisible( true );
		cflag = true; tray[0].demoflag =0;//tray[0].create();
		 windowflag = 0;firstflag[0] = true;setWC();setZT();
	}
	
	
	if(cflag){
	
	
	
	if(e.getSource() == ss){ if(firstflag[windowflag] == true){ boolean nonsense;
	nonsense = tray[windowflag].begin(); firstflag[windowflag] = false;}
	else{ if (pflag[windowflag] == false){ pflag[windowflag] = true;tray[windowflag].pauseflag = true;}
		else{ pflag[windowflag] = false; tray[windowflag].pauseflag = false; if(tray[windowflag].editflag == true){tray[windowflag].editflag = false;
		} if(tray[windowflag].editcellflag == true){tray[windowflag].editcellflag = false;}
		}}}
		
	if(e.getSource() == step){ if(tray[windowflag].paused){tray[windowflag].iterate();}}
	
	if(e.getSource() == clear){ 
	tray[windowflag].sfflag = true; tray[windowflag].sfopt = 2;}
	
	if(e.getSource() == eds){ if (tray[windowflag].editflag == false){ pflag[windowflag] = true;
	tray[windowflag].pauseflag = true; tray[windowflag].editflag = true;
	tray[windowflag].repaint();}
	else{tray[windowflag].editflag = false;tray[windowflag].repaint();}}
	
	if(e.getSource() == stf){int option = 1; if(ccd.getState()){option = 2;} if(rnd.getState()){option = 3;} 
	if(ccd.getState() && tbt.getState()){option = 4;}
			switch(option){
				case 1: tray[windowflag].sfflag = true; tray[windowflag].sfopt = 1; break;
				case 2: tray[windowflag].sfflag =true; tray[windowflag].sfopt =3; break;
				case 3: tray[windowflag].sfflag = true; tray[windowflag].sfopt = 4; break;
				case 4: tray[windowflag].sfflag = true; tray[windowflag].sfopt = 5; break;
				default: tray[windowflag].sfflag = true; tray[windowflag].sfopt = 3; break;}}
		
	if(e.getSource() == edc){ 
	if(tray[windowflag].editcellflag == false){pflag[windowflag] = true;
	tray[windowflag].pauseflag = true; tray[windowflag].editcellflag = true; 
	tray[windowflag].repaint();}
		else{tray[windowflag].editcellflag = false;tray[windowflag].repaint();}}	
		
	if(e.getSource() == cf){celleditoption = 1; if(ccd.getState()){celleditoption = 2;} 
		if (rnd.getState()){ celleditoption = 3;} if(ccd.getState() && tbt.getState()){celleditoption = 4;}
			switch (celleditoption){
				case 1: tray[windowflag].cellFill(); break;
				case 2: tray[windowflag].cellCheckFill(); break;
				case 3: tray[windowflag].cellRandFill(); break;
				case 4: tray[windowflag].cellCheckFilltbt(); break;
				default: tray[windowflag].cellFill(); break; }}
	
	
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
	
	if(e.getItemSelectable() == tbt){if (tbt.getState()){tray[windowflag].tbtflag = true;}
	else{tray[windowflag].tbtflag = false;}}
	
	if(e.getItemSelectable() == ccd){if (ccd.getState()){tray[windowflag].checkdrawflag = true;}
	else{tray[windowflag].checkdrawflag = false;}}
	
	if(e.getItemSelectable() == rnd){if (rnd.getState()){tray[windowflag].randoflag = true;}
	else{tray[windowflag].randoflag = false;}}
	
	if(e.getItemSelectable() == interact){if(interact.getState()){tray[windowflag].interactive = true;}
	else{tray[windowflag].interactive = false;}}
	
	if(e.getItemSelectable() == hwrap){if(hwrap.getState()){tray[windowflag].hwrapflag = true;}
	else{tray[windowflag].hwrapflag = false;}}
	
	if(e.getItemSelectable() == vwrap){if(vwrap.getState()){tray[windowflag].vwrapflag = true;}
	else{tray[windowflag].vwrapflag = false;}}
}
}
	

public void setWC(){
	if(modelA.getValue()=="Cell") {tray[windowflag].workcell = 0;}
		if(modelA.getValue()=="onCell") {tray[windowflag].workcell = 1;}
		if(modelA.getValue()=="Blinkcell") {tray[windowflag].workcell = 2;}
		if(modelA.getValue()=="Blinkcell2"){ tray[windowflag].workcell = 3;}
		if(modelA.getValue()=="Random cell") {tray[windowflag].workcell = 4;} 
		if(modelA.getValue()=="Life") {tray[windowflag].workcell = 5;}
		if(modelA.getValue()=="Seeds") {tray[windowflag].workcell = 6;}
		if(modelA.getValue()=="OddCell") {tray[windowflag].workcell = 7;}
		if(modelA.getValue()=="EvenCell") {tray[windowflag].workcell = 8;}
		if(modelA.getValue()=="Conveyor") {tray[windowflag].workcell = 9;}
		if(modelA.getValue()=="Wolfram"){tray[windowflag].workcell = 10;}
}

public void setWCB(){
	if(modelAA.getValue()=="Cell") {tray[windowflag].workcellB = 0;}
		if(modelAA.getValue()=="onCell") {tray[windowflag].workcellB = 1;}
		if(modelAA.getValue()=="Blinkcell") {tray[windowflag].workcellB = 2;}
		if(modelAA.getValue()=="Blinkcell2"){ tray[windowflag].workcellB = 3;}
		if(modelAA.getValue()=="Random cell") {tray[windowflag].workcellB = 4;} 
		if(modelAA.getValue()=="Life") {tray[windowflag].workcellB = 5;}
		if(modelAA.getValue()=="Seeds") {tray[windowflag].workcellB = 6;}
		if(modelAA.getValue()=="OddCell") {tray[windowflag].workcellB = 7;}
		if(modelAA.getValue()=="EvenCell") {tray[windowflag].workcellB = 8;}
		if(modelAA.getValue()=="Conveyor") {tray[windowflag].workcellB = 9;}
		if(modelAA.getValue()=="Wolfram"){tray[windowflag].workcellB = 10;}
}

public void setMat(){
	if(modelC.getValue() == "1") {tray[windowflag].workmat = 1;}
	if(modelC.getValue() == "2") {tray[windowflag].workmat = 2;}
	if(modelC.getValue() == "3") {tray[windowflag].workmat = 3;}
	if(modelC.getValue() == "4") {tray[windowflag].workmat = 4;}
}

public void setMatB(){
	if(modelCC.getValue() == "1"){tray[windowflag].workmatB = 1;}
	if(modelCC.getValue() == "2"){tray[windowflag].workmatB = 2;}
	if(modelCC.getValue() == "3"){tray[windowflag].workmatB = 3;}
	if(modelCC.getValue() == "4"){tray[windowflag].workmatB = 4;}
}

public void setDirA(){
		if (dirselA.getValue() == "DN"){tray[windowflag].workdirA = 4;}
		if (dirselA.getValue() == "LL"){tray[windowflag].workdirA = 5;}
		if (dirselA.getValue() == "L") {tray[windowflag].workdirA = 6;}
		if (dirselA.getValue() == "UL"){tray[windowflag].workdirA = 7;}
		if (dirselA.getValue() == "UP"){tray[windowflag].workdirA = 0;}
		if (dirselA.getValue() == "UR"){tray[windowflag].workdirA = 1;}
		if (dirselA.getValue() == "R") {tray[windowflag].workdirA = 2;}
		if (dirselA.getValue() == "LR"){tray[windowflag].workdirA = 3;}
}
	
public void setDirB(){
		if (dirselB.getValue() == "DN"){tray[windowflag].workdirB = 4;}
		if (dirselB.getValue() == "LL"){tray[windowflag].workdirB = 5;}
		if (dirselB.getValue() == "L") {tray[windowflag].workdirB = 6;}
		if (dirselB.getValue() == "UL"){tray[windowflag].workdirB = 7;}
		if (dirselB.getValue() == "UP"){tray[windowflag].workdirB = 0;}
		if (dirselB.getValue() == "UR"){tray[windowflag].workdirB = 1;}
		if (dirselB.getValue() == "R") {tray[windowflag].workdirB = 2;}
		if (dirselB.getValue() == "LR"){tray[windowflag].workdirB = 3;}
	
}

public void setZT(){
	if(modelB.getValue() == "Very Slow") {tray[windowflag].ztime = 2000;}
	if(modelB.getValue() == "Slow") {tray[windowflag].ztime = 200;}
	if(modelB.getValue() == "Fast") {tray[windowflag].ztime = 20;}
	if(modelB.getValue() == "Very Fast") {tray[windowflag].ztime = 2;}
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
