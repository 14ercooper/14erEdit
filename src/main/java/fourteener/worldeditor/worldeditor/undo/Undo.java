package fourteener.worldeditor.worldeditor.undo;

import java.util.LinkedList;

import org.bukkit.entity.Player;

public class Undo {
	// Whose undo is this?
	public Player owner;
	
	// Stores undo and redo elements
	private LinkedList<UndoElement> undoElements = new LinkedList<UndoElement>();
	private LinkedList<UndoElement> redoElements = new LinkedList<UndoElement>();
	
	// Create a new undo tracker for a player
	public static Undo newUndo (Player player) {
		Undo u = new Undo();
		u.owner = player;
		return u;
	}
	
	// Store a world change into the tracker
	public boolean storeUndo (UndoElement e) {
		undoElements.add(e);
		return true;
	}
	// Store a world change into the tracker
	private boolean storeRedo (UndoElement e) {
		redoElements.add(e);
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
			
			// First, register a redo element
			storeRedo(element.getInverseElement());
			
			// Then perform the undo
			element.applyElement();
			
			// And delete the undo element
			undoElements.removeLast();
		}
		return true;
	}
	
	// Undo a number of changes
	public boolean redoChanges (int number) {
		if (number > redoElements.size())
			number = redoElements.size();
		
		// Loop through the needed number of redos
		while (number-- > 0) {
			// This follows the same logic as the loop in undo changes
			UndoElement element = redoElements.getLast();
			storeUndo(element.getInverseElement());
			element.applyElement();
			redoElements.removeLast();
		}
		return true;
	}
}
