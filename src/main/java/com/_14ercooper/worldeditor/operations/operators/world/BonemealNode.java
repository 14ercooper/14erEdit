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

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BonemealNode extends Node {

    @Override
    public BonemealNode newNode(ParserState parserState) {
        return new BonemealNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        Block b = state.getCurrentBlock().block;
        b.applyBoneMeal(BlockFace.UP);
        return true;
    }

    @Override
    public int getArgCount() {
        return 0;
    }
}
