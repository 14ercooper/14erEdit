package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class NumericEqualityNode extends Node {

	public String name;
	public NumberNode val;

	public NumericEqualityNode newNode() {
		NumericEqualityNode node = new NumericEqualityNode();
		try {
			node.name = GlobalVars.operationParser.parseStringNode().contents;
			node.val = GlobalVars.operationParser.parseNumberNode();
		} catch (Exception e) {
			Main.logError("Error creating numeric equality node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.val == null) {
			Main.logError("Could not create numeric equality node. Two arguments required, but not given.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			return (Math.abs(Operator.numericVars.get(name).getValue() - val.getValue()) < 0.01);
		} catch (Exception e) {
			Main.logError("Error performing numeric equality node. Please check your syntax (does the variable exist?).", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 2;
	}
}
