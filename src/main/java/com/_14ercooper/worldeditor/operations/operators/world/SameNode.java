package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;

public class SameNode extends BlockNode {

    public static final NBTExtractor nbt = new NBTExtractor();

    @Override
    public SameNode newNode(ParserState parserState) {
        return new SameNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return true;
    }

    @Override
    public boolean getBlock(OperatorState state) {
        state.getOtherValues().put("BlockMaterial", state.getCurrentBlock().block.getType().toString());
        state.getOtherValues().put("BlockData", state.getCurrentBlock().block.getBlockData().getAsString());
        state.getOtherValues().put("BlockNbt", nbt.getNBT(state.getCurrentBlock().block));
        return true;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
