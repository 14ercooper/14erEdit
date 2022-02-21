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
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class CeilingNode extends NumberNode {

    NumberNode a;

    // Returns a new node
    @Override
    public CeilingNode newNode(ParserState parserState) {
        CeilingNode node = new CeilingNode();
        node.a = Parser.parseNumberNode(parserState);
        return node;
    }

    // Return the number
    @Override
    public double getValue(OperatorState state) {
        return Math.ceil(a.getValue(state));
    }

    @Override
    public int getInt(OperatorState state) {
        return (int) Math.round(getValue(state));
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 1;
    }
}
