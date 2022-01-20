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

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class GravityNode extends Node {

    @Override
    public GravityNode newNode(ParserState parserState) {
        return new GravityNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            Material mat = state.getCurrentBlock().block.getType();
            Block b = state.getCurrentBlock().block;

            PlayerWrapper playerWrapper = PlayerManager.INSTANCE.getPlayerWrapper(state.getCurrentPlayer());

            while (b.getX() != 0 && playerWrapper.getBrushMask().contains(b.getRelative(BlockFace.DOWN).getType())) {
                b = b.getRelative(BlockFace.DOWN);
            }
            b.setType(mat);
            state.getCurrentBlock().block.setType(Material.AIR);
            return true;
        } catch (Exception e) {
            Main.logError("Error performing gravity node. Please check your syntax (or tell 14er how you got here).",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 0;
    }

}
