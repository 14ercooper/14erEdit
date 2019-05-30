package fourteener.worldeditor.worldeditor.macros.macros;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BasicTreeMacro extends Macro {
	
	public static Macro createMacro (String[] args, Location loc) {
		Bukkit.getServer().broadcastMessage("§cThat feature isn't implemented yet");
		return new Macro();
	}
	
	public boolean performMacro () {
		return false;
	}
}
