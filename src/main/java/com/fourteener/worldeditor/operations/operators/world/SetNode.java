package com.fourteener.worldeditor.operations.operators.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
			Material storedMaterial = Operator.currentBlock.getType();
			String storedData = Operator.currentBlock.getBlockData().getAsString();
			String setMaterial = arg.getBlock();
			String setData = arg.getData();
			// Case only data to set, do the data merge
			if (setMaterial.equalsIgnoreCase("dataonly")) {
				// Step 1, convert the old data into a map
				Map<String, String> oldData = new HashMap<String, String>();
				for (String s : storedData.split("\\[")[1].replaceAll("\\]", "").split(",")) {
					oldData.put(s.split("=")[0], s.split("=")[1]);
				}
				// Step 2, merge the new data into the map
				for (String s : setData.split("\\[")[0].replaceAll("\\]", "").split(",")) {
					if (oldData.containsKey(s.split("=")[0])) oldData.remove(s.split("=")[0]);
					oldData.put(s.split("=")[0], s.split("=")[1]);
				}
				// Step 3, rebuild the data and set
				String newData = storedMaterial.toString().toLowerCase() + "[";
				for (Entry<String,String> e : oldData.entrySet()) {
					newData = newData + e.getKey() + "=" + e.getValue() + ",";
				}
				newData = newData.substring(0, newData.length() - 1);
				newData = newData + "]";
				Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(newData));
			}
			// Case NBT only, merge nbt
			else if (setMaterial.equalsIgnoreCase("nbtonly")) {
				String command = "data merge block " + Operator.currentBlock.getX() + " " + Operator.currentBlock.getY() + " " + Operator.currentBlock.getZ();
				command = command + " " + arg.getNBT();
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			// Case no data to set
			else if (setData == null) {
				// Try carry over data
				SetBlock.setMaterial(Operator.currentBlock, Material.matchMaterial(setMaterial), Operator.ignoringPhysics);
				try {
					if (!setMaterial.contains("minecraft:")) setMaterial = "minecraft:" + setMaterial;
					String newData = setMaterial + "[" + storedData.split("\\[")[1];
					BlockData data = Bukkit.getServer().createBlockData(newData);
					Operator.currentBlock.setBlockData(data);
				} catch (Exception e) {
					// Nothing needs to be done, new block can't take the existing data so no worries
				}
			}
			// If both provided
			else {
				// Case materials match (update data) - this is slightly faster in some cases
				if (storedMaterial == Material.matchMaterial(setMaterial)) {
					Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(setData));
				}
				// Case materials don't match (set all)
				else {
					SetBlock.setMaterial(Operator.currentBlock, Material.matchMaterial(setMaterial), Operator.ignoringPhysics);
					Operator.currentBlock.setBlockData(Bukkit.getServer().createBlockData(setData));
				}
			}

			return true;
		} catch (Exception e) {
			Main.logError("Error performing block set node. Please check your syntax.", Operator.currentPlayer);
			return false;
		}
	}

	public int getArgCount () {
		return 1;
	}
}
