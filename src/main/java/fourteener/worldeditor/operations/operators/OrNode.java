package fourteener.worldeditor.operations.operators;

public class OrNode extends Node {

	public Node arg1, arg2;
	
	public static OrNode newNode (Node first, Node second) {
		OrNode orNode = new OrNode();
		orNode.arg1 = first;
		orNode.arg2 = second;
		return orNode;
	}
	
	public boolean performNode () {
		return ((arg1.performNode()) || (arg2.performNode()));
	}
	
	public static int getArgCount () {
		return 2;
	}
}
