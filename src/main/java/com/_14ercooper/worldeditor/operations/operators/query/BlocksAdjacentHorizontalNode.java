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
import org.bukkit.block.BlockFace;

public class BlocksAdjacentHorizontalNode extends Node {

    public Node arg1;
    public RangeNode arg2;

    @Override
    public boolean isNextNodeRange() {
        return true;
    }

    @Override
    public BlocksAdjacentHorizontalNode newNode(ParserState parserState) {
        BlocksAdjacentHorizontalNode node = new BlocksAdjacentHorizontalNode();
        try {
            node.arg2 = Parser.parseRangeNode(parserState);
            node.arg1 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating blocks adjacent node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not parse blocks adjacent node. Two arguments are required, but not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        Main.logDebug("Performing faces exposed node"); // -----

        // Check if any adjacent blocks match arg1
        // Set up some variables
        int numAdjacentBlocks = 0;
        Block curBlock = state.getCurrentWorld().getBlockAt(state.getCurrentBlock().block.getLocation());

        // Check each direction
        Block blockAdj = curBlock.getRelative(BlockFace.NORTH);
        state.setCurrentBlock(blockAdj);
        if (arg1.performNode(state, true))
            numAdjacentBlocks++;
        blockAdj = curBlock.getRelative(BlockFace.SOUTH);
        state.setCurrentBlock(blockAdj);
        if (arg1.performNode(state, true))
            numAdjacentBlocks++;
        blockAdj = curBlock.getRelative(BlockFace.EAST);
        state.setCurrentBlock(blockAdj);
        if (arg1.performNode(state, true))
            numAdjacentBlocks++;
        blockAdj = curBlock.getRelative(BlockFace.WEST);
        state.setCurrentBlock(blockAdj);
        if (arg1.performNode(state, true))
            numAdjacentBlocks++;

        // Reset the current block
        state.setCurrentBlock(curBlock);

        return (numAdjacentBlocks >= arg2.getMin(state) - 0.1 && numAdjacentBlocks <= arg2.getMax(state) + 0.1);
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
