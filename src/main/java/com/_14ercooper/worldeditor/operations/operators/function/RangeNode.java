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

public class RangeNode extends Node {

    public NumberNode arg1, arg2;

    @Override
    public RangeNode newNode(ParserState parserState) {
        RangeNode node = new RangeNode();
        try {
            node.arg1 = Parser.parseNumberNode(parserState);
            node.arg2 = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError(
                    "Could not create range node. Please check your syntax (did you remember to create a range node?).",
                    parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create range node. Two numbers were expected, but not provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return false;
    }

    public double getMin(OperatorState state) {
        return arg1.getValue(state);
    }

    public double getMax(OperatorState state) {
        return arg2.getValue(state);
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
