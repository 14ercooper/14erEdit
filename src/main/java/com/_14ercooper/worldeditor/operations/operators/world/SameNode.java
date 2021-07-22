package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;

public class SameNode extends BlockNode {

    @Override
    public SameNode newNode(ParserState parserState) {
        return new SameNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return true;
    }

    @Override
    public String getBlock(OperatorState state) {
        return state.getCurrentBlock().block.getType().toString();
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
