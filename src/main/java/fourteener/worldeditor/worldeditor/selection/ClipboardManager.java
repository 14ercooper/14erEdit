package fourteener.worldeditor.worldeditor.selection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class ClipboardManager {
	
	public static List<Clipboard> clipboards = new ArrayList<Clipboard>();
	
	public static Clipboard getClipboard (Player owner) {
		// Check if a clipboard exists, otherwise keep a new one
		Clipboard clipboard = null;
		for (Clipboard c : clipboards) {
			if (c.owner.equals(owner)) {
				clipboard = c;
			}
		}
		
		// Otherwise create a new clipboard
		if (clipboard == null) {
			clipboard = Clipboard.newClipboard(owner);
			clipboards.add(clipboard);
		}
		
		// And return the clipboard
		return clipboard;
	}
}
