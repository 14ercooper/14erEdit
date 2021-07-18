package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.command.CommandSender;

public class EveryXNode extends Node {

    public int arg1 = -1;
    public NumberNode arg2;

    @Override
    public EveryXNode newNode(CommandSender currentPlayer) {
        EveryXNode node = new EveryXNode();
        try {
            String dim = GlobalVars.operationParser.parseStringNode(currentPlayer).contents;
            if (dim.equalsIgnoreCase("x")) {
                node.arg1 = 0;
            } else if (dim.equalsIgnoreCase("y")) {
                node.arg1 = 1;
            } else if (dim.equalsIgnoreCase("z")) {
                node.arg1 = 2;
            }
            arg2 = GlobalVars.operationParser.parseNumberNode(currentPlayer);
        } catch (Exception e) {
            Main.logError("Could not create remainder node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create remainder node. Requires an axis and a number, but these were not given.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        int base = (int) arg2.getValue(state);
        if (arg1 == 0) {
            int value = state.getCurrentBlock().getX();
            return Math.floorMod(value, base) == 0;
        } else if (arg1 == 1) {
            int value = state.getCurrentBlock().getY();
            return Math.floorMod(value, base) == 0;
        } else if (arg1 == 2) {
            int value = state.getCurrentBlock().getZ();
            return Math.floorMod(value, base) == 0;
        }
        Main.logError("Invalid axis provided to remainder node. Please check your syntax.", state.getCurrentPlayer(), null);
        return false;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
