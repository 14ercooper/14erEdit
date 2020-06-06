package com.fourteener.worldeditor.operations.operators.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class BlockNode extends Node {

	// Stores this node's argument
	public List<BlockInstance> blockList;
	public BlockInstance nextBlock;

	// Creates a new node
	public BlockNode newNode() {
		BlockNode node = new BlockNode();
		try {
			String[] data = GlobalVars.operationParser.parseStringNode().getText().split(",");
			node.blockList = new LinkedList<BlockInstance>();
			for (String s : data) {
				node.blockList.add(new BlockInstance(s));
			}
			if (node.blockList.size() == 0) {
				Main.logError("Error creating block node. No blocks were provided.", Operator.currentPlayer);
				return null;
			}
		} catch (Exception e) {
			Main.logError("Could not parse block node. Block name required, but not found.", Operator.currentPlayer);
			return null;
		}
		return node;
	}
	public BlockNode newNode(String input) {
		BlockNode node = new BlockNode();
		try {
			String[] data = input.split(",");
			node.blockList = new LinkedList<BlockInstance>();
			for (String s : data) {
				node.blockList.add(new BlockInstance(s));
			}
			if (node.blockList.size() == 0) {
				Main.logError("Error creating block node. No blocks were provided.", Operator.currentPlayer);
				return null;
			}
		} catch (Exception e) {
			Main.logError("Could not parse block node. Block name required, but not found.", Operator.currentPlayer);
			return null;
		}
		return node;
	}

	// Return the material this node references
	public String getBlock () {
		try {
			nextBlock = blockList.get(0).GetRandom(blockList);
		} catch (Exception e) {
			Main.logError("Error performing block node. Does it contain blocks?", Operator.currentPlayer);
			return null;
		}
		return nextBlock.mat;
	}

	// Get the data of this block
	public String getData () {
		try {
			return nextBlock.data;
		} catch (Exception e) {
			return null;
		}
	}

	// Check if it's the correct block
	public boolean performNode () {
		try {
			return blockList.get(0).Contains(blockList, Operator.currentBlock);
		} catch (Exception e) {
			Main.logError("Error performing block node. Does it contain blocks?", Operator.currentPlayer);
			return false;
		}
	}

	// Returns how many arguments this node takes
	public int getArgCount () {
		return 1;
	}

	// Nested class to make parsing , and % lists easier
	private class BlockInstance {
		String mat;
		String data;
		int weight;

		// Construct a new block instance from an input string
		BlockInstance (String input) {
			if (input.toCharArray()[0] == '[') {
				// Data only
				mat = "dataonly";
				data = input.replaceAll("[\\[\\]]", "");
				weight = 1;
			}
			else if (input.contains("%")) {
				if (input.contains("[")) {
					mat = input.split("%")[1].split("\\[")[0];
					data = input.split("%")[1];
					weight = Integer.parseInt(input.split("%")[0]);
				}
				else {
					mat = input.split("%")[1];
					data = null;
					weight = Integer.parseInt(input.split("%")[0]);
				}
			}
			else {
				if (input.contains("[")) {
					mat = input.split("\\[")[0];
					data = input;
					weight = 1;
				}
				else {
					mat = input;
					data = null;
					weight = 1;
				}
			}
		}

		// Does the list contain a certain block (for masking)
		boolean Contains(List<BlockInstance> list ,Block b) {
			Material testMat = b.getType();
			for (BlockInstance bi : list) {
				if (Material.matchMaterial(bi.mat) == testMat) return true;
			}
			return false;
		}

		// Get a random block from the list (for setting)
		BlockInstance GetRandom(List<BlockInstance> list) {
			int totalWeight = 0;
			for (BlockInstance bi : list) {
				totalWeight += bi.weight;
			}
			Random rand = new Random();
			int randNum = rand.nextInt(totalWeight);
			for (BlockInstance bi : list) {
				randNum -= bi.weight;
				if (randNum <= 0) return bi;
			}
			return null;
		}
	}
}
