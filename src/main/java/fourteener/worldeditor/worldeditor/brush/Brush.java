package fourteener.worldeditor.worldeditor.brush;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class Brush {
	// Together, these two parameters serve as the ID for the brush
	public Player owner; // Different people can have different brushes
	public ItemStack item; // Each person can have a different brush for different items
	
	// These are the brush parameters
	public int radius = -1;
	public double radiusCorrection = 0; // Used for sphere
	public int shape = -1; // 0 is radius sphere, 1 is sphere, 2 is square, 3 is diamond
	public String op = "";
	public Operator operator = null;
	
	public static boolean removeBrush (Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		
		Brush br = null;
		
		for (Brush b : BrushListener.brushes) {
			if (b.owner.equals(player) && b.item.equals(item)) {
				br = b;
			}
		}
		if (br != null) {
			BrushListener.brushes.remove(br);
		}
		return true;
	}
	
	public static boolean createBrush (String brushShape, String brushRadius, String[] brushOperation, int brushOpOffset, Player player) {
		
		ItemStack brushItem = player.getInventory().getItemInMainHand();
		
		// Make sure this brush doesn't already exist. If it does, remove it
		removeBrush(player);
		
		// Create a brush, and assign the easy variables to it
		Brush brush = new Brush();
		brush.owner = player;
		brush.item = brushItem;
		
		brushOpOffset += 3; // Used to remove brush parameters from the operation
		
		// Parse the brush radius and store it
		try {
			brush.radius = Integer.parseInt(brushRadius);
		} catch (Exception e) {
			return false;
		}
		
		// Parse the brush shape and store it
		if (brushShape.equalsIgnoreCase("radiussphere")
				|| brushShape.equalsIgnoreCase("rs")) {
			brush.shape = 0;
		}
		else if (brushShape.equalsIgnoreCase("sphere")
				|| brushShape.equalsIgnoreCase("s")) {
			brush.shape = 1;
			brushOpOffset++;
			brush.radiusCorrection = Double.parseDouble(brushOperation[3]);
		}
		else {
			return false;
		}
		
		// Construct the operator
		// Start by removing brush parameters
		List<String> opArray = new LinkedList<String>(Arrays.asList(brushOperation));
		while (brushOpOffset > 0) {
			opArray.remove(0);
			brushOpOffset--;
		}
		// Construct the string
		String opStr = "";
		for (String s : opArray) {
			opStr = opStr.concat(s).concat(" ");
		}
		brush.op = opStr;
		// And then construct the operator
		brush.operator = Operator.newOperator(brush.op);
		
		// Invalid operator?
		if (brush.operator == null)
			return false;
		
		// Store the brush and return success
		BrushListener.brushes.add(brush);
		player.sendMessage("§dBrush created successfully!");
		return true;
	}
	
	public boolean operate (double x, double y, double z) {
		// We're working with a radius sphere
		if (shape == 0) {
			// Build an array of all blocks to operate on
			List<Block> blockArray = new ArrayList<Block>();
			
			// This generates a list of all possible block positions, then filters them using the square of the distance to the center
			// It then looks up the block at the location if needed, and stores it in the array
			for (int rx = -radius; rx <= radius; rx++) {
				for (int rz = -radius; rz <= radius; rz++) {
					for (int ry = -radius; ry <= radius; ry++) {
						if (rx*rx + ry*ry + rz*rz <= radius*radius) {
							blockArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
						}
					}
				}
			}
			
			// Store an undo
			UndoManager.getUndo(owner).storeUndo(UndoElement.newUndoElement(blockArray));
			
			// Operate on them
			for (Block b : blockArray) {
				operator.operateOnBlock(b);
			}
			return true;
		}
		// We're working with a sphere
		if (shape == 1) {
			// Build an array of all blocks to operate on
			List<Block> blockArray = new ArrayList<Block>();

			// Generate a better sphere
			// This generates a list of all possible block positions, then filters them using the square of the distance to the center
			// It then looks up the block at the location if needed, and stores it in the array
			for (int rx = -radius; rx <= radius; rx++) {
				for (int rz = -radius; rz <= radius; rz++) {
					for (int ry = -radius; ry <= radius; ry++) {
						if (rx*rx + ry*ry + rz*rz <= (radius + radiusCorrection)*(radius + radiusCorrection)) {
							blockArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
						}
					}
				}
			}
			
			// Store an undo
			UndoManager.getUndo(owner).storeUndo(UndoElement.newUndoElement(blockArray));
			
			// Operate on them
			for (Block b : blockArray) {
				operator.operateOnBlock(b);
			}
			return true;
		}
		return false;
	}
}
