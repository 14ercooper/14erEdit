package fourteener.worldeditor.worldeditor.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class SelectionCommand {
	public static boolean performCommand (String[] args, Player player) {
		// First, get the wand that this player owns
		SelectionWand wand = null;
		for (SelectionWand s : SelectionWandListener.wands) {
			if (s.owner.equals(player)) {
				wand = s;
				break;
			}
		}
		if (wand == null) {
			return false;
		}
		
		// Then get the applicable wand manager
		SelectionManager manager = wand.manager;
		
		// This switch statement calls the various commands that can be done to the selection
		if (args[1].equalsIgnoreCase("op")) {
			return operate(manager, wand, args);
		} else if (args[1].equalsIgnoreCase("expand")) {
			return expand(manager, Double.parseDouble(args[2]), args[3], wand.owner);
		} else if (args[1].equalsIgnoreCase("reset")) {
			player.sendMessage("§dRegion reset");
			return manager.resetSelection();
		} else {
			return false;
		}
	}
	
	// Operate on the selection
	private static boolean operate (SelectionManager manager, SelectionWand wand, String[] brushOperation) {
		// Build an array of blocks within this selection
		double[] pos1 = manager.getMostNegativeCorner();
		double[] pos2 = manager.getMostPositiveCorner();
		List<Block> blockArray = new ArrayList<Block>();
		for (int x = (int) pos1[0]; x <= pos2[0]; x++) {
			for (int y = (int) pos1[1]; y <= pos2[1]; y++) {
				for (int z = (int) pos1[2]; z <= pos2[2]; z++) {
					blockArray.add(Main.world.getBlockAt(x, y, z));
				}
			}
		}
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Block array size is " + Integer.toString(blockArray.size())); // -----
		
		// Store an undo
		UndoManager.getUndo(wand.owner).storeUndo(UndoElement.newUndoElement(blockArray));
		
		// Construct the operation
		int brushOpOffset = 2;
		List<String> opArray = new LinkedList<String>(Arrays.asList(brushOperation));
		while (brushOpOffset > 0) {
			opArray.remove(0);
			brushOpOffset--;
		}
		// Construct the string
		String opStr = "";
		for (String s : opArray) {
			opStr = opStr.concat(s).concat(" ");
		}
		// And turn the string into an operation
		Operator operator = Operator.newOperator(opStr);
		
		// Finally, perform the operation
		if (operator == null)
			return false;
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Operating on selection"); // -----
		for (Block b : blockArray) {
			operator.operateOnBlock(b, wand.owner);
		}
		return true;
	}
	
	// Expand the selection
	// Note that using the wand afterwards doesn't reflect the changes
	private static boolean expand (SelectionManager manager, double amt, String dir, Player player) {
		return manager.expandSelection(amt, dir, player);
	}
}
