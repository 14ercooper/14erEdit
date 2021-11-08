package com._14ercooper.worldeditor.operations.operators;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;

public abstract class Node {

    public boolean performed = false;

    public boolean isNextNodeRange() {
        return false;
    }

    public boolean isNextNodeBlock() {
        return false;
    }

    public abstract Node newNode(ParserState parserState);

    public boolean performNode(OperatorState state) {
        performed = true;
        return performNode(state, true);
    }

    public abstract boolean performNode(OperatorState state, boolean perform);

    public abstract int getArgCount();
}
