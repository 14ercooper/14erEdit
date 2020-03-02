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
	public static BlockNode newNode (Material material) {
		BlockNode blockNode = new BlockNode();
		blockNode.arg1 = material;
		return blockNode;
	}
	
	// Creates a new node
	public static BlockNode newNode (Material material, BlockData blockData) {
		BlockNode blockNode = new BlockNode();
		blockNode.arg1 = material;
		blockNode.arg2 = blockData;
		return blockNode;
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
