package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

import java.util.Arrays;
import java.util.LinkedList;

public class CraftscriptNode extends Node {

    public StringNode arg;

    @Override
    public CraftscriptNode newNode(ParserState parserState) {
        try {
            CraftscriptNode node = new CraftscriptNode();
            node.arg = Parser.parseStringNode(parserState);
            return node;
        } catch (Exception e) {
            Main.logError("Error parsing craftscript. Operator requires an argument containing a script.",
                    parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            String label = arg.contents.split("\\{")[0];
            LinkedList<String> args = new LinkedList<>(
                    Arrays.asList(arg.contents.split("\\{")[1].replace("}", "").split(",")));
            return GlobalVars.scriptManager.runCraftscript(label, args, state.getCurrentPlayer());
        } catch (Exception e) {
            Main.logError(
                    "Could not parse craftscript. Is your input formatted correctly, with arguments contained in {}?",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
