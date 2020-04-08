package com.fourteener.worldeditor.selection;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import com.fourteener.schematics.Schematic;
import com.fourteener.worldeditor.main.*;

public class Clipboard {
	
	public Player owner; // The owner of this clipboard
	public int x = -1, y = -1, z = -1; // Stores the origin of this clipboard
	public int length = -1, width = -1, height = -1; // Height is Y, Width is X, Length is Z
	public LinkedList<String> blockData = new LinkedList<String>();
	public LinkedList<String> nbtData = new LinkedList<String>();
	
	public Clipboard(Player player) {
		owner = player;
	}
	
	public boolean mirrorClipboard (String direction) {
		// Get the mirror direction and mirrors the origin
		// 2 is X, 3 is Y, 5 is Z
		int dir = 1;
		if (direction.indexOf("x") >= 0) {
			dir *= 2;
			x *= -1;
		}
		else if (direction.indexOf("y") >= 0) {
			dir *= 3;
			y *= -1;
		}
		else if (direction.indexOf("z") >= 0) {
			dir *= 5;
			z *= -1;
		}
		else {
			Main.logDebug("Invalid flip direction");
			return false;
		}
		
		// Mirror the list
		// Mirror over X
		if (dir % 2 == 0) {
			// Create a new array to store the mirror
			int size = length * width * height;
			LinkedList<String> blockList = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				blockList.add("");
			}
			LinkedList<String> nbtList = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				nbtList.add("");
			}
			
			// Mirror into the new array
			for (int xN = 0; xN < width; xN++) {
				for (int yN = 0; yN < height; yN++) {
					for (int zN = 0; zN < length; zN++) {
						blockList.set((width - 1 - xN) + (zN * width) + (yN * width * length), blockData.get(xN + (zN * width) + (yN * width * length)));
						nbtList.set((width - 1 - xN) + (zN * width) + (yN * width * length), nbtData.get(xN + (zN * width) + (yN * width * length)));
					}
				}
			}
			
			// Replace the old array with the new array
			blockData = blockList;
			nbtData = nbtList;
		}
		
		// Mirror over Y
		else if (dir % 3 == 0) {
			// Create a new array to store the mirror
			int size = length * width * height;
			LinkedList<String> blockList = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				blockList.add("");
			}
			LinkedList<String> nbtList = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				nbtList.add("");
			}
			
			// Mirror into the new array
			for (int xN = 0; xN < width; xN++) {
				for (int yN = 0; yN < height; yN++) {
					for (int zN = 0; zN < length; zN++) {
						blockList.set(xN + (zN * width) + ((height - 1 - yN) * width * length), blockData.get(xN + (zN * width) + (yN * width * length)));
						nbtList.set((width - 1 - xN) + (zN * width) + (yN * width * length), nbtData.get(xN + (zN * width) + (yN * width * length)));
					}
				}
			}
			
			// Replace the old array with the new array
			blockData = blockList;
			nbtData = nbtList;
		}
		
		// Mirror over Z
		else if (dir % 5 == 0) {
			// Create a new array to store the mirror
			int size = length * width * height;
			LinkedList<String> blockList = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				blockList.add("");
			}
			LinkedList<String> nbtList = new LinkedList<String>();
			for (int i = 0; i < size; i++) {
				nbtList.add("");
			}
			
			// Mirror into the new array
			for (int xN = 0; xN < width; xN++) {
				for (int yN = 0; yN < height; yN++) {
					for (int zN = 0; zN < length; zN++) {
						blockList.set(xN + ((length - 1 - zN) * width) + (yN * width * length), blockData.get(xN + (zN * width) + (yN * width * length)));
						nbtList.set((width - 1 - xN) + (zN * width) + (yN * width * length), nbtData.get(xN + (zN * width) + (yN * width * length)));
					}
				}
			}
			
			// Replace the old array with the new array
			blockData = blockList;
			nbtData = nbtList;
		}
		
		return true;
	}
	
	// X,Y,Z stores the origin of the clipboard; blocks is the blocks to save
	public boolean saveToClipboard (int xOrigin, int yOrigin, int zOrigin, List<Block> blocks) {
		// First, figure out the most negative corner of the selection
		int xNeg = Integer.MAX_VALUE, yNeg = Integer.MAX_VALUE, zNeg = Integer.MAX_VALUE;
		for (Block b : blocks) {
			if (b.getX() < xNeg)
				xNeg = b.getX();
			if (b.getY() < yNeg)
				yNeg = b.getY();
			if (b.getZ() < zNeg)
				zNeg = b.getZ();
		}
		Main.logDebug("Negative corner of (" + Integer.toString(xNeg) + "," + Integer.toString(yNeg) + "," + Integer.toString(zNeg) + ")"); // ----
		// And the most positive corner
		int xPos = Integer.MIN_VALUE, yPos = Integer.MIN_VALUE, zPos = Integer.MIN_VALUE;
		for (Block b : blocks) {
			if (b.getX() > xPos)
				xPos = b.getX();
			if (b.getY() > yPos)
				yPos = b.getY();
			if (b.getZ() > zPos)
				zPos = b.getZ();
		}
		Main.logDebug("Positive corner of (" + Integer.toString(xPos) + "," + Integer.toString(yPos) + "," + Integer.toString(zPos) + ")"); // ----
		
		// Offset the origins appropriately and store them
		x = Math.abs(xOrigin - xNeg);
		if (xOrigin < xNeg)
			x *= -1;
		y = Math.abs(yOrigin - yNeg);
		if (yOrigin < yNeg)
			y *= -1;
		z = Math.abs(zOrigin - zNeg);
		if (zOrigin < zNeg)
			z *= -1;

		Main.logDebug("Origin of (" + Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + ")"); // ----
		
		// Calculate the dimensions of the selection
		length = Math.abs(zPos - zNeg) + 1;
		width = Math.abs(xPos - xNeg) + 1;
		height = Math.abs(yPos - yNeg) + 1;

		Main.logDebug("Dimensions of (" + Integer.toString(width) + "," + Integer.toString(height) + "," + Integer.toString(length) + ")"); // ----
		
		// Generate an array of blocks of the correct size
		int size = length * width * height;
		LinkedList<String> blockList = new LinkedList<String>();
		for (int i = 0; i < size; i++) {
			blockList.add("");
		}
		LinkedList<String> nbtList = new LinkedList<String>();
		for (int i = 0; i < size; i++) {
			nbtList.add("");
		}
		Main.logDebug("Block list of size " + Integer.toString(blockList.size())); // ----
		
		// Then store the blocks (as data) into the array
		NBTExtractor nbtExtractor = new NBTExtractor();
		for (Block b : blocks) {
			int xB = Math.abs(b.getX() - xNeg);
			int yB = Math.abs(b.getY() - yNeg);
			int zB = Math.abs(b.getZ() - zNeg);
			blockList.set(xB + (zB * width) + (yB * length * width), b.getBlockData().getAsString());
			nbtList.set(xB + (zB * width) + (yB * length * width), nbtExtractor.getNBT(b.getState()));
		}
		
		// And finally save that array to the clipboard
		blockData = blockList;
		nbtData = nbtList;

		Main.logDebug("" + Integer.toString(blockData.size()) + " blocks of data stored"); // ----
		owner.sendMessage("§dSelection copied");
		return true;
	}
	
	// Paste this clipboard with the origin at x,y,z
	public boolean pasteClipboard (int xPos, int yPos, int zPos, boolean setAir) {
		// Loop through the blocks, along X from negative to positive, Z negative to positive, Y negative to positive
		int rx = 0, ry = 0, rz = 0;
		Main.logDebug("Pasting blocks into the world"); // ----
		Main.logDebug("Dimensions of (" + Integer.toString(width) + "," + Integer.toString(height) + "," + Integer.toString(length) + ")"); // ----
		Main.logDebug("Offset of (" + Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + ")"); // ----
		
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
			String nbt = nbtData.get(i);
			
			Block b = GlobalVars.world.getBlockAt(xPos + rx - x - 1, yPos + ry - y, zPos + rz - z);
			// Set the block
			if (blockMat == Material.AIR) {
				if (setAir) {
					SetBlock.setMaterial(b, blockMat);
					b.setBlockData(blockDat);
					if (!nbt.equalsIgnoreCase("")) {
						String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " " + nbt;
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
					}
				}
			}
			else {
				SetBlock.setMaterial(b, blockMat);
				b.setBlockData(blockDat);
				if (!nbt.equalsIgnoreCase("")) {
					String command = "data merge block " + b.getX() + " " + b.getY() + " " + b.getZ() + " " + nbt;
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}
		}

		Main.logDebug("Blocks pasted successfully"); // ----
		
		owner.sendMessage("§dSelection pasted");
		return true;
	}
	
	// Save a schematic to a file
	// This code, violating DRY, is also in the macro and below
	public boolean saveToFile (String path) {
		path = ("plugins/14erEdit/schematics/" + path).replace("/", File.separator);
		int[] origin = {x,y,z};
		int[] dimensions = {width,height,length};
		Schematic schem = new Schematic(origin, dimensions, blockData, nbtData);
		return schem.saveSchematic(path);
	}
	
	// Load a schematic from a file, setting an offset
	public boolean loadFromFile (String path, int xOff, int yOff, int zOff) {
		path = ("plugins/14erEdit/schematics/" + path).replace("/", File.separator);
		String[] splitPath = path.split(".");
		// Using materials format
		if (splitPath[splitPath.length - 1].equals("matschem")) {
			Schematic schem = Schematic.loadSchematic(path);
			x = schem.getOrigin()[0] + xOff;
			y = schem.getOrigin()[1] + yOff;
			z = schem.getOrigin()[2] + zOff;
			width = schem.getDimensions()[0];
			height = schem.getDimensions()[1];
			length = schem.getDimensions()[2];
			blockData = schem.getBlocks();
			nbtData = schem.getBlockNbt();
		}
		// Using MCEdit format
		else if (splitPath[splitPath.length - 1].equals("schematic")) {
			owner.sendMessage("§dMCEdit format schematics are not supported at this time.");
		}
		// Using Sponge format
		else if (splitPath[splitPath.length - 1].equals("schem")) {
			owner.sendMessage("§dSponge format schematics are not supported at this time.");
		}
		return true;
	}
	
	// Load a schematic from a file, without setting an offset
	// This code, violating DRY, is also in the macro and above
	public boolean loadFromFile (String path) {
		path = ("plugins/14erEdit/schematics/" + path).replace("/", File.separator);
		if (!(path.contains(".matschem") || path.contains(".schematic") || path.contains(".schem"))) {
			path = path + ".matschem";
		}
		// Using materials format
		if (path.contains(".matschem")) {
			path.replace(".matschem", "");
			Schematic schem = Schematic.loadSchematic(path);
			x = schem.getOrigin()[0];
			y = schem.getOrigin()[1];
			z = schem.getOrigin()[2];
			width = schem.getDimensions()[0];
			height = schem.getDimensions()[1];
			length = schem.getDimensions()[2];
			blockData = schem.getBlocks();
			nbtData = schem.getBlockNbt();
			return true;
		}
		// Using MCEdit format
		else if (path.contains(".schematic")) {
			owner.sendMessage("§dMCEdit format schematics are not supported at this time.");
			return true;
		}
		// Using Sponge format
		else if (path.contains(".schem")) {
			owner.sendMessage("§dSponge format schematics are not supported at this time.");
			return true;
		}
		return false;
	}
}

/*
	Some notes on the Material-based schematics format
	The origin of the schematic is (0,0,0)
	The block at (x,y,z) is at position x + z * width + y * length * width
*/