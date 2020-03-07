package fourteener.worldeditor.operations.operators.logical;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.operators.Node;

public class OrNode extends Node {

	public Node arg1, arg2;
	
	public OrNode newNode() {
		OrNode node = new OrNode();
		node.arg1 = GlobalVars.operationParser.parsePart();
		node.arg2 = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		return ((arg1.performNode()) || (arg2.performNode()));
	}
	
	public int getArgCount () {
		return 2;
	}
}
