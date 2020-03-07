package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class GetSpawnerItemNode extends Node {
	
	String name;
	
	public GetSpawnerItemNode newNode() {
		GetSpawnerItemNode node = new GetSpawnerItemNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		SpawnerVar var = Operator.spawnerVars.get(name);
		String command = "give @s minecraft:spawner{BlockEntityTag:";
		command += var.getNBT();
		command += "}";
		Main.logDebug("Command: " + command);
		Operator.currentPlayer.performCommand(command);
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
