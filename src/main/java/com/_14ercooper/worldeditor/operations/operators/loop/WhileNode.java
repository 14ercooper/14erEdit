package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class WhileNode extends Node {

    Node cond, op;

    @Override
    public WhileNode newNode(ParserState parserState) {
        WhileNode node = new WhileNode();
        try {
            node.cond = Parser.parsePart(parserState);
            node.op = Parser.parsePart(parserState);
        } catch (Exception e) {
            Main.logError("Error creating while node. Please check your syntax.", parserState, e);
            return null;
        }
        if (node.op == null) {
            Main.logError("Could not create while node. Requires two arguments, but were not given.",
                    parserState, null);
        }
        return node;
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            boolean result = true;
            int loopsRun = 0;
            while (cond.performNode(state)) {
                if (loopsRun > GlobalVars.maxLoopLength) {
                    Main.logError("Max loop length exceeded. Async queue dropped.", state.getCurrentPlayer(), null);
                    AsyncManager.dropAsync();
                    return false;
                }
                boolean result2 = op.performNode(state);
                result = result && result2;
                loopsRun++;
            }
            return result;
        } catch (Exception e) {
            Main.logError("Error performing while node. Please check your syntax.", state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 2;
    }
}
