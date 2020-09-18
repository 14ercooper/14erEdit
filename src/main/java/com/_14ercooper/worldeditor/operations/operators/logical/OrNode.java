package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class OrNode extends Node {

    public Node arg1, arg2;

    public OrNode newNode() {
	OrNode node = new OrNode();
	try {
	    node.arg1 = GlobalVars.operationParser.parsePart();
	    node.arg2 = GlobalVars.operationParser.parsePart();
	}
	catch (Exception e) {
	    Main.logError("Error creating or node. Please check your syntax.", Operator.currentPlayer);
	    return null;
	}
	if (node.arg2 == null) {
	    Main.logError("Error creating or node. Two arguments required, but not provided.", Operator.currentPlayer);
	}
	return node;
    }

    public boolean performNode() {
	try {
	    return ((arg1.performNode()) || (arg2.performNode()));
	}
	catch (Exception e) {
	    Main.logError("Error performing or node. Please check your syntax.", Operator.currentPlayer);
	    return false;
	}
    }

    public int getArgCount() {
	return 2;
    }
}
