package fourteener.worldeditor.worldeditor.macros.macros;

import org.bukkit.Location;

// This is a blank macro, to use as a template for others, and so you can reference a generic macro
public class Macro {
	
	// Create a new macro
	public static Macro createMacro (String[] args, Location loc) {
		return new Macro();
	}
	
	// Run this macro
	public boolean performMacro () {
		return false;
	}
}
