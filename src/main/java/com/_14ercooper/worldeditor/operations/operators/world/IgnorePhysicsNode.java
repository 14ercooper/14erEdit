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

public class IgnorePhysicsNode extends Node {

    public Node arg;

    @Override
    public IgnorePhysicsNode newNode(ParserState parserState) {
        IgnorePhysicsNode node = new IgnorePhysicsNode();
        try {
            node.arg = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating physics node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Could not parse physics node. Requires an operation, but none was provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            state.setIgnoringPhysics(!state.getIgnoringPhysics());
            boolean output = arg.performNode(state, true);
            state.setIgnoringPhysics(!state.getIgnoringPhysics());
            return output;
        } catch (Exception e) {
            Main.logError("Error performing physics node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
