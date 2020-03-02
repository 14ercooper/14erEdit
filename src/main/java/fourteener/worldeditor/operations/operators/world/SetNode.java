package fourteener.worldeditor.operations.operators.world;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class SetNode extends Node {

	public BlockNode arg;
	
	public static SetNode newNode (Node blockNode) {
		SetNode setNode = new SetNode();
		setNode.arg = (BlockNode) blockNode;
		return setNode;
	}
	
	public boolean performNode () {
		Operator.currentBlock.setType(arg.getBlock());
		return true;
	}
	
	public static int getArgCount () {
		return 1;
	}
}
