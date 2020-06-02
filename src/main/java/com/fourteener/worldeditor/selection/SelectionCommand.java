package com.fourteener.worldeditor.selection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;

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
		if (wand == null && (args[1].equalsIgnoreCase("pos1") || args[1].equalsIgnoreCase("pos2"))) {
			SelectionWand newWand = (SelectionWand.giveNewWand((player).getPlayer()));
			SelectionWandListener.wands.add(newWand);
			wand = newWand;
		}
		else if (wand == null && args[2].equalsIgnoreCase("load") && (args[1].equalsIgnoreCase("schematic") || args[1].equalsIgnoreCase("schem"))) {
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
			return operate(manager, wand, args);
		}
		
		// Expand a selection
		else if (args[1].equalsIgnoreCase("expand")) {
			return expand(manager, Double.parseDouble(args[2]), args[3], wand.owner);
		}
		
		// Copy selection to clipboard
		else if (args[1].equalsIgnoreCase("copy")) {
			Main.logDebug("Copying clipboard");
			manager.clipboardOffset[0] = (int) (manager.getMostNegativeCorner()[0] - player.getLocation().getBlockX());
			manager.clipboardOffset[1] = (int) (manager.getMostNegativeCorner()[1] - player.getLocation().getBlockY());
			manager.clipboardOffset[2] = (int) (manager.getMostNegativeCorner()[2] - player.getLocation().getBlockZ());
			Main.logDebug("Offset " + manager.clipboardOffset[0] + " " + manager.clipboardOffset[1] + " " + manager.clipboardOffset[2]);
			return SchematicHandler.saveSchematic(getSchematicName(player), player);
		}
		
		// Paste the clipboard
		else if (args[1].equalsIgnoreCase("paste")) {
			Main.logDebug("Pasting clipboard");
			try {
				return SchematicHandler.loadSchematic(getSchematicName(player), player, manager.mirrorString, Boolean.parseBoolean(args[2]), manager.clipboardOffset);
			} catch (IndexOutOfBoundsException e) {
				return SchematicHandler.loadSchematic(getSchematicName(player), player, manager.mirrorString, true, manager.clipboardOffset);
			}
		}
		
		// Mirror the clipboard
		else if (args[1].equalsIgnoreCase("mirror")) {
			Main.logDebug("Mirroring clipboard");
			manager.mirrorString = args[2];
			return true;
		}
		
		// Shift origin/offset of clipboard
		else if (args[1].equalsIgnoreCase("origin") && args[2].equalsIgnoreCase("shift")) {
			Main.logDebug("Shifting clipboard origin");
			manager.clipboardOffset[0] += Integer.parseInt(args[3]);
			manager.clipboardOffset[0] += Integer.parseInt(args[4]);
			manager.clipboardOffset[0] += Integer.parseInt(args[5]);
			return true;
		}
		
		// Set origin/offset of the clipboard
		else if (args[1].equalsIgnoreCase("origin") && args[2].equalsIgnoreCase("set")) {
			Main.logDebug("Setting clipboard origin");
			manager.clipboardOffset[0] = Integer.parseInt(args[3]);
			manager.clipboardOffset[0] = Integer.parseInt(args[4]);
			manager.clipboardOffset[0] = Integer.parseInt(args[5]);
			return false;
		}
		
		// Reset a selection
		else if (args[1].equalsIgnoreCase("reset")) {
			player.sendMessage("§dRegion reset");
			return manager.resetSelection();
		}
		
		// Update pos1
		else if (args[1].equalsIgnoreCase("pos1")) {
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
			return manager.updatePositionOne(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), wand.owner);
		}
		
		// Update pos2
		else if (args[1].equalsIgnoreCase("pos2")) {
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
			return manager.updatePositionTwo(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), wand.owner);
		}
		
		else {
			return false;
		}
	}
	
	// Operate on the selection
	@SuppressWarnings("static-access")
	private static boolean operate (SelectionManager manager, SelectionWand wand, String[] brushOperation) {
		// Build an array of blocks within this selection
		BlockIterator blockArray = manager.getBlocks();
		Main.logDebug("Block array size is " + Long.toString(blockArray.getTotalBlocks())); // -----
		
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
		Operator operator = new Operator(opStr);
		
		GlobalVars.asyncManager.scheduleEdit(operator, wand.owner, blockArray);

		return true;
	}
	
	// Expand the selection
	// Note that using the wand afterwards doesn't reflect the changes
	private static boolean expand (SelectionManager manager, double amt, String dir, Player player) {
		return manager.expandSelection(amt, dir, player);
	}
	
	// Get a schematic file name for a player
	private static String getSchematicName(Player p) {
		return p.getDisplayName() + "_clipboard";
	}
}
