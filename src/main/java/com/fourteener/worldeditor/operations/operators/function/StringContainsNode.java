package com.fourteener.worldeditor.operations.operators.function;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.StringNode;
import com.fourteener.worldeditor.operations.operators.query.GetNBTNode;

public class StringContainsNode extends Node {

	Node arg1, arg2;
	
	@Override
	public Node newNode() {
		StringContainsNode node = new StringContainsNode();
		node.arg1 = GlobalVars.operationParser.parsePart();
		node.arg2 = GlobalVars.operationParser.parsePart();
		return node;
	}

	@Override
	public boolean performNode() {
		if (!(arg1 instanceof StringNode) || !(arg2 instanceof StringNode))
				return false;
		boolean result = false;
		if (arg1 instanceof GetNBTNode) {
			arg1.performNode();
			result = ((GetNBTNode)arg1).getText().contains(((StringNode)arg2).contents);
		}
		else {
			result = ((StringNode)arg1).contents.contains(((StringNode)arg2).contents);
		}
		return result;
	}

	@Override
	public int getArgCount() {
		return 2;
	}

}
