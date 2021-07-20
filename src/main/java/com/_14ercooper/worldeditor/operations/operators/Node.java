package com._14ercooper.worldeditor.operations.operators;

import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import org.bukkit.command.CommandSender;

public abstract class Node {

    public boolean isNextNodeRange() {
        return false;
    }

    public boolean isNextNodeBlock() {
        return false;
    }

    public abstract Node newNode(ParserState parserState);

    public abstract boolean performNode(OperatorState state);

    public abstract int getArgCount();
}
