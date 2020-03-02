package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericLessNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public static NumericLessNode newNode (String first, NumberNode second) {
		NumericLessNode node = new NumericLessNode();
		node.name = first;
		node.val = second;
		return node;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() < val.getValue());
	}
	
	public static int getArgCount () {
		return 2;
	}
}
