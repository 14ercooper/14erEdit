package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class NumericGreaterNode extends Node {

    public String name;
    public NumberNode val;

    @Override
    public NumericGreaterNode newNode() {
	NumericGreaterNode node = new NumericGreaterNode();
	try {
	    node.name = GlobalVars.operationParser.parseStringNode().contents;
	    node.val = GlobalVars.operationParser.parseNumberNode();
	}
	catch (Exception e) {
	    Main.logError("Error creating numeric greater than node. Please check your syntax.",
		    Operator.currentPlayer, e);
	    return null;
	}
	if (node.val == null) {
	    Main.logError("Could not create numeric greater than node. Two arguments required, but not given.",
		    Operator.currentPlayer, null);
	}
	return node;
    }

    @Override
    public boolean performNode() {
	try {
	    return (Operator.numericVars.get(name).getValue() > val.getValue());
	}
	catch (Exception e) {
	    Main.logError(
		    "Error performing numeric greater than node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer, e);
	    return false;
	}
    }

    @Override
    public int getArgCount() {
	return 2;
    }
}
