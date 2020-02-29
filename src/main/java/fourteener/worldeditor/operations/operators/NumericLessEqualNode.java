package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

public class NumericLessEqualNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public static NumericLessEqualNode newNode (String first, NumberNode second) {
		NumericLessEqualNode node = new NumericLessEqualNode();
		node.name = first;
		node.val = second;
		return node;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() <= val.getValue());
	}
	
	public static int getArgCount () {
		return 2;
	}
}
