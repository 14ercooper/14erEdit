package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.operators.Node;

public class FalseNode extends Node {

    @Override
    public FalseNode newNode() {
        return new FalseNode();
    }

    @Override
    public boolean performNode() {
        return false;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
