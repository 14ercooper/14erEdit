package com.fourteener.worldeditor.operations.operators.query;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

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
