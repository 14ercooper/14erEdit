package fourteener.worldeditor.operations.operators.core;

import fourteener.worldeditor.operations.operators.Node;

public class LinkerNode extends Node {
	
	public Node arg1, arg2;
	
	public LinkerNode(Node first, Node second) {
		arg1 = first;
		arg2 = second;
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
