// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandScript implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        if (sender instanceof Player) {
            LinkedList<String> argsToPass = new LinkedList<>(Arrays.asList(args));
            argsToPass.removeFirst();
            return CraftscriptManager.INSTANCE.runCraftscript(args[0], argsToPass, sender);
        }
        return false;
    }

    public static Set<String> getScriptNames() {
        return CraftscriptManager.registeredScripts.keySet();
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.addAll(CommandScript.getScriptNames());
            }
            if (args.length > 1) {
                tabArgs.add("[script_arg_" + (args.length - 2) + "]");
            }

            return tabArgs;
        }
    }
}
