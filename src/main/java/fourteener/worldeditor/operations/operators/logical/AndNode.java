package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class AndNode extends Node {
	
	public Node arg1, arg2;
	
	public AndNode(Node first, Node second) {
		arg1 = first;
		arg2 = second;
	}
	
	public boolean performNode () {
		return ((arg1.performNode()) && (arg2.performNode()));
	}
	
	public static int getArgCount () {
		return 2;
	}
}
