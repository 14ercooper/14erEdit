package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.NumericVar;

public class NumericVarNode extends Node{
	
	String name;
	
	public NumericVarNode(String varName) {
		name = varName;
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
