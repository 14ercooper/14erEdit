// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class RangeNode extends Node {

    public NumberNode arg1, arg2;

    @Override
    public RangeNode newNode(ParserState parserState) {
        RangeNode node = new RangeNode();
        try {
            node.arg1 = Parser.parseNumberNode(parserState);
            node.arg2 = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError(
                    "Could not create range node. Please check your syntax (did you remember to create a range node?).",
                    parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create range node. Two numbers were expected, but not provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return false;
    }

    public double getMin(OperatorState state) {
        return arg1.getValue(state);
    }

    public double getMax(OperatorState state) {
        return arg2.getValue(state);
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
