// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;

public class YNode extends NumberNode {

    // Returns a new node
    @Override
    public YNode newNode(ParserState parserState) {
        return new YNode();
    }

    // Return the number
    @Override
    public double getValue(OperatorState state) {
        return state.getCurrentBlock().y;
    }

    @Override
    public int getInt(OperatorState state) {
        return state.getCurrentBlock().y;
    }

    // Get how many arguments this type of node takes
    @Override
    public int getArgCount() {
        return 0;
    }

}
