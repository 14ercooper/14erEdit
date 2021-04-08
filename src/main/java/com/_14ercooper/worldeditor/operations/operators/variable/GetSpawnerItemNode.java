package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;

public class GetSpawnerItemNode extends Node {

    String name;

    @Override
    public GetSpawnerItemNode newNode() {
	GetSpawnerItemNode node = new GetSpawnerItemNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (!Operator.spawnerVars.containsKey(name)) {
	    Main.logError(
		    "Error performing get spawner item node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer, null);
	    return false;
	}
	SpawnerVar var = Operator.spawnerVars.get(name);
	String command = "give @s minecraft:spawner{BlockEntityTag:";
	command += var.getNBT();
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
