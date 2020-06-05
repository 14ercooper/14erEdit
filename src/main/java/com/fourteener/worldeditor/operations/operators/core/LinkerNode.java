package com.fourteener.worldeditor.operations.operators.core;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class LinkerNode extends Node {

	public Node arg1, arg2;

	public LinkerNode newNode() {
		LinkerNode node = new LinkerNode();
		try {
			node.arg1 = GlobalVars.operationParser.parsePart();
			node.arg2 = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Could not create linker node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg2 == null) {
			Main.logError("Could not create Linker node. Node requries two operations, two were not provided.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			boolean a1 = arg1.performNode();
			boolean a2 = arg2.performNode();
			return (a1 && a2);
		} catch (Exception e) {
			Main.logError("Error performing linker node. Please check your operation syntax.", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 2;
	}
}
