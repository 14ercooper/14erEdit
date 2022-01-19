// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class ElseNode extends Node {

    Node subNode;

    @Override
    public ElseNode newNode(ParserState parserState) {
        ElseNode node = new ElseNode();
        try {
            node.subNode = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating else node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.subNode == null) {
            Main.logError("Could not create else node. An argument is required but not provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return subNode.performNode(state, true);
        } catch (Exception e) {
            Main.logError("Error performing else node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
