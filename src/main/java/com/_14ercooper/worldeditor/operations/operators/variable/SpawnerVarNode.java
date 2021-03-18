package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;

public class SpawnerVarNode extends Node {

    String name;

    @Override
    public SpawnerVarNode newNode() {
	SpawnerVarNode node = new SpawnerVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (Operator.spawnerVars.containsKey(name)) {
	    Main.logError("Could not create new spawner variable. Does it already exist?", Operator.currentPlayer);
	    return false;
	}
	Operator.spawnerVars.put(name, new SpawnerVar());
	return true;
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
