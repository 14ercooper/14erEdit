package com.fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.fourteener.worldeditor.main.GlobalVars;

public class CommandLiquid implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		GlobalVars.targetLiquid = !GlobalVars.targetLiquid;
		
		return true;
	}
}
