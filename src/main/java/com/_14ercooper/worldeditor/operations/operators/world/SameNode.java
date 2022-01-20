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

package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;

public class SameNode extends BlockNode {

    public static final NBTExtractor nbt = new NBTExtractor();

    @Override
    public SameNode newNode(ParserState parserState) {
        return new SameNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return true;
    }

    @Override
    public boolean getBlock(OperatorState state) {
        state.getOtherValues().put("BlockMaterial", state.getCurrentBlock().block.getType().toString());
        state.getOtherValues().put("BlockData", state.getCurrentBlock().block.getBlockData().getAsString());
        state.getOtherValues().put("BlockNbt", nbt.getNBT(state.getCurrentBlock().block));
        return true;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
