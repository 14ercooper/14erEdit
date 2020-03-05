package fourteener.worldeditor.worldeditor.brush;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.worldeditor.undo.UndoElement;
import fourteener.worldeditor.worldeditor.undo.UndoManager;

public class Brush {
	// Together, these two parameters serve as the ID for the brush
	public Player owner; // Different people can have different brushes
	public ItemStack item; // Each person can have a different brush for different items
	
	// Variables the brush needs
	BrushShape shapeGenerator;
	List<Double> shapeArgs = new LinkedList<Double>();
	Operator operation;
	
	// Store brushes
	static Map<String,BrushShape> brushShapes = new HashMap<String,BrushShape>();
	
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
	
	public static boolean AddBrushShape(String name, BrushShape shape) {
		if (brushShapes.containsKey(name)) {
			return false;
		}
		brushShapes.put(name, shape);
		return true;
	}
	
	public Brush(String brushShape, String brushRadius, String[] brushOperation, int brushOpOffset, Player player) {
		
		ItemStack brushItem = player.getInventory().getItemInMainHand();
		
		// Make sure this brush doesn't already exist. If it does, remove it
		removeBrush(player);
		
		// Create a brush, and assign the easy variables to it
		owner = player;
		item = brushItem;
		
		brushOpOffset += 2; // Used to remove brush parameters from the operation
		
		// Get the shape generator, and store the args
		shapeGenerator = brushShapes.get(brushOperation[1]);
		for (int i = 0; i < shapeGenerator.GetArgCount(); i++) {
			shapeArgs.add(Double.parseDouble(brushOperation[2+i]));
			brushOpOffset++;
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
		// And then construct the operator
		operation = Operator.newOperator(opStr);
		
		// Invalid operator?
		if (operation == null)
			return;
		
		// Store the brush and return success
		BrushListener.brushes.add(this);
		player.sendMessage("§dBrush created successfully!");
	}
	
	@SuppressWarnings("static-access")
	public boolean operate (double x, double y, double z) {
		try {
			UndoManager.getUndo(owner).cancelConsolidatedUndo();
		}
		catch (Exception e) {}
		UndoManager.getUndo(owner).startTrackingConsolidatedUndo();
		// Build an array of all blocks to operate on
		List<Block> blockArray = shapeGenerator.GetBlocks(shapeArgs, x, y, z);
		
		if (blockArray.isEmpty()) {
			return false;
		}
		Main.logDebug("Block array size is " + Integer.toString(blockArray.size())); // -----
		
		List<BlockState> snapshotArray = new ArrayList<BlockState>();
		for (Block b : blockArray) {
			snapshotArray.add(b.getState());
		}
		
		// Store an undo
		UndoManager.getUndo(owner).storeUndo(new UndoElement(blockArray));
		
		// Operate on the blocks and apply them to the world
		List<BlockState> operatedArray = new ArrayList<BlockState>();
		for (BlockState bs : snapshotArray) {
			operation.operateOnBlock(bs, owner);
			operatedArray.add(operation.currentBlock);
		}
		
		// Apply the blocks to the world
		for (BlockState bs : operatedArray) {
			Location l = bs.getLocation();
			Block b = GlobalVars.world.getBlockAt(l);
			b.setType(bs.getType(), Operator.ignoringPhysics);
			b.setBlockData(bs.getBlockData(), Operator.ignoringPhysics);
		}
		
		return UndoManager.getUndo(owner).storeConsolidatedUndo();
	}
}
