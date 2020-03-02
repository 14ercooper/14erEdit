package fourteener.worldeditor.operations.operators.function;

import fourteener.worldeditor.operations.operators.Node;
import fourteener.worldeditor.operations.operators.core.NumberNode;

public class RangeNode extends Node {
	
	public NumberNode arg1, arg2;
	
	public RangeNode(NumberNode min, NumberNode max) {
		arg1 = min;
		arg2 = max;
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
