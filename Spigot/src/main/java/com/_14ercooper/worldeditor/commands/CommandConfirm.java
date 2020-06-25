package com._14ercooper.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.GlobalVars;

// These are dedicated versions of the undo and redo commands
public class CommandConfirm implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (command.getName().equalsIgnoreCase("confirm")) {
				int numToConfirm = 1;
				try {
					numToConfirm = Integer.parseInt(args[0]);
				} catch (Exception e) {
					numToConfirm = 1;
				}
				GlobalVars.asyncManager.confirmEdits(numToConfirm);;
				return true;
			} else if (command.getName().equalsIgnoreCase("cancel")) {
				int numToCancel = 1;
				try {
					numToCancel = Integer.parseInt(args[0]);
				} catch (Exception e) {
					numToCancel = 1;
				}
				GlobalVars.asyncManager.cancelEdits(numToCancel);
				return true;
			}
			return false;
		}
		return false;
	}
}
