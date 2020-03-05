package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.operators.Node;

public class IfNode extends Node {
	
	public Node arg1, arg2, arg3;
	
	public IfNode newNode() {
		IfNode node = new IfNode();
		node.arg1 = Main.operationParser.parsePart();
		node.arg2 = Main.operationParser.parsePart();
		node.arg3 = Main.operationParser.parsePart();
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
