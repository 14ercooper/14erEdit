package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAsync implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        try {
            if (args[0].equalsIgnoreCase("drop")) {
                GlobalVars.asyncManager.dropAsync();
                sender.sendMessage("§aAsync queue dropped.");
                return true;
            } else if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("queue")) {
                GlobalVars.asyncManager.asyncProgress(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("dump")) {
                GlobalVars.asyncManager.asyncDump(sender);
                return true;
            }

            Main.logError("Async command not provided. Please provide one of drop, status, dump.", sender, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error performing async operation.", sender, e);
            return false;
        }
    }
}
