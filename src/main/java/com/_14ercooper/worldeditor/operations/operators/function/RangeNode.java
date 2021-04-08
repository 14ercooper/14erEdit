package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class RangeNode extends Node {

    public NumberNode arg1, arg2;

    @Override
    public RangeNode newNode() {
	RangeNode node = new RangeNode();
	try {
	    node.arg1 = GlobalVars.operationParser.parseNumberNode();
	    node.arg2 = GlobalVars.operationParser.parseNumberNode();
	}
	catch (Exception e) {
	    Main.logError(
		    "Could not create range node. Please check your syntax (did you remember to create a range node?).",
		    Operator.currentPlayer, e);
	    return null;
	}
	if (node.arg2 == null) {
	    Main.logError("Could not create range node. Two numbers were expected, but not provided.",
		    Operator.currentPlayer, null);
	}
	return node;
    }

    @Override
    public boolean performNode() {
	return false;
    }

    public double getMin() {
	return arg1.getValue();
    }

    public double getMax() {
	return arg2.getValue();
    }

    @Override
    public int getArgCount() {
	return 2;
    }
}
