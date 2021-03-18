package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.Operator;

public class YNode extends NumberNode {

    // Returns a new node
    @Override
    public YNode newNode() {
	return new YNode();
    }

    // Return the number
    @Override
    public double getValue() {
	return Operator.currentBlock.getY();
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
	return 0;
    }

}
