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
			// Start tracking blockstates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			// UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(undoList));
			
			// Figure out the size of the tree
			Random rand = new Random();
			double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
			double treeHeight = size + actVariance;
			double leafBallSize = (treeHeight * 0.461235) + 1.35425;	// First number is what portion of the tree's height should be leaves (magic)
																		//second number is radius correction for sphere (also magic)
			
			
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
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Figure out the size of the tree (use 3 levels of branches)
			Random rand = new Random();
			double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
			double branchSize1 = size + actVariance; // This acts like the trunk
			double leafSize1 = (branchSize1 * 0.461235) + 1.35425;
			double branchSize2 = branchSize1 * (0.8 * rand.nextDouble());
			double leafSize2 = (branchSize2 * 0.461235) + 1.35425;
			double branchSize3 = branchSize2 * (0.8 * rand.nextDouble());
			double leafSize3 = (branchSize3 * 0.461235) + 1.35425;
			
			// These determine the density of branches
			double branch2StartDensity = 0.4;
			double branch3StartDensity = 0.7;
			
			// These determine how far from the start the branches must be to start growing
			double branch2StartOffset = branchSize1 * 0.5;
			double branch3StartOffset = branchSize2 * 0.5;
			
			// Create some variables to store end points
			List<Location> branch1Ends = new ArrayList<Location>();
			List<Location> branch2Ends = new ArrayList<Location>();
			List<Location> branch3Ends = new ArrayList<Location>();
			List<Location> branch2Starts = new ArrayList<Location>();
			List<Location> branch3Starts = new ArrayList<Location>();
			
			// Generate the first branch
			Block currentBlock = Main.world.getBlockAt(plantOn);
			for (int i = 1; i < branchSize1; i++) {
				currentBlock = currentBlock.getRelative(BlockFace.UP);
				if (i >= branch2StartOffset && rand.nextDouble() <= branch2StartDensity) {
					branch2Starts.add(currentBlock.getLocation());
				}
				if (currentBlock.getType() == Material.AIR) {
					undoList.add(currentBlock.getState());
					currentBlock.setType(trunk);
				}
			}
			branch1Ends.add(currentBlock.getLocation());
			
			// Generate the second branches
			for (Location l : branch2Starts) {
				currentBlock = Main.world.getBlockAt(l);
				// Pick an initial direction
				double randNum = rand.nextDouble();
				BlockFace dir;
				if (randNum <= 0.25) {
					dir = BlockFace.NORTH;
				} else if (randNum <= 0.5) {
					dir = BlockFace.EAST;
				} else if (randNum <= 0.75) {
					 dir = BlockFace.SOUTH;
				} else {
					dir = BlockFace.WEST;
				}
				
				for (int i = 1; i < branchSize2; i++) {
					// Pick a direction to grow, 20% of the time
					if (rand.nextDouble() <= 0.2) {
						randNum = rand.nextDouble();
						if (randNum <= 0.1666 ) {
							dir = BlockFace.NORTH;
						} else if (randNum <= 0.3333) {
							dir = BlockFace.EAST;
						} else if (randNum <= 0.5) {
							dir = BlockFace.SOUTH;
						} else if (randNum <= 0.6666) {
							dir = BlockFace.WEST;
						} else if (randNum <= 0.8333) {
							dir = BlockFace.UP;
						} else {
							dir = BlockFace.DOWN;
						}
					}
					// Grow there
					currentBlock = currentBlock.getRelative(dir);
					if (i >= branch3StartOffset && rand.nextDouble() <= branch3StartDensity) {
						branch3Starts.add(currentBlock.getLocation());
					}
					if (currentBlock.getType() == Material.AIR) {
						undoList.add(currentBlock.getState());
						currentBlock.setType(trunk);
					}
				}
				branch2Ends.add(currentBlock.getLocation());
			}
			
			// Generate the third branches
			for (Location l : branch3Starts) {
				currentBlock = Main.world.getBlockAt(l);
				// Pick an initial direction
				double randNum = rand.nextDouble();
				BlockFace dir;
				if (randNum <= 0.25) {
					dir = BlockFace.NORTH;
				} else if (randNum <= 0.5) {
					dir = BlockFace.EAST;
				} else if (randNum <= 0.75) {
					 dir = BlockFace.SOUTH;
				} else {
					dir = BlockFace.WEST;
				}
				
				for (int i = 1; i < branchSize3; i++) {
					// Pick a direction to grow, 20% of the time
					if (rand.nextDouble() <= 0.2) {
						randNum = rand.nextDouble();
						if (randNum <= 0.1666 ) {
							dir = BlockFace.NORTH;
						} else if (randNum <= 0.3333) {
							dir = BlockFace.EAST;
						} else if (randNum <= 0.5) {
							dir = BlockFace.SOUTH;
						} else if (randNum <= 0.6666) {
							dir = BlockFace.WEST;
						} else if (randNum <= 0.8333) {
							dir = BlockFace.UP;
						} else {
							dir = BlockFace.DOWN;
						}
					}
					// Grow there
					currentBlock = currentBlock.getRelative(dir);
					if (currentBlock.getType() == Material.AIR) {
						undoList.add(currentBlock.getState());
						currentBlock.setType(trunk);
					}
				}
				branch3Ends.add(currentBlock.getLocation());
			}
			
			// Generate the first leaf sphere
			for (Location startLoc : branch1Ends) {
				double invLeafBallSize = 1 / leafSize1; // Multiplication is fast, division is slow, and this is used for every leaf block
				// The -(int) ordering stops issues with how integers truncate in different directions when positive versus negative
				int x = startLoc.getBlockX();
				int y = startLoc.getBlockY();
				int z = startLoc.getBlockZ();
				for (int rx =  -(int)leafSize1; rx <= (int) leafSize1; rx++) {
					for (int ry = -(int)leafSize1; ry <= (int) leafSize1; ry++) {
						for (int rz = -(int)leafSize1; rz <= (int) leafSize1; rz++) {
							double distFromCenter = Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
							if (distFromCenter <= leafSize1) {
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
			}
			
			// Generate the second leaf spheres
			for (Location startLoc : branch2Ends) {
				double invLeafBallSize = 1 / leafSize2; // Multiplication is fast, division is slow, and this is used for every leaf block
				// The -(int) ordering stops issues with how integers truncate in different directions when positive versus negative
				int x = startLoc.getBlockX();
				int y = startLoc.getBlockY();
				int z = startLoc.getBlockZ();
				for (int rx =  -(int)leafSize2; rx <= (int) leafSize2; rx++) {
					for (int ry = -(int)leafSize2; ry <= (int) leafSize2; ry++) {
						for (int rz = -(int)leafSize2; rz <= (int) leafSize2; rz++) {
							double distFromCenter = Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
							if (distFromCenter <= leafSize2) {
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
			}
			
			// Generate the third leaf spheres
			for (Location startLoc : branch3Ends) {
				double invLeafBallSize = 1 / leafSize3; // Multiplication is fast, division is slow, and this is used for every leaf block
				// The -(int) ordering stops issues with how integers truncate in different directions when positive versus negative
				int x = startLoc.getBlockX();
				int y = startLoc.getBlockY();
				int z = startLoc.getBlockZ();
				for (int rx =  -(int)leafSize3; rx <= (int) leafSize3; rx++) {
					for (int ry = -(int)leafSize3; ry <= (int) leafSize3; ry++) {
						for (int rz = -(int)leafSize3; rz <= (int) leafSize3; rz++) {
							double distFromCenter = Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
							if (distFromCenter <= leafSize3) {
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
			}
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for big tree
		if (type == 3) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for bush tree
		if (type == 4) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for birch tree
		if (type == 5) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for darkoak tree
		if (type == 6) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for redmushroom tree
		if (type == 7) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for brownmushroom tree
		if (type == 8) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for jungle tree
		if (type == 9) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		// Generator for spruce tree
		if (type == 10) {
			// Start tracking BlockStates for an undo
			List<BlockState> undoList = new ArrayList<BlockState>();
			
			// Generator logic & code here
			
			// Actually register the undo
			UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
		}
		
		return false;
	}
}
