package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.command.CommandSender;

public class XNode extends NumberNode {

    // Returns a new node
    @Override
    public XNode newNode(CommandSender currentPlayer) {
        return new XNode();
    }

    // Return the number
    @Override
    public double getValue(OperatorState state) {
        return state.getCurrentBlock().getX();
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 0;
    }

}
