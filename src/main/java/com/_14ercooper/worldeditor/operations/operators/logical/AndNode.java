package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class AndNode extends Node {

    public Node arg1, arg2;

    @Override
    public AndNode newNode(CommandSender currentPlayer) {
        AndNode node = new AndNode();
        try {
            node.arg1 = GlobalVars.operationParser.parsePart(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parsePart(currentPlayer);
        } catch (Exception e) {
            Main.logError("Error creating and node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create and node. Two arguments are required, but were not given.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            return ((arg1.performNode(state)) && (arg2.performNode(state)));
        } catch (Exception e) {
            Main.logError("Error performing and node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
