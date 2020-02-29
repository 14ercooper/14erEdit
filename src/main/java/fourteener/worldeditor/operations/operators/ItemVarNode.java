package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.ItemVar;

public class ItemVarNode extends Node{
	
	String name;
	
	public static ItemVarNode newNode (String varName) {
		ItemVarNode node = new ItemVarNode();
		node.name = varName;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.itemVars.containsKey(name)) {
			return false;
		}
		Operator.itemVars.put(name, new ItemVar());
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
