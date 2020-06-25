package com._14ercooper.worldeditor.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

// These are dedicated versions of the undo and redo commands
public class CommandRun implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (sender instanceof Player) {
				String opStr = "";
				for (String s : args) {
					opStr = opStr.concat(s).concat(" ");
				}
				Operator op = new Operator(opStr, (Player) sender);
				Block b = ((Player) sender).getWorld().getBlockAt(((Player) sender).getLocation());
				op.operateOnBlock(b, (Player) sender);
				return true;
			}
			Main.logError("This must be run as a player.", sender);
			return false;
		} catch (Exception e) {
			Main.logError("Error performing run command. Please check your syntax.", sender);
			return false;
		}
	}
}
