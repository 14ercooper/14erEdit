package com.fourteener.worldeditor.operations.operators.query;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class SkylightNode extends Node {
	
	NumberNode arg;

	public SkylightNode newNode() {
		SkylightNode node = new SkylightNode();
		node.arg = GlobalVars.operationParser.parseNumberNode();
		return node;
	}

	public boolean performNode() {
		return Operator.currentBlock.getLightFromSky() >= arg.getValue();
	}

	public int getArgCount() {
		return 1;
	}

}
