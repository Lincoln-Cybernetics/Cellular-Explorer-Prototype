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
// controlPanel variables
JComboBox cellpick;
String[] Cells = new String[]{"Cell","MBOT"};

JComboBox  MBOTPick;
String[] MBOTCells = new String[]{"2x2", "3/4 Life", "Amoeba", "Assimilation", "Coagulations", "Coral", "Day and Night", "Diamoeba", "Dot Life",
"Dry Life", "Fredkin", "Gnarl", "High Life", "Life", "Life without Death", "Live Free or Die", "Long Life", "Maze", "Mazectric",
"Move", "Pseudo-life", "Replicator", "Seeds", "Serviettes", "Stains", "Vote", "Vote 4/5", "Walled Cities"};
String MBOTtype = "2x2";

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
int ct = 0;

public cellPicker(){
	cellpick = new JComboBox(Cells);
	MBOTPick = new JComboBox(MBOTCells);
	
	setLayout(new FlowLayout());
	add(cellpick);
	add(MBOTPick);
	MBOTPick.setVisible(false);
	MBOTPick.setEnabled(false);
	
	cellpick.addActionListener(this);
	MBOTPick.addActionListener(this);

	}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == cellpick){command = 1; setCell(); fireucEvent();}
	if(e.getSource() == MBOTPick){command = 2; MBOTtype = MBOTPick.getSelectedItem().toString(); fireucEvent();}
	
	}

public void itemStateChanged(ItemEvent e){}

private void setCell(){
	for(int cn = 0; cn < Cells.length; cn++){
		if(cellpick.getSelectedItem() == Cells[cn]){ ct = cn;}
		if(ct == 1){MBOTPick.setVisible(true); MBOTPick.setEnabled(true);} else{MBOTPick.setVisible(false); MBOTPick.setEnabled(false);}
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
