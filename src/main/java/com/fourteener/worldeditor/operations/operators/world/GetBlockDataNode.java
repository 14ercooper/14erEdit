package com.fourteener.worldeditor.operations.operators.world;

import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class GetBlockDataNode extends Node {
	
	public GetBlockDataNode newNode() {
		return new GetBlockDataNode();
	}
	
	public boolean performNode () {
		Operator.currentPlayer.sendMessage("§dBlock Data: " + Operator.currentBlock.getBlockData().getAsString(true));
		return true;
	}
	
	public int getArgCount () {
		return 0;
	}
}