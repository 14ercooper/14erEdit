package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class IfNode extends Node {
	
	public Node arg1, arg2, arg3;
	
	public static IfNode newNode (Node mask, Node ifTrue, Node ifFalse) {
		IfNode ifNode = new IfNode();
		ifNode.arg1 = mask;
		ifNode.arg2 = ifTrue;
		ifNode.arg3 = ifFalse;
		return ifNode;
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
