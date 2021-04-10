package com._14ercooper.worldeditor.operations.operators.function;

import org.bukkit.Location;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

// This node currently accepts both 2 and 3 as dimensions using worldspace
// 4 dimensions uses worldspace plus average of the three other dimensions, creating good looking results
public class SimplexNode extends Node {

    public NumberNode arg1, arg2, scaleFactor;

    @Override
    public SimplexNode newNode() {
        SimplexNode node = new SimplexNode();
        try {
            node.arg1 = GlobalVars.operationParser.parseNumberNode();
            node.arg2 = GlobalVars.operationParser.parseNumberNode();
            node.scaleFactor = GlobalVars.operationParser.parseNumberNode();
        } catch (Exception e) {
            Main.logError("Could not create simplex node. Please check your syntax.", Operator.currentPlayer, e);
            return null;
        }
        if (node.scaleFactor == null) {
            Main.logError("Could not create simplex node. Three numbers are required, but not provided.",
                    Operator.currentPlayer, null);
        }
        return node;
    }

    @Override
    public boolean performNode() {
        try {
            // The range on all of these are useful for double inaccuracy
            double scale = 4 * scaleFactor.getValue();
            if (arg1.getValue() <= 2.1 && arg1.getValue() >= 1.9) {
                Location loc = Operator.currentBlock.getLocation();
                return GlobalVars.simplexNoise.noise(loc.getX() / scale, loc.getZ() / scale) <= arg2.getValue();
            }
            if (arg1.getValue() <= 3.1 && arg1.getValue() >= 2.9) {
                Location loc = Operator.currentBlock.getLocation();
                return GlobalVars.simplexNoise.noise(loc.getX() / scale, loc.getY() / scale, loc.getZ() / scale) <= arg2
                        .getValue();
            }
            if (arg1.getValue() <= 4.1 && arg1.getValue() >= 3.9) {
                Location loc = Operator.currentBlock.getLocation();
                return GlobalVars.simplexNoise.noise(loc.getX() / scale, loc.getY() / scale, loc.getZ() / scale,
                        (loc.getX() + loc.getY() + loc.getZ()) * 0.33333333 / scale) <= arg2.getValue();
            }
            Main.logError("Simplex in " + arg1.getValue() + " dimensions not found. Please check your simplex syntax.",
                    Operator.currentPlayer, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error performing simplex node. Please check your syntax.", Operator.currentPlayer, e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
