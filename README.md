Cellular-Explorer-Prototype
===========================

This is a proof of concept prototype for a mixed-rules cellular automata program.  To see what mixed rules can do,
press the "demo" button and then the "Play/Pause" button.  The "New Culture" button will generate an empty array
of Conway's Game of Life.  The "Randomize" button will set each cell's on/off state randomly.  The "Clear" button
sets all cells to the off state.  Next to the "Play/Pause" button, there is a spinner that can set the speed of the
 automaton to one of four speeds.(Very Slow: 2000ms pause between iterations, Slow: 200ms, Fast: 20ms, and  
 Very Fast: 20ms) 
 
 The "Edit State" button will allow you to turn cells on or off by clicking on them. 
Draging the mouse will invert the state of cells that are draged over while in the edit state mode.

The "Edit Cells" button will allow you to change the ruleset of each cell by clicking or dragging the mouse.  The 
spinner next to the "Edit Cells" button selects the type of cell that is created in the grid.
The rule sets are as follows:
Black: always off
White: always on
Red:  blinks on and off (starts on)
Blue:  blinks on and off(starts off)
Orange: cell changes state randomly
Green: Conway's Game of Life (B3/S2)
Pink:   turns on if an odd number of its neighbors are on (otherwise off)
Yellow: turns on if an even number of its neighbors are on (otherwise off)
Grey: takes on the value of one of its neighbors (defaults to the neighbor 
directly beneath the cell, no support for other neighbors, yet)
Next to the  cell-type selector, is the maturity selector, which is used to select how many turns each cell goes between
state updates.
To return to normal running after Editing the cells or their states, press "Play/Pause".

The Cell Fill button can be used in or out of cell editing, and sets all cells in the array to the selected type.

The Cell CheckFill button is accompanied by a second cell-selection spinner.  Used in conjunction, these allow the user to
fill the automaton with a checkerboard pattern of cells alternating between the types selected in the two cell-selection spinners.
Next is the second maturity selector, which determines the number of turns between state updates for the second selected cell-type.

The "Create Monster" button fills the automaton with randomly selected cells, (generally resulting in pseudorandom noise).

The "Cell Check Edit" allows drawing cells in a checker-board pattern.  It works like the Check Fill button, but
without filling the entire array.

The "3x3" checkbox enables a 3x3 brush size in the Cell Editing function, for laying down large areas conveniently.



Created by Lincoln Cybernetics Novelty Computing Division

For more info see 
www.LincolnCybernetics.com
or www.Facebook.com/LincolnCybernetics

