package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.logical.IfNode;
import org.bukkit.command.CommandSender;

public class ReplaceNode extends Node {

    Node root = null;

    // /fx br s 5 replace gold_block|cobweb diamond_block|gravel
    @Override
    public ReplaceNode newNode(CommandSender currentPlayer) {
        ReplaceNode node = new ReplaceNode();
        try {
            String[] from = GlobalVars.operationParser.parseStringNode(currentPlayer).getText().split("\\|");
            String[] to = GlobalVars.operationParser.parseStringNode(currentPlayer).getText().split("\\|");
            if (from.length != to.length) {
                Main.logError("Replace node from list and to list are of uneven lengths.", currentPlayer, null);
                return null;
            }
            for (int i = from.length - 1; i >= 0; i--) {
                Main.logDebug("Made replace from " + from[i] + " to " + to[i]);
                node.root = new IfNode().newNode(new BlockNode().newNode(from[i], currentPlayer), new SetNode().newNode(new BlockNode().newNode(to[i], currentPlayer)), node.root);
            }
            return node;
        } catch (Exception e) {
            Main.logError("Could not create replace node. Please check your syntax.", currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        return root.performNode(state);
    }

    @Override
    public int getArgCount() {
        return 2;
    }

}
