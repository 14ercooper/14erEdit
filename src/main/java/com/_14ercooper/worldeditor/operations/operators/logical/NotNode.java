package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class NotNode extends Node {
    public Node arg;

    @Override
    public NotNode newNode() {
        NotNode node = new NotNode();
        try {
            node.arg = GlobalVars.operationParser.parsePart();
        } catch (Exception e) {
            Main.logError("Error creating not node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Error creating not node. An argument is required, but was not provided.",
                    Operator.currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode() {
        try {
            return !(arg.performNode());
        } catch (Exception e) {
            Main.logError("Error performing not node. Please check your syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
