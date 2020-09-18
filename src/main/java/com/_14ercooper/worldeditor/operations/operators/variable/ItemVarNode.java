package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.ItemVar;

public class ItemVarNode extends Node {

    String name;

    public ItemVarNode newNode() {
	ItemVarNode node = new ItemVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    public boolean performNode() {
	if (Operator.itemVars.containsKey(name)) {
	    Main.logError("Could not create item variable. Does it already exist?", Operator.currentPlayer);
	    return false;
	}
	Operator.itemVars.put(name, new ItemVar());
	return true;
    }

    public int getArgCount() {
	return 1;
    }
}
