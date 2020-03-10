package com.fourteener.worldeditor.operations.operators.loop;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.operators.Node;

public class WhileNode extends Node {
	
	Node cond, op;
	
	public WhileNode newNode() {
		WhileNode node = new WhileNode();
		node.cond = GlobalVars.operationParser.parsePart();
		node.op = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		boolean result = true;
		while (cond.performNode()) {
			boolean result2 = op.performNode();
			result = result && result2;
		}
		return result;
	}
	
	public int getArgCount () {
		return 2;
	}
}
