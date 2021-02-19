package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.NumericVar;

public class NumericVarNode extends Node {

    String name;

    @Override
    public NumericVarNode newNode() {
	NumericVarNode node = new NumericVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (Operator.numericVars.containsKey(name)) {
	    Main.logError("Could not create numeric variable. Does it already exist?", Operator.currentPlayer);
	    return false;
	}
	Operator.numericVars.put(name, new NumericVar());
	return true;
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
