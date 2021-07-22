package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import org.bukkit.block.Block;

public class BlocksAboveNode extends Node {

    RangeNode arg1;
    Node arg2;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public BlocksAboveNode newNode(ParserState parserState) {
        BlocksAboveNode node = new BlocksAboveNode();
        try {
            node.arg1 = Parser.parseRangeNode(parserState);
            node.arg2 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating blocks above node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not parse blocks above node. Range node and a block are required, but not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        Block currBlock = state.getCurrentWorld().getBlockAt(state.getCurrentBlock().block.getLocation());
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
