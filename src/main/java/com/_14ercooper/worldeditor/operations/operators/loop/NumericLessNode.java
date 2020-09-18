package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class NumericLessNode extends Node {

    public String name;
    public NumberNode val;

    public NumericLessNode newNode() {
	NumericLessNode node = new NumericLessNode();
	try {
	    node.name = GlobalVars.operationParser.parseStringNode().contents;
	    node.val = GlobalVars.operationParser.parseNumberNode();
	}
	catch (Exception e) {
	    Main.logError("Error creating numeric less than node. Please check your syntax.", Operator.currentPlayer);
	    return null;
	}
	if (node.val == null) {
	    Main.logError("Could not create numeric less than node. Two arguments required, but not provided.",
		    Operator.currentPlayer);
	}
	return node;
    }

    public boolean performNode() {
	try {
	    return (Operator.numericVars.get(name).getValue() < val.getValue());
	}
	catch (Exception e) {
	    Main.logError(
		    "Error performing numeric less than node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer);
	    return false;
	}
    }

    public int getArgCount() {
	return 2;
    }
}
