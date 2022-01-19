// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class RemainderNode extends Node {

    public NumberNode arg1, arg2;

    @Override
    public RemainderNode newNode(ParserState parserState) {
        RemainderNode node = new RemainderNode();
        try {
            node.arg1 = Parser.parseNumberNode(parserState);
            node.arg2 = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Could not create remainder node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create remainder node. Requires an axis and a number, but these were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int base = arg2.getInt(state);
        int modBase = base * 2;
        int value = arg1.getInt(state);
        return Math.floorMod(value, modBase) < base;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
