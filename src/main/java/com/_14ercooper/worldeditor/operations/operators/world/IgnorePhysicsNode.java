package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class IgnorePhysicsNode extends Node {

    public Node arg;

    public IgnorePhysicsNode newNode() {
	IgnorePhysicsNode node = new IgnorePhysicsNode();
	try {
	    node.arg = GlobalVars.operationParser.parsePart();
	}
	catch (Exception e) {
	    Main.logError("Error creating physics node. Please check your syntax.", Operator.currentPlayer);
	    return null;
	}
	if (node.arg == null) {
	    Main.logError("Could not parse physics node. Requires an operation, but none was provided.",
		    Operator.currentPlayer);
	}
	return node;
    }

    public boolean performNode() {
	try {
	    Operator.ignoringPhysics = !Operator.ignoringPhysics;
	    boolean output = arg.performNode();
	    Operator.ignoringPhysics = !Operator.ignoringPhysics;
	    return output;
	}
	catch (Exception e) {
	    Main.logError("Error performing physics node. Please check your syntax.", Operator.currentPlayer);
	    return false;
	}
    }

    public int getArgCount() {
	return 1;
    }

}
