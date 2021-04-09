package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.ArrayList;
import java.util.List;

public class AnyOfNode extends Node {

    NumberNode count, total;
    final List<Node> conditions = new ArrayList<>();

    @Override
    public AnyOfNode newNode() {
	AnyOfNode node = new AnyOfNode();
	try {
	    node.count = GlobalVars.operationParser.parseNumberNode();
	    node.total = GlobalVars.operationParser.parseNumberNode();

	    for (int i = 0; i < node.total.getInt(); i++) {
		node.conditions.add(GlobalVars.operationParser.parsePart());
	    }
	}
	catch (Exception e) {
	    Main.logError("Error creating and node. Please check your syntax.", Operator.currentPlayer, e);
	    return null;
	}
	if (node.conditions.size() != node.total.getInt()) {
	    Main.logError("Could not create AnyOf node. Did you provide enough arguments?", Operator.currentPlayer, null);
	    return null;
	}
	return node;
    }

    // /fx br s 5 if anyof 1 2 orange_concrete white_concrete set stone
    @Override
    public boolean performNode() {
        int trueCount = 0;

        for (Node condition : conditions) {
            boolean isTrue = condition.performNode();
            if (isTrue)
                trueCount++;

            if (trueCount == count.getInt())
                return true;
        }

        return false;
    }

    @Override
    public int getArgCount() {
	return 2;
    }
}
