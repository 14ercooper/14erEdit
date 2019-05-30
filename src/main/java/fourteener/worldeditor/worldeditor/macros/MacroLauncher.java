package fourteener.worldeditor.worldeditor.macros;

import org.bukkit.Location;

import fourteener.worldeditor.worldeditor.macros.macros.BasicTreeMacro;
import fourteener.worldeditor.worldeditor.macros.macros.ErodeMacro;

public class MacroLauncher {
	
	// This allows for macros to be launched and executed
	public static boolean launchMacro (String macro, Location location) {
		// First off, parse the macro for the macro name and arguments
		String[] split1 = macro.split("\\{");
		String macroName = split1[0];
		String temp1 = split1[0].replace("{", "");
		String[] macroArgs = temp1.split(",");
		return runMacro(macroName, macroArgs, location);
	}
	
	private static boolean runMacro (String macroName, String[] macroArgs, Location location) {
		
		// Determine which macro to run
		if (macroName.equalsIgnoreCase("erode")) {
			return ErodeMacro.createMacro(macroArgs, location).performMacro();
		} else if (macroName.equalsIgnoreCase("basictree")) {
			return BasicTreeMacro.createMacro(macroArgs, location).performMacro();
		} else {
			return false;
		}
	}
}
