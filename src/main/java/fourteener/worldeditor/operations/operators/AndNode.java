package fourteener.worldeditor.operations.operators;

public class AndNode extends Node {
	
	public Node arg1, arg2;
	
	public static AndNode newNode (Node first, Node second) {
		AndNode andNode = new AndNode();
		andNode.arg1 = first;
		andNode.arg2 = second;
		return andNode;
	}
	
	public boolean performNode () {
		return ((arg1.performNode()) && (arg2.performNode()));
	}
	
	public static int getArgCount () {
		return 2;
	}
}
