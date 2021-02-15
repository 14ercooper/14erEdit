package com._14ercooper.worldeditor.macros.macros.nature;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class VinesMacro extends Macro {

    double radius = 0, length = 0, variance = 0, density = 0;
    String block;
    Location pos;

    // Create a new macro
    private void SetupMacro(String[] args, Location loc) {
	try {
	    radius = Double.parseDouble(args[0]);
	    length = Double.parseDouble(args[1]);
	    variance = Double.parseDouble(args[2]);
	    density = Double.parseDouble(args[3]);
	    try {
		block = args[4];
	    }
	    catch (Exception e) {
		block = "vine";
	    }
	}
	catch (Exception e) {
	    Main.logError(
		    "Error parsing vine macro. Did you pass in radius, length, variance, density, and optionally block material?",
		    Operator.currentPlayer);
	}
	try {
	    Material m = Material.matchMaterial(block);
	    if (m == null)
		throw new Exception();
	}
	catch (Exception e) {
	    Main.logError("Error parsing vine macro. " + block + " is not a valid block.", Operator.currentPlayer);
	}
	pos = loc;
    }

    // Run this macro
    public boolean performMacro(String[] args, Location loc) {
	SetupMacro(args, loc);

	// Location of the brush
	double x = pos.getX();
	double y = pos.getY();
	double z = pos.getZ();

	// Generate the sphere
	int radiusInt = (int) Math.round(radius);
	List<Block> blockArray = new ArrayList<Block>();
	for (int rx = -radiusInt; rx <= radiusInt; rx++) {
	    for (int rz = -radiusInt; rz <= radiusInt; rz++) {
		for (int ry = -radiusInt; ry <= radiusInt; ry++) {
		    if (rx * rx + ry * ry + rz * rz <= (radius + 0.5) * (radius + 0.5)) {
			blockArray.add(
				Operator.currentPlayer.getWorld().getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
		    }
		}
	    }
	}
	Main.logDebug("Block array size: " + Integer.toString(blockArray.size())); // ----

	// Create a snapshot array
	List<BlockState> snapshotArray = new ArrayList<BlockState>();
	for (Block b : blockArray) {
	    snapshotArray.add(b.getState());
	}
	blockArray = null;
	Main.logDebug(Integer.toString(snapshotArray.size()) + " blocks in snapshot array");
	LinkedList<BlockState> operatedBlocks = new LinkedList<BlockState>();

	// OPERATE
	List<Material> nonsolidBlocks = new ArrayList<Material>();
	nonsolidBlocks.add(Material.AIR);
	nonsolidBlocks.add(Material.VINE);
	for (BlockState bs : snapshotArray) {
	    Block b = Operator.currentPlayer.getWorld().getBlockAt(bs.getLocation());
	    // Make sure this block is air
	    if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
		continue;
	    }

	    // Check density
	    if (GlobalVars.rand.nextDouble() >= density) {
		continue;
	    }

	    String blockStateTop = "[";
	    String blockState = "";
	    if (block.equalsIgnoreCase("vine")) {
		boolean firstState = true;
		List<String> dirs = new ArrayList<String>();
		// And next to a solid block
		if (!nonsolidBlocks.contains(b.getRelative(BlockFace.NORTH).getType())) {
		    if (firstState) {
			firstState = false;
		    }
		    else {
			blockStateTop = blockStateTop.concat(",");
		    }
		    blockStateTop = blockStateTop.concat("north=true");
		    dirs.add("[north=true]");
		}
		if (!nonsolidBlocks.contains(b.getRelative(BlockFace.EAST).getType())) {
		    if (firstState) {
			firstState = false;
		    }
		    else {
			blockStateTop = blockStateTop.concat(",");
		    }
		    blockStateTop = blockStateTop.concat("east=true");
		    dirs.add("[east=true]");
		}
		if (!nonsolidBlocks.contains(b.getRelative(BlockFace.SOUTH).getType())) {
		    if (firstState) {
			firstState = false;
		    }
		    else {
			blockStateTop = blockStateTop.concat(",");
		    }
		    blockStateTop = blockStateTop.concat("south=true");
		    dirs.add("[south=true]");
		}
		if (!nonsolidBlocks.contains(b.getRelative(BlockFace.WEST).getType())) {
		    if (firstState) {
			firstState = false;
		    }
		    else {
			blockStateTop = blockStateTop.concat(",");
		    }
		    blockStateTop = blockStateTop.concat("west=true");
		    dirs.add("[west=true]");
		}

		// Is this also a top vine?
		if (!blockStateTop.equals("[")) {
		    if (!nonsolidBlocks.contains(b.getRelative(BlockFace.UP).getType())) {
			blockStateTop = blockStateTop.concat(",up=true");
		    }
		}
		else {
		    continue;
		}

		// Pick the side for the vines that will be below this one
		if (dirs.size() > 1) {
		    blockState = dirs.get(GlobalVars.rand.nextInt(dirs.size() - 1));
		}
		else {
		    blockState = dirs.get(0);
		}

		// Close off the directional state; and move on if there was no solid block
		if (!blockStateTop.equals("[")) {
		    blockStateTop = blockStateTop.concat("]");
		}
	    }

	    // Determine the length of this vine
	    double actVariance = ((GlobalVars.rand.nextDouble() * 2.0) - 1.0) * variance;
	    int vineLength = (int) Math.round(length + actVariance);

	    // Grow the vine (checking to make sure only air gets replaced and registering
	    // operated blocks)
	    // Grow the top vine
	    BlockState state = b.getState();
	    state.setType(Material.matchMaterial(block));
	    if (block.equalsIgnoreCase("vine"))
		state.setBlockData(Bukkit.getServer().createBlockData("minecraft:vine" + blockState));
	    operatedBlocks.add(state);
	    // Grow all the other vines
	    for (int i = 1; i <= vineLength; i++) {
		state = b.getRelative(BlockFace.DOWN, i).getState();
		if (state.getType() == Material.AIR) {
		    state.setType(Material.matchMaterial(block));
		    if (block.equalsIgnoreCase("vine"))
			state.setBlockData(Bukkit.getServer().createBlockData("minecraft:vine" + blockState));
		    operatedBlocks.add(state);
		}
		else {
		    break; // Don't grow through solid blocks
		}
	    }
	}

	Main.logDebug("Operated on and now placing " + Integer.toString(operatedBlocks.size()) + " blocks");
	// Apply the changes to the world
	for (BlockState bs : operatedBlocks) {
	    Block b = Operator.currentPlayer.getWorld().getBlockAt(bs.getLocation());
	    SetBlock.setMaterial(b, bs.getType());
	    b.setBlockData(bs.getBlockData());
	}

	return true;
    }
}
