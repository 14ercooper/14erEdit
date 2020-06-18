package com.fourteener.worldeditor.operations.operators.world;

import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class GetBlockDataNode extends Node {

	public GetBlockDataNode newNode() {
		return new GetBlockDataNode();
	}

	public boolean performNode () {
		try {
			String s = Operator.currentBlock.getBlockData().getAsString(true);
			Operator.currentPlayer.sendMessage("§dBlock Data: " + s);
			return s.contains("[");
		} catch (Exception e) {
			Main.logError("Error performing get block data node. Please check your syntax (or tell 14er how you got here).", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 0;
	}
}
