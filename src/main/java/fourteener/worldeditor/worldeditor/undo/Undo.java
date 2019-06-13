package fourteener.worldeditor.worldeditor.undo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.Main;

public class Undo {
	// Whose undo is this?
	public Player owner;
	
	// How many undos and redos should be stored
	public static int undoSize = 25;
	
	// Stores undo and redo elements
	private LinkedList<UndoElement> undoElements = new LinkedList<UndoElement>();
	private LinkedList<UndoElement> redoElements = new LinkedList<UndoElement>();
	
	// For consolidating undos
	private int isConsolidating = 0;
	private int numToConsolidate = 0;
	private List<BlockState> consolidatedUndoStorage = new ArrayList<BlockState>();
	private List<Location> storedLocations = new ArrayList<Location>();
	
	
	// Create a new undo tracker for a player
	public static Undo newUndo (Player player) {
		Undo u = new Undo();
		u.owner = player;
		return u;
	}
	
	// Store a world change into the tracker
	public boolean storeUndo (UndoElement e) {
		// Not consolidating an undo
		if (isConsolidating == 0) {
			Main.logDebug("Storing a normal undo"); // -----
			undoElements.add(e);
			if (undoElements.size() > undoSize)
				undoElements.removeFirst();
			return true;
		}
		// We are consolidating an undo, so add the element to the queue
		else {
			List<BlockState> states = e.getBlocks();

			try {
				if (states == null) {
					return true;
				}
				else if (states.isEmpty()) {
					return true;
				}
				else if (states == (new ArrayList<BlockState>())) {
					return true;
				}
			}
			catch (NullPointerException error) {
				Main.logDebug("Nullptr in Undo::storeUndo"); // -----
				return true;
			}
			Main.logDebug("Number of locations stored: " + Integer.toString(storedLocations.size())); // -----
			Main.logDebug("Storing " + Integer.toString(states.size()) + " blocks to the consolidated undo queue"); // -----
			int c = 0;
			for (BlockState bs : states) {
				if (storedLocations.contains(bs.getLocation())) {
					continue;
				}
				else {
					storedLocations.add(bs.getLocation());
					consolidatedUndoStorage.add(bs);
					numToConsolidate++;
					c++;
				}
			}
			Main.logDebug("Added " + Integer.toString(c) + " new blocks to the consolidated undo queue"); // -----
			return true;
		}
	}
	// Store a world change into the tracker
	private boolean storeRedo (UndoElement e) {
		redoElements.add(e);
		if (redoElements.size() > undoSize)
			redoElements.removeFirst();
		return true;
	}
	
	// Start tracking a consolidated undo
	public void startTrackingConsolidatedUndo () {
		Main.logDebug("Now tracking consolidated undo"); // -----
		isConsolidating++;
		Main.logDebug("Consolidations nested: " + Integer.toString(isConsolidating)); // -----
	}
	
	// Get the number of undos in the consolidation queue
	public int getNumToConsolidate () {
		return numToConsolidate;
	}
	
	// Cancel a consolidated undo
	public boolean cancelConsolidatedUndo () {
		Main.logDebug("Cancelling consolidated undo"); // -----
		isConsolidating--;
		if (isConsolidating <= 0) {
			Main.logDebug("Full cancel"); // -----
			isConsolidating = 0;
			numToConsolidate = 0;
			consolidatedUndoStorage = new ArrayList<BlockState>();
			storedLocations = new ArrayList<Location>();
		}
		return true;
	}
	
	// Store a consolidated undo
	public boolean storeConsolidatedUndo () {
		Main.logDebug("Storing a consolidated undo"); // -----
		if (numToConsolidate == 0) {
			return cancelConsolidatedUndo ();
		}
		isConsolidating--;
		if (isConsolidating <= 0) {
			Main.logDebug("Writing to regular undo"); // -----
			isConsolidating = 0;
			this.storeUndo (UndoElement.newUndoElementFromStates(consolidatedUndoStorage));
			numToConsolidate = 0;
			consolidatedUndoStorage = new ArrayList<BlockState>();
			storedLocations = new ArrayList<Location>();
		}
		return true;
	}
	
	// Undo a number of changes
	public boolean undoChanges (int number) {
		if (number > undoElements.size()) {
			number = undoElements.size();
		}
		if (number < 1) {
			number = 1;
		}
		
		// Loop through the needed number of undos
		while (number-- > 0) {
			// Grab the UndoElement
			UndoElement element = undoElements.getLast();
			Main.logDebug("Undoing " + Integer.toString(element.getBlocks().size()) + " block changes"); // -----
			
			// First, register a redo element
			storeRedo(element.getInverseElement());
			
			// Then perform the undo
			element.applyElement();
			
			// And delete the undo element
			undoElements.removeLast();
		}
		return true;
	}
	
	// Redo a number of changes
	public boolean redoChanges (int number) {
		if (number > redoElements.size())
			number = redoElements.size();
		if (number < 1)
			number = 1;
		
		// Loop through the needed number of redos
		while (number-- > 0) {
			// This follows the same logic as the loop in undo changes
			UndoElement element = redoElements.getLast();
			Main.logDebug("Redoing " + Integer.toString(element.getBlocks().size()) + " block changes"); // -----
			storeUndo(element.getInverseElement());
			element.applyElement();
			redoElements.removeLast();
		}
		return true;
	}
}
