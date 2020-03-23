package com.fourteener.worldeditor.operations.operators.query;

import com.fourteener.worldeditor.main.NBTExtractor;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class GetNBTNode extends Node {

	@Override
	public GetNBTNode newNode() {
		return new GetNBTNode();
	}

	@Override
	public boolean performNode() {
		Operator.currentPlayer.sendMessage("§dNBT: " + (new NBTExtractor()).getNBT(Operator.currentBlock));
		return true;
	}

	@Override
	public int getArgCount() {
		return 0;
	}

}
