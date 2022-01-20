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

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import org.bukkit.block.BlockFace;

public class BlocklightNode extends Node {

    NumberNode arg;

    @Override
    public BlocklightNode newNode(ParserState parserState) {
        BlocklightNode node = new BlocklightNode();
        node.arg = Parser.parseNumberNode(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,
                BlockFace.WEST};
        int light = state.getCurrentBlock().block.getLightFromBlocks();
        for (BlockFace face : faces) {
            int l = state.getCurrentBlock().block.getRelative(face).getLightFromBlocks();
            if (l > light)
                light = l;
        }
        return light >= arg.getValue(state);
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
