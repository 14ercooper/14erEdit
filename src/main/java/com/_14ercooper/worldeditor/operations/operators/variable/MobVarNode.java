package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.MonsterVar;

public class MobVarNode extends Node {

    String name;

    @Override
    public MobVarNode newNode() {
	MobVarNode node = new MobVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (Operator.monsterVars.containsKey(name)) {
	    Main.logError("Could not create monster variable. Does it already exist?", Operator.currentPlayer);
	    return false;
	}
	Operator.monsterVars.put(name, new MonsterVar());
	return true;
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
