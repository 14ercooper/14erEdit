package fourteener.worldeditor.worldeditor.macros.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

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
		// Type 2 - Trunk with branches (branch)
		if (args[0].equalsIgnoreCase("branch")) {
			macro.type = 2;
		}
		// Type 3 - Large trunk with splits and leaves (big)
		if (args[0].equalsIgnoreCase("big")) {
			macro.type = 3;
		}
		// Type 4 - Ground layer bush (bush)
		if (args[0].equalsIgnoreCase("bush")) {
			macro.type = 4;
		}
		// Type 5 - Vanilla style oak/birch (birch)
		if (args[0].equalsIgnoreCase("birch")) {
			macro.type = 5;
		}
		// Type 6 - Vanilla style dark oak (darkoak)
		if (args[0].equalsIgnoreCase("darkoak")) {
			macro.type = 6;
		}
		// Type 7 - Vanilla red mushroom style (redmushroom)
		if (args[0].equalsIgnoreCase("redmushroom")) {
			macro.type = 7;
		}
		// Type 8 - Vanilla brown mushroom style (brownmushroom)
		if (args[0].equalsIgnoreCase("brownmushroom")) {
			macro.type = 8;
		}
		// Type 9 - Tall central trunk with short branches and platforms (jungle)
		if (args[0].equalsIgnoreCase("jungle")) {
			macro.type = 9;
		}
		// Type 10 - Vanilla spruce tree style (spruce)
		if (args[0].equalsIgnoreCase("spruce")) {
			macro.type = 10;
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
			double leafBallSize = (treeHeight * 0.461235) + 1.35425;	// First number is what portion of the tree's height should be leaves (magic)
																		//second number is radius correction for sphere (also magic)
			
			// Start tracking blockstates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			// UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(undoList));
			
			
			// Generate the trunk
			Block currentBlock = Main.world.getBlockAt(plantOn);
			for (int i = 0; i < treeHeight; i++) {
				currentBlock = currentBlock.getRelative(BlockFace.UP);
				if (currentBlock.getType() == Material.AIR) {
					undoList.add(currentBlock.getState());
					currentBlock.setType(trunk);
				}
			}
			// Populate the leaf sphere
			Location topBlock = currentBlock.getLocation();
			double invLeafBallSize = 1 / leafBallSize; // Multiplication is fast, division is slow, and this is used for every leaf block
			// The -(int) ordering stops issues with how integers truncate in different directions when positive versus negative
			int x = topBlock.getBlockX();
			int y = topBlock.getBlockY();
			int z = topBlock.getBlockZ();
			for (int rx =  -(int)leafBallSize; rx <= (int) leafBallSize; rx++) {
				for (int ry = -(int)leafBallSize; ry <= (int) leafBallSize; ry++) {
					for (int rz = -(int)leafBallSize; rz <= (int) leafBallSize; rz++) {
						double distFromCenter = Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
						if (distFromCenter <= leafBallSize) {
							// Okay, all blocks in here are in the sphere
							// Randomness based on distance from center
							// Further leaves have a lower chance of being placed
							if (1.0 - (distFromCenter * invLeafBallSize) < (rand.nextDouble() * 0.333)) {
								continue;
							}
							// Set leaves
							Block toSet = Main.world.getBlockAt(x + rx, y + ry, z + rz);
							if (toSet.getType() == Material.AIR) {
								undoList.add(toSet.getState());
								toSet.setType(leaves);
							}
						}
					}
				}
			}
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
			
			return true;
		}
		// Generator for branch tree
		if (type == 2) {
			// Start tracking BlockStates for an undo
			
			// 
		}
		return false;
	}
}
