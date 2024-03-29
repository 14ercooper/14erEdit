/**
 * This file is part of 14erEdit.
 * 
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.operations.operators.query;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class AngleNode extends Node {

    RangeNode angleForTrue = null;
    NumberNode distance = null;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public Node newNode(ParserState parserState) {
        try {
            AngleNode node = new AngleNode();
            node.angleForTrue = Parser.parseRangeNode(parserState);
            node.distance = Parser.parseNumberNode(parserState);
            if (node.distance == null) {
                Main.logError("Could not parse angle node. Did you provide a range node and a distance?",
                        parserState, null);
                return null;
            }
            return node;
        } catch (Exception e) {
            Main.logError("Error parsing range node. Please check your syntax.", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        // Get angle from each block pair
        int dist = (int) distance.getValue(state);
        int maxAngle = getAngle(state.getCurrentBlock().block.getRelative(dist, 0, 0),
                state.getCurrentBlock().block.getRelative(-dist, 0, 0), state);
        int angle = getAngle(state.getCurrentBlock().block.getRelative(0, 0, dist),
                state.getCurrentBlock().block.getRelative(0, 0, -dist), state);
        if (angle > maxAngle)
            maxAngle = angle;
        angle = getAngle(state.getCurrentBlock().block.getRelative((int) (dist * 0.707), 0, (int) (dist * 0.707)),
                state.getCurrentBlock().block.getRelative((int) (-dist * 0.707), 0, (int) (-dist * 0.707)), state);
        if (angle > maxAngle)
            maxAngle = angle;
        angle = getAngle(state.getCurrentBlock().block.getRelative((int) (dist * 0.707), 0, (int) (-dist * 0.707)),
                state.getCurrentBlock().block.getRelative((int) (-dist * 0.707), 0, (int) (dist * 0.707)), state);
        if (angle > maxAngle)
            maxAngle = angle;
        // Return if max angle found is in range
        return angleForTrue.getMin(state) <= maxAngle && angleForTrue.getMax(state) >= maxAngle;
    }

    @Override
    public int getArgCount() {
        return 2;
    }

    private int getAngle(Block b1, Block b2, OperatorState state) {
        // Check current states both blocks
        Material mat1 = b1.getType();
        Material mat2 = b2.getType();

        PlayerWrapper playerWrapper = PlayerManager.INSTANCE.getPlayerWrapper(state.getCurrentPlayer());

        // Both solid
        if (!playerWrapper.getBrushMask().contains(mat1) && !playerWrapper.getBrushMask().contains(mat2)) {
            return 0;
        }
        // Neither solid
        else if (playerWrapper.getBrushMask().contains(mat1) && playerWrapper.getBrushMask().contains(mat1)) {
            return 0;
        }
        // One solid
        else {
            if (playerWrapper.getBrushMask().contains(mat2)) {
                Block temp = b2;
                b2 = b1;
                b1 = temp;
            }
            int downVal = 0;
            while (playerWrapper.getBrushMask().contains(b1.getRelative(0, downVal, 0).getType())) {
                downVal--;
            }
            int upVal = 0;
            while (!playerWrapper.getBrushMask().contains(b2.getRelative(0, upVal, 0).getType())) {
                upVal++;
            }
            double distVert = upVal - downVal;
            double distHor = 2 * distance.getValue(state);
            return (int) Math.abs(Math.atan2(distVert, distHor) * 57.296);
        }
    }
}
