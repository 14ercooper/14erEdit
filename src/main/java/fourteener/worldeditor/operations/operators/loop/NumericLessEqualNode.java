package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class NumericLessEqualNode extends Node {
	
	public String name;
	public NumberNode val;
	
	public NumericLessEqualNode newNode() {
		NumericLessEqualNode node = new NumericLessEqualNode();
		node.name = Main.operationParser.parseStringNode();
		node.val = Main.operationParser.parseNumberNode();
		return node;
	}
	
	public boolean performNode () {
		return (Operator.numericVars.get(name).getValue() <= val.getValue());
	}
	
	public int getArgCount () {
		return 2;
	}
}
