package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.operators.Node;

public class IfNode extends Node {
	
	public Node arg1, arg2, arg3;
	
	public IfNode newNode() {
		IfNode node = new IfNode();
		node.arg1 = GlobalVars.operationParser.parsePart();
		node.arg2 = GlobalVars.operationParser.parsePart();
		node.arg3 = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		if (arg1.performNode()) {
			return arg2.performNode();
		}
		else {
			return arg3.performNode();
		}
	}
	
	public int getArgCount () {
		return 3;
	}
}
