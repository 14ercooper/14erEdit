package com.fourteener.worldeditor.operations.operators.query;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class BlocksAdjacentNode extends Node {
	
	public Node arg1;
	public NumberNode arg2;
	
	public BlocksAdjacentNode newNode() {
		BlocksAdjacentNode node = new BlocksAdjacentNode();
		node.arg1 = GlobalVars.operationParser.parsePart();
		node.arg2 = GlobalVars.operationParser.parseNumberNode();
		return node;
	}
	
	public boolean performNode () {
		Main.logDebug("Performing faces exposed node"); // -----
		
		// Check if any adjacent blocks match arg1
		// Set up some variables
		int numAdjacentBlocks = 0;
		Block curBlock = GlobalVars.world.getBlockAt(Operator.currentBlock.getLocation());
		
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
	
	public int getArgCount () {
		return 2;
	}
}
