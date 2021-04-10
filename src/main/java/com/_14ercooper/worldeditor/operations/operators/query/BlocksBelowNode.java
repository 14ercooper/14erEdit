package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;

public class BlocksBelowNode extends Node {

    RangeNode arg1;
    Node arg2;

    @Override
    public BlocksBelowNode newNode() {
        BlocksBelowNode node = new BlocksBelowNode();
        try {
            node.arg1 = GlobalVars.operationParser.parseRangeNode();
            node.arg2 = GlobalVars.operationParser.parsePart();
        } catch (Exception e) {
            Main.logError("Error creating blocks below node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create blocks below node. Two arguments are required, but not given.",
                    Operator.currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode() {
        Block currBlock = Operator.currentPlayer.getWorld().getBlockAt(Operator.currentBlock.getLocation());
        int x = currBlock.getX();
        int y = currBlock.getY();
        int z = currBlock.getZ();
        int min = (int) arg1.getMin();
        int max = (int) arg1.getMax();
        boolean blockRangeMet = true;

        for (int dy = y - min; dy >= y - max; dy--) {
            Operator.currentBlock = Operator.currentPlayer.getWorld().getBlockAt(x, dy, z);
            if (!(arg2.performNode()))
                blockRangeMet = false;
        }

        Operator.currentBlock = currBlock;
        return blockRangeMet;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
