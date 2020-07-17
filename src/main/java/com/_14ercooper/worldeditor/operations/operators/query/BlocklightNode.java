package com._14ercooper.worldeditor.operations.operators.query;

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
		return Operator.currentBlock.getLightFromBlocks() >= arg.getValue();
	}

	public int getArgCount() {
		return 1;
	}

}
