package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

public class IgnorePhysicsNode extends Node {
	
	public Node arg;
	
	public static IgnorePhysicsNode newNode (Node effect) {
		IgnorePhysicsNode ipNode = new IgnorePhysicsNode ();
		ipNode.arg = effect;
		return ipNode;
	}
	
	public boolean performNode () {
		Operator.ignoringPhysics = true;
		boolean output = arg.performNode();
		Operator.ignoringPhysics = false;
		return output;
	}
	
	public static int getArgCount () {
		return 1;
	}

}
