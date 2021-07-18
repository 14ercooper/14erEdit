package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class TrueNode extends Node {

    @Override
    public TrueNode newNode(CommandSender currentPlayer) {
        return new TrueNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        return true;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
