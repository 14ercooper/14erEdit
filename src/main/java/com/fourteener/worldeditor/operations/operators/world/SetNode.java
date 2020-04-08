package com.fourteener.worldeditor.operations.operators.world;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class SetNode extends Node {

	public BlockNode arg;
	
	public SetNode newNode() {
		SetNode node = new SetNode();
		node.arg = (BlockNode) GlobalVars.operationParser.parsePart();
		return node;
	}
	
	public boolean performNode () {
		boolean didSet = true;
		if (Operator.currentBlock.getType() == arg.getBlock()) {
			didSet = false;
		}
		SetBlock.setMaterial(Operator.currentBlock, arg.getBlock());
		return didSet;
	}
	
	public int getArgCount () {
		return 1;
	}
}
