package com.fourteener.worldeditor.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.GlobalVars;
import com.fourteener.worldeditor.operations.Operator;

// These are dedicated versions of the undo and redo commands
public class CommandRun implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			String opStr = "";
			for (String s : args) {
				opStr = opStr.concat(s).concat(" ");
			}
			Operator op = new Operator(opStr);
			Block b = GlobalVars.world.getBlockAt(((Player) sender).getLocation());
			op.operateOnBlock(b, (Player) sender);
			return true;
		}
		return false;
	}
}
