package com.fourteener.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;
import com.fourteener.worldeditor.operations.operators.core.NumberNode;

public class BlockAtNode extends Node {

	NumberNode x, y, z;
	Node node;

	@Override
	public BlockAtNode newNode() {
		BlockAtNode baNode = new BlockAtNode();
		try {
			baNode.x = GlobalVars.operationParser.parseNumberNode();
			baNode.y = GlobalVars.operationParser.parseNumberNode();
			baNode.z = GlobalVars.operationParser.parseNumberNode();
			baNode.node = GlobalVars.operationParser.parsePart();
		} catch (Exception e) {
			Main.logError("Error creating block at node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (baNode.node == null) {
			Main.logError("Could not parse block at node. Three numbers and an operation are required, but not given.", Operator.currentPlayer);
		}
		return baNode;
	}

	@Override
	public boolean performNode() {
		try {
			Block currBlock = Operator.currentBlock;
			Operator.currentBlock = GlobalVars.world.getBlockAt((int) x.getValue() + currBlock.getX(), (int) y.getValue() + currBlock.getY(), (int) z.getValue() + currBlock.getZ());
			boolean matches = node.performNode();
			Operator.currentBlock = currBlock;
			return matches;
		} catch (Exception e) {
			Main.logError("Error performing block at node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	@Override
	public int getArgCount() {
		return 4;
	}

}
