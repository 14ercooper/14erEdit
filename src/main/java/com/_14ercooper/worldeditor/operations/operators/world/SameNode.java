package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.operations.Operator;

public class SameNode extends BlockNode {

    public SameNode newNode() {
	return new SameNode();
    }

    public boolean performNode() {
	return true;
    }

    public String getBlock() {
	return Operator.currentBlock.getType().toString();
    }

    public int getArgCount() {
	return 0;
    }
}
