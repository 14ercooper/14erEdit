package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class WhileNode extends Node {

	Node cond, op;

	public WhileNode newNode() {
		WhileNode node = new WhileNode();
		try {
			node.cond = GlobalVars.operationParser.parsePart();
			node.op = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error creating while node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.op == null) {
			Main.logError("Could not create while node. Requires two arguments, but were not given.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			boolean result = true;
			while (cond.performNode()) {
				boolean result2 = op.performNode();
				result = result && result2;
			}
			return result;
		} catch (Exception e) {
			Main.logError("Error performing while node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 2;
	}
}
