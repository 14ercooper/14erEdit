package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.player.PlayerManager;
import com._14ercooper.worldeditor.player.PlayerWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandLimit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        // Get the value (or try to)
        long val;
        try {
            val = Long.parseLong(arg3[1]);
        } catch (NumberFormatException e) {
            if (arg3[1].equalsIgnoreCase("max")) {
                val = Long.MAX_VALUE;
            } else if (arg3[1].equalsIgnoreCase("min")) {
                val = Long.MIN_VALUE;
            } else {
                Main.logError("Could not parse value for limiter, " + arg3[1] + "is not a number", arg0, null);
                return false;
            }
        }

        PlayerWrapper playerWrapper = PlayerManager.INSTANCE.getPlayerWrapper(arg0);

        if (arg3[0].equalsIgnoreCase("px")) {
            playerWrapper.setMaxEditX(val);
            arg0.sendMessage("§aPositive X edit box changed to " + val);
            return true;
        } else if (arg3[0].equalsIgnoreCase("py")) {
            arg0.sendMessage("§aPositive Y edit box changed to " + val);
            playerWrapper.setMaxEditY(val);
            return true;
        } else if (arg3[0].equalsIgnoreCase("pz")) {
            arg0.sendMessage("§aPositive Z edit box changed to " + val);
            playerWrapper.setMaxEditZ(val);
            return true;
        } else if (arg3[0].equalsIgnoreCase("nx")) {
            arg0.sendMessage("§aPositive X edit box changed to " + val);
            playerWrapper.setMinEditX(val);
            return true;
        } else if (arg3[0].equalsIgnoreCase("ny")) {
            arg0.sendMessage("§aNegative Y edit box changed to " + val);
            playerWrapper.setMinEditY(val);
            return true;
        } else if (arg3[0].equalsIgnoreCase("nz")) {
            arg0.sendMessage("§aNegative Z edit box changed to " + val);
            playerWrapper.setMinEditZ(val);
            return true;
        } else {
            Main.logError("Invalid limiter provided: " + arg3[0], arg0, null);
            return false;
        }
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> tabArgs = new ArrayList<>();

            if (args.length == 1) {
                tabArgs.add("px");
                tabArgs.add("py");
                tabArgs.add("pz");
                tabArgs.add("nx");
                tabArgs.add("ny");
                tabArgs.add("nz");
            }
            if (args.length == 2) {
                tabArgs.add("<value>");
            }

            return tabArgs;
        }
    }

}
