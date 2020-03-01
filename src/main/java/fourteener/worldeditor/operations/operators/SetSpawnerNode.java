package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class SetSpawnerNode extends Node {
	
	String name;
	
	public static SetSpawnerNode newNode (String varName) {
		SetSpawnerNode node = new SetSpawnerNode();
		node.name = varName;
		return node;
	}
	
	public boolean performNode () {
		SpawnerVar var = Operator.spawnerVars.get(name);
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
	
	public static int getArgCount () {
		return 1;
	}
}
