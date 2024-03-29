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

package com._14ercooper.worldeditor.operations.operators.fun;

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.function.RangeNode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class SmallRuinNode extends Node {

    Node block;
    RangeNode stackSize;
    RangeNode xMax, zMax;

    @Override
    public SmallRuinNode newNode(ParserState parserState) {
        SmallRuinNode node = new SmallRuinNode();
        node.xMax = Parser.parseRangeNode(parserState);
        node.zMax = Parser.parseRangeNode(parserState);
        node.stackSize = Parser.parseRangeNode(parserState);
        node.block = Parser.parsePart(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int xSize = Main.randRange((int) xMax.getMin(state), (int) xMax.getMax(state));
        int zSize = Main.randRange((int) zMax.getMin(state), (int) zMax.getMax(state));

        int stackCount = Main.randRange((int) stackSize.getMin(state), (int) stackSize.getMax(state));

        BlockWrapper savedBlock = state.getCurrentBlock();

        for (int ruinNum = 0; ruinNum < stackCount; ruinNum++) {
            Block currBlock = savedBlock.block.getRelative(BlockFace.UP, 4 * ruinNum);

            // Create base including fill
            for (int xO = -xSize; xO <= xSize; xO++) {
                for (int zO = -zSize; zO <= zSize; zO++) {
                    Block currBlockOffset = currBlock.getRelative(xO, 0, zO);
                    // Set block
                    if (currBlockOffset.getType() == Material.AIR) {
                        state.setCurrentBlock(currBlockOffset);
                        block.performNode(state, true);
                    } else {
                        if (Main.getRand().nextBoolean()) {
                            state.setCurrentBlock(currBlockOffset);
                            block.performNode(state, true);
                        }
                    }

                    // Do base fill on bottom layer - 1 indexed due to initial offset
                    if (ruinNum == 0) {
                        for (int i = 1; i < 4; i++) {
                            state.setCurrentBlock(currBlockOffset.getRelative(BlockFace.DOWN, i));
                            if (state.getCurrentBlock().block.getType() == Material.AIR)
                                i--;
                            block.performNode(state, true);
                        }
                    }
                }
            }

            // Create walls
            for (int xO = -xSize; xO <= xSize; xO++) {
                for (int zO = -zSize; zO <= zSize; zO++) {
                    if (xO == -xSize || xO == xSize || zO == -zSize || zO == zSize) {
                        for (int i = 0; i < 5; i++) {
                            if (Main.getRand().nextInt(5) == 0)
                                break;
                            state.setCurrentBlock(currBlock.getRelative(xO, i, zO));
                            block.performNode(state, true);
                        }
                    }
                }
            }
        }

        // Clean up and return
        state.setCurrentBlock(savedBlock);
        return true;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
