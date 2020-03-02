package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.SpawnerVar;

public class SpawnerVarNode extends Node{
	
	String name;
	
	public SpawnerVarNode(String varName) {
		name = varName;
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
