package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class XorNode extends Node {
	
	public Node arg1, arg2;
	
	public XorNode(Node first, Node second) {
		arg1 = first;
		arg2 = second;
	}
	
	public boolean performNode () {
		boolean x = arg1.performNode();
		boolean y = arg2.performNode();
		return ((x || y) && !(x && y));
	}
	
	public static int getArgCount () {
		return 2;
	}
}
