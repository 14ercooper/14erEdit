package com.fourteener.worldeditor.operations.operators.world;

import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class GetBlockDataNode extends Node {
	
	public GetBlockDataNode newNode() {
		return new GetBlockDataNode();
	}
	
	public boolean performNode () {
		String s = Operator.currentBlock.getBlockData().getAsString(true);
		Operator.currentPlayer.sendMessage("§dBlock Data: " + s);
		return s.contains("[");
	}
	
	public int getArgCount () {
		return 0;
	}
}
