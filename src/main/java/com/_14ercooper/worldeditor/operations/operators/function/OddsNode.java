package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.command.CommandSender;

public class OddsNode extends Node {

    public NumberNode arg;

    @Override
    public OddsNode newNode(CommandSender currentPlayer) {
        OddsNode node = new OddsNode();
        try {
            node.arg = GlobalVars.operationParser.parseNumberNode(currentPlayer);
        } catch (Exception e) {
            Main.logError("Could not create odds node, argument is not a number.", currentPlayer, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Error creating odds node. Requires a number, but no number was found.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        double chance = GlobalVars.rand.nextDouble() * 100.0;
        return (chance < arg.getValue(state));
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
