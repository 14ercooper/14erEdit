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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class FacesExposedNode extends Node {

    public NumberNode arg;

    @Override
    public FacesExposedNode newNode(ParserState parserState) {
        FacesExposedNode node = new FacesExposedNode();
        try {
            node.arg = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Error creating faces exposed node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Could not create faces exposed node. Requires a number argument that was not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {

        // Count the number of faces
        // Basically check for air in each of the four directions
        int faceCount = 0;
        Block b = state.getCurrentWorld().getBlockAt(state.getCurrentBlock().block.getLocation());
        if (b.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
            faceCount++;
        }
        if (b.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
            faceCount++;
        }
        if (b.getRelative(BlockFace.EAST).getType() == Material.AIR) {
            faceCount++;
        }
        if (b.getRelative(BlockFace.WEST).getType() == Material.AIR) {
            faceCount++;
        }
        if (b.getRelative(BlockFace.UP).getType() == Material.AIR) {
            faceCount++;
        }
        if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            faceCount++;
        }

        // Perform the node
        return (faceCount >= arg.getValue(state) - 0.1);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
