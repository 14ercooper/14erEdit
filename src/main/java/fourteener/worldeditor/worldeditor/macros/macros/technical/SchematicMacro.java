package fourteener.worldeditor.worldeditor.macros.macros.technical;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import com.fourteener.schematics.Schematic;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.macros.macros.Macro;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class SchematicMacro extends Macro {
	
	int xOff = 0, yOff = 0, zOff = 0;
	int x = 0, y = 0, z = 0;
	int length = 0, width = 0, height = 0;
	LinkedList<String> blockData = new LinkedList<String>();
	boolean setAir = false;
	
	// Create a new macro
	public SchematicMacro(String[] args, Location loc) {
		super(args, loc);
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
	public boolean performMacro () {
		// Start tracking an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
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
			
			Block b = Main.world.getBlockAt(x + rx - xOff - 1, y + ry - yOff, z + rz - zOff);
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
		
		// Store the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		return true;
	}
}
