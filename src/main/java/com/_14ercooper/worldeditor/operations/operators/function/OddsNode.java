package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class OddsNode extends Node {

    public NumberNode arg;

    @Override
    public OddsNode newNode() {
	OddsNode node = new OddsNode();
	try {
	    node.arg = GlobalVars.operationParser.parseNumberNode();
	}
	catch (Exception e) {
	    Main.logError("Could not create odds node, argument is not a number.", Operator.currentPlayer, e);
	    return null;
	}
	if (node.arg == null) {
	    Main.logError("Error creating odds node. Requires a number, but no number was found.",
		    Operator.currentPlayer, null);
	}
	return node;
    }

    @Override
    public boolean performNode() {
	double chance = GlobalVars.rand.nextDouble() * 100.0;
	return (chance < arg.getValue());
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
