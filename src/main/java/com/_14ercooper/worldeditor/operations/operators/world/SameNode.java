package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.command.CommandSender;

public class SameNode extends BlockNode {

    @Override
    public SameNode newNode(CommandSender currentPlayer) {
        return new SameNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        return true;
    }

    @Override
    public String getBlock(OperatorState state) {
        return state.getCurrentBlock().getType().toString();
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
