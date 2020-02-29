package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

public class NumericGreaterEqualNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public static NumericGreaterEqualNode newNode (String first, NumberNode second) {
		NumericGreaterEqualNode node = new NumericGreaterEqualNode();
		node.name = first;
		node.val = second;
		return node;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() >= val.getValue());
	}
	
	public static int getArgCount () {
		return 2;
	}
}
