package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BonemealNode extends Node {

    @Override
    public BonemealNode newNode(ParserState parserState) {
        return new BonemealNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        Block b = state.getCurrentBlock().block;
        b.applyBoneMeal(BlockFace.UP);
        return true;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
