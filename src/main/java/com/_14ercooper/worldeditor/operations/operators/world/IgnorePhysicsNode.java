package com._14ercooper.worldeditor.operations.operators.world;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class IgnorePhysicsNode extends Node {

    public Node arg;

    @Override
    public IgnorePhysicsNode newNode(ParserState parserState) {
        IgnorePhysicsNode node = new IgnorePhysicsNode();
        try {
            node.arg = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating physics node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.arg == null) {
            Main.logError("Could not parse physics node. Requires an operation, but none was provided.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            state.setIgnoringPhysics(!state.getIgnoringPhysics());
            boolean output = arg.performNode(state);
            state.setIgnoringPhysics(!state.getIgnoringPhysics());
            return output;
        } catch (Exception e) {
            Main.logError("Error performing physics node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
