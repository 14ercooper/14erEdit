package com._14ercooper.worldeditor.commands;

import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.GlobalVars;

public class CommandScript implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (sender instanceof Player) {
	    if (!((Player) sender).isOp()) {
		sender.sendMessage("You must be opped to use 14erEdit");
		return false;
	    }
	}

	if (sender instanceof Player) {
	    LinkedList<String> argsToPass = new LinkedList<String>(Arrays.asList(args));
	    argsToPass.removeFirst();
	    return GlobalVars.scriptManager.runCraftscript(args[0], argsToPass, (Player) sender);
	}
	return false;
    }
}
