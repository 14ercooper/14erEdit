package fourteener.worldeditor.worldeditor.macros.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class BasicTreeMacro extends Macro {
	
	public int type = -1, size, variance;
	public Material leaves, trunk;
	public Location plantOn;
	
	// Type, leaves, trunk, size, variance
	public static BasicTreeMacro createMacro (String[] args, Location loc) {
		BasicTreeMacro macro = new BasicTreeMacro();
		macro.plantOn = loc;
		macro.size = Integer.parseInt(args[3]);
		macro.variance = Integer.parseInt(args[4]);
		macro.leaves = Material.matchMaterial(args[1]);
		macro.trunk = Material.matchMaterial(args[2]);
		
		// Type 1 - Trunk with sphere of leaves (oak)
		if (args[0].equalsIgnoreCase("oak")) {
			macro.type = 1;
		}
		
		return macro;
	}
	
	public boolean performMacro () {
		// Generator for oak tree
		if (type == 1) {			
			// Figure out the size of the tree
			Random rand = new Random();
			double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
			double treeHeight = size + actVariance;
			double leafBallSize = (treeHeight * 0.411235) + 1.35425;	// First number is what portion of the tree's height should be leaves (magic)
																		//second number is radius correction for sphere (also magic)
			
			// Register an undo
			List<Block> undoList = new ArrayList<Block>();
			int x = plantOn.getBlockX(), y = plantOn.getBlockY(), z = plantOn.getBlockZ();
			for (int rx = (-(int)leafBallSize - 3); rx <= leafBallSize + 3; rx++) {
				for (int ry = -3;  ry <= (-(int)(treeHeight + leafBallSize)) + 6; ry++) {
					for (int rz = (-(int)leafBallSize) - 3; rz <= leafBallSize + 3; rz++) {
						undoList.add(Main.world.getBlockAt(x + rx, y + ry, z + rz));
					}
				}
			}
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(undoList));
			
			
			// Generate the trunk
			Block currentBlock = Main.world.getBlockAt(plantOn);
			for (int i = 0; i < treeHeight; i++) {
				currentBlock = currentBlock.getRelative(BlockFace.UP);
				if (currentBlock.getType() == Material.AIR) {
					currentBlock.setType(trunk);
				}
			}
			// Populate the leaf sphere
			Location topBlock = currentBlock.getLocation();
			double invLeafBallSize = 1 / leafBallSize; // Multiplication is fast, division is slow, and this is used for every leaf block
			// The -(int) ordering stops issues with how integers truncate in different directions when positive versus negative
			x = topBlock.getBlockX();
			y = topBlock.getBlockY();
			z = topBlock.getBlockZ();
			for (int rx =  -(int)leafBallSize; rx <= (int) leafBallSize; rx++) {
				for (int ry = -(int)leafBallSize; ry <= (int) leafBallSize; ry++) {
					for (int rz = -(int)leafBallSize; rz <= (int) leafBallSize; rz++) {
						double distFromCenter = Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
						if (distFromCenter <= leafBallSize) {
							// Okay, all blocks in here are in the sphere
							// Randomness based on distance from center
							// Further leaves have a lower chance of being placed, with a maximum of 20%-[epsilon] chance to not place
							if ((distFromCenter * invLeafBallSize) < (rand.nextDouble() * 5.0)) {
								continue;
							}
							// Set leaves
							Block toSet = Main.world.getBlockAt(x + rx, y + ry, z + rz);
							if (toSet.getType() == Material.AIR) {
								toSet.setType(leaves);
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}
}
