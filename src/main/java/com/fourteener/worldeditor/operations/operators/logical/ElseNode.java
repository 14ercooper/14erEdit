package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.operators.Node;

public class ElseNode extends Node {

	Node subNode;
	
	@Override
	public ElseNode newNode() {
		ElseNode node = new ElseNode();
		node.subNode = GlobalVars.operationParser.parsePart();
		return node;
	}

	@Override
	public boolean performNode() {
		return subNode.performNode();
	}

	@Override
	public int getArgCount() {
		return 1;
	}

}
