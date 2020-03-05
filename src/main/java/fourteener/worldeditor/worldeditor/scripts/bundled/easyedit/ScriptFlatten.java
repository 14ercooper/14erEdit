package fourteener.worldeditor.worldeditor.scripts.bundled.easyedit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.worldeditor.scripts.Craftscript;
import fourteener.worldeditor.worldeditor.selection.SelectionManager;

public class ScriptFlatten extends Craftscript {

	@Override
	public Set<BlockState> perform(LinkedList<String> args, Player player, String label) {
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
				player.performCommand("fx br s 0 0.5 $ flatten{" + args.get(2) + ";" + "false" + ";" + args.get(0) + ";" + args.get(1) + "}");
			}
		}
		else if (label.equalsIgnoreCase("absflatten")) {
			if (useSelection) {
				return absoluteSelectionFlatten(player, height, block);
			}
			else {
				player.performCommand("fx br s 0 0.5 $ flatten{" + args.get(2) + ";" + "true" + ";" + args.get(0) + ";" + args.get(1) + "}");
			}
		}
		return null;
	}

	private Set<BlockState> selectionFlatten(Player player, double height, Material block) {
		SelectionManager sm = SelectionManager.getSelectionManager(player);
		LinkedList<BlockState> operatedBlocks = null;
		Set<BlockState> snapshotArray = new HashSet<BlockState>();
		if (sm != null) {
			double[] negCorner = sm.getMostNegativeCorner();
			double[] posCorner = sm.getMostPositiveCorner();
			operatedBlocks = new LinkedList<BlockState>();

			// Generate the box
			List<Block> blockArray = new ArrayList<Block>();
			for (int rx = (int) negCorner[0]; rx <= posCorner[0]; rx++) {
				for (int rz = (int) negCorner[2]; rz <= posCorner[2]; rz++) {
					for (int ry = (int) negCorner[1]; ry <= posCorner[1]; ry++) {
						blockArray.add(GlobalVars.world.getBlockAt(rx, ry, rz));
					}
				}
			}
			Main.logDebug("Block array size: " + Integer.toString(blockArray.size())); // ----
			
			// Create a snapshot array
			for (Block b : blockArray) {
				snapshotArray.add(b.getState());
			}
			blockArray = null;
			Main.logDebug(Integer.toString(snapshotArray.size()) + " blocks in snapshot array");
			
			for (BlockState bs : snapshotArray) {
				Block b = GlobalVars.world.getBlockAt(bs.getLocation());
				int yB = bs.getY();
				
				if (yB <= Math.round(height)) {
					BlockState state = b.getState();
					state.setType(block);
					operatedBlocks.add(state);
				}
				else {
					BlockState state = b.getState();
					state.setType(Material.AIR);
					operatedBlocks.add(state);
				}
			}
		}

		Main.logDebug("Operated on and now placing " + Integer.toString(operatedBlocks.size()) + " blocks");
		// Apply the changes to the world
		for (BlockState bs : operatedBlocks) {
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			b.setType(bs.getType());
			b.setBlockData(bs.getBlockData());
		}
		
		return snapshotArray;
	}

	private Set<BlockState> absoluteSelectionFlatten(Player player, double height, Material block) {
		SelectionManager sm = SelectionManager.getSelectionManager(player);
		LinkedList<BlockState> operatedBlocks = null;
		Set<BlockState> snapshotArray = new HashSet<BlockState>();
		if (sm != null) {
			double[] negCorner = sm.getMostNegativeCorner();
			double[] posCorner = sm.getMostPositiveCorner();
			operatedBlocks = new LinkedList<BlockState>();

			// Generate the box
			List<Block> blockArray = new ArrayList<Block>();
			for (int rx = (int) negCorner[0]; rx <= posCorner[0]; rx++) {
				for (int rz = (int) negCorner[2]; rz <= posCorner[2]; rz++) {
					for (int ry = 0; ry <= 255; ry++) {
						blockArray.add(GlobalVars.world.getBlockAt(rx, ry, rz));
					}
				}
			}
			Main.logDebug("Block array size: " + Integer.toString(blockArray.size())); // ----
			
			// Create a snapshot array
			for (Block b : blockArray) {
				snapshotArray.add(b.getState());
			}
			blockArray = null;
			Main.logDebug(Integer.toString(snapshotArray.size()) + " blocks in snapshot array");
			
			for (BlockState bs : snapshotArray) {
				Block b = GlobalVars.world.getBlockAt(bs.getLocation());
				int yB = bs.getY();
				
				if (yB <= Math.round(height)) {
					BlockState state = b.getState();
					state.setType(block);
					operatedBlocks.add(state);
				}
				else {
					BlockState state = b.getState();
					state.setType(Material.AIR);
					operatedBlocks.add(state);
				}
			}
		}
		
		Main.logDebug("Operated on and now placing " + Integer.toString(operatedBlocks.size()) + " blocks");
		// Apply the changes to the world
		for (BlockState bs : operatedBlocks) {
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			b.setType(bs.getType());
			b.setBlockData(bs.getBlockData());
		}
		
		return snapshotArray;
	}
}
