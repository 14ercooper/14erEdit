package com.fourteener.worldeditor.operations.operators.world;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class SetPlusNode extends Node {

	public BlockNode arg;
	
	public SetPlusNode newNode() {
		SetPlusNode node = new SetPlusNode();
		node.arg = (BlockNode) GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		Operator.currentBlock.setType(arg.getBlock());
		Operator.currentBlock.setBlockData(arg.getData());
		return true;
	}
	
	public int getArgCount () {
		return 1;
	}
}
