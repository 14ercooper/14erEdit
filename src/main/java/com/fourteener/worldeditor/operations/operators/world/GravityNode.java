package com.fourteener.worldeditor.operations.operators.world;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;
import com.fourteener.worldeditor.operations.operators.Node;

public class GravityNode extends Node {

	@Override
	public GravityNode newNode() {
		return new GravityNode();
	}

	@Override
	public boolean performNode() {
		String mat = Operator.currentBlock.getType().toString().toLowerCase();
		if (mat.equals("air")) {
			return false;
		}
		Operator.currentBlock.setType(Material.AIR);
		String command = "summon minecraft:falling_block " + Operator.currentBlock.getX() + " " + Operator.currentBlock.getY() + " " + Operator.currentBlock.getZ();
		command += " {BlockState:{Name:\"minecraft:";
		command += mat;
		command += "\"},Time:1}";
		Main.logDebug("Command: " + command);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
		return true;
	}

	@Override
	public int getArgCount() {
		return 0;
	}

}
