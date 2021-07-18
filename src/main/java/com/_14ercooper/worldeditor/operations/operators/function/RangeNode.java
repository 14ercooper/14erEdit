package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.command.CommandSender;

public class RangeNode extends Node {

    public NumberNode arg1, arg2;

    @Override
    public RangeNode newNode(CommandSender currentPlayer) {
        RangeNode node = new RangeNode();
        try {
            node.arg1 = GlobalVars.operationParser.parseNumberNode(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parseNumberNode(currentPlayer);
        } catch (Exception e) {
            Main.logError(
                    "Could not create range node. Please check your syntax (did you remember to create a range node?).",
                    currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create range node. Two numbers were expected, but not provided.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        return false;
    }

    public double getMin(OperatorState state) {
        return arg1.getValue(state);
    }

    public double getMax(OperatorState state) {
        return arg2.getValue(state);
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
