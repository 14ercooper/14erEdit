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

import com._14ercooper.worldeditor.functions.Function;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionNode extends NumberNode {

    String filename;
    final List<String> args = new ArrayList<>();

    @Override
    public FunctionNode newNode(ParserState parserState) {
        FunctionNode node = new FunctionNode();
        node.filename = Parser.parseStringNode(parserState).getText();
        int argNum;
        try {
            argNum = (int) Parser.parseNumberNode(parserState).getValue(new DummyState(parserState.getCurrentPlayer()));
        }
        catch (NullPointerException e) {
            parserState.setIndex(parserState.getIndex() - 1);
            argNum = (int) Parser.parseNumberNode(parserState).arg;
        }
        for (int i = 0; i < argNum; i++) {
            node.args.add(Parser.parseStringNode(parserState).getText());
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        Function fx = new Function(filename, args, state.getCurrentPlayer(), true, state);
        return Math.abs(fx.run()) > 0.001;
    }

    @Override
    public int getArgCount() {
        return 2;
    }

    @Override
    public double getValue(OperatorState state) {
        return getValue(state, 0);
    }

    @Override
    public double getValue(OperatorState state, double center) {
        Function fx = new Function(filename, args, state.getCurrentPlayer(), true, state);
        //	Main.logDebug("Function return: " + val);
        return fx.run();
    }

    @Override
    public int getInt(OperatorState state) {
        return getInt(state, 0);
    }

    @Override
    public int getInt(OperatorState state, int center) {
        Function fx = new Function(filename, args, state.getCurrentPlayer(), true, state);
        //	Main.logDebug("Function return: " + val);
        return (int) fx.run();
    }

}
