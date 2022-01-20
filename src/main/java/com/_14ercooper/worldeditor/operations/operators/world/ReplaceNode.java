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
import com._14ercooper.worldeditor.operations.operators.logical.IfNode;

public class ReplaceNode extends Node {

    Node root = null;

    // /fx br s 5 replace gold_block|cobweb diamond_block|gravel
    @Override
    public ReplaceNode newNode(ParserState parserState) {
        ReplaceNode node = new ReplaceNode();
        try {
            String[] from = Parser.parseStringNode(parserState).getText().split("\\|");
            String[] to = Parser.parseStringNode(parserState).getText().split("\\|");
            if (from.length != to.length) {
                Main.logError("Replace node from list and to list are of uneven lengths.", parserState, null);
                return null;
            }
            for (int i = from.length - 1; i >= 0; i--) {
                Main.logDebug("Made replace from " + from[i] + " to " + to[i]);
                node.root = new IfNode().newNode(new BlockNode().newNode(from[i], parserState), new SetNode().newNode(new BlockNode().newNode(to[i], parserState)), node.root);
            }
            return node;
        } catch (Exception e) {
            Main.logError("Could not create replace node. Please check your syntax.", parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return root.performNode(state, true);
    }

    @Override
    public int getArgCount() {
        return 2;
    }

}
