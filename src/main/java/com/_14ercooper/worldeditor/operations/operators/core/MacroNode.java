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

import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class MacroNode extends Node {

    StringNode arg;

    @Override
    public MacroNode newNode(ParserState parserState) {
        MacroNode node = new MacroNode();
        try {
            node.arg = Parser.parseStringNode(parserState);
            return node;
        } catch (Exception e) {
            Main.logError("Could not create macro node, no argument provided. At least one argument is required.",
                    parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        Main.logDebug("Performing macro node"); // ----
//	AsyncManager.doneOperations += (GlobalVars.blocksPerAsync * 0.5) + 1;
        return MacroLauncher.INSTANCE.launchMacro(arg.contents, state.getCurrentBlock().block.getLocation(), state);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
