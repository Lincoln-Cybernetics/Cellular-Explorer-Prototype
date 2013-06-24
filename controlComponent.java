import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
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


class controlComponent extends JComponent implements ActionListener, ChangeListener{
JButton newc;
JButton ss;
JButton rnd;
JButton eds;
JButton edc;
JButton demo;
JButton clear;
JButton about;
String[] cells = new String[]{"Cell", "onCell", "Blinkcell", "Blinkcell2", "Random cell", "Life", "Seeds"};
SpinnerListModel modelA = new SpinnerListModel(cells);
JSpinner cellpicker = new JSpinner( modelA);

CellComponent[] tray = new CellComponent[3];
int windowflag = 0;

boolean[] firstflag = new boolean[3];
boolean[] pflag = new boolean[3];
boolean cflag = false;


int xx = 0;
int yy = 0;
int[] magholder = new int[3];
public controlComponent(){
	
	for(windowflag=0;windowflag<=2;windowflag++){
		
		firstflag[windowflag] = true;
		pflag[windowflag] = false;
		magholder[windowflag] = 5;}
		windowflag=0;
	ss = new JButton("Play/Pause");
	newc = new JButton("New Culture");
	rnd = new JButton("Randomize");
	eds= new JButton("Edit State");
	edc = new JButton("Edit Cells");
	demo = new JButton("Demo");
	
	clear = new JButton("Clear");
	about = new JButton("About");
	setLayout( new FlowLayout() );
	
	add( newc );
	add(demo);
	
	add(ss);
	add(clear);
	add(rnd);
	add(eds);
	add(edc);
	add(cellpicker);
	add(about);
	
	cellpicker.addChangeListener(this);
	ss.addActionListener(this);
	newc.addActionListener(this);
	eds.addActionListener(this);
	rnd.addActionListener(this);
	edc.addActionListener(this);
	demo.addActionListener(this);
	clear.addActionListener(this);
	about.addActionListener(this);
	
	}

public void actionPerformed(ActionEvent e){
	
	if(e.getSource() == newc){
		//sets up the window, adds the logic component
		tray[0] = new CellComponent();
		JFrame garden = new JFrame("Game of Life");
		garden.getContentPane().add( new JScrollPane(tray[0]) );
		garden.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tray[0].xsiz = 400; tray[0].ysiz = 150;tray[0].magnify = 5;
		garden.setSize(tray[0].xsiz*tray[0].magnify, tray[0].ysiz*tray[0].magnify);
		garden.setVisible( true );
		cflag = true; tray[0].demoflag =0; windowflag = 0;firstflag[0] = true;
	}
	
		if(e.getSource() == demo){
			tray[1] = new CellComponent();
		//sets up the window, adds the logic component
		JFrame carden = new JFrame("Glider Bomb");
		carden.getContentPane().add( new JScrollPane(tray[1]) );
		carden.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tray[1].xsiz = 500; tray[1].ysiz = 300;tray[1].magnify = 3;
		carden.setSize(tray[1].xsiz*tray[1].magnify, tray[1].ysiz*tray[1].magnify);
		carden.setVisible( true );
		cflag = true; tray[1].demoflag =1;windowflag = 1;firstflag[1] = true;
	}
	
	
	
	
	
	
	if(e.getSource() == ss){if (cflag == true){ if(firstflag[windowflag] == true){ boolean nonsense;
	nonsense = tray[windowflag].begin(); firstflag[windowflag] = false;}
	else{ if (pflag[windowflag] == false){ pflag[windowflag] = true;tray[windowflag].pauseflag = true;}
		else{ pflag[windowflag] = false; tray[windowflag].pauseflag = false; if(tray[windowflag].editflag == true){tray[windowflag].editflag = false;
		tray[windowflag].hiliteflag = false;} if(tray[windowflag].editcellflag == true){tray[windowflag].editcellflag = false; tray[windowflag].hiliteflag = false;}
		if(tray[windowflag].magnify == 5){tray[windowflag].magnify = magholder[windowflag];}}}}}
		
	if(e.getSource() == rnd){ if (cflag == true){ 
	tray[windowflag].fillflag = true; tray[windowflag].randflag = true;}}
	
	if(e.getSource() == clear){ if (cflag == true){ 
	tray[windowflag].fillflag = true; tray[windowflag].clearflag = true;}}
	
	if(e.getSource() == eds){ if(cflag	== true){if (tray[windowflag].editflag == false){ pflag[windowflag] = true;
	tray[windowflag].pauseflag = true; tray[windowflag].editflag = true;if(tray[windowflag].magnify<=5){magholder[windowflag]= tray[windowflag].magnify;tray[windowflag].magnify = 5;}tray[windowflag].repaint();}
	else{tray[windowflag].editflag = false;
	tray[windowflag].hiliteflag = false; if(tray[windowflag].magnify == 5){tray[windowflag].magnify = magholder[windowflag];} tray[windowflag].pauseflag = false;pflag[windowflag] = false;}}}
		
	if(e.getSource() == edc){ if (cflag == true){if(tray[windowflag].editcellflag == false){pflag[windowflag] = true;
	tray[windowflag].pauseflag = true; tray[windowflag].editcellflag = true; if(tray[windowflag].magnify<=5){magholder[windowflag] = tray[windowflag].magnify; tray[windowflag].magnify = 5;}
	tray[windowflag].repaint();}
		else{tray[windowflag].editcellflag = false;tray[windowflag].hiliteflag = false;
		if(tray[windowflag].magnify == 5){tray[windowflag].magnify = magholder[windowflag];}
		pflag[windowflag] = false;tray[windowflag].pauseflag = false;}}}	
	
	
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

public void stateChanged(ChangeEvent e){
	if (e.getSource() == cellpicker){
		if(modelA.getValue()=="Cell") {tray[windowflag].workcell = 0;}
		if(modelA.getValue()=="onCell") {tray[windowflag].workcell = 1;}
		if(modelA.getValue()=="Blinkcell") {tray[windowflag].workcell = 2;}
		if(modelA.getValue()=="Blinkcell2"){ tray[windowflag].workcell = 3;}
		if(modelA.getValue()=="Random cell") {tray[windowflag].workcell = 4;} 
		if(modelA.getValue()=="Life") {tray[windowflag].workcell = 5;}
		if(modelA.getValue()=="Seeds") {tray[windowflag].workcell = 6;}
		}
		 }


}
