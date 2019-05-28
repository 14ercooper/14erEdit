package fourteener.worldeditor.worldeditor.brush;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class Brush {
	// Together, these two parameters serve as the ID for the brush
	public Player owner; // Different people can have different brushes
	public ItemStack item; // Each person can have a different brush for different items
	
	// These are the brush parameters
	public int radius = -1;
	public int shape = -1; // 1 is sphere, 2 is square, 3 is diamond
	public String op = "";
	public Operator operator = null;
	
	public static boolean removeBrush (Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		
		for (Brush b : BrushListener.brushes) {
			if (b.owner.equals(player) && b.item.equals(item)) {
				BrushListener.brushes.remove(b);
			}
		}
		return true;
	}
	
	public static boolean createBrush (String brushShape, String brushRadius, String brushOperation, Player player) {
		
		ItemStack brushItem = player.getInventory().getItemInMainHand();
		
		// Make sure this brush doesn't already exist. If it does, remove it
		for (Brush b : BrushListener.brushes) {
			if (b.owner.equals(player) && b.item.equals(brushItem)) {
				BrushListener.brushes.remove(b);
			}
		}
		
		// Create a brush, and assign the easy variables to it
		Brush brush = new Brush();
		brush.owner = player;
		brush.item = brushItem;
		brush.op = brushOperation;
		brush.operator = Operator.newOperator(brush.op);
		
		// Parse the brush radius and store it
		try {
			brush.radius = Integer.parseInt(brushRadius);
		} catch (Exception e) {
			return false;
		}
		
		// Parse the brush shape and store it
		if (brushShape.equalsIgnoreCase("sphere")
				|| brushShape.equalsIgnoreCase("s")) {
			brush.shape = 1;
		}
		else {
			return false;
		}
		
		// Store the brush and return success
		BrushListener.brushes.add(brush);
		return true;
	}
	
	public boolean operate (double x, double y, double z) {
		// We're working with a sphere
		if (shape == 1) {
			// Build an array of all blocks to operate on
			List<Block> blockArray = new ArrayList<Block>();
			// This generates a list of all possible block positions, then filters them using the square of the distance to the center
			// It then looks up the block at the location if needed, and stores it in the array
			// This can probably be optimized quite a bit
			for (int rx = -radius; rx <= radius; rx++) {
				for (int rz = -radius; rz <= radius; rz++) {
					for (int ry = -radius; ry <= radius; ry++) {
						if (rx*rx + ry*ry + rx*rz <= radius*radius) {
							blockArray.add(Main.world.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
						}
					}
				}
			}
			
			// Operate on them
			for (Block b : blockArray) {
				operator.operateOnBlock(b);
			}
		}
		return false;
	}
}
