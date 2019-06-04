package fourteener.worldeditor.operations.operators;

import fourteener.worldeditor.operations.Operator;

public class GetBlockDataNode extends Node {
	
	public static GetBlockDataNode newNode () {
		return new GetBlockDataNode();
	}
	
	public boolean performNode () {
		Operator.currentPlayer.sendMessage("§dBlock Data: " + Operator.currentBlock.getBlockData().getAsString(true));
		return true;
	}
	
	public static int getArgCount () {
		return 0;
	}
}
