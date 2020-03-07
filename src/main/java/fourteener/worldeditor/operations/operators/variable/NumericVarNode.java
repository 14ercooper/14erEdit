package fourteener.worldeditor.operations.operators.variable;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.type.NumericVar;

public class NumericVarNode extends Node{
	
	String name;
	
	public NumericVarNode newNode() {
		NumericVarNode node = new NumericVarNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		return node;
	}
	
	public boolean performNode () {
		if (Operator.numericVars.containsKey(name)) {
			return false;
		}
		Operator.numericVars.put(name, new NumericVar());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
