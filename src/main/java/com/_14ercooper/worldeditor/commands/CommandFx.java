package com._14ercooper.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.selection.SchematicHandler;
import com._14ercooper.worldeditor.selection.SelectionCommand;
import com._14ercooper.worldeditor.selection.SelectionWand;
import com._14ercooper.worldeditor.selection.SelectionWandListener;
import com._14ercooper.worldeditor.undo.UndoManager;

// For the fx command
public class CommandFx implements CommandExecutor {

    private static int argOffset = 0;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	try {
	    if (args.length < argOffset + 1) {
		Main.logError("fx requires at least one argument.", sender);
		return false;
	    }
	    if (sender instanceof Player) {
		// Calls the wand command, giving the player a wand
		if (args[argOffset].equalsIgnoreCase("wand")) {
		    SelectionWand wand = (SelectionWand.giveNewWand(((Player) sender).getPlayer()));
		    if (SelectionWandListener.wands.contains(wand))
			return true;
		    else {
			SelectionWandListener.wands.add(wand);
			return true;
		    }
		}

		// Calls the brush command, handling the creation of a new brush
		else if (args[argOffset].equalsIgnoreCase("brush") || args[argOffset].equalsIgnoreCase("br")) {
		    // Unassign a brush if asked
		    if (args[argOffset + 1].equalsIgnoreCase("none")) {
			sender.sendMessage("§dBrush removed.");
			return Brush.removeBrush((Player) sender);
		    }
		    // Create a new brush as requested
		    else {
			return !(new Brush(args[argOffset + 1], args[argOffset + 2], args, argOffset,
				(Player) sender) == null);
		    }
		}

		// Calls the selection command, handling operating on selections
		else if (args[argOffset].equalsIgnoreCase("selection") || args[argOffset].equalsIgnoreCase("sel")) {
		    return SelectionCommand.performCommand(args, (Player) sender);
		}

		// Undo and redo commands
		else if (args[argOffset].equalsIgnoreCase("undo")) {
		    return UndoManager.getUndo((Player) sender).undoChanges(Integer.parseInt(args[argOffset + 1])) > 0;
		}
		else if (args[argOffset].equalsIgnoreCase("redo")) {
		    return UndoManager.getUndo((Player) sender).redoChanges(Integer.parseInt(args[argOffset + 1])) > 0;
		}

		// Save and load schematics
		else if (args[argOffset].equalsIgnoreCase("schem")) {
		    if (args[argOffset + 1].equalsIgnoreCase("save")) {
			SchematicHandler.saveSchematic(args[argOffset + 2], (Player) sender);
			return true;
		    }
		    else if (args[argOffset + 1].equalsIgnoreCase("load")) {
			SchematicHandler.loadSchematic(args[argOffset + 2], (Player) sender,
				args.length > argOffset + 4 ? args[argOffset + 3] : "",
				args.length > argOffset + 5 ? Boolean.parseBoolean(args[argOffset + 4]) : false,
				args.length > argOffset + 6 ? Integer.parseInt(args[argOffset + 5]) : 0);
			return true;
		    }
		}

		return false;
	    }
	    Main.logError("This command must be run as a player.", sender);
	    return false;
	}
	catch (Exception e) {
	    Main.logError("Error in fx command. Please check your syntax.", sender);
	    return false;
	}
    }
}
