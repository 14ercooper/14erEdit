package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class LinkerNode extends Node {

    public Node arg1, arg2;

    @Override
    public LinkerNode newNode(CommandSender currentPlayer) {
        LinkerNode node = new LinkerNode();
        try {
            node.arg1 = GlobalVars.operationParser.parsePart(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parsePart(currentPlayer);
        } catch (Exception e) {
            Main.logError("Could not create linker node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create Linker node. Node requries two operations, two were not provided.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            boolean a1 = arg1.performNode(state);
            boolean a2 = arg2.performNode(state);
            return (a1 && a2);
        } catch (Exception e) {
            Main.logError("Error performing linker node. Please check your operation syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
