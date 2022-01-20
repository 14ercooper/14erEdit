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

package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;

import java.util.Arrays;
import java.util.LinkedList;

public class CraftscriptNode extends Node {

    public StringNode arg;

    @Override
    public CraftscriptNode newNode(ParserState parserState) {
        try {
            CraftscriptNode node = new CraftscriptNode();
            node.arg = Parser.parseStringNode(parserState);
            return node;
        } catch (Exception e) {
            Main.logError("Error parsing craftscript. Operator requires an argument containing a script.",
                    parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            String label = arg.contents.split("\\{")[0];
            LinkedList<String> args = new LinkedList<>(
                    Arrays.asList(arg.contents.split("\\{")[1].replace("}", "").split(",")));
            return CraftscriptManager.INSTANCE.runCraftscript(label, args, state.getCurrentPlayer());
        } catch (Exception e) {
            Main.logError(
                    "Could not parse craftscript. Is your input formatted correctly, with arguments contained in {}?",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
