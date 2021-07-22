package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import org.bukkit.block.Block;

public class NearbyNode extends Node {

    Node blockMask = null;
    NumberNode distance = null;
    RangeNode trueRange = null;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public NearbyNode newNode(ParserState parserState) {
        try {
            NearbyNode node = new NearbyNode();
            node.trueRange = Parser.parseRangeNode(parserState);
            node.blockMask = Parser.parsePart(parserState);
            node.distance = Parser.parseNumberNode(parserState);
            if (node.trueRange == null) {
                Main.logError(
                        "Could not create nearby node. Did you provide a block mask, a distance, and a range node?",
                        parserState, null);
                return null;
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parsing nearby node. Please check your syntax.", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        int dist = (int) distance.getValue(state);
        int trueSeen = 0;
        for (int x = -dist; x <= dist; x++) {
            for (int y = -dist; y <= dist; y++) {
                for (int z = -dist; z <= dist; z++) {
                    Block currBlock = state.getCurrentBlock().block;
                    state.setCurrentBlock(currBlock.getRelative(x, y, z));
                    boolean isTrue = blockMask.performNode(state);
                    state.setCurrentBlock(currBlock);
                    if (isTrue)
                        trueSeen++;
                }
            }
        }
        return trueRange.getMin(state) <= trueSeen && trueRange.getMax(state) >= trueSeen;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
