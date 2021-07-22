package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;
import com._14ercooper.worldeditor.operations.operators.query.GetNBTNode;

public class StringContainsNode extends Node {

    Node arg1, arg2;

    @Override
    public Node newNode(ParserState parserState) {
        StringContainsNode node = new StringContainsNode();
        try {
            node.arg1 = Parser.parsePart(parserState);
            node.arg2 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Could not create string contains node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create string contains node. Two string nodes required, but not provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            if (!(arg1 instanceof StringNode) || !(arg2 instanceof StringNode))
                return false;
            boolean result;
            if (arg1 instanceof GetNBTNode) {
                arg1.performNode(state, true);
                result = ((GetNBTNode) arg1).getText().contains(((StringNode) arg2).contents);
            } else {
                result = ((StringNode) arg1).contents.contains(((StringNode) arg2).contents);
            }
            return result;
        } catch (Exception e) {
            Main.logError("Could not perform string contains node. Are the child nodes of the appropriate types?",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }

}
