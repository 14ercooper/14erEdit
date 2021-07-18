package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class EntryNode {
    public Node node;

    public EntryNode(Node newNode) {
        node = newNode;
    }

    public boolean performNode(OperatorState state) {
        try {
            return node.performNode(state);
        } catch (Exception e) {
            Main.logError("Error performing node. Async queue dropped.", state.getCurrentPlayer(), e);
            e.printStackTrace();
            AsyncManager.dropAsync();
            return false;
        }
    }
}
