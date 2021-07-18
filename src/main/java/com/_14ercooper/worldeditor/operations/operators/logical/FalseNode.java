package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class FalseNode extends Node {

    @Override
    public FalseNode newNode(CommandSender currentPlayer) {
        return new FalseNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        return false;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
