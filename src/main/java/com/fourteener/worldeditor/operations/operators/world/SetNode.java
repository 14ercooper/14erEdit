package com.fourteener.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class SetNode extends Node {

	public BlockNode arg;

	public SetNode newNode() {
		SetNode node = new SetNode();
		try {
			node.arg = (BlockNode) GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error parsing set block node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg == null) {
			Main.logError("Could not create set block node. A block is required, but not provided.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {
		try {
			boolean didSet = true;
			if (Operator.currentBlock.getType() == arg.getBlock()) {
				didSet = false;
			}
			if (arg.getData() != null && Operator.currentBlock.getBlockData() == arg.getData()) {
				didSet = false;
			}

			Material storedMaterial = Operator.currentBlock.getType();
			BlockData storedData = null;
			if (arg.getData() == null) {
				storedData = Operator.currentBlock.getBlockData();
			}
			SetBlock.setMaterial(Operator.currentBlock, arg.getBlock());
			if (arg.getData() != null) {
				Operator.currentBlock.setBlockData(arg.getData());
			}
			else if (storedData != null && Operator.currentBlock.getType() == storedMaterial) {
				try {
					String blockMat = storedData.getAsString().split("\\[")[1];
					blockMat = Operator.currentBlock.getType().toString() + "[" + blockMat;
					Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(blockMat));
				} catch (Exception e) {
					// nothing needs to be done here
				}
			}

			return didSet;
		} catch (Exception e) {
			Main.logError("Error performing block set node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 1;
	}
}
