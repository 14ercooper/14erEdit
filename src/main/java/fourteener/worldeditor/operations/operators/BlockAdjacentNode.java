package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class BlockAdjacentNode extends Node {
	
	public Node arg1, arg2, arg3;
	
	public static BlockAdjacentNode newNode (Node block, Node ifTrue, Node ifFalse) {
		BlockAdjacentNode baNode = new BlockAdjacentNode();
		baNode.arg1 = block;
		baNode.arg2 = ifTrue;
		baNode.arg3 = ifFalse;
		return baNode;
	}
	
	public boolean performNode () {
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Performing faces exposed node"); // -----
		
		// Check if any adjacent blocks match arg1
		// Set up some variables
		boolean hasAdjacentBlock = false;
		Block curBlock = Operator.currentBlock;
		
		// Check each direction
		Block blockAdj = curBlock.getRelative(BlockFace.NORTH);
		Operator.currentBlock = blockAdj;
		if (arg1.performNode())
			hasAdjacentBlock = true;
		blockAdj = curBlock.getRelative(BlockFace.SOUTH);
		Operator.currentBlock = blockAdj;
		if (arg1.performNode())
			hasAdjacentBlock = true;
		blockAdj = curBlock.getRelative(BlockFace.EAST);
		Operator.currentBlock = blockAdj;
		if (arg1.performNode())
			hasAdjacentBlock = true;
		blockAdj = curBlock.getRelative(BlockFace.WEST);
		Operator.currentBlock = blockAdj;
		if (arg1.performNode())
			hasAdjacentBlock = true;
		blockAdj = curBlock.getRelative(BlockFace.UP);
		Operator.currentBlock = blockAdj;
		if (arg1.performNode())
			hasAdjacentBlock = true;
		blockAdj = curBlock.getRelative(BlockFace.DOWN);
		Operator.currentBlock = blockAdj;
		if (arg1.performNode())
			hasAdjacentBlock = true;
		
		// Reset the current block
		Operator.currentBlock = curBlock;
		
		
		
		// Perform the node
		if (hasAdjacentBlock) {
			return arg2.performNode();
		} else {
			return arg3.performNode();
		}
	}
	
	public static int getArgCount () {
		return 3;
	}
}
