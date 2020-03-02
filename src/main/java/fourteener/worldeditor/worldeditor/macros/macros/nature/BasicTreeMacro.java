package fourteener.worldeditor.worldeditor.macros.macros.nature;

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
import fourteener.worldeditor.worldeditor.macros.macros.Macro;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class BasicTreeMacro extends Macro {
	
	public int type = -1, size, variance;
	public Material leaves, trunk;
	public Location plantOn;
	
	// Type, leaves, trunk, size, variance
	public BasicTreeMacro(String[] args, Location loc) {
		super(args, loc);
		plantOn = loc;
		size = Integer.parseInt(args[3]);
		variance = (int) Math.ceil(Double.parseDouble(args[4]));
		leaves = Material.matchMaterial(args[1]);
		trunk = Material.matchMaterial(args[2]);
		
		// Type 1 - Trunk with sphere of leaves (oak)
		if (args[0].equalsIgnoreCase("oak")) {
			type = 1;
		}
		// Type 2 - Trunk with branches (branch)
		if (args[0].equalsIgnoreCase("branch")) {
			type = 2;
		}
		// Type 3 - Large trunk with splits and leaves (big)
		if (args[0].equalsIgnoreCase("big")) {
			type = 3;
		}
		// Type 4 - Ground layer bush (bush)
		if (args[0].equalsIgnoreCase("bush")) {
			type = 4;
		}
		// Type 5 - Vanilla style oak/birch (birch)
		if (args[0].equalsIgnoreCase("birch")) {
			type = 5;
		}
		// Type 6 - Vanilla style dark oak (darkoak)
		if (args[0].equalsIgnoreCase("darkoak")) {
			type = 6;
		}
		// Type 7 - Vanilla red mushroom style (redmushroom)
		if (args[0].equalsIgnoreCase("redmushroom")) {
			type = 7;
		}
		// Type 8 - Vanilla brown mushroom style (brownmushroom)
		if (args[0].equalsIgnoreCase("brownmushroom")) {
			type = 8;
		}
		// Type 9 - Tall central trunk with short branches and platforms (jungle)
		if (args[0].equalsIgnoreCase("jungle")) {
			type = 9;
		}
	}
	
	public boolean performMacro () {
		// Generator for oak tree
		if (type == 1) {
			oakTreeGenerator();
			return true;
		}
		
		// Generator for branch tree
		if (type == 2) {
			branchTreeGenerator();
			return true;
		}
		
		// Generator for big tree
		if (type == 3) {
			bigTreeGenerator();
			return true;
		}
		
		// Generator for bush tree
		if (type == 4) {
			bushTreeGenerator();
			return true;
		}
		
		// Generator for birch tree
		if (type == 5) {
			birchTreeGenerator();
			return true;
		}
		
		// Generator for darkoak tree
		if (type == 6) {
			darkOakTreeGenerator();
			return true;
		}
		
		// Generator for redmushroom tree
		if (type == 7) {
			redMushroomTreeGenerator();
			return true;
		}
		
		// Generator for brownmushroom tree
		if (type == 8) {
			brownMushroomTreeGenerator();
			return true;
		}
		
		// Generator for jungle tree
		if (type == 9) {
			jungleTreeGenerator();
			return true;
		}
		
		return false;
	}

	private void jungleTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Calculate the size of the tree
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double treeSize = size + actVariance;
		double eTopX = treeSize * 0.9;
		double eTopZ = eTopX, eTopY = eTopX * 0.5;
		
		// Calculate the size and frequency of branches
		double branchSize = treeSize * 0.35;
		double branchVariance = variance * 0.35;
		double branchStartHeight = treeSize * 0.4;
		double branchFrequency = ((270.0 / treeSize) / (treeSize - branchStartHeight)) * 0.5;
		double eBranchX = branchSize * 1.25;
		double eBranchZ = eBranchX, eBranchY = eBranchX * 0.7;
		
		// Stores the centers of the various ellipoids and starts of branches
		Block currentBlock = Main.world.getBlockAt(plantOn);
		Block topBlock;
		List<Block> branchEnds = new ArrayList<Block>();
		List<Block> branchStarts = new ArrayList<Block>();
		List<Block> trunkBlocks = new ArrayList<Block>();
		List<Block> leafBlocks = new ArrayList<Block>();
		
		// Generate the central trunk
		for (int i = 1; i <= treeSize + ((treeSize * 0.25 > 7) ? (treeSize * 0.25) : 7); i++) {
			// Move the current block
			currentBlock = currentBlock.getRelative(BlockFace.UP);
			
			// Set the current block and other trunk blocks
			trunkBlocks.add(currentBlock);
			trunkBlocks.add(currentBlock.getRelative(BlockFace.NORTH));
			trunkBlocks.add(currentBlock.getRelative(BlockFace.EAST));
			trunkBlocks.add(currentBlock.getRelative(BlockFace.NORTH_EAST));
			
			// Add a new branch if needed
			if (rand.nextDouble() <= branchFrequency && i >= branchStartHeight && i <= treeSize) {
				branchStarts.add(currentBlock);
			}
		}
		topBlock = currentBlock.getRelative(BlockFace.UP);
		
		// Generate the branches
		for (Block b : branchStarts) {
			currentBlock = b;
			boolean firstBlock = true;
			BlockFace branchDir;
			double randNum = rand.nextDouble();
			if (randNum <= 0.25) {
				branchDir = BlockFace.NORTH;
				currentBlock = currentBlock.getRelative(BlockFace.NORTH, 2);
			} else if (randNum <= 0.5) {
				branchDir = BlockFace.EAST;
				currentBlock = currentBlock.getRelative(BlockFace.EAST, 2);
			} else if (randNum <= 0.75) {
				branchDir = BlockFace.SOUTH;
				currentBlock = currentBlock.getRelative(BlockFace.SOUTH);
			} else {
				branchDir = BlockFace.WEST;
				currentBlock = currentBlock.getRelative(BlockFace.WEST);
			}
			
			double branchLength = branchSize + (((rand.nextDouble() * 2.0) - 1.0) * branchVariance);
			for (int i = 1; i <= branchLength; i++) {
				// Maybe curve the branch?
				if (!firstBlock) {
					currentBlock = currentBlock.getRelative(branchDir);
					if (rand.nextDouble() <= 0.33) {
						if (branchDir == BlockFace.NORTH || branchDir == BlockFace.SOUTH) {
							if (rand.nextBoolean()) {
								currentBlock = currentBlock.getRelative(BlockFace.EAST);
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.WEST);
							}
						}
						if (branchDir == BlockFace.EAST || branchDir == BlockFace.WEST) {
							if (rand.nextBoolean()) {
								currentBlock = currentBlock.getRelative(BlockFace.NORTH);
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.SOUTH);
							}
						}
					}
					if (rand.nextDouble() <= 0.15) {
						currentBlock = currentBlock.getRelative(BlockFace.UP);
					}
				}
				
				// Add the block to the array
				trunkBlocks.add(currentBlock);
				firstBlock = false;
			}
			branchEnds.add(currentBlock.getRelative(BlockFace.UP));
		}
		
		// Generate the top leaf semi-ellipse
		int x = topBlock.getX(), y = topBlock.getY(), z = topBlock.getZ();
		for (double rx = -eTopX; rx <= eTopX; rx++) {
			for (double ry = -(eTopY / 3.0); ry <= eTopY; ry++) {
				for (double rz = -eTopZ; rz <= eTopZ; rz++) {
					double ellipsoidValue = (((rx * rx) / (eTopX * eTopX)) + ((ry * ry) / (eTopY * eTopY)) + ((rz * rz) / (eTopZ * eTopZ)));
					if (ellipsoidValue <= (1.15)) {
						leafBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y + ry), (int) (z + rz)));
					}
				}
			}
		}
		
		// Generate the leaf semi-ellipses for the branches
		for (Block b : branchEnds) {
			x = b.getX();
			y = b.getY();
			z = b.getZ();
			for (double rx = -eBranchX; rx <= eBranchX; rx++) {
				for (double ry = -(eBranchY / 3.0); ry <= eBranchY / 2.0; ry++) {
					for (double rz = -eBranchZ; rz <= eBranchZ; rz++) {
						double ellipsoidValue = (((rx * rx) / (eBranchX * eBranchX)) + ((ry * ry) / (eBranchY * eBranchY)) + ((rz * rz) / (eBranchZ * eBranchZ)));
						if (ellipsoidValue <= (1.15)) {
							leafBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y + ry), (int) (z + rz)));
						}
					}
				}
			}
		}
		
		// Place the trunk blocks
		for (Block b : trunkBlocks) {
			if (b.getType() == Material.AIR) {
				undoList.add(b.getState());
				b.setType(trunk);
			}
		}
		
		// Place the leaf blocks
		for (Block b : leafBlocks) {
			if (b.getType() == Material.AIR) {
				undoList.add(b.getState());
				b.setType(leaves);
			}
		}
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void brownMushroomTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Determine the size of the mushroom
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double treeSize = size + actVariance;
		Main.logDebug("Generating mushroom of size " + Double.toString(treeSize)); // -----
		
		// One, two, or 3 layer cap?
		// Also calculate cap circle cutoffs
		int numCaps = 0;
		if (treeSize > 60) {
			Main.logDebug("5 cap mushroom"); // -----
			numCaps = 5;
		}
		else if (treeSize > 40) {
			Main.logDebug("4 cap mushroom"); // -----
			numCaps = 4;
		}
		else if (treeSize > 27) {
			Main.logDebug("3 cap mushroom"); // -----
			numCaps = 3;
		}
		else if (treeSize > 15) {
			Main.logDebug("2 cap mushroom"); // -----
			numCaps = 2;
		}
		else {
			Main.logDebug("1 cap mushroom"); // -----
			numCaps = 1;
		}
		double radiusCorrection = 0.35;
		double cap1Cut = 0, cap2Cut = 0, cap3Cut = 0, cap4Cut = 0, cap5Cut = 0; // These are radii
		if (numCaps <= 3) {
			Main.logDebug("Less than or equal to 3 caps will be generated"); // -----
			cap1Cut = treeSize * 0.35;
			cap2Cut = treeSize * 0.65;
			cap3Cut = treeSize;
		}
		else {
			Main.logDebug("More than 3 caps will be generated"); // -----
			cap1Cut = treeSize * 0.25;
			cap2Cut = treeSize * 0.4;
			cap3Cut = treeSize * 0.55;
			cap4Cut = treeSize * 0.7;
			cap5Cut = treeSize * 0.85;
		}
		
		// Generate the stem of the mushroom
		Main.logDebug("Generating mushroom stem"); // -----
		Block currentBlock = Main.world.getBlockAt(plantOn);
		BlockFace currentDirection = BlockFace.DOWN;
		for (int i = 1; i <= treeSize; i++) {
			// Update the current block
			double randNum = rand.nextDouble();
			if (randNum <= 0.333) {
				randNum = rand.nextDouble();
				if (randNum <= 0.125 || currentDirection == BlockFace.DOWN) {
					randNum = rand.nextDouble();
					if (randNum <= 0.125) {
						currentDirection = BlockFace.NORTH;
					} else if (randNum <= 0.25) {
						currentDirection = BlockFace.NORTH_EAST;
					} else if (randNum <= 0.375) {
						currentDirection = BlockFace.EAST;
					} else if (randNum <= 0.5) {
						currentDirection = BlockFace.SOUTH_EAST;
					} else if (randNum <= 0.625) {
						currentDirection = BlockFace.SOUTH;
					} else if (randNum <= 0.75) {
						currentDirection = BlockFace.SOUTH_WEST;
					} else if (randNum <= 0.875) {
						currentDirection = BlockFace.WEST;
					} else {
						currentDirection = BlockFace.NORTH_WEST;
					}
				}
				currentBlock = currentBlock.getRelative(currentDirection);
			}
			currentBlock = currentBlock.getRelative(BlockFace.UP);
			
			// Place the current block
			if (currentBlock.getType() == Material.AIR) {
				undoList.add(currentBlock.getState());
				currentBlock.setType(trunk);
			}
		}
		currentBlock = currentBlock.getRelative(BlockFace.UP);
		
		// Generate the three cap layers as needed
		double x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
		Main.logDebug("Generating cap at (" + Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z) + ")"); // -----
		List<Block> capBlocks = new ArrayList<Block>();
		if (numCaps >= 1) {
			Main.logDebug("Size 1 cap generating"); // -----
			for (double rx = -cap1Cut; rx <= cap1Cut; rx++) {
				for (double rz = -cap1Cut; rz <= cap1Cut; rz++) {
					if ((rx * rx) + (rz * rz) <= ((cap1Cut + radiusCorrection) * (cap1Cut + radiusCorrection))) {
						capBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y), (int) (z + rz)));
					}
				}
			}
			Main.logDebug("Number of blocks: " + Integer.toString(capBlocks.size())); // -----
		}
		if (numCaps >= 2) {
			Main.logDebug("Size 2 cap generating"); // -----
			for (int rx = -(int)cap2Cut; rx <= cap2Cut; rx++) {
				for (int rz = -(int)cap2Cut; rz <= cap2Cut; rz++) {
					if ((rx * rx) + (rz * rz) <= ((cap2Cut + radiusCorrection) * (cap2Cut + radiusCorrection))
							&& (rx * rx) + (rz * rz) >= ((cap1Cut - radiusCorrection) * (cap1Cut - radiusCorrection))) {
						capBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y - 1), (int) (z + rz)));
					}
				}
			}
			Main.logDebug("Number of blocks: " + Integer.toString(capBlocks.size())); // -----
		}
		if (numCaps >= 3) {
			Main.logDebug("Size 3 cap generating"); // -----
			for (int rx = -(int)cap3Cut; rx <= cap3Cut; rx++) {
				for (int rz = -(int)cap3Cut; rz <= cap3Cut; rz++) {
					if ((rx * rx) + (rz * rz) <= ((cap3Cut + radiusCorrection) * (cap3Cut + radiusCorrection))
							&& (rx * rx) + (rz * rz) >= ((cap2Cut - radiusCorrection) * (cap2Cut - radiusCorrection))) {
						capBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y - 2), (int) (z + rz)));
					}
				}
			}
			Main.logDebug("Number of blocks: " + Integer.toString(capBlocks.size())); // -----
		}
		if (numCaps >= 4) {
			Main.logDebug("Size 4 cap generating"); // -----
			for (int rx = -(int)cap4Cut; rx <= cap4Cut; rx++) {
				for (int rz = -(int)cap4Cut; rz <= cap4Cut; rz++) {
					if ((rx * rx) + (rz * rz) <= ((cap4Cut + radiusCorrection) * (cap4Cut + radiusCorrection))
							&& (rx * rx) + (rz * rz) >= ((cap3Cut - radiusCorrection) * (cap3Cut - radiusCorrection))) {
						capBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y - 3), (int) (z + rz)));
					}
				}
			}
			Main.logDebug("Number of blocks: " + Integer.toString(capBlocks.size())); // -----
		}
		if (numCaps >= 5) {
			Main.logDebug("Size 5 cap generating"); // -----
			for (int rx = -(int)cap5Cut; rx <= cap5Cut; rx++) {
				for (int rz = -(int)cap5Cut; rz <= cap5Cut; rz++) {
					if ((rx * rx) + (rz * rz) <= ((cap5Cut + radiusCorrection) * (cap5Cut + radiusCorrection))
							&& (rx * rx) + (rz * rz) >= ((cap4Cut - radiusCorrection) * (cap4Cut - radiusCorrection))) {
						capBlocks.add(Main.world.getBlockAt((int) (x + rx), (int) (y - 2), (int) (z + rz)));
					}
				}
			}
			Main.logDebug("Number of blocks: " + Integer.toString(capBlocks.size())); // -----
		}
		
		// Place the cap blocks
		Main.logDebug("Placing cap blocks"); // -----
		for (Block bl : capBlocks) {
			if (bl.getType() == Material.AIR) {
				undoList.add(bl.getState());
				bl.setType(leaves);
			}
		}
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void redMushroomTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Create the stem (with a slight curve)
		Random rand = new Random ();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double treeSize = size + actVariance;
		Block currentBlock = Main.world.getBlockAt(plantOn);
		BlockFace currentDirection = BlockFace.DOWN;
		for (int i = 1; i <= treeSize; i++) {
			// Update the current block
			double randNum = rand.nextDouble();
			if (randNum <= 0.25) {
				randNum = rand.nextDouble();
				if (randNum <= 0.25 || currentDirection == BlockFace.DOWN) {
					randNum = rand.nextDouble();
					if (randNum <= 0.125) {
						currentDirection = BlockFace.NORTH;
					} else if (randNum <= 0.25) {
						currentDirection = BlockFace.NORTH_EAST;
					} else if (randNum <= 0.375) {
						currentDirection = BlockFace.EAST;
					} else if (randNum <= 0.5) {
						currentDirection = BlockFace.SOUTH_EAST;
					} else if (randNum <= 0.625) {
						currentDirection = BlockFace.SOUTH;
					} else if (randNum <= 0.75) {
						currentDirection = BlockFace.SOUTH_WEST;
					} else if (randNum <= 0.875) {
						currentDirection = BlockFace.WEST;
					} else {
						currentDirection = BlockFace.NORTH_WEST;
					}
				}
				currentBlock = currentBlock.getRelative(currentDirection);
			}
			currentBlock = currentBlock.getRelative(BlockFace.UP);
			
			// Place the current block
			if (currentBlock.getType() == Material.AIR) {
				undoList.add(currentBlock.getState());
				currentBlock.setType(trunk);
			}
		}
		
		// Create the cap
		List<Block> leafList = new ArrayList<Block>();
		leafList.add(currentBlock.getRelative(BlockFace.UP));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST));
		leafList.add(currentBlock.getRelative(BlockFace.NORTH,2));
		leafList.add(currentBlock.getRelative(BlockFace.EAST,2));
		leafList.add(currentBlock.getRelative(BlockFace.SOUTH,2));
		leafList.add(currentBlock.getRelative(BlockFace.WEST,2));
		leafList.add(currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.WEST));
		double numCapDrops = (treeSize * 0.333 > 2) ? (treeSize * 0.333) : 2;
		for (int j = 1; j <= numCapDrops; j++) {
			leafList.add(currentBlock.getRelative(BlockFace.NORTH,2).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.EAST,2).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH,2).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.WEST,2).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN, j));
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN, j));
		}
		
		// Place the cap
		for (Block b : leafList) {
			if (b.getType() == Material.AIR) {
				undoList.add(b.getState());
				b.setType(leaves);}
		}
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void darkOakTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Generate the trunk of the tree (build up, with a 2-3 curves to the side; placing a shaft with blocks also the the N/E/NE)
		// Make the trunk 3-wide for particularly large trees, with 6-7 curves to the side
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double treeSize = size + actVariance;
		Block currentBlock = Main.world.getBlockAt(plantOn);
		BlockFace curveDirection;
		// Determine the curve direction
		double randNum = rand.nextDouble();
		if (randNum <= 0.125) {
			curveDirection = BlockFace.NORTH;
		} else if (randNum <= 0.25) {
			curveDirection = BlockFace.NORTH_EAST;
		} else if (randNum <= 0.375) {
			curveDirection = BlockFace.EAST;
		} else if (randNum <= 0.5) {
			curveDirection = BlockFace.SOUTH_EAST;
		} else if (randNum <= 0.625) {
			curveDirection = BlockFace.SOUTH;
		} else if (randNum <= 0.75) {
			curveDirection = BlockFace.SOUTH_WEST;
		} else if (randNum <= 0.875) {
			curveDirection = BlockFace.WEST;
		} else {
			curveDirection = BlockFace.NORTH_WEST;
		}
		// Cutoff for 3-wide trunk logic
		int trunkSize = 1;
		double curveGap = 1;
		if (treeSize > 23) {
			trunkSize = 3;
			curveGap = (treeSize * 0.5) / 7.0;
		} else if (treeSize > 12) {
			trunkSize = 2;
			curveGap = (treeSize * 0.5) / 3.0;
		} else {
			trunkSize = 2;
			curveGap = (treeSize * 0.5) / 2.0;
		}
		
		// Grow the tree trunk
		double curveCut = 0;
		boolean doCurve = false;
		for (int i = 0; i <= treeSize; i++) {
			// If over half the tree has been grown, curve logic takes effect
			doCurve = false;
			if (i >= (treeSize * 0.5)) {
				if (curveCut >= curveGap) {
					doCurve = true;
					curveCut -= curveGap;
				}
				curveCut += 1;
			}
			
			// Calculate the new corner block
			if (doCurve) {
				currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(curveDirection);
			}
			else {
				currentBlock = currentBlock.getRelative(BlockFace.UP);
			}
			
			// Place the blocks as needed
			List<Block> blockList = new ArrayList<Block>();
			if (trunkSize == 2) {
				blockList.add(currentBlock);
				blockList.add(currentBlock.getRelative(BlockFace.NORTH));
				blockList.add(currentBlock.getRelative(BlockFace.EAST));
				blockList.add(currentBlock.getRelative(BlockFace.NORTH_EAST));
				for (Block b : blockList) {
					undoList.add(b.getState());
					b.setType(trunk);
				}
			}
			if (trunkSize == 3) {
				blockList.add(currentBlock);
				blockList.add(currentBlock.getRelative(BlockFace.NORTH));
				blockList.add(currentBlock.getRelative(BlockFace.EAST));
				blockList.add(currentBlock.getRelative(BlockFace.NORTH_EAST));
				blockList.add(currentBlock.getRelative(BlockFace.NORTH, 2));
				blockList.add(currentBlock.getRelative(BlockFace.EAST, 2));
				blockList.add(currentBlock.getRelative(BlockFace.NORTH_EAST, 2));
				blockList.add(currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.NORTH));
				blockList.add(currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.EAST));
				for (Block b : blockList) {
					undoList.add(b.getState());
					b.setType(trunk);
				}
			}
		}
		
		// Place the semi-ellipse of leaves
		double eX = treeSize * 0.75;
		double eY = eX * 0.42;
		double eZ = eX;
		int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
		for (double rx = -eX; rx <= eX; rx++) {
			for (double ry = -(eY / 3.0); ry <= eY; ry++) {
				for (double rz = -eZ; rz <= eZ; rz++) {
					double ellipsoidValue = (((rx * rx) / (eX * eX)) + ((ry * ry) / (eY * eY)) + ((rz * rz) / (eZ * eZ)));
					if (ellipsoidValue <= (1.15)) {
						Block toPlace = Main.world.getBlockAt((int) (x + rx), (int) (y + ry), (int) (z + rz));
						if (toPlace.getType() == Material.AIR) {
							undoList.add(toPlace.getState());
							toPlace.setType(leaves);
						}
					}
				}
			}
		}
		
		// Generator logic & code here
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void birchTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Create the vertical log
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double treeSize = size + actVariance;
		Block currentBlock = Main.world.getBlockAt(plantOn);
		for (int i = 1; i <= treeSize; i++) {
			currentBlock = currentBlock.getRelative(BlockFace.UP);
			if (currentBlock.getType() == Material.AIR) {
				undoList.add(currentBlock.getState());
				currentBlock.setType(trunk);
			}
		}
		
		// Create the leaves
		// First create an array of locations where leaves will go
		List<Block> leafList = new ArrayList<Block>();
		// These leaves are guaranteed
		leafList.add(currentBlock.getRelative(BlockFace.UP));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.NORTH, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.EAST, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.SOUTH, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.WEST, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH, 2));
		leafList.add(currentBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST, 2));
		// The eight corner leaves are 50/50
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.NORTH, 2).getRelative(BlockFace.EAST, 2));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.NORTH, 2).getRelative(BlockFace.WEST, 2));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.SOUTH, 2).getRelative(BlockFace.EAST, 2));
		}
		if (rand.nextBoolean()) {
			leafList.add(currentBlock.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.SOUTH, 2).getRelative(BlockFace.WEST, 2));
		}
		
		// Then place the leaf blocks
		for (Block b : leafList) {
			if (b.getType() == Material.AIR) {
				undoList.add(b.getState());
				b.setType(leaves);
			}
		}
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void bushTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Place the log block
		Block baseBlock = Main.world.getBlockAt(plantOn).getRelative(BlockFace.UP);
		if (baseBlock.getType() == Material.AIR) {
			undoList.add(baseBlock.getState());
			baseBlock.setType(trunk);
		}
		
		// Generate the ellipsoid of leaves
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double bushSize = size + actVariance;
		double eX = bushSize * 0.55;
		double eY = eX * 0.333;
		double eZ = eX;
		int x = baseBlock.getX(), y = baseBlock.getY(), z = baseBlock.getZ();
		for (double rx = -eX; rx <= eX; rx++) {
			for (double ry = -eY; ry <= eY; ry++) {
				for (double rz = -eZ; rz <= eZ; rz++) {
					double ellipsoidValue = (((rx * rx) / (eX * eX)) + ((ry * ry) / (eY * eY)) + ((rz * rz) / (eZ * eZ)));
					if (ellipsoidValue <= (1.15)) {
						Block toPlace = Main.world.getBlockAt((int) (x + rx), (int) (y + ry), (int) (z + rz));
						// Further leaves are less likely to get placed
						if (1.0 - (ellipsoidValue * 0.869565217) < (rand.nextDouble() * 0.5)) {
							continue;
						}
						if (toPlace.getType() == Material.AIR) {
							undoList.add(toPlace.getState());
							toPlace.setType(leaves);
						}
					}
				}
			}
		}
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void bigTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Calculate the dimensions of the tree, other needed variables
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double treeHeight = size + actVariance;
		int numSplits, baseSize;
		// Splits should be at 1/2; 3/4; 7/8; 15/16; and 31/32 of the tree
		// Preserve the same number of blocks per layer before and after the split (1,3,5 are triple; 2,4 are double)
		if (treeHeight <= 15) {
			numSplits = 1;
			baseSize = 3;
		} else if (treeHeight <= 30) {
			numSplits = 2;
			baseSize = 6;
		} else if (treeHeight <= 60) {
			numSplits = 3;
			baseSize = 18;
		} else if (treeHeight <= 90) {
			numSplits = 4;
			baseSize = 36;
		} else {
			numSplits = 5;
			baseSize = 108;
		}
		
		// Variables needed by the generator
		List<Location> endPoints = new ArrayList<Location>();
		double leafBallSize = (treeHeight * 0.56) + 1.3;
		
		// Generate the trunk of the tree
		// This is done by generating a "stick" up (with some curves) then creating circles around it
		double startBranchThickness = baseSize, nextBranchThickness = baseSize;
		List<Location> branchStarts = new ArrayList<Location>();
		branchStarts.add(Main.world.getBlockAt(plantOn).getRelative(BlockFace.UP).getLocation());
		double branchHeight = treeHeight;
		for (int i = 1; i <= (numSplits + 1); i++) {
			Main.logDebug("i value of " + Integer.toString(i)); // -----
			List<Location> theseBranches = branchStarts;
			branchStarts = new ArrayList<Location>();
			// Adjust the length of branch to generate accordingly
			if (numSplits == 1) {
				branchHeight *= 0.75;
			} else if (numSplits == 2) {
				branchHeight *= 0.6666;
			} else if (numSplits == 3) {
				branchHeight *= 0.59;
			} else if (numSplits == 4) {
				branchHeight *= 0.54;
			} else {
				branchHeight *= 0.5;
			}

			if (i % 2 == 0)
				nextBranchThickness /= 3;
			else
				nextBranchThickness /= 2;
			
			double branchTaper = (startBranchThickness - nextBranchThickness) / branchHeight;
			double branchThickness = startBranchThickness;
			
			// Generate the stick up with circles around each layer
			// For the first branch, build up
			if (i == 1) {
				Main.logDebug("First branch growing, number of locations equals " + Integer.toString(theseBranches.size())); // -----
				for (Location startLoc : theseBranches) {
					// Grow up
					Block currentBlock = Main.world.getBlockAt(startLoc);
					for (int j = 1; j <= branchHeight; j++) {
						branchThickness -= branchTaper;
						// Place the central block
						if (currentBlock.getType() == Material.AIR) {
							undoList.add(currentBlock.getState());
							currentBlock.setType(trunk);
						}
						
						// Build the disk
						double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
						int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
						for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
							for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
								if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
									Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
									if (b.getType() == Material.AIR) {
										undoList.add(b.getState());
										b.setType(trunk);
									}
								}
							}
						}
						
						// Update the current block
						if (rand.nextDouble() <= 0.2) { // 20% chance to move a block to the side by 1 in a random direction
							double randNum = rand.nextDouble();
							if (randNum <= 0.125) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
							}
							else if (randNum <= 0.25) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
							}
							else if (randNum <= 0.375) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
							}
							else if (randNum <= 0.5) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
							}
							else if (randNum <= 0.625) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
							}
							else if (randNum <= 0.75) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
							}
							else if (randNum <= 0.875) {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
							}
						}
						else {
							currentBlock = currentBlock.getRelative(BlockFace.UP);
						}
					}
					
					// Store a branch start
					branchStarts.add(currentBlock.getLocation());
				}
			}
				
			// For branches 2, 4, grow out N/S or E/W
			else if (i % 2 == 1) {

				Main.logDebug("Even branch growing, number of locations " + Integer.toString(theseBranches.size())); // -----
				for (Location startLoc : theseBranches) {
					// Case N/S
					if (rand.nextBoolean()) {
						// North branch
						Block currentBlock = Main.world.getBlockAt(startLoc);
						for (int j = 1; j <= branchHeight; j++) {
							branchThickness -= branchTaper;
							// Place the central block
							if (currentBlock.getType() == Material.AIR) {
								undoList.add(currentBlock.getState());
								currentBlock.setType(trunk);
							}
							
							// Build the disk
							double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
							int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
							for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
								for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
									if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
										Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
										if (b.getType() == Material.AIR) {
											undoList.add(b.getState());
											b.setType(trunk);
										}
									}
								}
							}
							
							// Update the current block
							if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
								if (rand.nextBoolean()) {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
								}
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP);
							}
						}
						// Store a branch start
						branchStarts.add(currentBlock.getLocation());
						
						// South branch
						currentBlock = Main.world.getBlockAt(startLoc);
						branchThickness = startBranchThickness;
						for (int j = 1; j <= branchHeight; j++) {
							branchThickness -= branchTaper;
							// Place the central block
							if (currentBlock.getType() == Material.AIR) {
								undoList.add(currentBlock.getState());
								currentBlock.setType(trunk);
							}
							
							// Build the disk
							double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
							int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
							for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
								for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
									if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
										Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
										if (b.getType() == Material.AIR) {
											undoList.add(b.getState());
											b.setType(trunk);
										}
									}
								}
							}
							
							// Update the current block
							if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
								if (rand.nextBoolean()) {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
								}
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP);
							}
						}
						// Store a branch start
						branchStarts.add(currentBlock.getLocation());
					}
					
					// Case E/W
					else {
						// East branch
						Block currentBlock = Main.world.getBlockAt(startLoc);
						for (int j = 1; j <= branchHeight; j++) {
							branchThickness -= branchTaper;
							// Place the central block
							if (currentBlock.getType() == Material.AIR) {
								undoList.add(currentBlock.getState());
								currentBlock.setType(trunk);
							}
							
							// Build the disk
							double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
							int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
							for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
								for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
									if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
										Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
										if (b.getType() == Material.AIR) {
											undoList.add(b.getState());
											b.setType(trunk);
										}
									}
								}
							}
							
							// Update the current block
							if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
								if (rand.nextBoolean()) {
									currentBlock = currentBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
								}
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP);
							}
						}
						// Store a branch start
						branchStarts.add(currentBlock.getLocation());
						
						// West branch
						currentBlock = Main.world.getBlockAt(startLoc);
						branchThickness = startBranchThickness;
						for (int j = 1; j <= branchHeight; j++) {
							branchThickness -= branchTaper;
							// Place the central block
							if (currentBlock.getType() == Material.AIR) {
								undoList.add(currentBlock.getState());
								currentBlock.setType(trunk);
							}
							
							// Build the disk
							double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
							int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
							for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
								for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
									if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
										Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
										if (b.getType() == Material.AIR) {
											undoList.add(b.getState());
											b.setType(trunk);
										}
									}
								}
							}
							
							// Update the current block
							if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
								if (rand.nextBoolean()) {
									currentBlock = currentBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
								}
							}
							else {
								currentBlock = currentBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP);
							}
						}
						// Store a branch start
						branchStarts.add(currentBlock.getLocation());
					}
				}
			}
			
			// For branches 3, 5, grow out with one branch N/W/E/S and the other two rotated 120 degrees
			else if (i % 2 == 0) {
				Main.logDebug("Odd branch growing, number of locations " + Integer.toString(theseBranches.size())); // -----
				for (Location startLoc : theseBranches) {
					if (rand.nextBoolean()) {
						// Case N
						if (rand.nextBoolean()) {
							// North branch
							Block currentBlock = Main.world.getBlockAt(startLoc);
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Southeast branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Southwest branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
						}
						
						// Case S
						else {
							// South branch
							Block currentBlock = Main.world.getBlockAt(startLoc);
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Northeast branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Northwest branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
						}
					}
					else {
						// Case E
						if (rand.nextBoolean()) {
							// East branch
							Block currentBlock = Main.world.getBlockAt(startLoc);
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Northwest branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Southwest branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
						}
						
						// Case W
						else {
							// West branch
							Block currentBlock = Main.world.getBlockAt(startLoc);
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Northeast branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
							
							// Southeast branch
							currentBlock = Main.world.getBlockAt(startLoc);
							branchThickness = startBranchThickness;
							for (int j = 1; j <= branchHeight; j++) {
								branchThickness -= branchTaper;
								// Place the central block
								if (currentBlock.getType() == Material.AIR) {
									undoList.add(currentBlock.getState());
									currentBlock.setType(trunk);
								}
								
								// Build the disk
								double thicknessSquared = (branchThickness + 0.5) * (branchThickness + 0.5); // 0.5 for radius correction
								int x = currentBlock.getX(), y = currentBlock.getY(), z = currentBlock.getZ();
								for (int rx = -(int)branchThickness; rx <= branchThickness; rx++) {
									for (int rz = -(int)branchThickness; rz <= branchThickness; rz++) {
										if (((rx * rx) + (rz * rz)) <= thicknessSquared) {
											Block b = Main.world.getBlockAt(x + rx, y, z + rz);	
											if (b.getType() == Material.AIR) {
												undoList.add(b.getState());
												b.setType(trunk);
											}
										}
									}
								}
								
								// Update the current block
								if (rand.nextDouble() <= 0.2) { // 20% chance to shift to the side
									if (rand.nextBoolean()) {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
									}
									else {
										currentBlock = currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
									}
								}
								else {
									currentBlock = currentBlock.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.UP);
								}
							}
							// Store a branch start
							branchStarts.add(currentBlock.getLocation());
						}
					}
				}
			}
			
			// Update variables for the new branches
			startBranchThickness = nextBranchThickness;
		}
		
		// Where to build leaves from
		endPoints = branchStarts;
		
		// Jesus that's a lot of leaves to generate
		Main.logDebug("Generating leaf spheres"); // -----
		for (Location startLoc : endPoints) {
			double invLeafBallSize = 1 / leafBallSize; // Multiplication is fast, division is slow, and this is used for every leaf block
			// The -(int) ordering stops issues with how integers truncate in different directions when positive versus negative
			int x = startLoc.getBlockX();
			int y = startLoc.getBlockY();
			int z = startLoc.getBlockZ();
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
		}
		
		// Actually register the undo
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElementFromStates(undoList));
	}

	private void branchTreeGenerator() {
		// Start tracking BlockStates for an undo
		List<BlockState> undoList = new ArrayList<BlockState>();
		
		// Figure out the size of the tree (use 3 levels of branches)
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double branchSize1 = size + actVariance; // This acts like the trunk
		double leafSize1 = (branchSize1 * 0.461235) + 1.35425;
		double branchSize2 = branchSize1 * (0.8 * rand.nextDouble());
		double leafSize2 = (branchSize2 * 0.411235) + 1.35425;
		double branchSize3 = branchSize2 * (0.8 * rand.nextDouble());
		double leafSize3 = (branchSize3 * 0.361235) + 1.35425;
		
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

	private void oakTreeGenerator() {
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
	}
}
