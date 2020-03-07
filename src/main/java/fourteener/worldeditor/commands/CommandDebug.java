package fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fourteener.worldeditor.main.GlobalVars;

// These are dedicated versions of the undo and redo commands
public class CommandDebug implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		GlobalVars.isDebug = !GlobalVars.isDebug;
		return true;
	}
}
