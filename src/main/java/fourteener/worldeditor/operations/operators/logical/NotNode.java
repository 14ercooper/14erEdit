package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class NotNode extends Node {
	public Node arg;
	
	public static NotNode newNode (Node node) {
		NotNode notNode = new NotNode();
		notNode.arg = node;
		return notNode;
	}
	
	public boolean performNode () {
		return !(arg.performNode());
	}
	
	public static int getArgCount () {
		return 1;
	}
}
