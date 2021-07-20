package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class OrNode extends Node {

    public Node arg1, arg2;

    @Override
    public OrNode newNode(ParserState parserState) {
        OrNode node = new OrNode();
        try {
            node.arg1 = Parser.parsePart(parserState);
            node.arg2 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating or node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Error creating or node. Two arguments required, but not provided.", parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            return ((arg1.performNode(state)) || (arg2.performNode(state)));
        } catch (Exception e) {
            Main.logError("Error performing or node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
