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

public class OddsNode extends Node {

    public NumberNode arg;

    @Override
    public OddsNode newNode(ParserState parserState) {
        OddsNode node = new OddsNode();
        try {
            node.arg = Parser.parseNumberNode(parserState);
        } catch (Exception e) {
            Main.logError("Could not create odds node, argument is not a number.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Error creating odds node. Requires a number, but no number was found.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        double chance = Main.getRand().nextDouble() * 100.0;
        return (chance < arg.getValue(state));
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
