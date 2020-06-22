package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class NotNode extends Node {
	public Node arg;

	public NotNode newNode() {
		NotNode node = new NotNode();
		try {
			node.arg = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error creating not node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg == null) {
			Main.logError("Error creating not node. An argument is required, but was not provided.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			return !(arg.performNode());
		} catch (Exception e) {
			Main.logError("Error performing not node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 1;
	}
}
