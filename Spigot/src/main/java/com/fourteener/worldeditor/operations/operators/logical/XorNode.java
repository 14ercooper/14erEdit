package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class XorNode extends Node {

	public Node arg1, arg2;

	public XorNode newNode() {
		XorNode node = new XorNode();
		try {
			node.arg1 = GlobalVars.operationParser.parsePart();
			node.arg2 = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error creating xor node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg2 == null) {
			Main.logError("Error creating xor node. Requires 2 arguments, but these were not provided.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			boolean x = arg1.performNode();
			boolean y = arg2.performNode();
			return ((x || y) && !(x && y));
		} catch (Exception e) {
			Main.logError("Error performing xor node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 2;
	}
}
