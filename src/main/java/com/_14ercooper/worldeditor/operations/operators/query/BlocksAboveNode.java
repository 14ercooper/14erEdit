package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import org.bukkit.command.CommandSender;

public class BlocksAboveNode extends Node {

    RangeNode arg1;
    Node arg2;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public BlocksAboveNode newNode(CommandSender currentPlayer) {
        BlocksAboveNode node = new BlocksAboveNode();
        try {
            node.arg1 = GlobalVars.operationParser.parseRangeNode(currentPlayer);
            node.arg2 = GlobalVars.operationParser.parsePart(currentPlayer);
        } catch (Exception e) {
            Main.logError("Error creating blocks above node. Please check your syntax.", currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not parse blocks above node. Range node and a block are required, but not given.",
                    currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        Block currBlock = state.getCurrentWorld().getBlockAt(state.getCurrentBlock().getLocation());
        int x = currBlock.getX();
        int y = currBlock.getY();
        int z = currBlock.getZ();
        int min = (int) arg1.getMin(state);
        int max = (int) arg1.getMax(state);
        boolean blockRangeMet = true;

        for (int dy = y + min; dy <= y + max; dy++) {
            state.setCurrentBlock(state.getCurrentWorld().getBlockAt(x, dy, z));
            if (!(arg2.performNode(state)))
                blockRangeMet = false;
        }

        state.setCurrentBlock(currBlock);
        return blockRangeMet;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
