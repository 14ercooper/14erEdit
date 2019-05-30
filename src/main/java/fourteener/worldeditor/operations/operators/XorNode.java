package fourteener.worldeditor.operations.operators;

public class XorNode extends Node {
	
	public Node arg1, arg2;
	
	public static XorNode newNode (Node first, Node second) {
		XorNode xorNode = new XorNode();
		xorNode.arg1 = first;
		xorNode.arg2 = second;
		return xorNode;
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
