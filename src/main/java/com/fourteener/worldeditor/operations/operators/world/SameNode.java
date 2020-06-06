package com.fourteener.worldeditor.operations.operators.world;

import com.fourteener.worldeditor.operations.Operator;

public class SameNode extends BlockNode {
	
	public SameNode newNode() {
		return new SameNode();
	}
	
	public boolean performNode () {
		return true;
	}
	
	public String getBlock () {
		return Operator.currentBlock.getType().toString();
	}
	
	public int getArgCount () {
		return 0;
	}
}
