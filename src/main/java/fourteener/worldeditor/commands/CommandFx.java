package fourteener.worldeditor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fourteener.worldeditor.worldeditor.brush.Brush;
import fourteener.worldeditor.worldeditor.selection.SelectionWand;
import fourteener.worldeditor.worldeditor.selection.SelectionWandListener;

// For the fx command
public class CommandFx implements CommandExecutor {
	
	private static int argOffset = 1;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
					return Brush.removeBrush((Player) sender);
				}
				// Make sure there are the proper number of arguments
				else if (args.length != argOffset + 3) {
					return false;
				}
				// Create a new brush as requested
				else {
					return Brush.createBrush(args[argOffset + 1], args[argOffset + 2], args[argOffset + 3], (Player) sender);
				}
			}
			return false;
		}
		return false;
	}
}
