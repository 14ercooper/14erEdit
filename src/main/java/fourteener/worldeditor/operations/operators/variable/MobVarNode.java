package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.MonsterVar;

public class MobVarNode extends Node{
	
	String name;
	
	public MobVarNode(String varName) {
		name = varName;
	}
	
	public boolean performNode () {
		if (Operator.monsterVars.containsKey(name)) {
			return false;
		}
		Operator.monsterVars.put(name, new MonsterVar());
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
