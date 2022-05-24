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

package com._14ercooper.worldeditor.operations.operators.variable;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;
import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;

public class GetPlayerVariableNode extends NumberNode {

    StringNode name;

    @Override
    public GetPlayerVariableNode newNode(ParserState parserState) {
        GetPlayerVariableNode node = new GetPlayerVariableNode();
        node.name = Parser.parseStringNode(parserState);
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return !(Math.abs(getValue(state)) < 0.01);
    }

    @Override
    public double getValue(OperatorState state) {
        PlayerWrapper playerWrapper = PlayerManager.getPlayerWrapper(state.getCurrentPlayer());
        return playerWrapper.getVariable(name.getText());
    }

    @Override
    public int getInt(OperatorState state) {
        return (int) getValue(state);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
