package com.fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.main.Main;

public class CommandAsync implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (args[0].equalsIgnoreCase("drop")) {
				GlobalVars.asyncManager.dropAsync();
				return true;
			}
			else if (args[0].equalsIgnoreCase("status")) {
				GlobalVars.asyncManager.asyncProgress((Player) sender);
				return true;
			}

			Main.logError("Async command not provided. Please provide either drop or status.", (Player) sender); 
			return false;
		} catch (Exception e) {
			Main.logError("This must be run as a player.", sender);
			return false;
		}
	}
}
