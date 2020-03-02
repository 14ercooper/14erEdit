package fourteener.worldeditor.operations.operators.query;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class BlockAdjacentNode extends Node {
	
	public Node arg1;
	public NumberNode arg2;
	
	public BlockAdjacentNode(Node block, NumberNode count) {
		arg1 = block;
		arg2 = count;
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
