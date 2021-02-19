package com._14ercooper.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;

public class CommandAsync implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (sender instanceof Player) {
	    if (!((Player) sender).isOp()) {
		sender.sendMessage("You must be opped to use 14erEdit");
		return false;
	    }
	}

	try {
	    if (args[0].equalsIgnoreCase("drop")) {
		GlobalVars.asyncManager.dropAsync();
		return true;
	    }
	    else if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("queue")) {
		GlobalVars.asyncManager.asyncProgress(sender);
		return true;
	    }
	    else if (args[0].equalsIgnoreCase("dump")) {
		GlobalVars.asyncManager.asyncDump(sender);
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
