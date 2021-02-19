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
	}
	catch (NumberFormatException e) {
	    if (arg3[1].equalsIgnoreCase("max")) {
		val = Long.MAX_VALUE;
	    }
	    else if (arg3[1].equalsIgnoreCase("min")) {
		val = Long.MIN_VALUE;
	    }
	    else {
		Main.logError("Could not parse value for limiter, " + arg3[1] + "is not a number", arg0);
		return false;
	    }
	}

	if (arg3[0].equalsIgnoreCase("px")) {
	    GlobalVars.maxEditX = val;
	    return true;
	}
	else if (arg3[0].equalsIgnoreCase("py")) {
	    GlobalVars.maxEditY = val;
	    return true;
	}
	else if (arg3[0].equalsIgnoreCase("pz")) {
	    GlobalVars.maxEditZ = val;
	    return true;
	}
	else if (arg3[0].equalsIgnoreCase("nx")) {
	    GlobalVars.minEditX = val;
	    return true;
	}
	else if (arg3[0].equalsIgnoreCase("ny")) {
	    GlobalVars.minEditY = val;
	    return true;
	}
	else if (arg3[0].equalsIgnoreCase("nz")) {
	    GlobalVars.minEditZ = val;
	    return true;
	}
	else {
	    Main.logError("Invalid limiter provided: " + arg3[0], arg0);
	    return false;
	}
    }

}
