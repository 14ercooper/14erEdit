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

package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class EveryXNode extends Node {

    public int arg1 = -1;
    public NumberNode arg2;

    @Override
    public EveryXNode newNode(ParserState parserState) {
        EveryXNode node = new EveryXNode();
        try {
            String dim = Parser.parseStringNode(parserState).contents;
            if (dim.equalsIgnoreCase("x")) {
                node.arg1 = 0;
            } else if (dim.equalsIgnoreCase("y")) {
                node.arg1 = 1;
            } else if (dim.equalsIgnoreCase("z")) {
                node.arg1 = 2;
            }
            node.arg2 = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Could not create remainder node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create remainder node. Requires an axis and a number, but these were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int base = (int) arg2.getValue(state);
        if (arg1 == 0) {
            int value = state.getCurrentBlock().block.getX();
            return Math.floorMod(value, base) == 0;
        } else if (arg1 == 1) {
            int value = state.getCurrentBlock().block.getY();
            return Math.floorMod(value, base) == 0;
        } else if (arg1 == 2) {
            int value = state.getCurrentBlock().block.getZ();
            return Math.floorMod(value, base) == 0;
        }
        Main.logError("Invalid axis provided to remainder node. Please check your syntax.", state.getCurrentPlayer(), null);
        return false;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
