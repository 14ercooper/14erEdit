package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class AndNode extends Node {

    public Node arg1, arg2;

    @Override
    public AndNode newNode() {
	AndNode node = new AndNode();
	try {
	    node.arg1 = GlobalVars.operationParser.parsePart();
	    node.arg2 = GlobalVars.operationParser.parsePart();
	}
	catch (Exception e) {
	    Main.logError("Error creating and node. Please check your syntax.", Operator.currentPlayer, e);
	    return null;
	}
	if (node.arg2 == null) {
	    Main.logError("Could not create and node. Two arguments are required, but were not given.",
		    Operator.currentPlayer, null);
	}
	return node;
    }

    @Override
    public boolean performNode() {
	try {
	    return ((arg1.performNode()) && (arg2.performNode()));
	}
	catch (Exception e) {
	    Main.logError("Error performing and node. Please check your syntax.", Operator.currentPlayer, e);
	    return false;
	}
    }

    @Override
    public int getArgCount() {
	return 2;
    }
}
