package com.fourteener.worldeditor.operations.operators.function;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.StringNode;
import com.fourteener.worldeditor.operations.operators.query.GetNBTNode;

public class StringContainsNode extends Node {

	Node arg1, arg2;

	@Override
	public Node newNode() {
		StringContainsNode node = new StringContainsNode();
		try {
			node.arg1 = GlobalVars.operationParser.parsePart();
			node.arg2 = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Could not create string contains node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg2 == null) {
			Main.logError("Could not create string contains node. Two string nodes required, but not provided.", Operator.currentPlayer);
		}
		return node;
	}

	@Override
	public boolean performNode() {
		try {
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
		} catch (Exception e) {
			Main.logError("Could not perform string contains node. Are the child nodes of the appropriate types?", Operator.currentPlayer);
			return false;
		}
	}

	@Override
	public int getArgCount() {
		return 2;
	}

}
