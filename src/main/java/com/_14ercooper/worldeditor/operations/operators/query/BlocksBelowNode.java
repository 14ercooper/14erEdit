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
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import org.bukkit.block.Block;

public class BlocksBelowNode extends Node {

    RangeNode arg1;
    Node arg2;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public BlocksBelowNode newNode(ParserState parserState) {
        BlocksBelowNode node = new BlocksBelowNode();
        try {
            node.arg1 = Parser.parseRangeNode(parserState);
            node.arg2 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating blocks below node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create blocks below node. Two arguments are required, but not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        Block currBlock = state.getCurrentWorld().getBlockAt(state.getCurrentBlock().block.getLocation());
        int x = currBlock.getX();
        int y = currBlock.getY();
        int z = currBlock.getZ();
        int min = (int) arg1.getMin(state);
        int max = (int) arg1.getMax(state);
        boolean blockRangeMet = true;

        for (int dy = y - min; dy >= y - max; dy--) {
            state.setCurrentBlock(state.getCurrentWorld().getBlockAt(x, dy, z));
            if (!(arg2.performNode(state, true)))
                blockRangeMet = false;
        }

        state.setCurrentBlock(currBlock);
        return blockRangeMet;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
