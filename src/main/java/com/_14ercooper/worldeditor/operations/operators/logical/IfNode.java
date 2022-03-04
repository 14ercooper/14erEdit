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

public class IfNode extends Node {

    public Node arg1, arg2, arg3;

    @Override
    public IfNode newNode(ParserState parserState) {
        IfNode node = new IfNode();
        try {
            node.arg1 = Parser.parsePart(parserState);
            node.arg2 = Parser.parsePart(parserState);

            int iter = parserState.getIndex();
            node.arg3 = Parser.parsePart(parserState);
            if (!(node.arg3 instanceof ElseNode)) {
                Main.logDebug("Did not find an instance of an else node.");
                node.arg3 = null;
                parserState.setIndex(iter);
            }
        } catch (Exception e) {
            Main.logError("Error creating if node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg2 == null) {
            Main.logError(
                    "Error creating if node. At least a condition and on-true operator are required, but are not provided.",
                    parserState, null);
        }
        return node;
    }

    public IfNode newNode(Node cond, Node onTrue, Node onFalse) {
        IfNode node = new IfNode();
        node.arg1 = cond;
        node.arg2 = onTrue;
        node.arg3 = onFalse;
        return node;
    }

    // /fx br v if bedrock if both simplex 3 130 4 not simplex 3 110 4 set
    // polished_andesite else if simplex 3 110 4 set
    // 70%andesite;10%gravel;10%stone;10%cobblestone
    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            boolean isTrue = arg1.performNode(state, true);
            if (isTrue) {
//		Main.logDebug("condition true");
                arg2.performNode(state, true);
                return true;
            } else if (arg3 == null) {
//		Main.logDebug("no else");
                return false;
            } else {
//		Main.logDebug("else");
                arg3.performNode(state, true);
                return false;
            }
        } catch (Exception e) {
            Main.logError("Error performing if node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 3;
    }
}
