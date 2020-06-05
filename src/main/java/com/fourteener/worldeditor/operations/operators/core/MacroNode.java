package com.fourteener.worldeditor.operations.operators.core;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class MacroNode extends Node {

	StringNode arg;

	public MacroNode newNode() {
		MacroNode node = new MacroNode();
		try {
			node.arg = GlobalVars.operationParser.parseStringNode();
			return node;
		} catch (Exception e) {
			Main.logError("Could not create macro node, no argument provided. At least one argument is required.", Operator.currentPlayer);
			return null;
		}
	}

	public boolean performNode () {
		Main.logDebug("Performing macro node"); // ----
		return GlobalVars.macroLauncher.launchMacro(arg.contents, Operator.currentBlock.getLocation());
	}

	public int getArgCount () {
		return 1;
	}
}
