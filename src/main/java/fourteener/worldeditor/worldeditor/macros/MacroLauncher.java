package fourteener.worldeditor.worldeditor.macros;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.macros.macros.Macro;

public class MacroLauncher {
	
	Map<String, Macro> macros = new HashMap<String, Macro>();
	
	// This allows for macros to be launched and executed
	public boolean launchMacro (String macro, Location location) {
		Main.logDebug("Launching macro " + macro); // ----
		// First off, parse the macro for the macro name and arguments
		String[] split1 = macro.split("\\{");
		String macroName = split1[0];
		String temp1 = split1[1].replace("}", "");
		String[] macroArgs = temp1.split(";");
		return macros.get(macroName).performMacro(macroArgs, location);
	}
	
	public boolean addMacro(String name, Macro macro) {
		if (macros.containsKey(name)) {
			return false;
		}
		macros.put(name, macro);
		return true;
	}
}
