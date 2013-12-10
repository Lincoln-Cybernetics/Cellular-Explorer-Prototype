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
String[] Cells = new String[]{"Cell", "OffCell", "OnCell", "Blink", "Sequence", "Random", "Life", "Seeds", "Parity", "Conveyor", 
"Wolfram", "Symmetrical", "Mirror", "Majority", "Gnarl", "Amoeba", "HighLife", "Prime", "Day and Night"};

// relate to sending command events
private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
int command = 0;
int ct = 0;

public cellPicker(){
	cellpick = new JComboBox(Cells);
	
	
	setLayout(new FlowLayout());
	add(cellpick);
	
	cellpick.addActionListener(this);

	}

public void actionPerformed(ActionEvent e){
	if(e.getSource() == cellpick){command = 1; setCell(); fireucEvent();}
	
	}

public void itemStateChanged(ItemEvent e){}

public void setCell(){
	for(int cn = 0; cn < Cells.length; cn++){
		if(cellpick.getSelectedItem() == Cells[cn]){ ct = cn;}
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
	
}
