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

public class GrassMacro extends Macro {
	
	double radius = 0;
	String blockMix = "";
	double airSpaces = 0;
	double density = 0;
	Location pos;
	
	// Create a new macro
	private void SetupMacro(String[] args, Location loc) {
		radius = Double.parseDouble(args[0]);
		blockMix = args[1];
		airSpaces = Double.parseDouble(args[2]);
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
		
		// Parse the block mixture
		LinkedList<Double> odds = new LinkedList<Double>();
		LinkedList<String> blocks = new LinkedList<String>();
		for (String s : blockMix.split(",")) {
			String[] split = s.split("%");
			odds.add(Double.parseDouble(split[0]));
			blocks.add(split[1]);
		}
		Main.logDebug("Found " + Integer.toString(blocks.size()) + " blocks");
		
		// Operate on the sphere
		List<BlockState> operatedBlocks = new ArrayList<BlockState>();
		Random rand = new Random();
		for (BlockState bs : snapshotArray) {
			// Check if this block is air
			if (bs.getType() != Material.AIR) {
				continue;
			}
			
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			// Check if it's on a solid block
			if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				Main.logDebug("Skip block, is floating");
				continue;
			}
			
			// Check density
			if (rand.nextDouble() >= density) {
				Main.logDebug("Skip block, density not met");
				continue;
			}
			
			// Check air spaces
			boolean doCont = false;
			for (int i = 1; i <= airSpaces; i++) {
				if (b.getRelative(BlockFace.UP, i).getType() != Material.AIR) {
					doCont = true;
				}
			}
			if (doCont) {
				Main.logDebug("Skip block, too little air above");
				continue;
			}
			
			// Figure out which block to place
			Main.logDebug("Placing grass block");
			double randNum = rand.nextDouble() * 100.0;
			double oddsChance = 0.0;
			int i = -1;
			do {
				i++;
				oddsChance += odds.get(i);
			} while (oddsChance <= randNum);
			String blockToPlace = blocks.get(i);
			
			// Place the block
			BlockState operated = b.getState();
			if (blockToPlace.contains("[")) {
				String[] split = blockToPlace.split("\\[");
				operated.setType(Material.matchMaterial(split[0]));
				operated.setBlockData(Bukkit.getServer().createBlockData(blockToPlace));
			}
			else {
				operated.setType(Material.matchMaterial(blockToPlace));
			}
			operatedBlocks.add(operated);
		}
		
		Main.logDebug("Operated on and now placing " + Integer.toString(operatedBlocks.size()) + " blocks");
		// Apply the changes to the world
		for (BlockState bs : operatedBlocks) {
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			b.setType(bs.getType());
			b.setBlockData(bs.getBlockData());
		}
		
		return true;
	}
}
