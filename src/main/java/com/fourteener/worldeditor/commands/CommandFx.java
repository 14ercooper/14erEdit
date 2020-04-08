package com.fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.brush.Brush;
import com.fourteener.worldeditor.selection.SelectionCommand;
import com.fourteener.worldeditor.selection.SelectionWand;
import com.fourteener.worldeditor.selection.SelectionWandListener;
import com.fourteener.worldeditor.undo.UndoManager;

// For the fx command
public class CommandFx implements CommandExecutor {
	
	private static int argOffset = 0;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < argOffset + 1) {
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
					return !(new Brush(args[argOffset + 1], args[argOffset + 2], args, argOffset, (Player) sender) == null);
				}
			}
			
			// Calls the selection command, handling operating on selections
			else if (args[argOffset].equalsIgnoreCase("selection") || args[argOffset].equalsIgnoreCase("sel")) {
				return SelectionCommand.performCommand(args, (Player) sender);
			}
			
			// Undo and redo commands
			else if (args[argOffset].equalsIgnoreCase("undo")) {
				return UndoManager.getUndo((Player) sender).undoChanges(Integer.parseInt(args[argOffset + 1])) > 0;
			} else if (args[argOffset].equalsIgnoreCase("redo")) {
				return UndoManager.getUndo((Player) sender).redoChanges(Integer.parseInt(args[argOffset + 1])) > 0;
			}
			return false;
		}
		return false;
	}
}
