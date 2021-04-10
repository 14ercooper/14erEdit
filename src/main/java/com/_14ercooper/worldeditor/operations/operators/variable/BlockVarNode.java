package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.BlockVar;

public class BlockVarNode extends Node {

    String name;

    @Override
    public BlockVarNode newNode() {
        BlockVarNode node = new BlockVarNode();
        node.name = GlobalVars.operationParser.parseStringNode().contents;
        return node;
    }

    @Override
    public boolean performNode() {
        if (Operator.blockVars.containsKey(name)) {
            Main.logError("Could not create block variable. Does it already exist?", Operator.currentPlayer, null);
            return false;
        }
        Operator.blockVars.put(name, new BlockVar());
        return true;
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
