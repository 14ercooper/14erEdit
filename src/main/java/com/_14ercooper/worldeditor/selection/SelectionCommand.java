package com._14ercooper.worldeditor.selection;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SelectionCommand {
    public static boolean performCommand(String[] args, Player player) {
	try {
	    // First, get the wand that this player owns
	    SelectionWand wand = null;
	    for (SelectionWand s : SelectionWandListener.wands) {
		if (s.owner.equals(player)) {
		    wand = s;
		    break;
		}
	    }
	    if (wand == null && (args[1].equalsIgnoreCase("pos1") || args[1].equalsIgnoreCase("pos2"))) {
		SelectionWand newWand = (SelectionWand.giveNewWand((player).getPlayer()));
		SelectionWandListener.wands.add(newWand);
		wand = newWand;
	    }
	    else if (wand == null && args[2].equalsIgnoreCase("load")
		    && (args[1].equalsIgnoreCase("schematic") || args[1].equalsIgnoreCase("schem"))) {
		SelectionWand newWand = (SelectionWand.giveNewWand((player).getPlayer()));
		SelectionWandListener.wands.add(newWand);
		wand = newWand;
	    }
	    if (wand == null) {
		return false;
	    }

	    // Then get the applicable wand manager
	    SelectionManager manager = wand.manager;

	    // Perform an operation
	    if (args[1].equalsIgnoreCase("op")) {
		try {
		    return operate(manager, wand, args);
		}
		catch (Exception e) {
		    Main.logError("Error performing operation. Do you have a selection box?", player, e);
		}
	    }

	    // Expand a selection
	    else if (args[1].equalsIgnoreCase("expand")) {
		return expand(manager, Double.parseDouble(args[2]), args[3], wand.owner);
	    }

	    // Copy selection to clipboard
	    else if (args[1].equalsIgnoreCase("copy")) {
		try {
		    Main.logDebug("Copying clipboard");
		    player.sendMessage("§aCopying clipboard.");
		    manager.clipboardOffset[0] = (int) (manager.getMostNegativeCorner()[0]
			    - player.getLocation().getBlockX());
		    manager.clipboardOffset[1] = (int) (manager.getMostNegativeCorner()[1]
			    - player.getLocation().getBlockY());
		    manager.clipboardOffset[2] = (int) (manager.getMostNegativeCorner()[2]
			    - player.getLocation().getBlockZ());
		    Main.logDebug("Offset " + manager.clipboardOffset[0] + " " + manager.clipboardOffset[1] + " "
			    + manager.clipboardOffset[2]);
		    manager.mirrorString = "";
		    return SchematicHandler.saveSchematic(getSchematicName(player), player);
		}
		catch (Exception e) {
		    Main.logError("Error copying clipboard.", player, e);
		}
	    }

	    // Paste the clipboard
	    else if (args[1].equalsIgnoreCase("paste")) {
		try {
		    Main.logDebug("Pasting clipboard");
		    player.sendMessage("§aPasting clipboard.");
		    try {
			return SchematicHandler.loadSchematic(getSchematicName(player), player, manager.mirrorString,
				Boolean.parseBoolean(args[2]), manager.clipboardOffset, manager.executionOrder);
		    }
		    catch (IndexOutOfBoundsException e) {
			return SchematicHandler.loadSchematic(getSchematicName(player), player, manager.mirrorString,
				true, manager.clipboardOffset, manager.executionOrder);
		    }
		}
		catch (Exception e) {
		    Main.logError("Error pasting clipboard.", player, e);
		}
	    }

	    // Mirror the clipboard
	    else if (args[1].equalsIgnoreCase("mirror")) {
		try {
		    Main.logDebug("Mirroring clipboard");
		    manager.mirrorString = args[2];
		    player.sendMessage("§aClipboard mirrored.");
		    return true;
		}
		catch (Exception e) {
		    Main.logError("Error mirroring clipboard. Did you provide a mirror axis(es)?", player, e);
		}
	    }

	    // Rotation
	    else if (args[1].equalsIgnoreCase("rotate")) {
		try {
		    Main.logDebug("Rotating clipboard");
		    manager.executionOrder = Integer.parseInt(args[2]);
		    player.sendMessage("§aClipboard rotated.");
		    return true;
		}
		catch (Exception e) {
		    Main.logError("Error rotating clipboard. Did you provide a mirror value?", player, e);
		}
	    }

	    // Shift origin/offset of clipboard
	    else if (args[1].equalsIgnoreCase("origin") && args[2].equalsIgnoreCase("shift")) {
		try {
		    Main.logDebug("Shifting clipboard origin");
		    player.sendMessage("§aClipboard origin shifted.");
		    manager.clipboardOffset[0] += Integer.parseInt(args[3]);
		    manager.clipboardOffset[0] += Integer.parseInt(args[4]);
		    manager.clipboardOffset[0] += Integer.parseInt(args[5]);
		    return true;
		}
		catch (Exception e) {
		    Main.logError("Error shifting clipboard origin. Did you provide an amount?", player, e);
		}
	    }

	    // Set origin/offset of the clipboard
	    else if (args[1].equalsIgnoreCase("origin") && args[2].equalsIgnoreCase("set")) {
		try {
		    Main.logDebug("Setting clipboard origin");
		    player.sendMessage("§aClipboard origin set.");
		    manager.clipboardOffset[0] = Integer.parseInt(args[3]);
		    manager.clipboardOffset[0] = Integer.parseInt(args[4]);
		    manager.clipboardOffset[0] = Integer.parseInt(args[5]);
		    return true;
		}
		catch (Exception e) {
		    Main.logError("Error setting clipboard origin. Did you provide a new offset?", player, e);
		}
	    }

	    // Reset a selection
	    else if (args[1].equalsIgnoreCase("reset")) {
		try {
		    player.sendMessage("§dRegion reset");
		    return manager.resetSelection();
		}
		catch (Exception e) {
		    Main.logError("Could not reset selection.", player, e);
		}
	    }

	    // Clone selection
	    else if (args[1].equalsIgnoreCase("clone")) {
		try {
		    BlockIterator b = manager.getBlocks(player.getWorld());
		    int[] offset = { 0, 0, 0 };
		    offset[0] = Integer.parseInt(args[2]);
		    offset[1] = Integer.parseInt(args[3]);
		    offset[2] = Integer.parseInt(args[4]);
		    int times = Integer.parseInt(args[5]);
		    boolean delOriginal = Boolean.parseBoolean(args[6]);
		    GlobalVars.asyncManager.scheduleEdit(b, offset, times, delOriginal, player);
		    return true;
		}
		catch (Exception e) {
		    Main.logError("Could not set up selection clone. Please check your syntax.", player, e);
		}
	    }

	    // Update pos1
	    else if (args[1].equalsIgnoreCase("pos1")) {
		try {
		    if (args.length > 4) {
			int x = 0, y = 0, z = 0;
			// X with relative
			if (args[2].contains("~")) {
			    x = Integer.parseInt(args[2].replaceAll("~", "")) + player.getLocation().getBlockX();
			}
			else {
			    x = Integer.parseInt(args[2]);
			}
			// Y with relative
			if (args[3].contains("~")) {
			    y = Integer.parseInt(args[3].replaceAll("~", "")) + player.getLocation().getBlockY();
			}
			else {
			    y = Integer.parseInt(args[3]);
			}
			// Z with relative
			if (args[4].contains("~")) {
			    z = Integer.parseInt(args[4].replaceAll("~", "")) + player.getLocation().getBlockZ();
			}
			else {
			    z = Integer.parseInt(args[4]);
			}
			return manager.updatePositionOne(x, y, z, wand.owner);
		    }
		    return manager.updatePositionOne(player.getLocation().getBlockX(), player.getLocation().getBlockY(),
			    player.getLocation().getBlockZ(), wand.owner);
		}
		catch (Exception e) {
		    Main.logError("Failed to update first position.", player, e);
		}
	    }

	    // Update pos2
	    else if (args[1].equalsIgnoreCase("pos2")) {
		try {
		    if (args.length > 4) {
			int x = 0, y = 0, z = 0;
			// X with relative
			if (args[2].contains("~")) {
			    x = Integer.parseInt(args[2].replaceAll("~", "")) + player.getLocation().getBlockX();
			}
			else {
			    x = Integer.parseInt(args[2]);
			}
			// Y with relative
			if (args[3].contains("~")) {
			    y = Integer.parseInt(args[3].replaceAll("~", "")) + player.getLocation().getBlockY();
			}
			else {
			    y = Integer.parseInt(args[3]);
			}
			// Z with relative
			if (args[4].contains("~")) {
			    z = Integer.parseInt(args[4].replaceAll("~", "")) + player.getLocation().getBlockZ();
			}
			else {
			    z = Integer.parseInt(args[4]);
			}
			return manager.updatePositionTwo(x, y, z, wand.owner);
		    }
		    return manager.updatePositionTwo(player.getLocation().getBlockX(), player.getLocation().getBlockY(),
			    player.getLocation().getBlockZ(), wand.owner);
		}
		catch (Exception e) {
		    Main.logError("Failed to update second position.", player, e);
		}
	    }

	    Main.logError("Could not find a selection subcommand. Did you provide a valid one?", player, null);
	    return false;
	}
	catch (Exception e) {
	    Main.logError("Error in selection command. Please check your syntax.", player, e);
	    return false;
	}
    }

    // Operate on the selection
    private static boolean operate(SelectionManager manager, SelectionWand wand, String[] brushOperation) {
		// Build an array of blocks within this selection
		BlockIterator blockArray = manager.getBlocks(wand.owner.getWorld());
		Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

		// Construct the operation
		int brushOpOffset = 2;
		List<String> opArray = new LinkedList<>(Arrays.asList(brushOperation));
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
	Operator operator = new Operator(opStr, wand.owner);

	GlobalVars.asyncManager.scheduleEdit(operator, wand.owner, blockArray);
	GlobalVars.errorLogged = false;

	return true;
    }

    // Expand the selection
    // Note that using the wand afterwards doesn't reflect the changes
    private static boolean expand(SelectionManager manager, double amt, String dir, Player player) {
	return manager.expandSelection(amt, dir, player);
    }

    // Get a schematic file name for a player
    private static String getSchematicName(Player p) {
	return p.getDisplayName() + "_clipboard";
    }
}
