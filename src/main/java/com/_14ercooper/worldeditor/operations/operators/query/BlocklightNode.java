package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.block.BlockFace;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class BlocklightNode extends Node {
	
	NumberNode arg;

	public BlocklightNode newNode() {
		BlocklightNode node = new BlocklightNode();
		node.arg = GlobalVars.operationParser.parseNumberNode();
		return node;
	}

	public boolean performNode() {
		BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
		int light = Operator.currentBlock.getLightFromBlocks();
		for (BlockFace face : faces) {
			int l = Operator.currentBlock.getRelative(face).getLightFromBlocks();
			if (l > light) light = l;
		}
		return light >= arg.getValue();
	}

	public int getArgCount() {
		return 1;
	}

}
