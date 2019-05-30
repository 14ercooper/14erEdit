package fourteener.worldeditor.worldeditor.macros.macros;

import org.bukkit.Location;

public class BasicTreeMacro extends Macro {
	
	public static Macro createMacro (String[] args, Location loc) {
		return new Macro();
	}
	
	public boolean performMacro () {
		return false;
	}
}
