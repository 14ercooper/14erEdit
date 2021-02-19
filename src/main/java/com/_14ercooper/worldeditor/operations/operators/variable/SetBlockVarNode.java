package com._14ercooper.worldeditor.operations.operators.variable;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.type.BlockVar;

public class SetBlockVarNode extends Node {

    public String name;

    @Override
    public SetBlockVarNode newNode() {
	SetBlockVarNode node = new SetBlockVarNode();
	node.name = GlobalVars.operationParser.parseStringNode().contents;
	return node;
    }

    @Override
    public boolean performNode() {
	if (!Operator.blockVars.containsKey(name)) {
	    Main.logError("Error performing set block var node. Please check your syntax (does the variable exist?).",
		    Operator.currentPlayer);
	    return false;
	}
	BlockVar bv = Operator.blockVars.get(name);
	Operator.currentBlock.setType(Material.matchMaterial(bv.getType()));
	if (!bv.getData().isEmpty()) {
	    Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(bv.getData()));
	}
	if (!bv.getNBT().isEmpty()) {
	    String command = "data merge block ";
	    command += Operator.currentBlock.getLocation().getBlockX() + " ";
	    command += Operator.currentBlock.getLocation().getBlockY() + " ";
	    command += Operator.currentBlock.getLocation().getBlockZ() + " ";
	    command += bv.getNBT();
	    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
	}
	return true;
    }

    @Override
    public int getArgCount() {
	return 1;
    }
}
