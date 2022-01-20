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

package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Material;

public class StringNode extends Node {

    public String contents = "undefined";

    @Override
    public StringNode newNode(ParserState parserState) {
        return new StringNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return state.getCurrentBlock().block.getType() == Material.matchMaterial(contents);
        } catch (Exception e) {
            Main.logError("Error performing string node. " + contents + " could not be resolved to a block.",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    public String getText() {
        return contents;
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
