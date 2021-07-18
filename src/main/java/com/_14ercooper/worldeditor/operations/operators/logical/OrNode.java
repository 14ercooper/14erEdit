package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class OrNode extends Node {

    public Node arg1, arg2;

    @Override
    public OrNode newNode(CommandSender currentPlayer) {
        OrNode node = new OrNode();
        try {
            node.arg1 = GlobalVars.operationParser.parsePart(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parsePart(currentPlayer);
        } catch (Exception e) {
            Main.logError("Error creating or node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Error creating or node. Two arguments required, but not provided.", currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            return ((arg1.performNode(state)) || (arg2.performNode(state)));
        } catch (Exception e) {
            Main.logError("Error performing or node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
