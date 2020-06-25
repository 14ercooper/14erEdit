package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import com._14ercooper.worldeditor.selection.SelectionManager;

public class ScriptFlatten extends Craftscript {

	@Override
	public boolean perform(LinkedList<String> args, Player player, String label) {
		try {
			boolean useSelection = true;
			double height = Double.parseDouble(args.get(0));
			Material block = Material.matchMaterial(args.get(1));
			if (args.size() > 2) {
				useSelection = false;
			}
			if (label.equalsIgnoreCase("flatten")) {
				if (useSelection) {
					return selectionFlatten(player, height, block);
				}
				else {
					return player.performCommand("fx br s 0 0.5 $ flatten{" + args.get(2) + ";" + "false" + ";" + args.get(0) + ";" + args.get(1) + "}");
				}
			}
			else if (label.equalsIgnoreCase("absflatten")) {
				if (useSelection) {
					return absoluteSelectionFlatten(player, height, block);
				}
				else {
					return player.performCommand("fx br s 0 0.5 $ flatten{" + args.get(2) + ";" + "true" + ";" + args.get(0) + ";" + args.get(1) + "}");
				}
			}
			return false;
		} catch (Exception e) {
			Main.logError("Could not parse flatten script. Did you pass in the correct arguments?", Operator.currentPlayer);
			return false;
		}
	}

	private boolean selectionFlatten(Player player, double height, Material block) {
		SelectionManager sm = SelectionManager.getSelectionManager(player);
		if (sm != null) {
			double[] negCorner = sm.getMostNegativeCorner();
			double[] posCorner = sm.getMostPositiveCorner();

			// Generate the box
			for (int rx = (int) negCorner[0]; rx <= posCorner[0]; rx++) {
				for (int rz = (int) negCorner[2]; rz <= posCorner[2]; rz++) {
					for (int ry = (int) negCorner[1]; ry <= posCorner[1]; ry++) {
						if (ry <= Math.round(height)) {
							Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
							SetBlock.setMaterial(b, block);
						}
						else {
							Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
							SetBlock.setMaterial(b, Material.AIR);
						}
					}
				}
			}
		}
		
		return true;
	}

	private boolean absoluteSelectionFlatten(Player player, double height, Material block) {
		SelectionManager sm = SelectionManager.getSelectionManager(player);
		if (sm != null) {
			double[] negCorner = sm.getMostNegativeCorner();
			double[] posCorner = sm.getMostPositiveCorner();

			// Generate the box
			for (int rx = (int) negCorner[0]; rx <= posCorner[0]; rx++) {
				for (int rz = (int) negCorner[2]; rz <= posCorner[2]; rz++) {
					for (int ry = 0; ry <= 255; ry++) {
						if (ry <= Math.round(height)) {
							Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
							SetBlock.setMaterial(b, block);
						}
						else {
							Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
							SetBlock.setMaterial(b, Material.AIR);
						}
					}
				}
			}
		}
		
		return true;
	}
}
