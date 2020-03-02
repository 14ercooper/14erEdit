package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.operations.operators.Node;

public class WhileNode extends Node {
	
	Node cond, op;
	
	public static WhileNode newNode (Node arg1, Node arg2) {
		WhileNode node = new WhileNode();
		node.cond = arg1;
		node.cond = arg2;
		return node;
	}
	
	public boolean performNode () {
		boolean result = true;
		while (cond.performNode()) {
			boolean result2 = op.performNode();
			result = result && result2;
		}
		return result;
	}
	
	public static int getArgCount () {
		return 2;
	}
}
