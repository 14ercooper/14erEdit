package com.fourteener.worldeditor.macros.macros.nature;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.undo.UndoElement;
import com.fourteener.worldeditor.undo.UndoManager;

public class VinesMacro extends Macro {
	
	double radius = 0, length = 0, variance = 0, density = 0;
	Location pos;
	
	// Create a new macro
	private void SetupMacro(String[] args, Location loc) {
		radius = Double.parseDouble(args[0]);
		length = Double.parseDouble(args[1]);
		variance = Double.parseDouble(args[2]);
		density = Double.parseDouble(args[3]);
		pos = loc;
	}
	
	// Run this macro
	public boolean performMacro (String[] args, Location loc) {
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
					if (rx*rx + ry*ry + rz*rz <= (radius + 0.5)*(radius + 0.5)) {
						blockArray.add(GlobalVars.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
					}
				}
			}
		}
		Main.logDebug("Block array size: " + Integer.toString(blockArray.size())); // ----
		
		// Register an undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(new UndoElement(blockArray));
		
		// Create a snapshot array
		List<BlockState> snapshotArray = new ArrayList<BlockState>();
		for (Block b : blockArray) {
			snapshotArray.add(b.getState());
		}
		blockArray = null;
		Main.logDebug(Integer.toString(snapshotArray.size()) + " blocks in snapshot array");
		LinkedList<BlockState> operatedBlocks = new LinkedList<BlockState>();
		
		// OPERATE
		Random rand = new Random();
		for (BlockState bs : snapshotArray) {
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			// Make sure this block is air
			if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
				continue;
			}
			
			// Check density
			if (rand.nextDouble() >= density) {
				continue;
			}
			
			String blockStateTop = "[";
			String blockState = "";
			boolean firstState = true;
			List<String> dirs = new ArrayList<String>();
			// And next to a solid block
			if (b.getRelative(BlockFace.NORTH).getType() != Material.AIR) {
				if (firstState) {
					firstState = false;
				}
				else {
					blockStateTop = blockStateTop.concat(",");
				}
				blockStateTop = blockStateTop.concat("north=true");
				dirs.add("[north=true]");
			}
			if (b.getRelative(BlockFace.EAST).getType() != Material.AIR) {
				if (firstState) {
					firstState = false;
				}
				else {
					blockStateTop = blockStateTop.concat(",");
				}
				blockStateTop = blockStateTop.concat("east=true");
				dirs.add("[east=true]");
			}
			if (b.getRelative(BlockFace.SOUTH).getType() != Material.AIR) {
				if (firstState) {
					firstState = false;
				}
				else {
					blockStateTop = blockStateTop.concat(",");
				}
				blockStateTop = blockStateTop.concat("south=true");
				dirs.add("[south=true]");
			}
			if (b.getRelative(BlockFace.WEST).getType() != Material.AIR) {
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
				if (b.getRelative(BlockFace.UP).getType() != Material.AIR) {
					blockStateTop = blockStateTop.concat(",up=true");
				}
			}
			else {
				continue;
			}
			
			// Pick the side for the vines that will be below this one
			if (dirs.size() > 1) {
				blockState = dirs.get(rand.nextInt(dirs.size() - 1));
			}
			else {
				blockState = dirs.get(0);
			}
			
			// Close off the directional state; and move on if there was no solid block
			if (!blockStateTop.equals("[")) {
				blockStateTop = blockStateTop.concat("]");
			}
			
			// Determine the length of this vine
			double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
			int vineLength = (int) Math.round(length + actVariance);
			
			// Grow the vine (checking to make sure only air gets replaced and registering operated blocks)
			// Grow the top vine
			Main.logDebug("Growing a vine of length " + Integer.toString(vineLength));
			BlockState state = b.getState();
			state.setType(Material.VINE);
			state.setBlockData(Bukkit.getServer().createBlockData("minecraft:vine" + blockStateTop));
			operatedBlocks.add(state);
			// Grow all the other vines
			for (int i = 1; i <= vineLength; i++) {
				state = b.getRelative(BlockFace.DOWN, i).getState();
				if (state.getType() == Material.AIR) {
					state.setType(Material.VINE);
					state.setBlockData(Bukkit.getServer().createBlockData("minecraft:vine" + blockState));
					operatedBlocks.add(state);
				}
				else {
					Main.logDebug("Stopping vine due to solid block");
					break; // Don't grow through solid blocks
				}
			}
		}
		
		// Process edited blocks and register the undo
		List<Block> blocksToUndo = new ArrayList<Block>();
		for (BlockState bs : operatedBlocks) {
			blocksToUndo.add(GlobalVars.world.getBlockAt(bs.getLocation()));
		}
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(new UndoElement(blocksToUndo));
		
		
		Main.logDebug("Operated on and now placing " + Integer.toString(operatedBlocks.size()) + " blocks");
		// Apply the changes to the world
		for (BlockState bs : operatedBlocks) {
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			SetBlock.setMaterial(b, bs.getType());
			b.setBlockData(bs.getBlockData());
		}
		
		return true;
	}
}
