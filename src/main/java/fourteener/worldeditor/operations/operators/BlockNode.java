package fourteener.worldeditor.operations.operators;

import org.bukkit.Material;

import fourteener.worldeditor.operations.Operator;

public class BlockNode extends Node {
	
	// Stores this node's argument
	public Material arg;
	
	// Creates a new node
	public static BlockNode newNode (Material material) {
		BlockNode blockNode = new BlockNode();
		blockNode.arg = material;
		return blockNode;
	}
	
	// Return the material this node references
	public Material getBlock () {
		return arg;
	}
	
	// Check if it's the correct block
	public boolean performNode () {
		return Operator.currentBlock.getType().equals(arg);
	}
	
	// Returns how many arguments this node takes
	public static int getArgCount () {
		return 1;
	}
}
