package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class ElseNode extends Node {

	Node subNode;

	@Override
	public ElseNode newNode() {
		ElseNode node = new ElseNode();
		try {
			node.subNode = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error creating else node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.subNode == null) {
			Main.logError("Could not create else node. An argument is required but not provided.", Operator.currentPlayer);
		}
		return node;
	}

	@Override
	public boolean performNode() {
		try {
			return subNode.performNode();
		} catch (Exception e) {
			Main.logError("Error performing else node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	@Override
	public int getArgCount() {
		return 1;
	}

}
