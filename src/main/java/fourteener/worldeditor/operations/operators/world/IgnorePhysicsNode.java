package fourteener.worldeditor.operations.operators.world;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

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
