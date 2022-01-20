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
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.ArrayList;
import java.util.List;

public class MultiNoiseNode extends Node {

    NumberNode noiseCount;
    NoiseNode noiseFunction;
    final List<Node> noises = new ArrayList<>();

    @Override
    public MultiNoiseNode newNode(ParserState parserState) {
        try {
            MultiNoiseNode node = new MultiNoiseNode();

            try {
                node.noiseCount = Parser.parseNumberNode(parserState);
            } catch (Exception e) {
                Main.logError("Multinoise node expected first argument to be a number, but it was not.",
                        parserState, e);
                return null;
            }

            try {
                node.noiseFunction = (NoiseNode) Parser.parsePart(parserState);
            } catch (Exception e) {
                Main.logError("Multinoise node requires a noise node, but none was found.", parserState, e);
                return null;
            }

            try {
                for (int i = 0; i < node.noiseCount.getMaxInt(new DummyState(parserState.getCurrentPlayer())); i++) {
                    node.noises.add(Parser.parsePart(parserState));
                }
            } catch (Exception e) {
                Main.logError("Error parsing nodes for multinoise, likely ran out of nodes.", parserState, e);
                return null;
            }

            return node;
        } catch (Exception e) {
            Main.logError("Could not create multinoise node. Are you missing an argument?", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        // Get the noise value and the value of the noise count
        int c = noiseCount.getInt(state) + 1;
        float n = NoiseNode.scaleTo255(noiseFunction.getNum(state));
        n = n * sigmoid(n / 127.5f);

        // Collapse to [0,c-1] range
        float cInv = (float) c / 255.0f;
        float nColl = n * cInv;
        int nodeToRun = (int) (nColl);

        if (nodeToRun >= noises.size()) {
            nodeToRun = noises.size() - 1;
        }

        // Perform correct node
        return noises.get(nodeToRun).performNode(state, true);
    }

    private float sigmoid(float val) {
        return (float) (1.0f / (1 + Math.exp(-2.0f * (val - 1.0f))));
    }

    @Override
    public int getArgCount() {
        return 3;
    }

}
