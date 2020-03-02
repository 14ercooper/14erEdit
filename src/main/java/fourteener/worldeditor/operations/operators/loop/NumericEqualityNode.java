package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericEqualityNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public static NumericEqualityNode newNode (String first, NumberNode second) {
		NumericEqualityNode node = new NumericEqualityNode();
		node.name = first;
		node.val = second;
		return node;
	}
	
	public boolean performNode () {
		return (Math.abs(Operator.numericVars.get(name).getValue() - val.getValue()) < 0.01);
	}
	
	public static int getArgCount () {
		return 2;
	}
}
