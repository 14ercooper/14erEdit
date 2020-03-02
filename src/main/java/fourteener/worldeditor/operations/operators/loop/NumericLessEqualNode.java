package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericLessEqualNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public NumericLessEqualNode(String first, NumberNode second) {
		name = first;
		val = second;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() <= val.getValue());
	}
	
	public static int getArgCount () {
		return 2;
	}
}
