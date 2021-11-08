package com._14ercooper.worldeditor.operations.operators.logical;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

import java.util.ArrayList;
import java.util.List;

public class AnyOfNode extends Node {

    NumberNode count, total;
    final List<Node> conditions = new ArrayList<>();

    @Override
    public AnyOfNode newNode(ParserState parserState) {
        AnyOfNode node = new AnyOfNode();
        try {
            node.count = Parser.parseNumberNode(parserState);
            node.total = Parser.parseNumberNode(parserState);

            for (int i = 0; i < node.total.getInt(new DummyState(parserState.getCurrentPlayer())); i++) {
                node.conditions.add(Parser.parsePart(parserState));
            }
        } catch (Exception e) {
            Main.logError("Error creating and node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.conditions.size() != node.total.getInt(new DummyState(parserState.getCurrentPlayer()))) {
            Main.logError("Could not create AnyOf node. Did you provide enough arguments?", parserState, null);
            return null;
        }
        return node;
    }

    // /fx br s 5 if anyof 1 2 orange_concrete white_concrete set stone
    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        int trueCount = 0;

        for (Node condition : conditions) {
            boolean isTrue = condition.performNode(state, true);
            if (isTrue)
                trueCount++;

            if (trueCount == count.getInt(state))
                return true;
        }

        return false;
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
