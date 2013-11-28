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

public class masterControl extends JComponent implements ActionListener, ChangeListener{
	// Main Controls
	JButton[] buttons = new JButton[9];
	JSlider throttle;
	
	// relate to sending command events
	private ArrayList<ucListener> _audience = new ArrayList<ucListener>();
	int cntrl = 0;
	
	//flags
	//is there an active automaton to control?
	boolean cflag = false;
	
	public masterControl(){
		// create main controls
		buttons[0] = new JButton("New");
		buttons[1] = new JButton("Play/Pause");
		buttons[2] = new JButton("Step");
		buttons[3] = new JButton("State Editor");
		buttons[4] = new JButton("Cell Editor");
		buttons[5] = new JButton("Cell Picker");
		buttons[6] = new JButton("Selection Tools");
		buttons[7] = new JButton("Brushes");
		buttons[8] = new JButton("About");
		throttle = new JSlider(0,1000);
		// set layout
		setLayout( new FlowLayout() );
		
		// add controls
		for( int c = 0; c<= buttons.length-1; c++){
			if(c == 2){add(throttle); throttle.addChangeListener(this);}
			add(buttons[c]); buttons[c].addActionListener(this);
		}
		
		}
	
	public void actionPerformed(ActionEvent e){
		//create a new automaton
		if(e.getSource() == buttons[0]){if(!cflag){ cntrl = 1; cflag = true; fireucEvent();}}
		if(e.getSource() == buttons[8]){ aboutMe();}
		if(cflag){
			/* send commands based on what button is pressed
			 *  1 = New
			 *  2 = Play/Pause
			 *  3 = Step
			 *  4 = Edit State
			 *  5 = Edit Cells
			 *  6 = Cell Picker
			 *  6 = Selection Tools
			 *  7 = Brushes
			 *  8 = Set Iteration Speed
			 */ 
			for( int cnum = 1; cnum <= buttons.length-2; cnum++){
				if(e.getSource() == buttons[cnum]){ cntrl = cnum+1; fireucEvent();}
			}
			
		}
		}
		
		public void stateChanged(ChangeEvent e){
			if(cflag){
			if(e.getSource() == throttle) { cntrl = 9; fireucEvent();}
		}
		}
		
		// get control values
		public int getZTime(){
			return throttle.getValue();}
		
		//control event generation
		//adds listeners for command events
		public synchronized void adducListener(ucListener listener){
		_audience.add(listener);}
	
		//removes listeners for command events	
		public synchronized void removeucListener(ucListener listener){
		_audience.remove(listener);}
	
		
		//notifies application when a command is sent	
		private synchronized void fireucEvent(){
		ucEvent cmd = new ucEvent(this);
		cmd.setCommand(cntrl);
		Iterator i = _audience.iterator();
		while(i.hasNext()){
		((ucListener) i.next()).handleControl(cmd);}
		}
		
		public void aboutMe(){
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
  String noticem = "The GPL is appended to the end of the file cellularExplorer.java";
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
