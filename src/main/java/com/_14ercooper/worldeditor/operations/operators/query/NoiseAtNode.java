package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;

public class NoiseAtNode extends Node {

    NoiseNode noise;
    NumberNode midplane, amplitude;
    Node function;

    @Override
    public NoiseAtNode newNode(ParserState parserState) {
        NoiseAtNode node = new NoiseAtNode();
        node.noise = (NoiseNode) Parser.parsePart(parserState);
        node.midplane = Parser.parseNumberNode(parserState);
        node.amplitude = Parser.parseNumberNode(parserState);
        node.function = Parser.parsePart(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        BlockWrapper b = state.getCurrentBlock();
        int x = b.block.getX();
        int z = b.block.getZ();
        int y;
        if (midplane.getValue(state) < 0) {
            y = (int) ((noise.getNum(state) * amplitude.getValue(state)) + b.block.getY());
        } else {
            y = (int) ((noise.getNum(state) * amplitude.getValue(state)) + midplane.getValue(state));
        }
        state.setCurrentBlock(state.getCurrentWorld().getBlockAt(x, y, z));
        boolean result = function.performNode(state, true);
        state.setCurrentBlock(b);
        return result;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
