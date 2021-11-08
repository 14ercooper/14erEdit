package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class ModulusNode extends Node {

    public NumberNode arg1, arg2, arg3, arg4;

    @Override
    public ModulusNode newNode(ParserState parserState) {
        ModulusNode node = new ModulusNode();
        try {
            node.arg1 = (NumberNode) Parser.parsePart(parserState);
            node.arg2 = (NumberNode) Parser.parsePart(parserState);
            node.arg3 = (NumberNode) Parser.parsePart(parserState);
            node.arg4 = (NumberNode) Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Could not create modulus node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg4 == null) {
            Main.logError("Could not create modulus node. Requires an axis and 3 numbers, but these were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int base = arg2.getInt(state);
        int cutLow = arg3.getInt(state);
        int cutHigh = arg4.getInt(state);
        int value = arg1.getInt(state);
        int floorMod = Math.floorMod(value, base);
        return floorMod >= cutLow && floorMod < cutHigh;
    }

    @Override
    public int getArgCount() {
        return 4;
    }
}
