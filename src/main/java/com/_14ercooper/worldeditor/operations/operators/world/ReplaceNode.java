// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


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
