package com.fourteener.worldeditor.operations.operators.function;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class RangeNode extends Node {

	public NumberNode arg1, arg2;

	public RangeNode newNode() {
		RangeNode node = new RangeNode();
		try {
			node.arg1 = GlobalVars.operationParser.parseNumberNode();
			node.arg2 = GlobalVars.operationParser.parseNumberNode();
		} catch (Exception e) {
			Main.logError("Could not create range node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg2 == null) {
			Main.logError("Could not create range node. Two numbers were expected, but not provided.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		return false;
	}

	public double getMin () {
		return arg1.getValue();
	}

	public double getMax () {
		return arg2.getValue();
	}

	public int getArgCount () {
		return 2;
	}
}
