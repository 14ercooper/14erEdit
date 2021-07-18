package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class ElseNode extends Node {

    Node subNode;

    @Override
    public ElseNode newNode(CommandSender currentPlayer) {
        ElseNode node = new ElseNode();
        try {
            node.subNode = GlobalVars.operationParser.parsePart(currentPlayer);
        } catch (Exception e) {
            Main.logError("Error creating else node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.subNode == null) {
            Main.logError("Could not create else node. An argument is required but not provided.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            return subNode.performNode(state);
        } catch (Exception e) {
            Main.logError("Error performing else node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
