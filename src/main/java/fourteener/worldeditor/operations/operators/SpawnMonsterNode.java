package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.MonsterVar;

public class SpawnMonsterNode extends Node {
	
	String name;
	
	public static SpawnMonsterNode newNode (String varName) {
		SpawnMonsterNode node = new SpawnMonsterNode();
		node.name = varName;
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
	
	public static int getArgCount () {
		return 1;
	}
}
