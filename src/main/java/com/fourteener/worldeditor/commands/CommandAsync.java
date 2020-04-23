package com.fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.GlobalVars;

public class CommandAsync implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equalsIgnoreCase("drop")) {
			GlobalVars.asyncManager.dropAsync();
			return true;
		}
		else if (args[0].equalsIgnoreCase("status")) {
			GlobalVars.asyncManager.asyncProgress((Player) sender);
			return true;
		}
		
		return false;
	}
}
