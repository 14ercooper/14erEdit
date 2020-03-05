package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.MonsterVar;

public class MobVarNode extends Node{
	
	String name;
	
	public MobVarNode newNode() {
		MobVarNode node = new MobVarNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (Operator.monsterVars.containsKey(name)) {
			return false;
		}
		Operator.monsterVars.put(name, new MonsterVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
