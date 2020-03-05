package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class SpawnerVarNode extends Node{
	
	String name;
	
	public SpawnerVarNode newNode() {
		SpawnerVarNode node = new SpawnerVarNode();
		node.name = Main.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (Operator.spawnerVars.containsKey(name)) {
			return false;
		}
		Operator.spawnerVars.put(name, new SpawnerVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
