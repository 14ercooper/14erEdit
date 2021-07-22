package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SimplexNoise;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.Location;

// This node currently accepts both 2 and 3 as dimensions using worldspace
// 4 dimensions uses worldspace plus average of the three other dimensions, creating good looking results
public class SimplexNode extends Node {

    public NumberNode arg1, arg2, scaleFactor;

    @Override
    public SimplexNode newNode(ParserState parserState) {
        SimplexNode node = new SimplexNode();
        try {
            node.arg1 = Parser.parseNumberNode(parserState);
            node.arg2 = Parser.parseNumberNode(parserState);
            node.scaleFactor = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Could not create simplex node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.scaleFactor == null) {
            Main.logError("Could not create simplex node. Three numbers are required, but not provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            // The range on all of these are useful for double inaccuracy
            double scale = 4 * scaleFactor.getValue(state);
            if (arg1.getValue(state) <= 2.1 && arg1.getValue(state) >= 1.9) {
                Location loc = state.getCurrentBlock().block.getLocation();
                return SimplexNoise.simplexNoise.noise(loc.getX() / scale, loc.getZ() / scale) <= arg2.getValue(state);
            }
            if (arg1.getValue(state) <= 3.1 && arg1.getValue(state) >= 2.9) {
                Location loc = state.getCurrentBlock().block.getLocation();
                return SimplexNoise.simplexNoise.noise(loc.getX() / scale, loc.getY() / scale, loc.getZ() / scale) <= arg2
                        .getValue(state);
            }
            if (arg1.getValue(state) <= 4.1 && arg1.getValue(state) >= 3.9) {
                Location loc = state.getCurrentBlock().block.getLocation();
                return SimplexNoise.simplexNoise.noise(loc.getX() / scale, loc.getY() / scale, loc.getZ() / scale,
                        (loc.getX() + loc.getY() + loc.getZ()) * 0.33333333 / scale) <= arg2.getValue(state);
            }
            Main.logError("Simplex in " + arg1.getValue(state) + " dimensions not found. Please check your simplex syntax.",
                    state.getCurrentPlayer(), null);
            return false;
        } catch (Exception e) {
            Main.logError("Error performing simplex node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
