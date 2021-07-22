package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;

public class MacroNode extends Node {

    StringNode arg;

    @Override
    public MacroNode newNode(ParserState parserState) {
        MacroNode node = new MacroNode();
        try {
            node.arg = Parser.parseStringNode(parserState);
            return node;
        } catch (Exception e) {
            Main.logError("Could not create macro node, no argument provided. At least one argument is required.",
                    parserState, e);
            return null;
        }
    }

    @Override
    public boolean performNode(OperatorState state) {
        Main.logDebug("Performing macro node"); // ----
//	AsyncManager.doneOperations += (GlobalVars.blocksPerAsync * 0.5) + 1;
        return MacroLauncher.INSTANCE.launchMacro(arg.contents, state.getCurrentBlock().block.getLocation(), state.getCurrentUndo(), state);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
