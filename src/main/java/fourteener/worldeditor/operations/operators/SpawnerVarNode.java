package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class SpawnerVarNode extends Node{
	
	String name;
	
	public static SpawnerVarNode newNode (String varName) {
		SpawnerVarNode node = new SpawnerVarNode();
		node.name = varName;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.spawnerVars.containsKey(name)) {
			return false;
		}
		Operator.spawnerVars.put(name, new SpawnerVar());
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
