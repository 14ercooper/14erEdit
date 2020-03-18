package com.fourteener.worldeditor.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandInfo implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Bukkit.broadcastMessage("§d14erEdit");
		Bukkit.broadcastMessage("§dVersion 1.3.0-SNAPSHOT");
		
		return true;
	}
}
