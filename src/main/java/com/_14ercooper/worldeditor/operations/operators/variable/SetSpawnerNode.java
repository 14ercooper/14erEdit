package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.SpawnerVar;

public class SetSpawnerNode extends Node {

    String name;

    public SetSpawnerNode newNode() {
	SetSpawnerNode node = new SetSpawnerNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    public boolean performNode() {
	if (!Operator.spawnerVars.containsKey(name)) {
	    Main.logError("Error performing set spawner node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer);
	    return false;
	}
	SpawnerVar var = Operator.spawnerVars.get(name);
	GlobalVars.currentUndo.storeBlock(Operator.currentBlock);
	String command = "setblock " + Operator.currentBlock.getX();
	command += " " + Operator.currentBlock.getY();
	command += " " + Operator.currentBlock.getZ();
	command += " minecraft:spawner";
	command += var.getNBT();
	command += " replace";
	Main.logDebug("Command: " + command);
	Operator.currentPlayer.performCommand(command);
	return true;
    }

    public int getArgCount() {
	return 1;
    }
}
