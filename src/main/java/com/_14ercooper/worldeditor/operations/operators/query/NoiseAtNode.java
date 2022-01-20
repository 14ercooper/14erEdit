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

import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.function.NoiseNode;

public class NoiseAtNode extends Node {

    NoiseNode noise;
    NumberNode midplane, amplitude;
    Node function;

    @Override
    public NoiseAtNode newNode(ParserState parserState) {
        NoiseAtNode node = new NoiseAtNode();
        node.noise = (NoiseNode) Parser.parsePart(parserState);
        node.midplane = Parser.parseNumberNode(parserState);
        node.amplitude = Parser.parseNumberNode(parserState);
        node.function = Parser.parsePart(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        BlockWrapper b = state.getCurrentBlock();
        int x = b.block.getX();
        int z = b.block.getZ();
        int y;
        if (midplane.getValue(state) < 0) {
            y = (int) ((noise.getNum(state) * amplitude.getValue(state)) + b.block.getY());
        } else {
            y = (int) ((noise.getNum(state) * amplitude.getValue(state)) + midplane.getValue(state));
        }
        state.setCurrentBlock(state.getCurrentWorld().getBlockAt(x, y, z));
        boolean result = function.performNode(state, true);
        state.setCurrentBlock(b);
        return result;
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
