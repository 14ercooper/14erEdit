package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.operations.Operator;

public class SameNode extends BlockNode {

    @Override
    public SameNode newNode() {
	return new SameNode();
    }

    @Override
    public boolean performNode() {
	return true;
    }

    @Override
    public String getBlock() {
	return Operator.currentBlock.getType().toString();
    }

    @Override
    public int getArgCount() {
	return 0;
    }
}
