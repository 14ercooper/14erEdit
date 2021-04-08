package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class XorNode extends Node {

    public Node arg1, arg2;

    @Override
    public XorNode newNode() {
	XorNode node = new XorNode();
	try {
	    node.arg1 = GlobalVars.operationParser.parsePart();
	    node.arg2 = GlobalVars.operationParser.parsePart();
	}
	catch (Exception e) {
	    Main.logError("Error creating xor node. Please check your syntax.", Operator.currentPlayer, e);
	    return null;
	}
	if (node.arg2 == null) {
	    Main.logError("Error creating xor node. Requires 2 arguments, but these were not provided.",
		    Operator.currentPlayer, null);
	}
	return node;
    }

    @Override
    public boolean performNode() {
	try {
	    boolean x = arg1.performNode();
	    boolean y = arg2.performNode();
	    return ((x || y) && !(x && y));
	}
	catch (Exception e) {
	    Main.logError("Error performing xor node. Please check your syntax.", Operator.currentPlayer, e);
	    return false;
	}
    }

    @Override
    public int getArgCount() {
	return 2;
    }
}
