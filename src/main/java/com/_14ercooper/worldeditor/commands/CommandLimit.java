package com._14ercooper.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;

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

        if (arg3[0].equalsIgnoreCase("px")) {
            GlobalVars.maxEditX = val;
            arg0.sendMessage("§aPositive X edit box changed to " + val);
            return true;
        } else if (arg3[0].equalsIgnoreCase("py")) {
            arg0.sendMessage("§aPositive Y edit box changed to " + val);
            GlobalVars.maxEditY = val;
            return true;
        } else if (arg3[0].equalsIgnoreCase("pz")) {
            arg0.sendMessage("§aPositive Z edit box changed to " + val);
            GlobalVars.maxEditZ = val;
            return true;
        } else if (arg3[0].equalsIgnoreCase("nx")) {
            arg0.sendMessage("§aPositive X edit box changed to " + val);
            GlobalVars.minEditX = val;
            return true;
        } else if (arg3[0].equalsIgnoreCase("ny")) {
            arg0.sendMessage("§aNegative Y edit box changed to " + val);
            GlobalVars.minEditY = val;
            return true;
        } else if (arg3[0].equalsIgnoreCase("nz")) {
            arg0.sendMessage("§aNegative Z edit box changed to " + val);
            GlobalVars.minEditZ = val;
            return true;
        } else {
            Main.logError("Invalid limiter provided: " + arg3[0], arg0, null);
            return false;
        }
    }

}
