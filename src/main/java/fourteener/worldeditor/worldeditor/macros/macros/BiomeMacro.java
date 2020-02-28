package fourteener.worldeditor.worldeditor.macros.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class BiomeMacro extends Macro {
	
	double radius;
	Biome biome;
	Location pos;
	
	// Create a new macro
	public static BiomeMacro createMacro (String[] args, Location loc) {
		BiomeMacro bm = new BiomeMacro();
		bm.radius = Double.parseDouble(args[0]);
		bm.biome = Biome.valueOf(args[1].toUpperCase(Locale.ROOT));
		bm.pos = loc;
		return bm;
	}
	
	// Run this macro
	public boolean performMacro () {
		// Location of the brush
		double x = pos.getX();
		double z = pos.getZ();
		
		// Generate the sphere
		int radiusInt = (int) Math.round(radius);
		List<Block> blockArray = new ArrayList<Block>();
		for (int rx = -radiusInt; rx <= radiusInt; rx++) {
			for (int rz = -radiusInt; rz <= radiusInt; rz++) {
				if (rx*rx + rz*rz <= (radius + 0.5)*(radius + 0.5)) {
					blockArray.add(Main.world.getBlockAt((int) x + rx, 1, (int) z + rz));
				}
			}
		}
		Main.logDebug("Block array size: " + Integer.toString(blockArray.size())); // ----
		
		// Register an undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(blockArray));
		
		// Create a snapshot array
		List<BlockState> snapshotArray = new ArrayList<BlockState>();
		for (Block b : blockArray) {
			snapshotArray.add(b.getState());
		}
		blockArray = null;
		Main.logDebug(Integer.toString(snapshotArray.size()) + " blocks in snapshot array");
		
		// OPERATE
		for (BlockState bs : snapshotArray) {
			Block b = Main.world.getBlockAt(bs.getLocation());
			b.setBiome(biome);
		}
		
		return true;
	}
}