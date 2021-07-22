package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class RemainderNode extends Node {

    public int arg1 = -1;
    public NumberNode arg2;

    @Override
    public RemainderNode newNode(ParserState parserState) {
        RemainderNode node = new RemainderNode();
        try {
            String dim = Parser.parseStringNode(parserState).contents;
            if (dim.equalsIgnoreCase("x")) {
                node.arg1 = 0;
            } else if (dim.equalsIgnoreCase("y")) {
                node.arg1 = 1;
            } else if (dim.equalsIgnoreCase("z")) {
                node.arg1 = 2;
            }
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
        int base = (int) arg2.getValue(state);
        int modBase = base * 2;
        if (arg1 == 0) {
            int value = state.getCurrentBlock().x;
            return Math.floorMod(value, modBase) < base;
        } else if (arg1 == 1) {
            int value = state.getCurrentBlock().y;
            return Math.floorMod(value, modBase) < base;
        } else if (arg1 == 2) {
            int value = state.getCurrentBlock().z;
            return Math.floorMod(value, modBase) < base;
        }
        Main.logError("Invalid axis provided to remainder node. Please check your syntax.", state.getCurrentPlayer(), null);
        return false;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
