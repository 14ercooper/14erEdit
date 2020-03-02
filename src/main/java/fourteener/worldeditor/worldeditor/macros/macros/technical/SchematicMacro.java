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
	public static SchematicMacro createMacro (String[] args, Location loc) {
		SchematicMacro macro = new SchematicMacro();
		// First parse the offset
		macro.xOff = Integer.parseInt(args[1]) * -1;
		macro.yOff = Integer.parseInt(args[2]) * -1;
		macro.zOff = Integer.parseInt(args[3]) * -1;
		// And the center of the paste
		macro.x = loc.getBlockX();
		macro.y = loc.getBlockY();
		macro.z = loc.getBlockZ();
		// Get if air should be set
		macro.setAir = Boolean.parseBoolean(args[4]);
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
			macro.blockData = schem.getBlocks();
			macro.width = schem.getDimensions()[0];
			macro.height = schem.getDimensions()[1];
			macro.length = schem.getDimensions()[2];
		}
		// Using MCEdit format
		else if (path.contains(".schematic")) {
			Operator.currentPlayer.sendMessage("§dMCEdit format schematics are not supported at this time.");
			return new SchematicMacro();
		}
		// Using Sponge format
		else if (path.contains(".schem")) {
			Operator.currentPlayer.sendMessage("§dSponge format schematics are not supported at this time.");
			return new SchematicMacro();
		}
		return macro;
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
