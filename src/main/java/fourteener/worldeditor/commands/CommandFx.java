package fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fourteener.worldeditor.commands.fx.SelectionWand;
import fourteener.worldeditor.commands.fx.SelectionWandListener;

// For the fx command
public class CommandFx implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args[0].equalsIgnoreCase("wand")) {
				SelectionWand wand = (SelectionWand.giveNewWand(((Player) sender).getPlayer()));
				if (SelectionWandListener.wands.contains(wand))
					return true;
				else {
					SelectionWandListener.wands.add(wand);
					return true;
				}
			}
			return false;
		}
		return false;
	}
}
