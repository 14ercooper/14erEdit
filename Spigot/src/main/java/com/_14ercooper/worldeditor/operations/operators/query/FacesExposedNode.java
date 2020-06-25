package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;

public class FacesExposedNode extends Node {

	public NumberNode arg;

	public FacesExposedNode newNode() {
		FacesExposedNode node = new FacesExposedNode();
		try {
			node.arg = GlobalVars.operationParser.parseNumberNode();
		} catch (Exception e) {
			Main.logError("Error creating faces exposed node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (node.arg == null) {
			Main.logError("Could not create faces exposed node. Requires a number argument that was not given.", Operator.currentPlayer);
		}
		return node;
	}

	public boolean performNode () {

		// Count the number of faces
		// Basically check for air in each of the four directions
		int faceCount = 0;
		Block b = Operator.currentPlayer.getWorld().getBlockAt(Operator.currentBlock.getLocation());
		if (b.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.EAST).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.WEST).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.UP).getType() == Material.AIR) {
			faceCount++;
		}
		if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			faceCount++;
		}

		// Perform the node
		return (faceCount >= arg.getValue() - 0.1);
	}

	public int getArgCount () {
		return 1;
	}
}
