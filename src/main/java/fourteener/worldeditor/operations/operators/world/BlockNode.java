package fourteener.worldeditor.operations.operators.world;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class BlockNode extends Node {
	
	// Stores this node's argument
	public Material arg1;
	public BlockData arg2 = null;
	
	// Creates a new node
	public BlockNode(Material material) {
		arg1 = material;
	}
	
	public BlockNode() {
		return;
	}
	
	// Creates a new node
	public BlockNode(Material material, BlockData blockData) {
		arg1 = material;
		arg2 = blockData;
	}
	
	// Return the material this node references
	public Material getBlock () {
		return arg1;
	}
	
	// Check if it's the correct block
	public boolean performNode () {
		if (arg2 == null) {
			return Operator.currentBlock.getType().equals(arg1);
		}
		else {
			return Operator.currentBlock.getBlockData().matches(arg2);
		}
	}
	
	// Returns how many arguments this node takes
	public static int getArgCount () {
		return 1;
	}
}
