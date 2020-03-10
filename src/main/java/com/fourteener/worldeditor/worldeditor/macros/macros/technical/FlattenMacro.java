package com.fourteener.worldeditor.worldeditor.macros.macros.technical;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.worldeditor.undo.UndoElement;
import com.fourteener.worldeditor.worldeditor.undo.UndoManager;

public class FlattenMacro extends Macro {
	
	double radius;
	boolean isAbsolute;
	double height;
	Material block;
	Location pos;
	
	// Create a new macro
	private void SetupMacro (String[] args, Location loc) {
		radius = Double.parseDouble(args[0]);
		isAbsolute = Boolean.parseBoolean(args[1]);
		height = Double.parseDouble(args[2]);
		block = Material.matchMaterial(args[3]);
		pos = loc;
	}
	
	// Run this macro
	public boolean performMacro (String[] args, Location loc) {
		SetupMacro(args, loc);
		
		// Location of the brush
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		LinkedList<BlockState> operatedBlocks = new LinkedList<BlockState>();
		
		// OPERATE
		if (isAbsolute) {
			Main.logDebug("Absolute flatten");
			absoluteFlatten(x, y, z, operatedBlocks);
		}
		else {
			Main.logDebug("Brush flatten");
			notAbsoluteFlatten(x, y, z, operatedBlocks);
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
			b.setType(bs.getType());
			b.setBlockData(bs.getBlockData());
		}
		
		return true;
	}

	private void absoluteFlatten(double x, double y, double z, LinkedList<BlockState> operatedBlocks) {
		// Generate the cylinder
		int radiusInt = (int) Math.round(radius);
		List<Block> blockArray = new ArrayList<Block>();
		for (int rx = -radiusInt; rx <= radiusInt; rx++) {
			for (int rz = -radiusInt; rz <= radiusInt; rz++) {
				for (int ry = 0; ry <= 255; ry++) {
					if (rx*rx + rz*rz <= (radius + 0.5)*(radius + 0.5)) {
						blockArray.add(GlobalVars.world.getBlockAt((int) x + rx, ry, (int) z + rz));
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

	private void notAbsoluteFlatten(double x, double y, double z, LinkedList<BlockState> operatedBlocks) {
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
		blockArray.add(GlobalVars.world.getBlockAt((int)x,(int)y,(int)z));
		Main.logDebug("Block array size: " + Integer.toString(blockArray.size())); // ----
		
		// Create a snapshot array
		List<BlockState> snapshotArray = new ArrayList<BlockState>();
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
}
