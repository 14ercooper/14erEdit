package fourteener.worldeditor.operations.operators;

public class LinkerNode extends Node {
	
	public Node arg1, arg2;
	
	public static LinkerNode newNode (Node first, Node second) {
		LinkerNode linkerNode = new LinkerNode();
		linkerNode.arg1 = first;
		linkerNode.arg2 = second;
		return linkerNode;
	}
	
	public boolean performNode () {
		boolean a1 = arg1.performNode();
		boolean a2 = arg2.performNode();
		return (a1 && a2);
	}
	
	public static int getArgCount () {
		return 2;
	}
}
