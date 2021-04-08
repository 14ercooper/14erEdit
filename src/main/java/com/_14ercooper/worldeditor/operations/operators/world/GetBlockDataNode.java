package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class GetBlockDataNode extends Node {

    @Override
    public GetBlockDataNode newNode() {
	return new GetBlockDataNode();
    }

    @Override
    public boolean performNode() {
	try {
	    String s = Operator.currentBlock.getBlockData().getAsString(true);
	    Operator.currentPlayer.sendMessage("§dBlock Data: " + s);
	    return s.contains("[");
	}
	catch (Exception e) {
	    Main.logError(
		    "Error performing get block data node. Please check your syntax (or tell 14er how you got here).",
		    Operator.currentPlayer, e);
	    return false;
	}
    }

    @Override
    public int getArgCount() {
	return 0;
    }
}
