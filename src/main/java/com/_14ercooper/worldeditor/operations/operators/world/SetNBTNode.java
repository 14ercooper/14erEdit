package com._14ercooper.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;

public class SetNBTNode extends Node {

    String nbt;

    public SetNBTNode newNode() {
	SetNBTNode node = new SetNBTNode();
	try {
	    node.nbt = GlobalVars.operationParser.parseStringNode().contents;
	}
	catch (Exception e) {
	    Main.logError("Error creating set NBT node. Please check your syntax.", Operator.currentPlayer);
	    return null;
	}
	if (node.nbt.isEmpty()) {
	    Main.logError("Could not parse set NBT node. Requires NBT, but did not find nay.", Operator.currentPlayer);
	}
	return node;
    }

    public boolean performNode() {
	try {
	    String command = "data merge block ";
	    command += Operator.currentBlock.getLocation().getBlockX() + " ";
	    command += Operator.currentBlock.getLocation().getBlockY() + " ";
	    command += Operator.currentBlock.getLocation().getBlockZ() + " ";
	    command += nbt.replaceAll("_", " ");
	    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
	    return true;
	}
	catch (Exception e) {
	    Main.logError("Error performing set NBT node. Please check your syntax.", Operator.currentPlayer);
	    return false;
	}
    }

    public int getArgCount() {
	return 1;
    }
}
