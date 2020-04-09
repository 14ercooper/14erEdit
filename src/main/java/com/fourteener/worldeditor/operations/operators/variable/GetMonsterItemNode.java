package com.fourteener.worldeditor.operations.operators.variable;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.type.MonsterVar;

public class GetMonsterItemNode extends Node {
	
	String name;
	
	public GetMonsterItemNode newNode() {
		GetMonsterItemNode node = new GetMonsterItemNode();
		node.name = GlobalVars.operationParser.parseStringNode().contents;
		return node;
	}
	
	public boolean performNode () {
		MonsterVar var = Operator.monsterVars.get(name);
		String command = "give @s minecraft:" + var.getType();
		command += "_spawn_egg{EntityTag:";
		command += var.asNBT();
		command += "}";
		Main.logDebug("Command: " + command);
		Operator.currentPlayer.performCommand(command);
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
