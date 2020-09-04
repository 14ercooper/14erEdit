package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.BlockVar;

public class BlockVarNode extends Node {

    String name;

    public BlockVarNode newNode() {
	BlockVarNode node = new BlockVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    public boolean performNode() {
	if (Operator.blockVars.containsKey(name)) {
	    Main.logError("Could not create block variable. Does it already exist?", Operator.currentPlayer);
	    return false;
	}
	Operator.blockVars.put(name, new BlockVar());
	return true;
    }

    public int getArgCount() {
	return 1;
    }
}
