package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.ItemVar;

public class ItemVarNode extends Node{
	
	String name;
	
	public ItemVarNode newNode() {
		ItemVarNode node = new ItemVarNode();
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.itemVars.containsKey(name)) {
			Main.logError("Could not create item variable. Does it already exist?", Operator.currentPlayer);
			return false;
		}
		Operator.itemVars.put(name, new ItemVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
