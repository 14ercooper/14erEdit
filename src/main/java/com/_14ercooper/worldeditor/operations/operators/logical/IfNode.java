package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class IfNode extends Node {

    public Node arg1, arg2, arg3;

    @Override
    public IfNode newNode(CommandSender currentPlayer) {
        IfNode node = new IfNode();
        try {
            node.arg1 = GlobalVars.operationParser.parsePart(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parsePart(currentPlayer);

            int iter = GlobalVars.operationParser.index;
            node.arg3 = GlobalVars.operationParser.parsePart(currentPlayer);
            if (!(node.arg3 instanceof ElseNode)) {
                Main.logDebug("Did not find an instance of an else node.");
                node.arg3 = null;
                GlobalVars.operationParser.index = iter;
            }
        } catch (Exception e) {
            Main.logError("Error creating if node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError(
                    "Error creating if node. At least a condition and on-true operator are required, but are not provided.",
                    currentPlayer, null);
        }
        return node;
    }

    public IfNode newNode(Node cond, Node onTrue, Node onFalse) {
        IfNode node = new IfNode();
        node.arg1 = cond;
        node.arg2 = onTrue;
        node.arg3 = onFalse;
        return node;
    }

    // /fx br v if bedrock if both simplex 3 130 4 not simplex 3 110 4 set
    // polished_andesite else if simplex 3 110 4 set
    // 70%andesite;10%gravel;10%stone;10%cobblestone
    @Override
    public boolean performNode(OperatorState state) {
        try {
            boolean isTrue = arg1.performNode(state);
            boolean toReturn;
            if (isTrue) {
//		Main.logDebug("condition true");
                toReturn = arg2.performNode(state);
            } else if (arg3 == null) {
//		Main.logDebug("no else");
                return false;
            } else {
//		Main.logDebug("else");
                toReturn = arg3.performNode(state);
            }
            return toReturn;
        } catch (Exception e) {
            Main.logError("Error performing if node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 3;
    }
}
