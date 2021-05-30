package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;

public class NearbyNode extends Node {

    Node blockMask = null;
    NumberNode distance = null;
    RangeNode trueRange = null;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public NearbyNode newNode() {
        try {
            NearbyNode node = new NearbyNode();
            node.blockMask = GlobalVars.operationParser.parsePart();
            node.distance = GlobalVars.operationParser.parseNumberNode();
            node.trueRange = GlobalVars.operationParser.parseRangeNode();
            if (node.trueRange == null) {
                Main.logError(
                        "Could not create nearby node. Did you provide a block mask, a distance, and a range node?",
                        Operator.currentPlayer, null);
                return null;
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parsing nearby node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode() {
        int dist = (int) distance.getValue();
        int trueSeen = 0;
        for (int x = -dist; x <= dist; x++) {
            for (int y = -dist; y <= dist; y++) {
                for (int z = -dist; z <= dist; z++) {
                    Block currBlock = Operator.currentBlock;
                    Operator.currentBlock = currBlock.getRelative(x, y, z);
                    boolean isTrue = blockMask.performNode();
                    Operator.currentBlock = currBlock;
                    if (isTrue)
                        trueSeen++;
                }
            }
        }
        return trueRange.getMin() <= trueSeen && trueRange.getMax() >= trueSeen;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
