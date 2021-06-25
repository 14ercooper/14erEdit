package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
                AsyncManager.dropAsync();
                sender.sendMessage("§aAsync queue dropped.");
                return true;
            } else if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("queue")) {
                AsyncManager.asyncProgress(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("dump")) {
                AsyncManager.asyncDump(sender);
                return true;
            }

            Main.logError("Async command not provided. Please provide one of drop, status, dump.", sender, null);
            return false;
        } catch (Exception e) {
            Main.logError("Error performing async operation.", sender, e);
            return false;
        }
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.add("drop");
                tabArgs.add("status");
                tabArgs.add("queue");
                tabArgs.add("dump");
            }

            return tabArgs;
        }
    }
}
