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

package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class WhileNode extends Node {

    public static long maxLoopLength = 5000;

    Node cond, op;

    @Override
    public WhileNode newNode(ParserState parserState) {
        WhileNode node = new WhileNode();
        try {
            node.cond = Parser.parsePart(parserState);
            node.op = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating while node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.op == null) {
            Main.logError("Could not create while node. Requires two arguments, but were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            boolean result = true;
            int loopsRun = 0;
            while (cond.performNode(state, true)) {
                if (loopsRun > maxLoopLength) {
                    Main.logError("Max loop length exceeded. Async queue dropped.", state.getCurrentPlayer(), null);
                    AsyncManager.dropAsync();
                    return false;
                }
                boolean result2 = op.performNode(state, true);
                result = result && result2;
                loopsRun++;
            }
            return result;
        } catch (Exception e) {
            Main.logError("Error performing while node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
