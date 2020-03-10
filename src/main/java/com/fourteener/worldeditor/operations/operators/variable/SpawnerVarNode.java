package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.SpawnerVar;

public class SpawnerVarNode extends Node{
	
	String name;
	
	public SpawnerVarNode newNode() {
		SpawnerVarNode node = new SpawnerVarNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (Operator.spawnerVars.containsKey(name)) {
			return false;
		}
		Operator.spawnerVars.put(name, new SpawnerVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
