package com.fourteener.worldeditor.macros.macros.technical;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import com.fourteener.schematics.Schematic;
import com.fourteener.worldeditor.macros.macros.Macro;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.undo.UndoElement;
import com.fourteener.worldeditor.undo.UndoManager;

public class SchematicMacro extends Macro {
	
	int xOff = 0, yOff = 0, zOff = 0;
	int x = 0, y = 0, z = 0;
	int length = 0, width = 0, height = 0;
	LinkedList<String> blockData = new LinkedList<String>();
	LinkedList<String> blockNbt = new LinkedList<String>();
	boolean setAir = false;
	
	// Create a new macro
	private void SetupMacro(String[] args, Location loc) {
		// First parse the offset
		xOff = Integer.parseInt(args[1]) * -1;
		yOff = Integer.parseInt(args[2]) * -1;
		zOff = Integer.parseInt(args[3]) * -1;
		// And the center of the paste
		x = loc.getBlockX();
		y = loc.getBlockY();
		z = loc.getBlockZ();
		// Get if air should be set
		setAir = Boolean.parseBoolean(args[4]);
		// And finally parse the block data
		// This code, violating DRY, is also in the clipboard
		String path = args[0];
		path = ("plugins/14erEdit/schematics/" + path).replace("/", File.separator);
		if (!(path.contains(".matschem") || path.contains(".schematic") || path.contains(".schem"))) {
			path = path + ".matschem";
		}
		// Using materials format
		if (path.contains(".matschem")) {
			path.replace(".matschem", "");
			Schematic schem = Schematic.loadSchematic(path);
			blockData = schem.getBlocks();
			width = schem.getDimensions()[0];
			height = schem.getDimensions()[1];
			length = schem.getDimensions()[2];
			blockNbt = schem.getBlockNbt();
		}
		// Using MCEdit format
		else if (path.contains(".schematic")) {
			Operator.currentPlayer.sendMessage("§dMCEdit format schematics are not supported at this time.");
		}
		// Using Sponge format
		else if (path.contains(".schem")) {
			Operator.currentPlayer.sendMessage("§dSponge format schematics are not supported at this time.");
		}
	}
	
	// Run this macro
	public boolean performMacro (String[] args, Location loc) {
		SetupMacro(args, loc);
		// Start tracking an undo
		Set<BlockState> undoList = new HashSet<BlockState>();
		
		// Parse the list into the world
		int rx = 0, ry = 0, rz = 0;
		for (int i = 0; i < (length * width * height); i++) {
			if (rx >= width) {
				rz++;
				rx = 0;
			}
			if (rz >= length) {
				ry++;
				rz = 0;
			}
			rx++;
			
			Material blockMat = Material.matchMaterial(blockData.get(i).split("\\[")[0]);
			BlockData blockDat = Bukkit.getServer().createBlockData(blockData.get(i));
			String nbt = blockNbt.get(i);
			
			Block b = GlobalVars.world.getBlockAt(x + rx - xOff - 1, y + ry - yOff, z + rz - zOff);
			// Set the block
			if (blockMat == Material.AIR) {
				if (setAir) {
					undoList.add(b.getState());
					b.setType(blockMat);
					b.setBlockData(blockDat);
					if (!nbt.equalsIgnoreCase("")) {
						String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " " + nbt;
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
					}
				}
			}
			else {
				undoList.add(b.getState());
				b.setType(blockMat);
				b.setBlockData(blockDat);
				if (!nbt.equalsIgnoreCase("")) {
					String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " " + nbt;
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}
		}
		
		// Store the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(new UndoElement(undoList));
		return true;
	}
}
