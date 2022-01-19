// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class EntryNode {
    public Node node;
    public final int consumedArgs;

    public EntryNode(Node newNode, int consumedArgs) {
        node = newNode;
        this.consumedArgs = consumedArgs;
    }

    public boolean performNode(OperatorState state) {
        try {
            return node.performNode(state, true);
        } catch (Exception e) {
            Main.logError("Error performing node. Async queue dropped.", state.getCurrentPlayer(), e);
            e.printStackTrace();
            AsyncManager.dropAsync();
            return false;
        }
    }
}
