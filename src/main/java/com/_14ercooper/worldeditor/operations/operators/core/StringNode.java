package com._14ercooper.worldeditor.operations.operators.core;

import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Material;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import org.bukkit.command.CommandSender;

public class StringNode extends Node {

    public String contents = "undefined";

    @Override
    public StringNode newNode(CommandSender currentPlayer) {
        return new StringNode();
    }

    @Override
    public boolean performNode(OperatorState state) {
        try {
            return state.getCurrentBlock().getType() == Material.matchMaterial(contents);
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
