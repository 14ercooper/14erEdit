package fourteener.worldeditor.operations.operators.world;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class SetNode extends Node {

	public BlockNode arg;
	
	public SetNode newNode() {
		SetNode node = new SetNode();
		node.arg = (BlockNode) Main.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		Operator.currentBlock.setType(arg.getBlock());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
