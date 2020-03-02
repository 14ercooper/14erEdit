package fourteener.worldeditor.operations.operators.world;

import fourteener.worldeditor.operations.Operator;
import fourteener.worldeditor.operations.operators.Node;

public class GetBlockDataNode extends Node {
	
	public GetBlockDataNode() {
		return;
	}
	
	public boolean performNode () {
		Operator.currentPlayer.sendMessage("§dBlock Data: " + Operator.currentBlock.getBlockData().getAsString(true));
		return true;
	}
	
	public static int getArgCount () {
		return 0;
	}
}
