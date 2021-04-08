package com._14ercooper.worldeditor.operations.operators.core;

import org.bukkit.Material;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class StringNode extends Node {

    public String contents = "undefined";

    @Override
    public StringNode newNode() {

	return new StringNode();
    }

    @Override
    public boolean performNode() {
	try {
	    return Operator.currentBlock.getType() == Material.matchMaterial(contents);
	}
	catch (Exception e) {
	    Main.logError("Error performing string node. " + contents + " could not be resolved to a block.",
		    Operator.currentPlayer, e);
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
