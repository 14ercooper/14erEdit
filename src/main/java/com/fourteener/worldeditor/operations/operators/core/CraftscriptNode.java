package com.fourteener.worldeditor.operations.operators.core;

import java.util.Arrays;
import java.util.LinkedList;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class CraftscriptNode extends Node {
	
	public StringNode arg;
	
	public CraftscriptNode newNode() {
		CraftscriptNode node = new CraftscriptNode();
		node.arg = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		String label = arg.contents.split("{")[0];
		LinkedList<String> args = new LinkedList<String>(Arrays.asList(arg.contents.split("{")[1].replace("}", "").split(",")));
		return GlobalVars.scriptManager.runCraftscript(label, args, Operator.currentPlayer);
	}
	
	public int getArgCount () {
		return 1;
	}
}
