package com.fourteener.worldeditor.operations.operators.loop;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericGreaterNode extends Node {

	public String name;
	public NumberNode val;

	public NumericGreaterNode newNode() {
		NumericGreaterNode node = new NumericGreaterNode();
		try {
			node.name = GlobalVars.operationParser.parseStringNode().contents;
			node.val = GlobalVars.operationParser.parseNumberNode();
		} catch (Exception e) {
			Main.logError("Error creating numeric greater than node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.val == null) {
			Main.logError("Could not create numeric greater than node. Two arguments required, but not given.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			return (Operator.numericVars.get(name).getValue() > val.getValue());
		} catch (Exception e) {
			Main.logError("Error performing numeric greater than node. Please check your syntax (does the variable exist?).", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 2;
	}
}
