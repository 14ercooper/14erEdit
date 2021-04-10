package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class LinkerNode extends Node {

    public Node arg1, arg2;

    @Override
    public LinkerNode newNode() {
        LinkerNode node = new LinkerNode();
        try {
            node.arg1 = GlobalVars.operationParser.parsePart();
            node.arg2 = GlobalVars.operationParser.parsePart();
        } catch (Exception e) {
            Main.logError("Could not create linker node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create Linker node. Node requries two operations, two were not provided.",
                    Operator.currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode() {
        try {
            boolean a1 = arg1.performNode();
            boolean a2 = arg2.performNode();
            return (a1 && a2);
        } catch (Exception e) {
            Main.logError("Error performing linker node. Please check your operation syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
