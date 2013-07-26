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
// state editing buttons
JButton eds;
JButton clear;
JButton rnd;
// cell editing buttons
JButton edc;
JButton cf;
JButton ccf;
JButton crf;
//JButton demo;
JButton about;

//cell editing
Checkbox tbt = new Checkbox("3x3");
Checkbox ccd = new Checkbox("Checkerboard");

 // cell type selection
String[] cells = new String[]{"Cell", "onCell", "Blinkcell", "Blinkcell2", "Random cell", "Life", "Seeds", "OddCell", "EvenCell", "Conveyor"};
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

//speed control
String[] speed = new String[]{"Very Slow", "Slow", "Fast", "Very Fast"};
SpinnerListModel modelB = new SpinnerListModel(speed);
JSpinner scon = new JSpinner(modelB);

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
	rnd = new JButton("Randomize");
	eds= new JButton("Edit State");
	edc = new JButton("Edit Cells");
	cf = new JButton("Cell Fill");
	ccf = new JButton("Cell CheckFill");
	crf = new JButton("Create Monster");
	
	//demo = new JButton("Demo");
	
	clear = new JButton("Clear");
	about = new JButton("About");
	setLayout( new FlowLayout() );
	
	add( newc );
	//add(demo);
	
	add(ss);
	add(scon);
	add(clear);
	add(rnd);
	add(eds);
	add(edc);
	add(cellpicker);
	add(matpicker);
	add(cf);
	add(ccf);
	add(Bcellpicker);
	add(Bmatpicker);
	add(crf);
	add(ccd);
	add(tbt);
	add(about);
	
	cellpicker.addChangeListener(this);
	Bcellpicker.addChangeListener(this);
	matpicker.addChangeListener(this);
	Bmatpicker.addChangeListener(this);
	scon.addChangeListener(this);
	ss.addActionListener(this);
	newc.addActionListener(this);
	eds.addActionListener(this);
	rnd.addActionListener(this);
	edc.addActionListener(this);
	cf.addActionListener(this);
	ccf.addActionListener(this);
	crf.addActionListener(this);
	//demo.addActionListener(this);
	clear.addActionListener(this);
	ccd.addItemListener(this);
	tbt.addItemListener(this);
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
		cflag = true; tray[0].demoflag =0;tray[0].create(); windowflag = 0;firstflag[0] = true;setWC();setZT();
	}
	
		/*if(e.getSource() == demo){
			tray[1] = new CellComponent();
		//sets up the window, adds the cell component
		JFrame carden = new JFrame("Cellular Explorer");
		carden.getContentPane().add( new JScrollPane(tray[1]) );
		carden.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tray[1].xsiz = 300; tray[1].ysiz = 100;tray[1].magnify = 5;
		carden.setSize(tray[1].xsiz*tray[1].magnify, tray[1].ysiz*tray[1].magnify);
		carden.setVisible( true );
		cflag = true; tray[1].demoflag =1; tray[1].create();windowflag = 1;firstflag[1] = true;setWC();setZT();
	}*/
	
	
	
	
	
	
	if(e.getSource() == ss){if (cflag == true){ if(firstflag[windowflag] == true){ boolean nonsense;
	nonsense = tray[windowflag].begin(); firstflag[windowflag] = false;}
	else{ if (pflag[windowflag] == false){ pflag[windowflag] = true;tray[windowflag].pauseflag = true;}
		else{ pflag[windowflag] = false; tray[windowflag].pauseflag = false; if(tray[windowflag].editflag == true){tray[windowflag].editflag = false;
		} if(tray[windowflag].editcellflag == true){tray[windowflag].editcellflag = false;}
		}}}}
		
	if(e.getSource() == rnd){ if (cflag == true){ 
	tray[windowflag].fillflag = true; tray[windowflag].randflag = true;}}
	
	if(e.getSource() == clear){ if (cflag == true){ 
	tray[windowflag].fillflag = true; tray[windowflag].clearflag = true;}}
	
	if(e.getSource() == eds){ if(cflag	== true){if (tray[windowflag].editflag == false){ pflag[windowflag] = true;
	tray[windowflag].pauseflag = true; tray[windowflag].editflag = true;
	tray[windowflag].repaint();}
	else{tray[windowflag].editflag = false;tray[windowflag].repaint();}}}
		
	if(e.getSource() == edc){ if (cflag == true){
	if(tray[windowflag].editcellflag == false){pflag[windowflag] = true;
	tray[windowflag].pauseflag = true; tray[windowflag].editcellflag = true; 
	tray[windowflag].repaint();}
		else{tray[windowflag].editcellflag = false;tray[windowflag].repaint();}}}	
		
	if(e.getSource() == cf){if (cflag == true){tray[windowflag].cellFill();}}
	
	if(e.getSource() == ccf){if(cflag == true){tray[windowflag].cellCheckFill();}}
	
	if(e.getSource() == crf){if(cflag == true){tray[windowflag].cellRandFill();}}
	
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
	
	if(e.getItemSelectable() == tbt){if (tbt.getState()){tray[windowflag].tbtflag = true;}
	else{tray[windowflag].tbtflag = false;}}
	
	if(e.getItemSelectable() == ccd){if (ccd.getState()){tray[windowflag].checkdrawflag = true;}
	else{tray[windowflag].checkdrawflag = false;}}
	
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

public void setZT(){
	if(modelB.getValue() == "Very Slow") {tray[windowflag].ztime = 2000;}
	if(modelB.getValue() == "Slow") {tray[windowflag].ztime = 200;}
	if(modelB.getValue() == "Fast") {tray[windowflag].ztime = 20;}
	if(modelB.getValue() == "Very Fast") {tray[windowflag].ztime = 2;}
}

public void stateChanged(ChangeEvent e){
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
		 
	if (e.getSource() == scon){ 
		setZT();
	}
	}

}
