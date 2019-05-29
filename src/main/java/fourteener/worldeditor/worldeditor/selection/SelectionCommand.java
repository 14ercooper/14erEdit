package fourteener.worldeditor.worldeditor.selection;

import org.bukkit.entity.Player;

public class SelectionCommand {
	public static boolean performCommand (String[] args, Player player) {
		// First, get the wand that this player owns
		SelectionWand wand = null;
		for (SelectionWand s : SelectionWandListener.wands) {
			if (s.owner.equals(player)) {
				wand = s;
				break;
			}
		}
		if (wand == null) {
			return false;
		}
		
		// Then get the applicable wand manager
		SelectionManager manager = wand.manager;
		
		// This switch statement calls the various commands that can be done to the selection
		if (args[1].equalsIgnoreCase("op")) {
			return operate();
		} else if (args[1].equalsIgnoreCase("expand")) {
			return expand();
		} else if (args[1].equalsIgnoreCase("reset")) {
			return reset();
		} else {
			return false;
		}
	}
	
	// Operate on the selection
	private static boolean operate () {
		return false;
	}
	
	// Expand the selection
	private static boolean expand () {
		return false;
	}
	
	// Reset the selection
	private static boolean reset () {
		return false;
	}
}
