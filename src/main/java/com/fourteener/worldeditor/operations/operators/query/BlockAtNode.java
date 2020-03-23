package com.fourteener.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class BlockAtNode extends Node {

	NumberNode x, y, z;
	Node node;
	
	@Override
	public BlockAtNode newNode() {
		BlockAtNode baNode = new BlockAtNode();
		baNode.x = GlobalVars.operationParser.parseNumberNode();
		baNode.y = GlobalVars.operationParser.parseNumberNode();
		baNode.z = GlobalVars.operationParser.parseNumberNode();
		baNode.node = GlobalVars.operationParser.parsePart();
		return baNode;
	}

	@Override
	public boolean performNode() {
		Block currBlock = GlobalVars.world.getBlockAt(Operator.currentBlock.getLocation());
		Operator.currentBlock = GlobalVars.world.getBlockAt((int) x.getValue() + currBlock.getX(), (int) y.getValue() + currBlock.getY(), (int) z.getValue() + currBlock.getZ()).getState();
		boolean matches = node.performNode();
		Operator.currentBlock = currBlock.getState();
		return matches;
	}

	@Override
	public int getArgCount() {
		return 4;
	}

}
