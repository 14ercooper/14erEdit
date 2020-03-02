package fourteener.worldeditor.operations.operators.loop;

import fourteener.worldeditor.operations.operators.Node;

public class WhileNode extends Node {
	
	Node cond, op;
	
	public WhileNode(Node arg1, Node arg2) {
		cond = arg1;
		op = arg2;
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
