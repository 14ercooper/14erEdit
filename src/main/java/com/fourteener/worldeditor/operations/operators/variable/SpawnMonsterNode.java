package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.MonsterVar;

public class SpawnMonsterNode extends Node {
	
	String name;
	
	public SpawnMonsterNode newNode() {
		SpawnMonsterNode node = new SpawnMonsterNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
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
