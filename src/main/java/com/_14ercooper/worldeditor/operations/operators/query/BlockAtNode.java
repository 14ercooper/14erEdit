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

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.world.BlockNode;

public class BlockAtNode extends BlockNode {

    NumberNode x, y, z;
    boolean xA = false, yA = false, zA = false;
    Node node;
    private static final NBTExtractor nbt = new NBTExtractor();

    @Override
    public BlockAtNode newNode(ParserState parserState) {
        BlockAtNode baNode = new BlockAtNode();
        try {
            baNode.x = Parser.parseNumberNode(parserState);
            baNode.y = Parser.parseNumberNode(parserState);
            baNode.z = Parser.parseNumberNode(parserState);
            baNode.xA = baNode.x.isAbsolute;
            baNode.yA = baNode.y.isAbsolute;
            baNode.zA = baNode.z.isAbsolute;
            try {
                baNode.node = Parser.parsePart(parserState);
            } catch (Exception e) {
                Main.logDebug("Block at created with type blocknode");
            }
        } catch (Exception e) {
            Main.logError("Error creating block at node. Please check your syntax.", parserState, e);
            return null;
        }
        if (baNode.z == null) {
            Main.logError(
                    "Could not parse block at node. Three numbers and optionally an operation are required, but not given.",
                    parserState, null);
        }
        return baNode;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            BlockWrapper currBlock = state.getCurrentBlock();
            xA = x.isAbsolute;
            yA = y.isAbsolute;
            zA = z.isAbsolute;
            state.setCurrentBlock(state.getCurrentWorld().getBlockAt(
                    x.getInt(state) + (xA ? 0 : currBlock.block.getX()), y.getInt(state) + (yA ? 0 : currBlock.block.getY()),
                    z.getInt(state) + (zA ? 0 : currBlock.block.getZ())));
            boolean matches = node.performNode(state, true);
            state.setCurrentBlock(currBlock);
            return matches;
        } catch (Exception e) {
            Main.logError("Error performing block at node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    // Return the material this node references
    int xV, yV, zV;

    @Override
    public boolean getBlock(OperatorState state) {
        xV = x.getInt(state);
        yV = y.getInt(state);
        zV = z.getInt(state);
        xA = x.isAbsolute;
        yA = y.isAbsolute;
        zA = z.isAbsolute;
        state.getOtherValues().put("BlockMaterial", state.getCurrentWorld().getBlockAt(xV + (xA ? 0 : state.getCurrentBlock().block.getX()),
                yV + (yA ? 0 : state.getCurrentBlock().block.getY()), zV + (zA ? 0 : state.getCurrentBlock().block.getZ()))
                .toString());
        state.getOtherValues().put("BlockData", state.getCurrentWorld().getBlockAt(xV + (xA ? 0 : state.getCurrentBlock().block.getX()),
                yV + (yA ? 0 : state.getCurrentBlock().block.getY()), zV + (zA ? 0 : state.getCurrentBlock().block.getZ()))
                .toString());
        state.getOtherValues().put("BlockNbt", nbt.getNBT(state.getCurrentWorld().getBlockAt(xV + (xA ? 0 : state.getCurrentBlock().block.getX()),
                yV + (yA ? 0 : state.getCurrentBlock().block.getY()), zV + (zA ? 0 : state.getCurrentBlock().block.getZ()))));
        return true;
    }

    @Override
    public int getArgCount() {
        return 4;
    }

}
