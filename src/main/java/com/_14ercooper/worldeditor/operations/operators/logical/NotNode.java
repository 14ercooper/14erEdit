// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class NotNode extends Node {
    public Node arg;

    @Override
    public NotNode newNode(ParserState parserState) {
        NotNode node = new NotNode();
        try {
            node.arg = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating not node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Error creating not node. An argument is required, but was not provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return !(arg.performNode(state, true));
        } catch (Exception e) {
            Main.logError("Error performing not node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
