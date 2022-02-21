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

package com._14ercooper.worldeditor.operations.operators.math;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class BetweenNode extends Node {

    NumberNode min, max, val;

    // Returns a new node
    @Override
    public BetweenNode newNode(ParserState parserState) {
        BetweenNode node = new BetweenNode();
        node.min = Parser.parseNumberNode(parserState);
        node.max = Parser.parseNumberNode(parserState);
        node.val = Parser.parseNumberNode(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        double minVal = min.getValue(state);
        double maxVal = max.getValue(state);
        double valVal = val.getValue(state);
        return valVal > minVal && valVal < maxVal;
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 2;
    }
}
