package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class ModulusNode extends Node {

    public int arg1 = -1;
    public NumberNode arg2, arg3, arg4;

    @Override
    public ModulusNode newNode(ParserState parserState) {
        ModulusNode node = new ModulusNode();
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
            node.arg3 = Parser.parseNumberNode(parserState);
            node.arg4 = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Could not create remainder node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg4 == null) {
            Main.logError("Could not create remainder node. Requires an axis and a number, but these were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int base = arg2.getInt(state);
        int cutLow = arg3.getInt(state);
        int cutHigh = arg4.getInt(state);
        int value;
        if (arg1 == 0) {
            value = state.getCurrentBlock().x;
        } else if (arg1 == 1) {
            value = state.getCurrentBlock().y;
        } else if (arg1 == 2) {
            value = state.getCurrentBlock().z;
        }
        else {
            Main.logError("Invalid axis provided to remainder node. Please check your syntax.", state.getCurrentPlayer(), null);
            return false;
        }
        int floorMod = Math.floorMod(value, base);
        return floorMod >= cutLow && floorMod < cutHigh;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
