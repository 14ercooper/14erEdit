package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class FalseNode extends Node {

    @Override
    public FalseNode newNode(ParserState parserState) {
        return new FalseNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return false;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
