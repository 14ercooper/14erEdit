package com.fourteener.worldeditor.operations.operators.logical;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;

public class IfNode extends Node {
	
	public Node arg1, arg2, arg3;
	
	public IfNode newNode() {
		IfNode node = new IfNode();
		node.arg1 = GlobalVars.operationParser.parsePart();
		node.arg2 = GlobalVars.operationParser.parsePart();
		node.arg3 = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		boolean isTrue = arg1.performNode();
		boolean toReturn = false;
		if (isTrue) {
			Main.logDebug("Is true");
			toReturn = arg2.performNode();
		}
		if (arg3 == null) {
			return toReturn;
		}
		if (arg3 instanceof ElseNode && !isTrue) {
			toReturn = arg3.performNode();
		}
		if (!(arg3 instanceof ElseNode)) {
			toReturn = arg3.performNode() && toReturn;
		}
		return toReturn;
	}
	
	public int getArgCount () {
		return 3;
	}
}
