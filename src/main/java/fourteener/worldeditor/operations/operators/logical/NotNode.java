package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.operations.operators.Node;

public class NotNode extends Node {
	public Node arg;
	
	public NotNode(Node node) {
		arg = node;
	}
	
	public boolean performNode () {
		return !(arg.performNode());
	}
	
	public static int getArgCount () {
		return 1;
	}
}
