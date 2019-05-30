package fourteener.worldeditor.worldeditor.macros.macros;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class ErodeMacro extends Macro {
	
	private int erodeRadius = -1; // The radius to actually erode within
	private int erodeType = -1; // 0 for melt, 1 for blendball
	private int erodeSubtype = -1; // -1 if no subtype, 0 for more subtractive, 1 for more additive
	private Location erodeCenter;
	
	public static ErodeMacro createMacro (String[] args, Location loc) {
		ErodeMacro macro = new ErodeMacro();
		macro.erodeRadius = Integer.parseInt(args[0]);
		macro.erodeCenter = loc;
		
		// Determine the type of the erode brush
		if (args[1].equalsIgnoreCase("melt")) {
			macro.erodeType = 0;
		} else if (args[1].equalsIgnoreCase("blend")) {
			macro.erodeType = 1;
		}
		
		// Cut or raise melt?
		if (macro.erodeType == 0) {
			if (args[2].equalsIgnoreCase("cut")) {
				macro.erodeSubtype = 0;
			} else if (args[2].equalsIgnoreCase("raise")) {
				macro.erodeSubtype = 1;
			}
		}
		
		return macro;
	}
	
	public boolean performMacro () {
		// Location of the brush
		double x = erodeCenter.getX();
		double y = erodeCenter.getY();
		double z = erodeCenter.getZ();
		
		// Generate the erode sphere
		List<Block> erosionArray = new ArrayList<Block>();
		for (int rx = -erodeRadius; rx <= erodeRadius; rx++) {
			for (int rz = -erodeRadius; rz <= erodeRadius; rz++) {
				for (int ry = -erodeRadius; ry <= erodeRadius; ry++) {
					if (rx*rx + ry*ry + rz*rz <= (erodeRadius + 0.5)*(erodeRadius + 0.5)) {
						erosionArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
					}
				}
			}
		}
		
		// Generate a snapshot to use for eroding (erode in this, read from world)
		List<BlockState> snapshotArray = new ArrayList<BlockState>();
		for (Block b : erosionArray) {
			snapshotArray.add(b.getState());
		}
		
		// Generate and store an undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(erosionArray));
		erosionArray = null; // This is no longer needed, so clean it up
		
		// Melt cut erosion
		if (erodeType == 0 && erodeSubtype == 0) {
			// TODO Implement
		}
		
		// Melt raise erosion
		if (erodeType == 0 && erodeSubtype == 1) {
			// TODO Implement
		}
		
		// Blend erosion
		if (erodeType == 1) {
			// TODO Implement
		}
		
		// Apply the snapshot to the world, thus completing the erosion
		for (BlockState b : snapshotArray) {
			Location l = b.getLocation();
			Block block = Main.world.getBlockAt(l);
			block.setType(b.getType());
			block.setBlockData(b.getBlockData());
		}
		return true;
	}
}
