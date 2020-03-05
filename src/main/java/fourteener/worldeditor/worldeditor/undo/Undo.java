package fourteener.worldeditor.worldeditor.undo;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.Main;

public class Undo {
	// Whose undo is this?
	public Player owner;
	
	// How many undos and redos should be stored
	public static int undoSize = 25;
	
	// Stores undo and redo elements
	private ArrayDeque<UndoElement> undoElements = new ArrayDeque<UndoElement>();
	private ArrayDeque<UndoElement> redoElements = new ArrayDeque<UndoElement>();
	
	// For consolidating undos
	private int isConsolidating = 0;
	private int numToConsolidate = 0;
	private Set<BlockState> consolidatedUndoStorage = new HashSet<BlockState>();
	//private List<Location> storedLocations = new ArrayList<Location>();
	
	
	// Create a new undo tracker for a player
	public Undo(Player player) {
		owner = player;
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
			Set<BlockState> states = e.getBlocks();

			try {
				if (states == null) {
					return true;
				}
				else if (states.isEmpty()) {
					return true;
				}
			}
			catch (NullPointerException error) {
				Main.logDebug("Nullptr in Undo::storeUndo"); // -----
				return true;
			}
			Main.logDebug("Number of blocks registered: " + Integer.toString(consolidatedUndoStorage.size())); // -----
			Main.logDebug("Storing " + Integer.toString(states.size()) + " blocks to the consolidated undo queue"); // -----
			for (BlockState bs : states) {
				consolidatedUndoStorage.add(bs);
			}
			numToConsolidate++;
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
			consolidatedUndoStorage = new HashSet<BlockState>();
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
			this.storeUndo (new UndoElement(consolidatedUndoStorage));
			numToConsolidate = 0;
			consolidatedUndoStorage = new HashSet<BlockState>();
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
