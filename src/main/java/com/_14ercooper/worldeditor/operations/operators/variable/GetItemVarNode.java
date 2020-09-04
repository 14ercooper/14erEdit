package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.ItemVar;

public class GetItemVarNode extends Node {

    String name;

    public GetItemVarNode newNode() {
	GetItemVarNode node = new GetItemVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    public boolean performNode() {
	if (!Operator.itemVars.containsKey(name)) {
	    Main.logError(
		    "Error performing get item variable node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer);
	    return false;
	}
	ItemVar iv = Operator.itemVars.get(name);
	String command = "give @s ";
	command += iv.getType().toLowerCase();
	command += iv.getNBT() + " ";
	command += iv.getCount();
	Main.logDebug("Command: " + command);
	Operator.currentPlayer.performCommand(command);
	return true;
    }

    public int getArgCount() {
	return 1;
    }
}
