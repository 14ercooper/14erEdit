// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class TrueNode extends Node {

    @Override
    public TrueNode newNode(ParserState parserState) {
        return new TrueNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return true;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
