package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.MonsterVar;

public class GetMonsterItemNode extends Node {

    String name;

    @Override
    public GetMonsterItemNode newNode() {
	GetMonsterItemNode node = new GetMonsterItemNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (!Operator.monsterVars.containsKey(name)) {
	    Main.logError(
		    "Error performing get monster item node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer, null);
	    return false;
	}
	MonsterVar var = Operator.monsterVars.get(name);
	String command = "give @s minecraft:" + var.getType();
	command += "_spawn_egg{EntityTag:";
	command += var.asNBT();
	command += "}";
	Main.logDebug("Command: " + command);
	Operator.currentPlayer.performCommand(command);
	return true;
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
