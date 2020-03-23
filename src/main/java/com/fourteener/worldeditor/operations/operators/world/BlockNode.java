package com.fourteener.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class BlockNode extends Node {
	
	// Stores this node's argument
	public Material arg1;
	public BlockData arg2 = null;
	
	// Creates a new node
	public BlockNode newNode() {
		BlockNode node = new BlockNode();
		node.arg1 = Material.matchMaterial(GlobalVars.operationParser.parseStringNode());
		return node;
	}
	
	// Creates a new node
	public BlockNode newNode(boolean overload) {
		BlockNode node = new BlockNode();
		String data = GlobalVars.operationParser.parseStringNode();
		node.arg1 = Material.matchMaterial(data.split("\\[")[0]);
		node.arg2 = Bukkit.getServer().createBlockData(data);
		return node;
	}
	
	// Return the material this node references
	public Material getBlock () {
		return arg1;
	}
	
	// Get the data of this block
	public BlockData getData () {
		return arg2;
	}
	
	// Check if it's the correct block
	public boolean performNode () {
		if (arg2 == null) {
			return Operator.currentBlock.getType().equals(arg1);
		}
		else {
			return Operator.currentBlock.getBlockData().matches(arg2);
		}
	}
	
	// Returns how many arguments this node takes
	public int getArgCount () {
		return 1;
	}
}
