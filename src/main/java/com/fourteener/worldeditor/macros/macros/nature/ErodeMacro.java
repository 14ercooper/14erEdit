package com.fourteener.worldeditor.macros.macros.nature;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;

public class ErodeMacro extends Macro {

	public int erodeRadius = -1; // The radius to actually erode within
	public int erodeType = -1; // 0 for melt, 1 for blendball, 2 for mix
	public int erodeSubtype = -1; // -1 if no subtype, 0 for more subtractive, 1 for more additive, 2 for neutral
	public Location erodeCenter;

	private void SetupMacro(String[] args, Location loc) {
		try {
			erodeRadius = Integer.parseInt(args[0]);
		} catch (Exception e) {
			Main.logError("Could not parse erode macro. Is your radius a number?", Operator.currentPlayer);
		}
		erodeCenter = loc;

		// Determine the type of the erode brush
		try {
			if (args[1].equalsIgnoreCase("melt")) {
				erodeType = 0;
			}
			else if (args[1].equalsIgnoreCase("blend")) {
				erodeType = 1;
			}
			else if (args[1].equalsIgnoreCase("mix")) {
				erodeType = 2;
			}

			// Cut or raise melt?
			if (erodeType == 0) {
				if (args[2].equalsIgnoreCase("cut")) {
					erodeSubtype = 0;
				}
				else if (args[2].equalsIgnoreCase("raise")) {
					erodeSubtype = 1;
				}
				else if (args[2].equalsIgnoreCase("smooth")) {
					erodeSubtype = 2;
				}
				else if (args[2].equalsIgnoreCase("lift")) {
					erodeSubtype = 3;
				}
				else if (args[2].equalsIgnoreCase("carve")) {
					erodeSubtype = 4;
				}
			}
		} catch (Exception e) {
			Main.logError("Could not parse erode macro. Did you provide a valid mode?", Operator.currentPlayer);
		}
	}

	public boolean performMacro (String[] args, Location loc) {
		SetupMacro(args, loc);

		// Location of the brush
		double x = erodeCenter.getX();
		double y = erodeCenter.getY();
		double z = erodeCenter.getZ();

		List<BlockState> snapshotArray = generateSnapshotArray(x, y, z);

		// Melt cut erosion
		if (erodeType == 0 && erodeSubtype == 0) {
			snapshotArray = meltCutErosion(snapshotArray);
		}

		// Melt raise erosion
		if (erodeType == 0 && erodeSubtype == 1) {
			snapshotArray = meltRaiseErosion(snapshotArray);
		}

		// Melt smooth erosion
		if (erodeType == 0 && erodeSubtype == 2) {
			snapshotArray = meltSmoothErosion(snapshotArray);
		}

		// Melt lift erosion
		if (erodeType == 0 && erodeSubtype == 3) {
			snapshotArray = meltLiftErosion(snapshotArray);
		}

		// Melt carve erosion
		if (erodeType == 0 && erodeSubtype == 4) {
			snapshotArray = meltCarveErosion(snapshotArray);
		}

		// Blend erosion
		if (erodeType == 1) {
			Bukkit.getServer().broadcastMessage("§cBlend erosion is not yet implemented");
		}

		// Mix erosion
		if (erodeType == 2) {
			snapshotArray = mixErosion (snapshotArray, x, y, z);
		}

		// Apply the snapshot to the world, thus completing the erosion
		applyToWorld(snapshotArray);
		return true;
	}

	private List<BlockState> generateSnapshotArray(double x, double y, double z) {
		// Generate the erode sphere
		List<Block> erosionArray = new ArrayList<Block>();
		for (int rx = -erodeRadius; rx <= erodeRadius; rx++) {
			for (int rz = -erodeRadius; rz <= erodeRadius; rz++) {
				for (int ry = -erodeRadius; ry <= erodeRadius; ry++) {
					if (rx*rx + ry*ry + rz*rz <= (erodeRadius + 0.5)*(erodeRadius + 0.5)) {
						erosionArray.add(GlobalVars.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
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

		erosionArray = null; // This is no longer needed, so clean it up
		return snapshotArray;
	}

	private List<BlockState> mixErosion (List<BlockState> snapshotArray, double x, double y, double z) {
		snapshotArray = meltRaiseErosion(snapshotArray);
		applyToWorld(snapshotArray);
		snapshotArray = generateSnapshotArray (x, y, z);
		snapshotArray = meltCutErosion(snapshotArray);
		applyToWorld(snapshotArray);
		snapshotArray = generateSnapshotArray (x, y, z);
		snapshotArray = meltCutErosion(snapshotArray);
		applyToWorld(snapshotArray);
		snapshotArray = generateSnapshotArray (x, y, z);
		snapshotArray = meltRaiseErosion(snapshotArray);
		applyToWorld(snapshotArray);
		snapshotArray = generateSnapshotArray (x, y, z);
		snapshotArray = meltSmoothErosion(snapshotArray);
		return snapshotArray;
	}

	private void applyToWorld(List<BlockState> snapshotArray) {
		for (BlockState b : snapshotArray) {
			Location l = b.getLocation();
			Block block = GlobalVars.world.getBlockAt(l);
			SetBlock.setMaterial(block, b.getType());
			block.setBlockData(b.getBlockData());
		}
	}

	private List<BlockState> meltSmoothErosion(List<BlockState> snapshotArray) {
		Main.logDebug("Starting melt smooth erode"); // ----
		int airCut = 4; // Nearby air to make air
		int solidCut = 4; // Nearby solid to make solid
		// Iterate through each block
		List<BlockState> snapshotCopy = new ArrayList<BlockState>();
		for (BlockState b : snapshotArray) {
			// First get the adjacent blocks
			Block current = GlobalVars.world.getBlockAt(b.getLocation());
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
		return snapshotCopy;
	}

	private List<BlockState> meltRaiseErosion(List<BlockState> snapshotArray) {
		Main.logDebug("Starting melt raise erode"); // ----
		int airCut = 4; // Nearby air to make air
		int solidCut = 2; // Nearby solid to make solid
		// Iterate through each block
		List<BlockState> snapshotCopy = new ArrayList<BlockState>();
		for (BlockState b : snapshotArray) {
			// First get the adjacent blocks
			Block current = GlobalVars.world.getBlockAt(b.getLocation());
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
		return snapshotCopy;
	}

	private List<BlockState> meltLiftErosion(List<BlockState> snapshotArray) {
		Main.logDebug("Starting melt lift erode"); // ----
		int airCut = 4; // Nearby air to make air
		int solidCut = 1; // Nearby solid to make solid
		// Iterate through each block
		List<BlockState> snapshotCopy = new ArrayList<BlockState>();
		for (BlockState b : snapshotArray) {
			// First get the adjacent blocks
			Block current = GlobalVars.world.getBlockAt(b.getLocation());
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
		return snapshotCopy;
	}

	private List<BlockState> meltCutErosion(List<BlockState> snapshotArray) {
		Main.logDebug("Starting melt cut erode"); // ----
		int airCut = 3; // Nearby air to make air
		int solidCut = 4; // Nearby solid to make solid
		// Iterate through each block
		List<BlockState> snapshotCopy = new ArrayList<BlockState>();
		for (BlockState b : snapshotArray) {
			// First get the adjacent blocks
			Block current = GlobalVars.world.getBlockAt(b.getLocation());
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
		return snapshotCopy;
	}

	private List<BlockState> meltCarveErosion(List<BlockState> snapshotArray) {
		Main.logDebug("Starting melt cut erode"); // ----
		int airCut = 1; // Nearby air to make air
		int solidCut = 4; // Nearby solid to make solid
		// Iterate through each block
		List<BlockState> snapshotCopy = new ArrayList<BlockState>();
		for (BlockState b : snapshotArray) {
			// First get the adjacent blocks
			Block current = GlobalVars.world.getBlockAt(b.getLocation());
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
		return snapshotCopy;
	}
}
