package com._14ercooper.worldeditor.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.make.Make;

public class CommandMake implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
	if (!(arg0 instanceof Player)) {
	    Main.logError("This must be run as a player.", arg0);
	    return false;
	}

	Player p = (Player) arg0;

	String tag = "";
	try {
	    tag = arg3[0];
	}
	catch (Exception e) {
	    Main.logError("What make to run must be provided.", p);
	    return false;
	}

	Map<String, String> args = new HashMap<String, String>();
	try {
	    for (int i = 1; i < arg3.length; i++) {
		if (i % 2 == 1) {
		    args.put(arg3[i], arg3[i + 1]);
		}
	    }
	}
	catch (Exception e) {
	    Main.logError("Every provided tag must have a value.", p);
	    return false;
	}

	return Make.make(p, tag, args);
    }
}
