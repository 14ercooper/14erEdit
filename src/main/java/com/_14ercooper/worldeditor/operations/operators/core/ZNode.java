package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.command.CommandSender;

public class ZNode extends NumberNode {

    // Returns a new node
    @Override
    public ZNode newNode(CommandSender currentPlayer) {
        return new ZNode();
    }

    // Return the number
    @Override
    public double getValue(OperatorState state) {
        return state.getCurrentBlock().getZ();
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 0;
    }

}
