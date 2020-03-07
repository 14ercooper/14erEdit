package fourteener.worldeditor.operations.operators.world;

import fourteener.worldeditor.main.*;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class IgnorePhysicsNode extends Node {
	
	public Node arg;
	
	public IgnorePhysicsNode newNode() {
		IgnorePhysicsNode node = new IgnorePhysicsNode();
		node.arg = GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		Operator.ignoringPhysics = true;
		boolean output = arg.performNode();
		Operator.ignoringPhysics = false;
		return output;
	}
	
	public int getArgCount () {
		return 1;
	}

}
