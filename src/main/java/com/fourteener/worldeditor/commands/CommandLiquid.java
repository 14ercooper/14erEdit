package com.fourteener.worldeditor.commands;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.fourteener.worldeditor.main.GlobalVars;

public class CommandLiquid implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		GlobalVars.targetMask = new HashSet<Material>();
		for (String s : args) {
			GlobalVars.targetMask.add(Material.matchMaterial(s));
		}
		
		return true;
	}
}
