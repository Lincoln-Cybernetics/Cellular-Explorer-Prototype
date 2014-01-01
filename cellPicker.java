import java.awt.*;
import java.util.*;
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

public class cellPicker extends JComponent implements ActionListener, ItemListener{
	
cellOptionHandler gate;
// controlPanel variables
JComboBox cellpick;
String[] Cells = new String[]{"Cell","MBOT"};

JComboBox  MBOTPick;
String[] MBOTCells = new String[]{"Custom", "2x2", "3/4 Life", "Amoeba", "Assimilation", "Coagulations", "Coral", "Day and Night", "Diamoeba", "Dot Life",
"Dry Life", "Fredkin", "Gnarl", "High Life", "Life", "Life without Death", "Live Free or Die", "Long Life", "Maze", "Mazectric",
"Move", "Pseudo-life", "Replicator", "Seeds", "Serviettes", "Stains", "Vote", "Vote 4/5", "Walled Cities"};
String MBOTtype = "Custom";
Checkbox[] opts = new Checkbox[20];
JLabel jack;
JLabel jill;


// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
int ct = 0;

public cellPicker(){
	cellpick = new JComboBox(Cells);
	MBOTPick = new JComboBox(MBOTCells);
	jack = new JLabel("Born");
	jill = new JLabel("Survives");
	opts[0] = new Checkbox("Ages"); 
	opts[1] = new Checkbox("Fades");
	// born
	opts[2] = new Checkbox("0"); opts[3] = new Checkbox("1"); opts[4] = new Checkbox("2"); 
	opts[5] = new Checkbox("3"); opts[6] = new Checkbox("4"); opts[7] = new Checkbox("5"); 
	opts[8] = new Checkbox("6"); opts[9] = new Checkbox("7"); opts[10] = new Checkbox("8"); 
	// survives
	opts[11] = new Checkbox("0"); opts[12] = new Checkbox("1"); opts[13] = new Checkbox("2"); 
	opts[14] = new Checkbox("3"); opts[15] = new Checkbox("4"); opts[16] = new Checkbox("5"); 
	opts[17] = new Checkbox("6"); opts[18] = new Checkbox("7"); opts[19] = new Checkbox("8"); 
	
	GroupLayout cpLayout = new GroupLayout(this);
	cpLayout.setAutoCreateGaps(false);
	cpLayout.setAutoCreateContainerGaps(false);
	
	cpLayout.setHorizontalGroup(
		cpLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(cellpick)
			.addComponent(MBOTPick)
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(opts[0])
				.addComponent(opts[1]))
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(jack)
				.addComponent(opts[2])
				.addComponent(opts[3])
				.addComponent(opts[4])
				.addComponent(opts[5])
				.addComponent(opts[6])
				.addComponent(opts[7])
				.addComponent(opts[8])
				.addComponent(opts[9])
				.addComponent(opts[10]))
			.addGroup(cpLayout.createSequentialGroup()
				.addComponent(jill)
				.addComponent(opts[11])
				.addComponent(opts[12])
				.addComponent(opts[13])
				.addComponent(opts[14])
				.addComponent(opts[15])
				.addComponent(opts[16])
				.addComponent(opts[17])
				.addComponent(opts[18])
				.addComponent(opts[19]))
				);
				
	cpLayout.setVerticalGroup(
		cpLayout.createSequentialGroup()
			.addComponent(cellpick)
			.addComponent(MBOTPick)
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(opts[0])
				.addComponent(opts[1]))
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(jack)
				.addComponent(opts[2])
				.addComponent(opts[3])
				.addComponent(opts[4])
				.addComponent(opts[5])
				.addComponent(opts[6])
				.addComponent(opts[7])
				.addComponent(opts[8])
				.addComponent(opts[9])
				.addComponent(opts[10]))
			.addGroup(cpLayout.createParallelGroup()
				.addComponent(jill)
				.addComponent(opts[11])
				.addComponent(opts[12])
				.addComponent(opts[13])
				.addComponent(opts[14])
				.addComponent(opts[15])
				.addComponent(opts[16])
				.addComponent(opts[17])
				.addComponent(opts[18])
				.addComponent(opts[19]))
				);	
		setLayout(cpLayout);
		setPreferredSize(new Dimension(325,150));
		cellpick.setMaximumSize(new Dimension(250, 10));
		MBOTPick.setMaximumSize(new Dimension(250, 10));
		for (int ab = 0; ab < opts.length; ab++){
			opts[ab].setMaximumSize(new Dimension(10,10));opts[ab].setVisible(false);
			 opts[ab].setEnabled(false); opts[ab].addItemListener(this);}
		jack.setVisible(false); jill.setVisible(false);
		
	
	MBOTPick.setVisible(false);
	MBOTPick.setEnabled(false);
	
	cellpick.addActionListener(this);
	MBOTPick.addActionListener(this);

	}
	
public void setCOH(cellOptionHandler ned){ gate = ned;}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == cellpick){command = 1; setCell(); fireucEvent();}
	if(e.getSource() == MBOTPick){command = 2; MBOTtype = MBOTPick.getSelectedItem().toString(); setCell(); fireucEvent();}
	
	}

public void itemStateChanged(ItemEvent e){//age, fade, born, survives
	boolean option = false;
	for(int i = 0; i< opts.length; i++){
		if(e.getItemSelectable() == opts[i]){
		switch(i){
			case 0: gate.setBool("Ages", opts[i].getState()); break;
			case 1: gate.setBool("Fades", opts[i].getState()); break;
			case 2: gate.setBool("B0", opts[i].getState()); break;
			case 3: gate.setBool("B1", opts[i].getState()); break;
			case 4: gate.setBool("B2", opts[i].getState()); break;
			case 5: gate.setBool("B3", opts[i].getState()); break;
			case 6: gate.setBool("B4", opts[i].getState()); break;
			case 7: gate.setBool("B5", opts[i].getState()); break;
			case 8: gate.setBool("B6", opts[i].getState()); break;
			case 9: gate.setBool("B7", opts[i].getState()); break;
			case 10: gate.setBool("B8", opts[i].getState()); break;
			case 11: gate.setBool("S0", opts[i].getState()); break;
			case 12: gate.setBool("S1", opts[i].getState()); break;
			case 13: gate.setBool("S2", opts[i].getState()); break;
			case 14: gate.setBool("S3", opts[i].getState()); break;
			case 15: gate.setBool("S4", opts[i].getState()); break;
			case 16: gate.setBool("S5", opts[i].getState()); break;
			case 17: gate.setBool("S6", opts[i].getState()); break;
			case 18: gate.setBool("S7", opts[i].getState()); break;
			case 19: gate.setBool("S8", opts[i].getState()); break;
			}
		}
	}
}

private void setCell(){
	for(int cn = 0; cn < Cells.length; cn++){
		if(cellpick.getSelectedItem() == Cells[cn]){ ct = cn;}
		setOpts(gate.generateCell());
		if(ct == 1){MBOTPick.setVisible(true); MBOTPick.setEnabled(true);} else{MBOTPick.setVisible(false); MBOTPick.setEnabled(false);}
	}
}

private void setOpts(cell darwin){
	boolean visifier = false;
	String[] names = new String[]{"Age", "Fade", "Mat", "Born", "Survives"};
	for(int concount = 0; concount< names.length; concount++){
		switch(concount){
			case 3: if(MBOTtype == "Custom"){visifier = true;} else{visifier = false;} break;
			case 4: if(MBOTtype == "Custom"){visifier = true;} else{visifier = false;} break;
			
			default: visifier =  darwin.getControls(names[concount]);  break;
		}
		toggleControl(concount, visifier);
	}
}

private void toggleControl(int a, boolean b){
	switch(a){
		case 0: opts[0].setVisible(b); opts[0].setEnabled(b); break;
		case 1: opts[1].setVisible(b); opts[1].setEnabled(b); break;
		case 2: break;
		case 3: for(int c = 0; c < 9; c++){opts[c+2].setLabel(String.valueOf(c)); opts[c+2].setVisible(b); opts[c+2].setEnabled(b);} jack.setVisible(b); break;
		case 4: for(int c = 0; c < 9; c++){opts[c+11].setLabel(String.valueOf(c)); opts[c+11].setVisible(b); opts[c+11].setEnabled(b);} jill.setVisible(b); break;
	}
}

//event generation
//adds listeners for command events
public synchronized void adducListener(ucListener listener){
	_audience.add(listener);}
	
//removes listeners for command events	
public synchronized void removeucListener(ucListener listener){
	_audience.remove(listener);}
	
//notifies application when a command is sent	
private synchronized void fireucEvent(){
	ucEvent cmd = new ucEvent(this);
	cmd.setCommand(command);
	Iterator i = _audience.iterator();
	while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
	}
	
public int getCT(){ return ct;}

public String getMBOT(){ return MBOTtype;}
	
}
