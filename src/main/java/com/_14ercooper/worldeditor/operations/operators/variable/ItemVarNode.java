package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.ItemVar;

public class ItemVarNode extends Node {

    String name;

    @Override
    public ItemVarNode newNode() {
	ItemVarNode node = new ItemVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (Operator.itemVars.containsKey(name)) {
	    Main.logError("Could not create item variable. Does it already exist?", Operator.currentPlayer, null);
	    return false;
	}
	Operator.itemVars.put(name, new ItemVar());
	return true;
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
