package com.fourteener.worldeditor.worldeditor.macros.macros.nature;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.worldeditor.undo.UndoElement;
import com.fourteener.worldeditor.worldeditor.undo.UndoManager;

public class BiomeMacro extends Macro {
	
	double radius;
	Biome biome;
	Location pos;
	
	// Create a new macro
	private void SetupMacro(String[] args, Location loc) {
		radius = Double.parseDouble(args[0]);
		biome = Biome.valueOf(args[1].toUpperCase(Locale.ROOT));
		pos = loc;
	}
	
	// Run this macro
	public boolean performMacro (String[] args, Location loc) {
		SetupMacro(args, loc);
		
		// Location of the brush
		double x = pos.getX();
		double z = pos.getZ();
		
		// Generate the sphere
		int radiusInt = (int) Math.round(radius);
		List<Block> blockArray = new ArrayList<Block>();
		for (int rx = -radiusInt; rx <= radiusInt; rx++) {
			for (int rz = -radiusInt; rz <= radiusInt; rz++) {
				if (rx*rx + rz*rz <= (radius + 0.5)*(radius + 0.5)) {
					blockArray.add(GlobalVars.world.getBlockAt((int) x + rx, 1, (int) z + rz));
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
		
		// OPERATE
		for (BlockState bs : snapshotArray) {
			Block b = GlobalVars.world.getBlockAt(bs.getLocation());
			b.setBiome(biome);
		}
		
		return true;
	}
}
