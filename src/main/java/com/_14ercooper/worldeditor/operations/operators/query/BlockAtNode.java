package com._14ercooper.worldeditor.operations.operators.query;

import org.bukkit.block.Block;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.NBTExtractor;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.world.BlockNode;

public class BlockAtNode extends BlockNode {

	NumberNode x, y, z;
	Node node;

	public BlockAtNode newNode() {
		BlockAtNode baNode = new BlockAtNode();
		try {
			baNode.x = GlobalVars.operationParser.parseNumberNode();
			baNode.y = GlobalVars.operationParser.parseNumberNode();
			baNode.z = GlobalVars.operationParser.parseNumberNode();
			try {
				baNode.node = GlobalVars.operationParser.parsePart();
			} catch (Exception e) {
				Main.logDebug("Block at created with type blocknode");
			}
		} catch (Exception e) {
			Main.logError("Error creating block at node. Please check your syntax.", Operator.currentPlayer);
			return null;
		}
		if (baNode.z == null) {
			Main.logError("Could not parse block at node. Three numbers and optionally an operation are required, but not given.", Operator.currentPlayer);
		}
		return baNode;
	}

	public boolean performNode() {
		try {
			Block currBlock = Operator.currentBlock;
			Operator.currentBlock = Operator.currentPlayer.getWorld().getBlockAt((int) x.getValue() + currBlock.getX(), (int) y.getValue() + currBlock.getY(), (int) z.getValue() + currBlock.getZ());
			boolean matches = node.performNode();
			Operator.currentBlock = currBlock;
			return matches;
		} catch (Exception e) {
			Main.logError("Error performing block at node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}
	
	// Return the material this node references
	public String getBlock () {
		return Operator.currentBlock.getRelative((int) x.getValue(), (int) y.getValue(), (int) z.getValue()).getType().toString();
	}

	// Get the data of this block
	public String getData () {
		return Operator.currentBlock.getRelative((int) x.getValue(), (int) y.getValue(), (int) z.getValue()).getBlockData().getAsString();
	}
	
	// Get the NBT of this block
	public String getNBT() {
		NBTExtractor nbt = new NBTExtractor();
		return nbt.getNBT(Operator.currentBlock.getRelative((int) x.getValue(), (int) y.getValue(), (int) z.getValue()));
	}

	public int getArgCount() {
		return 4;
	}

}
