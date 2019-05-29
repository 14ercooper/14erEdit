package fourteener.worldeditor.worldeditor.undo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// For the undo and redo commands
public class UndoCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (command.getName().equalsIgnoreCase("undo")) {
				return UndoManager.getUndo((Player) sender).undoChanges(Integer.parseInt(args[0]));
			} else if (command.getName().equalsIgnoreCase("redo")) {
				return UndoManager.getUndo((Player) sender).redoChanges(Integer.parseInt(args[0]));
			}
		}
		return false;
	}
}
