package fourteener.worldeditor.worldeditor.macros.macros;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class ErodeMacro extends Macro {
	
	public int erodeRadius = -1; // The radius to actually erode within
	public int erodeType = -1; // 0 for melt, 1 for blendball
	public int erodeSubtype = -1; // -1 if no subtype, 0 for more subtractive, 1 for more additive, 2 for neutral
	public Location erodeCenter;
	
	public static ErodeMacro createMacro (String[] args, Location loc) {
		ErodeMacro macro = new ErodeMacro();
		macro.erodeRadius = Integer.parseInt(args[0]);
		macro.erodeCenter = loc;
		
		// Determine the type of the erode brush
		if (args[1].equalsIgnoreCase("melt")) {
			Main.logDebug("Erode type: melt"); // ----
			macro.erodeType = 0;
		} else if (args[1].equalsIgnoreCase("blend")) {
			Main.logDebug("Erode type: blend"); // ----
			macro.erodeType = 1;
		}
		
		// Cut or raise melt?
		if (macro.erodeType == 0) {
			if (args[2].equalsIgnoreCase("cut")) {
				Main.logDebug("Melt type: cut"); // ----
				macro.erodeSubtype = 0;
			} else if (args[2].equalsIgnoreCase("raise")) {
				Main.logDebug("Melt type: raise"); // ----
				macro.erodeSubtype = 1;
			} else if (args[2].equalsIgnoreCase("smooth")) {
				Main.logDebug("Melt type: smooth"); // ----
				macro.erodeSubtype = 2;
			}
		}
		
		return macro;
	}
	
	public boolean performMacro () {
		// Location of the brush
		double x = erodeCenter.getX();
		double y = erodeCenter.getY();
		double z = erodeCenter.getZ();
		
		// Generate the erode sphere
		List<Block> erosionArray = new ArrayList<Block>();
		for (int rx = -erodeRadius; rx <= erodeRadius; rx++) {
			for (int rz = -erodeRadius; rz <= erodeRadius; rz++) {
				for (int ry = -erodeRadius; ry <= erodeRadius; ry++) {
					if (rx*rx + ry*ry + rz*rz <= (erodeRadius + 0.5)*(erodeRadius + 0.5)) {
						erosionArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
					}
				}
			}
		}
		Main.logDebug("Erosion array size: " + Integer.toString(erosionArray.size())); // ----
		
		// Generate a snapshot to use for eroding (erode in this, read from world)
		List<BlockState> snapshotArray = new ArrayList<BlockState>();
		for (Block b : erosionArray) {
			snapshotArray.add(b.getState());
		}
		
		// Generate and store an undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(erosionArray));
		erosionArray = null; // This is no longer needed, so clean it up
		
		// Melt cut erosion
		if (erodeType == 0 && erodeSubtype == 0) {
			Main.logDebug("Starting melt cut erode"); // ----
			int airCut = 3; // Nearby air to make air
			int solidCut = 4; // Nearby solid to make solid
			// Iterate through each block
			List<BlockState> snapshotCopy = new ArrayList<BlockState>();
			for (BlockState b : snapshotArray) {
				// First get the adjacent blocks
				Block current = Main.world.getBlockAt(b.getLocation());
				BlockState currentState = b;
				List<Block> adjBlocks = new ArrayList<Block>();
				adjBlocks.add(current.getRelative(BlockFace.UP));
				adjBlocks.add(current.getRelative(BlockFace.DOWN));
				adjBlocks.add(current.getRelative(BlockFace.NORTH));
				adjBlocks.add(current.getRelative(BlockFace.SOUTH));
				adjBlocks.add(current.getRelative(BlockFace.EAST));
				adjBlocks.add(current.getRelative(BlockFace.WEST));
				
				// Logic for non-air blocks
				if (b.getType() != Material.AIR) {
					// Count how many adjacent blocks are air
					int airCount = 0;
					for (Block adjBlock : adjBlocks) {
						if (adjBlock == null)
							continue;
						if (adjBlock.getType() == Material.AIR)
							airCount++;
					}
					
					// If air count is large, make this air
					if (airCount >= airCut) {
						currentState.setType(Material.AIR);
						snapshotCopy.add(currentState);
					}
					// Otherwise return in place
					else {
						snapshotCopy.add(currentState);
					}
				}
				
				// Logic for air blocks
				else {
					int blockCount = 0;
					Material adjMaterial = Material.AIR;
					for (Block adjBlock : adjBlocks) {
						if (adjBlock == null)
							continue;
						if (adjBlock.getType() != Material.AIR) {
							blockCount++;
							adjMaterial = adjBlock.getType();
						}
					}
					
					// If there are a lot of blocks nearby, make this solid
					if (blockCount >= solidCut) {
						currentState.setType(adjMaterial);
						snapshotCopy.add(currentState);
					}
					
					// Otherwise return in place
					else {
						snapshotCopy.add(currentState);
					}
				}
			}
			snapshotArray = snapshotCopy;
		}
		
		// Melt raise erosion
		if (erodeType == 0 && erodeSubtype == 1) {
			Main.logDebug("Starting melt cut erode"); // ----
			int airCut = 4; // Nearby air to make air
			int solidCut = 2; // Nearby solid to make solid
			// Iterate through each block
			List<BlockState> snapshotCopy = new ArrayList<BlockState>();
			for (BlockState b : snapshotArray) {
				// First get the adjacent blocks
				Block current = Main.world.getBlockAt(b.getLocation());
				BlockState currentState = b;
				List<Block> adjBlocks = new ArrayList<Block>();
				adjBlocks.add(current.getRelative(BlockFace.UP));
				adjBlocks.add(current.getRelative(BlockFace.DOWN));
				adjBlocks.add(current.getRelative(BlockFace.NORTH));
				adjBlocks.add(current.getRelative(BlockFace.SOUTH));
				adjBlocks.add(current.getRelative(BlockFace.EAST));
				adjBlocks.add(current.getRelative(BlockFace.WEST));
				
				// Logic for non-air blocks
				if (b.getType() != Material.AIR) {
					// Count how many adjacent blocks are air
					int airCount = 0;
					for (Block adjBlock : adjBlocks) {
						if (adjBlock == null)
							continue;
						if (adjBlock.getType() == Material.AIR)
							airCount++;
					}
					
					// If air count is large, make this air
					if (airCount >= airCut) {
						currentState.setType(Material.AIR);
						snapshotCopy.add(currentState);
					}
					// Otherwise return in place
					else {
						snapshotCopy.add(currentState);
					}
				}
				
				// Logic for air blocks
				else {
					int blockCount = 0;
					Material adjMaterial = Material.AIR;
					for (Block adjBlock : adjBlocks) {
						if (adjBlock == null)
							continue;
						if (adjBlock.getType() != Material.AIR) {
							blockCount++;
							adjMaterial = adjBlock.getType();
						}
					}
					
					// If there are a lot of blocks nearby, make this solid
					if (blockCount >= solidCut) {
						currentState.setType(adjMaterial);
						snapshotCopy.add(currentState);
					}
					
					// Otherwise return in place
					else {
						snapshotCopy.add(currentState);
					}
				}
			}
			snapshotArray = snapshotCopy;
		}
		
		// Melt smooth erosion
		if (erodeType == 0 && erodeSubtype == 2) {
			Main.logDebug("Starting melt cut erode"); // ----
			int airCut = 4; // Nearby air to make air
			int solidCut = 4; // Nearby solid to make solid
			// Iterate through each block
			List<BlockState> snapshotCopy = new ArrayList<BlockState>();
			for (BlockState b : snapshotArray) {
				// First get the adjacent blocks
				Block current = Main.world.getBlockAt(b.getLocation());
				BlockState currentState = b;
				List<Block> adjBlocks = new ArrayList<Block>();
				adjBlocks.add(current.getRelative(BlockFace.UP));
				adjBlocks.add(current.getRelative(BlockFace.DOWN));
				adjBlocks.add(current.getRelative(BlockFace.NORTH));
				adjBlocks.add(current.getRelative(BlockFace.SOUTH));
				adjBlocks.add(current.getRelative(BlockFace.EAST));
				adjBlocks.add(current.getRelative(BlockFace.WEST));
				
				// Logic for non-air blocks
				if (b.getType() != Material.AIR) {
					// Count how many adjacent blocks are air
					int airCount = 0;
					for (Block adjBlock : adjBlocks) {
						if (adjBlock == null)
							continue;
						if (adjBlock.getType() == Material.AIR)
							airCount++;
					}
					
					// If air count is large, make this air
					if (airCount >= airCut) {
						currentState.setType(Material.AIR);
						snapshotCopy.add(currentState);
					}
					// Otherwise return in place
					else {
						snapshotCopy.add(currentState);
					}
				}
				
				// Logic for air blocks
				else {
					int blockCount = 0;
					Material adjMaterial = Material.AIR;
					for (Block adjBlock : adjBlocks) {
						if (adjBlock == null)
							continue;
						if (adjBlock.getType() != Material.AIR) {
							blockCount++;
							adjMaterial = adjBlock.getType();
						}
					}
					
					// If there are a lot of blocks nearby, make this solid
					if (blockCount >= solidCut) {
						currentState.setType(adjMaterial);
						snapshotCopy.add(currentState);
					}
					
					// Otherwise return in place
					else {
						snapshotCopy.add(currentState);
					}
				}
			}
			snapshotArray = snapshotCopy;
		}
		
		// Blend erosion
		if (erodeType == 1) {
			Bukkit.getServer().broadcastMessage("§cBlend erosion is not yet implemented");
		}
		
		// Apply the snapshot to the world, thus completing the erosion
		for (BlockState b : snapshotArray) {
			Location l = b.getLocation();
			Block block = Main.world.getBlockAt(l);
			block.setType(b.getType());
			block.setBlockData(b.getBlockData());
		}
		return true;
	}
}
