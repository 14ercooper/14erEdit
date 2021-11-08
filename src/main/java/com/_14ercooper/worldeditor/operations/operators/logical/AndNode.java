package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class AndNode extends Node {

    public Node arg1, arg2;

    @Override
    public AndNode newNode(ParserState parserState) {
        AndNode node = new AndNode();
        try {
            node.arg1 = Parser.parsePart(parserState);
            node.arg2 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating and node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create and node. Two arguments are required, but were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return ((arg1.performNode(state, true)) && (arg2.performNode(state, true)));
        } catch (Exception e) {
            Main.logError("Error performing and node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
