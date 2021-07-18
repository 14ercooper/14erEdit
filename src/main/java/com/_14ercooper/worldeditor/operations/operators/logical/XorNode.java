package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class XorNode extends Node {

    public Node arg1, arg2;

    @Override
    public XorNode newNode(CommandSender currentPlayer) {
        XorNode node = new XorNode();
        try {
            node.arg1 = GlobalVars.operationParser.parsePart(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parsePart(currentPlayer);
        } catch (Exception e) {
            Main.logError("Error creating xor node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Error creating xor node. Requires 2 arguments, but these were not provided.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            boolean x = arg1.performNode(state);
            boolean y = arg2.performNode(state);
            return ((x || y) && !(x && y));
        } catch (Exception e) {
            Main.logError("Error performing xor node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
