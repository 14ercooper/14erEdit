package fourteener.worldeditor.operations.operators.function;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class RangeNode extends Node {
	
	public NumberNode arg1, arg2;
	
	public RangeNode newNode() {
		RangeNode node = new RangeNode();
		node.arg1 = GlobalVars.operationParser.parseNumberNode();
		node.arg2 = GlobalVars.operationParser.parseNumberNode();
		return node;
	}
	
	public boolean performNode () {
		return false;
	}
	
	public double getMin () {
		return arg1.getValue();
	}
	
	public double getMax () {
		return arg2.getValue();
	}
	
	public int getArgCount () {
		return 2;
	}
}
