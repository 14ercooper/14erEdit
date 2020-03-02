package fourteener.worldeditor.operations.operators.function;

import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class RangeNode extends Node {
	
	public NumberNode arg1, arg2;
	
	public static RangeNode newNode (NumberNode min, NumberNode max) {
		RangeNode rangeNode = new RangeNode();
		rangeNode.arg1 = min;
		rangeNode.arg2 = max;
		return rangeNode;
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
	
	public static int getArgCount () {
		return 2;
	}
}
