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

package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class AndNode extends Node {

    public Node arg1, arg2;

    @Override
    public AndNode newNode(ParserState parserState) {
        AndNode node = new AndNode();
        try {
            node.arg1 = Parser.parsePart(parserState);
            node.arg2 = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating and node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError("Could not create and node. Two arguments are required, but were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return ((arg1.performNode(state, true)) && (arg2.performNode(state, true)));
        } catch (Exception e) {
            Main.logError("Error performing and node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
