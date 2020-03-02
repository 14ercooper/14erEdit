package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class IfNode extends Node {
	
	public Node arg1, arg2, arg3;
	
	public IfNode(Node mask, Node ifTrue, Node ifFalse) {
		arg1 = mask;
		arg2 = ifTrue;
		arg3 = ifFalse;
	}
	
	public boolean performNode () {
		if (arg1.performNode()) {
			return arg2.performNode();
		}
		else {
			return arg3.performNode();
		}
	}
	
	public static int getArgCount () {
		return 3;
	}
}
