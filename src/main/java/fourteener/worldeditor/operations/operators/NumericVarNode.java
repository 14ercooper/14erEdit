package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.type.NumericVar;

public class NumericVarNode extends Node{
	
	String name;
	
	public static NumericVarNode newNode (String varName) {
		NumericVarNode node = new NumericVarNode();
		node.name = varName;
		return node;
	}
	
	public boolean performNode () {
		if (Operator.numericVars.containsKey(name)) {
			return false;
		}
		Operator.numericVars.put(name, new NumericVar());
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
