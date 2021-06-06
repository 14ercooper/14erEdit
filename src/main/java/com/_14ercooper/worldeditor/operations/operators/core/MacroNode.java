package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class MacroNode extends Node {

    StringNode arg;

    @Override
    public MacroNode newNode() {
        MacroNode node = new MacroNode();
        try {
            node.arg = GlobalVars.operationParser.parseStringNode();
            return node;
        } catch (Exception e) {
            Main.logError("Could not create macro node, no argument provided. At least one argument is required.",
                    Operator.currentPlayer, e);
            return null;
        }
    }

    @Override
    public boolean performNode() {
        Main.logDebug("Performing macro node"); // ----
//	AsyncManager.doneOperations += (GlobalVars.blocksPerAsync * 0.5) + 1;
        return GlobalVars.macroLauncher.launchMacro(arg.contents, Operator.currentBlock.getLocation(), Operator.currentUndo);
    }

    @Override
    public int getArgCount() {
        return 1;
    }
}
