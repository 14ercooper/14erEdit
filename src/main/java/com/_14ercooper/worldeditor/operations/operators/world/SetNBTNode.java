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
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Bukkit;

public class SetNBTNode extends Node {

    String nbt;

    @Override
    public SetNBTNode newNode(ParserState parserState) {
        SetNBTNode node = new SetNBTNode();
        try {
            node.nbt = Parser.parseStringNode(parserState).contents;
        } catch (Exception e) {
            Main.logError("Error creating set NBT node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.nbt.isEmpty()) {
            Main.logError("Could not parse set NBT node. Requires NBT, but did not find nay.", parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            String command = "data merge block ";
            command += state.getCurrentBlock().block.getLocation().getBlockX() + " ";
            command += state.getCurrentBlock().block.getLocation().getBlockY() + " ";
            command += state.getCurrentBlock().block.getLocation().getBlockZ() + " ";
            command += nbt.replaceAll("_", " ");
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            return true;
        } catch (Exception e) {
            Main.logError("Error performing set NBT node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
