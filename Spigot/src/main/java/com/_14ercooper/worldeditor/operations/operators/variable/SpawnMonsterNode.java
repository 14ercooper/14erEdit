package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.MonsterVar;

public class SpawnMonsterNode extends Node {
	
	String name;
	
	public SpawnMonsterNode newNode() {
		SpawnMonsterNode node = new SpawnMonsterNode();
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		return node;
	}
	
	public boolean performNode () {
		if (!Operator.monsterVars.containsKey(name)) {
			Main.logError("Error performing spawn monster node. Please check your syntax (does the variable exist?).", Operator.currentPlayer);
			return false;
		}
		MonsterVar var = Operator.monsterVars.get(name);
		String command = "summon minecraft:" + var.getType();
		command += " ~ ~ ~ ";
		command += var.getNBT();
		Main.logDebug("Command: " + command);
		Operator.currentPlayer.performCommand(command);
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
