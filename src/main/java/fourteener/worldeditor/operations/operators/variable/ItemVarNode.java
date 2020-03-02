package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.ItemVar;

public class ItemVarNode extends Node{
	
	String name;
	
	public ItemVarNode(String varName) {
		name = varName;
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
