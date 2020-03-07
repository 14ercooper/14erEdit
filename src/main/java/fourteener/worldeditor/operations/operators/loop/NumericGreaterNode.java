package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericGreaterNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public NumericGreaterNode newNode() {
		NumericGreaterNode node = new NumericGreaterNode();
		node.name = GlobalVars.operationParser.parseStringNode();
		node.val = GlobalVars.operationParser.parseNumberNode();
		return node;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() > val.getValue());
	}
	
	public int getArgCount () {
		return 2;
	}
}
