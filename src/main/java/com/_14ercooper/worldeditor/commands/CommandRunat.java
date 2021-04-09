package com._14ercooper.worldeditor.commands;

import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.brush.shapes.Voxel;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CommandRunat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be opped to use 14erEdit");
                return false;
            }
        }

        try {
            double x = 0, y = 0, z = 0;
            // X with relative
	    if (args[0].contains("~")) {
		if (sender instanceof BlockCommandSender) {
		    x = Double.parseDouble(args[0].replaceAll("~", ""))
			    + ((BlockCommandSender) sender).getBlock().getX();
		}
		if (sender instanceof Entity) {
		    x = Double.parseDouble(args[0].replaceAll("~", "")) + ((Entity) sender).getLocation().getX();
		}
	    }
	    else {
		x = Double.parseDouble(args[0]);
	    }
	    // Y with relative
	    if (args[1].contains("~")) {
		if (sender instanceof BlockCommandSender) {
		    y = Double.parseDouble(args[1].replaceAll("~", ""))
			    + ((BlockCommandSender) sender).getBlock().getY();
		}
		if (sender instanceof Entity) {
		    y = Double.parseDouble(args[1].replaceAll("~", "")) + ((Entity) sender).getLocation().getY();
		}
	    }
	    else {
		y = Double.parseDouble(args[1]);
	    }
	    // Z with relative
	    if (args[0].contains("~")) {
		if (sender instanceof BlockCommandSender) {
		    z = Double.parseDouble(args[2].replaceAll("~", ""))
			    + ((BlockCommandSender) sender).getBlock().getZ();
		}
		if (sender instanceof Entity) {
		    z = Double.parseDouble(args[2].replaceAll("~", "")) + ((Entity) sender).getLocation().getZ();
		}
	    }
	    else {
		z = Double.parseDouble(args[2]);
	    }

	    try {
            BrushShape shape = new Voxel();
            StringBuilder opStr = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                opStr.append(args[i]).append(" ");
            }
            Operator op = new Operator(opStr.toString(), (Player) sender);
            GlobalVars.asyncManager.scheduleEdit(op, null, shape.GetBlocks(x, y, z, sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().get(0)));
            return true;
        }
	    catch (Exception e) {
		Main.logError("Error in runat. Please check your syntax.", sender, e);
		return false;
	    }
	}
	catch (Exception e) {
	    Main.logError("Could not parse runat command. Please check your syntax.", sender, e);
	    return false;
	}
    }
}
