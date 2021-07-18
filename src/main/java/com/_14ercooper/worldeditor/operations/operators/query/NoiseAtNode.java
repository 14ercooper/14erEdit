package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

public class NoiseAtNode extends Node {

    NoiseNode noise;
    NumberNode midplane, amplitude;
    Node function;

    @Override
    public NoiseAtNode newNode(CommandSender currentPlayer) {
        NoiseAtNode node = new NoiseAtNode();
        node.noise = (NoiseNode) GlobalVars.operationParser.parsePart(currentPlayer);
        node.midplane = GlobalVars.operationParser.parseNumberNode(currentPlayer);
        node.amplitude = GlobalVars.operationParser.parseNumberNode(currentPlayer);
        node.function = GlobalVars.operationParser.parsePart(currentPlayer);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        Block b = state.getCurrentBlock();
        int x = b.getX();
        int z = b.getZ();
        int y;
        if (midplane.getValue(state) < 0) {
            y = (int) ((noise.getNum(state) * amplitude.getValue(state)) + b.getY());
        } else {
            y = (int) ((noise.getNum(state) * amplitude.getValue(state)) + midplane.getValue(state));
        }
        state.setCurrentBlock(state.getCurrentWorld().getBlockAt(x, y, z));
        boolean result = function.performNode(state);
        state.setCurrentBlock(b);
        return result;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
