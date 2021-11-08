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
