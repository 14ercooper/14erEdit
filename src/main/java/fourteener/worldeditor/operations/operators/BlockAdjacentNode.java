package fourteener.worldeditor.operations.operators;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;

public class BlockAdjacentNode extends Node {
	
	public Node arg1;
	public NumberNode arg2;
	
	public static BlockAdjacentNode newNode (Node block, NumberNode count) {
		BlockAdjacentNode baNode = new BlockAdjacentNode();
		baNode.arg1 = block;
		baNode.arg2 = count;
		return baNode;
	}
	
	public boolean performNode () {
		Main.logDebug("Performing faces exposed node"); // -----
		
		// Check if any adjacent blocks match arg1
		// Set up some variables
		int numAdjacentBlocks = 0;
		Block curBlock = Main.world.getBlockAt(Operator.currentBlock.getLocation());
		
		// Check each direction
		Block blockAdj = curBlock.getRelative(BlockFace.NORTH);
		Operator.currentBlock = blockAdj.getState();
		if (arg1.performNode())
			numAdjacentBlocks++;
		blockAdj = curBlock.getRelative(BlockFace.SOUTH);
		Operator.currentBlock = blockAdj.getState();
		if (arg1.performNode())
			numAdjacentBlocks++;
		blockAdj = curBlock.getRelative(BlockFace.EAST);
		Operator.currentBlock = blockAdj.getState();
		if (arg1.performNode())
			numAdjacentBlocks++;
		blockAdj = curBlock.getRelative(BlockFace.WEST);
		Operator.currentBlock = blockAdj.getState();
		if (arg1.performNode())
			numAdjacentBlocks++;
		blockAdj = curBlock.getRelative(BlockFace.UP);
		Operator.currentBlock = blockAdj.getState();
		if (arg1.performNode())
			numAdjacentBlocks++;
		blockAdj = curBlock.getRelative(BlockFace.DOWN);
		Operator.currentBlock = blockAdj.getState();
		if (arg1.performNode())
			numAdjacentBlocks++;
		
		// Reset the current block
		Operator.currentBlock = curBlock.getState();
		
		return (numAdjacentBlocks >= arg2.getValue() - 0.1);
	}
	
	public static int getArgCount () {
		return 2;
	}
}
