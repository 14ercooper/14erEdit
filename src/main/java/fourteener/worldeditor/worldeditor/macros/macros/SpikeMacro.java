package fourteener.worldeditor.worldeditor.macros.macros;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.math.AffineTransform;
import fourteener.worldeditor.math.Matrix;
import fourteener.worldeditor.math.Vector;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class SpikeMacro extends Macro {
	
	Material block;
	double size, variance, baseSize;
	Location pos;
	
	// Create a new macro
	public static SpikeMacro createMacro (String[] args, Location loc) {
		SpikeMacro sm = new SpikeMacro();
		sm.block = Material.matchMaterial(args[0]);
		sm.size = Double.parseDouble(args[1]);
		sm.variance = Double.parseDouble(args[2]);
		sm.baseSize = Double.parseDouble(args[3]);
		sm.pos = loc;
		return sm;
	}
	
	// Run this macro
	public boolean performMacro () {
		// Location of the brush
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		
		// Player location
		double xp = Operator.currentPlayer.getLocation().getX();
		double yp = Operator.currentPlayer.getLocation().getY();
		double zp = Operator.currentPlayer.getLocation().getZ();
		
		// Points from the player to the base
		double dx = (Math.abs(xp - x) * Math.signum(xp - x));
		double dy = (Math.abs(yp - y) * Math.signum(yp - y));
		double dz = (Math.abs(zp - z) * Math.signum(zp - z));
		
		// Figure out how large a spike to generate
		Random rand = new Random();
		double actVariance = ((rand.nextDouble() * 2.0) - 1.0) * variance;
		double spikeSize = size + actVariance;
		Main.logDebug("Spike size: " + Double.toString(spikeSize));
		Main.logDebug("Base size: " + Double.toString(baseSize));
		
		// Calculate needed variables
		Vector x1 = AffineTransform.getPosition(0, 1, 0);
		Vector x2 = AffineTransform.getPosition(0, 0, 0);
		
		// Create a slightly larger cylinder and generate the cone (so the spikes stick to walls better)
		LinkedList<Vector> blocks = new LinkedList<Vector>();
		for (int ry = -3; ry <= spikeSize; ry++) {
			Main.logDebug("Starting new Y slice");
			double radiusAtHeight = baseSize - (baseSize / spikeSize * ry) + 0.5;
			for (int rx = (-(int)Math.round(baseSize)) - 2; rx <= baseSize + 2; rx++) {
				Main.logDebug("Starting new X slice");
				for (int rz = (-(int)Math.round(baseSize)) - 2; rz <= baseSize + 2; rz++) {
					Vector p = AffineTransform.getPosition(rx, ry, rz);
					double pointRadius = (Vector.cross(Vector.add(p, Vector.mult(x1, -1.0)), Vector.add(p, Vector.mult(x2, -1.0)))).getMagnitude() / (Vector.add(x2, Vector.mult(x1, -1.0))).getMagnitude();
					if (pointRadius <= radiusAtHeight) {
						Main.logDebug("Radiuses: " + Double.toString(radiusAtHeight) + "/" + Double.toString(pointRadius));
						blocks.add(p);
					}
				}
			}
		}
		Main.logDebug("Cone contains " + Integer.toString(blocks.size()) + " blocks");
		
		// Rotate the cylinder
		/*
		Vector dir = Vector.normaize(AffineTransform.getPosition(dx, dy, dz));
		double thetaX = dir.getContents()[0];
		double thetaY = dir.getContents()[1];
		double thetaZ = dir.getContents()[2];
		Matrix rX = AffineTransform.getRotationXMatrix(thetaX);
		Matrix rY = AffineTransform.getRotationYMatrix(thetaY);
		Matrix rZ = AffineTransform.getRotationZMatrix(thetaZ);
		Matrix rotMatrix = Matrix.mult(rX, Matrix.mult(rY, rZ));
		LinkedList<Vector> rotVectors = new LinkedList<Vector>();
		for (Vector v : blocks) {
			rotVectors.add(Vector.integerize(Matrix.mult(rotMatrix, v)));
		}
		Main.logDebug("Rotated cone contains " + Integer.toString(rotVectors.size()) + " blocks");
		*/
		LinkedList<Vector> rotVectors = blocks;
		
		// Generate the snapshot array
		LinkedList<BlockState> snapshotArray = new LinkedList<BlockState>();
		for (Vector v : rotVectors) {
			snapshotArray.add(Main.world.getBlockAt((int) (v.getContents()[0] + x), (int) (v.getContents()[1]  + y), (int) (v.getContents()[2] + z)).getState());
		}
		Main.logDebug(Integer.toString(snapshotArray.size()) + " blocks in the snapshot array");
		
		// Operate on the snapshot array
		LinkedList<BlockState> operatedBlocks = new LinkedList<BlockState>();
		for (BlockState bs : snapshotArray) {
			Block b = Main.world.getBlockAt(bs.getLocation());
			BlockState state = b.getState();
			state.setType(block);
			operatedBlocks.add(state);
		}
		
		// Process edited blocks and register the undo
		List<Block> blocksToUndo = new ArrayList<Block>();
		for (BlockState bs : operatedBlocks) {
			blocksToUndo.add(Main.world.getBlockAt(bs.getLocation()));
		}
		UndoManager.getUndo(Operator.currentPlayer).storeUndo(UndoElement.newUndoElement(blocksToUndo));
		
		
		Main.logDebug("Operated on and now placing " + Integer.toString(operatedBlocks.size()) + " blocks");
		// Apply the changes to the world
		for (BlockState bs : operatedBlocks) {
			Block b = Main.world.getBlockAt(bs.getLocation());
			b.setType(bs.getType());
		}
		
		return true;
	}
}
