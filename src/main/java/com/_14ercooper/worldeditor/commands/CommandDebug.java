package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.main.GlobalVars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// These are dedicated versions of the undo and redo commands
public class CommandDebug implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (sender instanceof Player) {
        if (!sender.isOp()) {
            sender.sendMessage("You must be opped to use 14erEdit");
            return false;
        }
    }

	GlobalVars.isDebug = !GlobalVars.isDebug;
	Bukkit.broadcastMessage("§dDebug toggled to " + GlobalVars.isDebug);
	return true;
    }
}
