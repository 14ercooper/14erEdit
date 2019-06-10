package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

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
