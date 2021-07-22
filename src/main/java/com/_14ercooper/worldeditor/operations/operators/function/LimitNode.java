package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class LimitNode extends Node {

    public NumberNode arg1, arg2;

    @Override
    public LimitNode newNode(ParserState parserState) {
        LimitNode node = new LimitNode();
        try {
            node.arg1 = Parser.parseNumberNode(parserState);
            node.arg2 = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Could not create limit node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create limit node. Requires an axis and a number, but these were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return arg1.getValue(state) <= arg2.getValue(state) + 0.01;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
