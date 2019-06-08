package fourteener.worldeditor.worldeditor.scripts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class CraftscriptManager {
	
	// Stores registered scripts
	Map<String,Craftscript> registeredScripts = new HashMap<String,Craftscript>();
	
	// Create a new manager
	public static CraftscriptManager newManager () {
		return new CraftscriptManager();
	}
	
	// Register a new Craftscript, called by label label and handled by handler
	public boolean registerCraftscript (String label, Craftscript handler) {
		registeredScripts.put(label, handler);
		return true;
	}
	
	// Run the Craftscript label, with arguments args, and player player
	public boolean runCraftscript (String label, LinkedList<String> args, Player player) {
		UndoManager.getUndo(player).startTrackingConsolidatedUndo();
		try {
			List<BlockState> toStoreInUndo = registeredScripts.get(label).perform(args, player);
			if (toStoreInUndo == null && UndoManager.getUndo(player).getNumToConsolidate() == 0) {
				return UndoManager.getUndo(player).cancelConsolidatedUndo();
			}
			else {
				UndoManager.getUndo(player).storeUndo(UndoElement.newUndoElementFromStates(toStoreInUndo));
				return UndoManager.getUndo(player).storeConsolidatedUndo();
			}
		}
		catch (Exception e) {
			return UndoManager.getUndo(player).cancelConsolidatedUndo();
		}
	}
}
