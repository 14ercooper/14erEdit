// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.loop;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class WhileNode extends Node {

    public static long maxLoopLength = 5000;

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
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            boolean result = true;
            int loopsRun = 0;
            while (cond.performNode(state, true)) {
                if (loopsRun > maxLoopLength) {
                    Main.logError("Max loop length exceeded. Async queue dropped.", state.getCurrentPlayer(), null);
                    AsyncManager.dropAsync();
                    return false;
                }
                boolean result2 = op.performNode(state, true);
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
