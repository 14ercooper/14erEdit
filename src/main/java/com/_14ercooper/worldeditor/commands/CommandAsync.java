package com._14ercooper.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;

public class CommandAsync implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	try {
	    if (args[0].equalsIgnoreCase("drop")) {
		GlobalVars.asyncManager.dropAsync();
		return true;
	    }
	    else if (args[0].equalsIgnoreCase("status")) {
		GlobalVars.asyncManager.asyncProgress(sender);
		return true;
	    }

	    Main.logError("Async command not provided. Please provide either drop or status.", sender);
	    return false;
	}
	catch (Exception e) {
	    Main.logError("Error performing async operation.", sender);
	    return false;
	}
    }
}
