package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.MonsterVar;

public class MobVarNode extends Node{
	
	String name;
	
	public static MobVarNode newNode (String varName) {
		MobVarNode node = new MobVarNode();
		node.name = varName;
		return node;
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
