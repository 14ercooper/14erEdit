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
		String s = (new NBTExtractor()).getNBT(Operator.currentBlock);
		Operator.currentPlayer.sendMessage("§dNBT: " + s);
		return s.length() > 2;
	}

	@Override
	public int getArgCount() {
		return 0;
	}

}
