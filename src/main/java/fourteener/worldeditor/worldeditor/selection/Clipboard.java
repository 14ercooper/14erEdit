package fourteener.worldeditor.worldeditor.selection;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import com.fourteener.schematics.Schematic;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class Clipboard {
	
	public Player owner; // The owner of this clipboard
	public int x = -1, y = -1, z = -1; // Stores the origin of this clipboard
	public int length = -1, width = -1, height = -1; // Height is Y, Width is X, Length is Z
	public LinkedList<String> blockData = new LinkedList<String>();
	
	public static Clipboard newClipboard (Player player) {
		Clipboard c = new Clipboard();
		c.owner = player;
		return c;
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
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Negative corner of (" + Integer.toString(xNeg) + "," + Integer.toString(yNeg) + "," + Integer.toString(zNeg) + ")"); // ----
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
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Positive corner of (" + Integer.toString(xPos) + "," + Integer.toString(yPos) + "," + Integer.toString(zPos) + ")"); // ----
		
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

		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Origin of (" + Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + ")"); // ----
		
		// Calculate the dimensions of the selection
		length = Math.abs(zPos - zNeg) + 1;
		width = Math.abs(xPos - xNeg) + 1;
		height = Math.abs(yPos - yNeg) + 1;

		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Dimensions of (" + Integer.toString(width) + "," + Integer.toString(height) + "," + Integer.toString(length) + ")"); // ----
		
		// Generate an array of blocks of the correct size
		int size = length * width * height;
		LinkedList<String> blockList = new LinkedList<String>();
		for (int i = 0; i < size; i++) {
			blockList.add("");
		}
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Block list of size " + Integer.toString(blockList.size())); // ----
		
		// Then store the blocks (as data) into the array
		for (Block b : blocks) {
			int xB = Math.abs(b.getX() - xNeg);
			int yB = Math.abs(b.getY() - yNeg);
			int zB = Math.abs(b.getZ() - zNeg);
			blockList.set(xB + (zB * width) + (yB * length * width), b.getBlockData().getAsString());
		}
		
		// And finally save that array to the clipboard
		blockData = blockList;

		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] " + Integer.toString(blockData.size()) + " blocks of data stored"); // ----
		owner.sendMessage("§dSelection copied");
		return true;
	}
	
	// Paste this clipboard with the origin at x,y,z
	public boolean pasteClipboard (int xPos, int yPos, int zPos, boolean setAir) {
		// Start tracking for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Loop through the blocks, along X from negative to positive, Z negative to positive, Y negative to positive
		int rx = 0, ry = 0, rz = 0;
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Pasting blocks into the world"); // ----
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Dimensions of (" + Integer.toString(width) + "," + Integer.toString(height) + "," + Integer.toString(length) + ")"); // ----
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Offset of (" + Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + ")"); // ----
		
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
			
			Block b = Main.world.getBlockAt(xPos + rx - x - 1, yPos + ry - y, zPos + rz - z);
			// Set the block
			if (blockMat == Material.AIR) {
				if (setAir) {
					undoList.add(b.getState());
					b.setType(blockMat);
					b.setBlockData(blockDat);
				}
			}
			else {
				undoList.add(b.getState());
				b.setType(blockMat);
				b.setBlockData(blockDat);
			}
		}
		
		// Store an undo

		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Blocks pasted: " + undoList.size()); // ----
		UndoManager.getUndo(owner).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		
		owner.sendMessage("§dSelection pasted");
		return true;
	}
	
	// Save a schematic to a file
	public boolean saveToFile (String path) {
		path = ("plugins/14erEdit/schematics/" + path).replace("/", File.separator);
		int[] origin = {x,y,z};
		int[] dimensions = {width,height,length};
		Schematic schem = new Schematic(origin, dimensions, blockData);
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
	// This code, violating DRY, is also in the macro
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