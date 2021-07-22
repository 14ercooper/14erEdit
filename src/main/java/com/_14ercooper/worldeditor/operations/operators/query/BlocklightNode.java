package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.block.BlockFace;

public class BlocklightNode extends Node {

    NumberNode arg;

    @Override
    public BlocklightNode newNode(ParserState parserState) {
        BlocklightNode node = new BlocklightNode();
        node.arg = Parser.parseNumberNode(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,
                BlockFace.WEST};
        int light = state.getCurrentBlock().block.getLightFromBlocks();
        for (BlockFace face : faces) {
            int l = state.getCurrentBlock().block.getRelative(face).getLightFromBlocks();
            if (l > light)
                light = l;
        }
        return light >= arg.getValue(state);
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
