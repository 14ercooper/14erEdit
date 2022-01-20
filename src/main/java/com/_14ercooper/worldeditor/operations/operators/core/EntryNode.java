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

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class EntryNode {
    public Node node;
    public final int consumedArgs;

    public EntryNode(Node newNode, int consumedArgs) {
        node = newNode;
        this.consumedArgs = consumedArgs;
    }

    public boolean performNode(OperatorState state) {
        try {
            return node.performNode(state, true);
        } catch (Exception e) {
            Main.logError("Error performing node. Async queue dropped.", state.getCurrentPlayer(), e);
            e.printStackTrace();
            AsyncManager.dropAsync();
            return false;
        }
    }
}
