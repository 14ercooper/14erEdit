package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class ElseNode extends Node {

    Node subNode;

    @Override
    public ElseNode newNode() {
        ElseNode node = new ElseNode();
        try {
            node.subNode = GlobalVars.operationParser.parsePart();
        } catch (Exception e) {
            Main.logError("Error creating else node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
        if (node.subNode == null) {
            Main.logError("Could not create else node. An argument is required but not provided.",
                    Operator.currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode() {
        try {
            return subNode.performNode();
        } catch (Exception e) {
            Main.logError("Error performing else node. Please check your syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
