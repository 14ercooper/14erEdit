package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class GetBlockDataNode extends Node {

    @Override
    public GetBlockDataNode newNode(CommandSender currentPlayer) {
        return new GetBlockDataNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            String s = state.getCurrentBlock().getBlockData().getAsString(true);
            state.getCurrentPlayer().sendMessage("§dBlock Data: " + s);
            return s.contains("[");
        } catch (Exception e) {
            Main.logError(
                    "Error performing get block data node. Please check your syntax (or tell 14er how you got here).",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
