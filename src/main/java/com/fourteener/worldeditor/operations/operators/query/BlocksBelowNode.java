package com.fourteener.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.function.RangeNode;

public class BlocksBelowNode extends Node {

	RangeNode arg1;
	Node arg2;

	public BlocksBelowNode newNode() {
		BlocksBelowNode node = new BlocksBelowNode();
		try {
			node.arg1 = GlobalVars.operationParser.parseRangeNode();
			node.arg2 = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error creating blocks below node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg2 == null) {
			Main.logError("Could not create blocks below node. Two arguments are required, but not given.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		Block currBlock = GlobalVars.world.getBlockAt(Operator.currentBlock.getLocation());
		int x = currBlock.getX();
		int y = currBlock.getY();
		int z = currBlock.getZ();
		int min = (int) arg1.getMin();
		int max = (int) arg1.getMax();
		boolean blockRangeMet = true;

		for (int dy = y - min; dy >= y - max; dy--) {
			Operator.currentBlock = GlobalVars.world.getBlockAt(x, dy, z);
			if (!(arg2.performNode()))
				blockRangeMet = false;
		}

		Operator.currentBlock = currBlock;
		return blockRangeMet;
	}

	public int getArgCount () {
		return 2;
	}
}
