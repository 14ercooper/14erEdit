package com._14ercooper.worldeditor.commands;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;

public class CommandBrmask implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (sender instanceof Player) {
	    if (!((Player) sender).isOp()) {
		sender.sendMessage("You must be opped to use 14erEdit");
		return false;
	    }
	}

	try {
	    GlobalVars.brushMask = new HashSet<Material>();
	    for (String s : args) {
		GlobalVars.brushMask.add(Material.matchMaterial(s));
	    }

	    return true;
	}
	catch (Exception e) {
	    Main.logError("Failed to set block mask.", sender, e);
	    return false;
	}
    }
}
