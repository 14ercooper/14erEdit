package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.operators.Node;

public class TrueNode extends Node {

    @Override
    public TrueNode newNode() {
	return new TrueNode();
    }

    @Override
    public boolean performNode() {
	return true;
    }

    @Override
    public int getArgCount() {
	return 0;
    }
}
