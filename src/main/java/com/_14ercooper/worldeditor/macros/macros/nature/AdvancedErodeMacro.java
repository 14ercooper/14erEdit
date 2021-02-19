package com._14ercooper.worldeditor.macros.macros.nature;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;

public class AdvancedErodeMacro extends Macro {

    public int erodeRadius = -1; // The radius to actually erode within
    public int solidCut = 3;
    public int airCut = 3;
    public Location erodeCenter;

    private void SetupMacro(String[] args, Location loc) {
	try {
	    erodeRadius = Integer.parseInt(args[0]);
	}
	catch (Exception e) {
	    Main.logError("Could not parse advanced erode macro. Is your radius a valid number?",
		    Operator.currentPlayer);
	}
	erodeCenter = loc;

	try {
	    solidCut = Integer.parseInt(args[1]);
	    airCut = Integer.parseInt(args[2]);
	}
	catch (Exception e) {
	    Main.logError("Could not parse advanced erode macro. Did you provide integers for both solid and air cut?",
		    Operator.currentPlayer);
	}
    }

    @Override
    public boolean performMacro(String[] args, Location loc) {
	SetupMacro(args, loc);

	// Location of the brush
	double x = erodeCenter.getX();
	double y = erodeCenter.getY();
	double z = erodeCenter.getZ();

	List<BlockState> snapshotArray = generateSnapshotArray(x, y, z);

	snapshotArray = doErosion(snapshotArray);

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
		    if (rx * rx + ry * ry + rz * rz <= (erodeRadius + 0.5) * (erodeRadius + 0.5)) {
			erosionArray.add(
				Operator.currentPlayer.getWorld().getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
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

    private void applyToWorld(List<BlockState> snapshotArray) {
	for (BlockState b : snapshotArray) {
	    Location l = b.getLocation();
	    Block block = Operator.currentPlayer.getWorld().getBlockAt(l);
	    SetBlock.setMaterial(block, b.getType());
	    block.setBlockData(b.getBlockData());
	}
    }

    private List<BlockState> doErosion(List<BlockState> snapshotArray) {
	Main.logDebug("Starting advanced erode"); // ----
	// Iterate through each block
	List<BlockState> snapshotCopy = new ArrayList<BlockState>();
	for (BlockState b : snapshotArray) {
	    // First get the adjacent blocks
	    Block current = Operator.currentPlayer.getWorld().getBlockAt(b.getLocation());
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
