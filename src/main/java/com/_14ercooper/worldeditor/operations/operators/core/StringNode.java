// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.Material;

public class StringNode extends Node {

    public String contents = "undefined";

    @Override
    public StringNode newNode(ParserState parserState) {
        return new StringNode();
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        try {
            return state.getCurrentBlock().block.getType() == Material.matchMaterial(contents);
        } catch (Exception e) {
            Main.logError("Error performing string node. " + contents + " could not be resolved to a block.",
                    state.getCurrentPlayer(), e);
            return false;
        }
    }

    public String getText() {
        return contents;
    }

    @Override
    public int getArgCount() {
        return 1;
    }

}
