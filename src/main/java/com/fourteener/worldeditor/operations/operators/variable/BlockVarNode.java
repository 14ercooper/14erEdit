package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.BlockVar;

public class BlockVarNode extends Node{
	
	String name;
	
	public BlockVarNode newNode() {
		BlockVarNode node = new BlockVarNode();
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.blockVars.containsKey(name)) {
			Main.logError("Could not create block variable. Does it already exist?", Operator.currentPlayer);
			return false;
		}
		Operator.blockVars.put(name, new BlockVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
