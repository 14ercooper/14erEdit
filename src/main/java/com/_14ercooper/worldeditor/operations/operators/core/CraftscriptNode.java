package com._14ercooper.worldeditor.operations.operators.core;

import java.util.Arrays;
import java.util.LinkedList;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class CraftscriptNode extends Node {

    public StringNode arg;

    @Override
    public CraftscriptNode newNode() {
	try {
	    CraftscriptNode node = new CraftscriptNode();
	    node.arg = GlobalVars.operationParser.parseStringNode();
	    return node;
	}
	catch (Exception e) {
	    Main.logError("Error parsing craftscript. Operator requires an argument containing a script.",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public boolean performNode() {
	try {
	    String label = arg.contents.split("{")[0];
	    LinkedList<String> args = new LinkedList<String>(
		    Arrays.asList(arg.contents.split("{")[1].replace("}", "").split(",")));
	    return GlobalVars.scriptManager.runCraftscript(label, args, Operator.currentPlayer);
	}
	catch (Exception e) {
	    Main.logError(
		    "Could not parse craftscript. Is your input formatted correctly, with arguments contained in {}?",
		    Operator.currentPlayer);
	    return false;
	}
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
