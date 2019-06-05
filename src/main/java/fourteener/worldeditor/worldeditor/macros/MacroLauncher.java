package fourteener.worldeditor.worldeditor.macros;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.macros.macros.BasicTreeMacro;
import fourteener.worldeditor.worldeditor.macros.macros.ErodeMacro;

public class MacroLauncher {
	
	// This allows for macros to be launched and executed
	public static boolean launchMacro (String macro, Location location) {
		if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Launching macro " + macro); // ----
		// First off, parse the macro for the macro name and arguments
		String[] split1 = macro.split("\\{");
		String macroName = split1[0];
		String temp1 = split1[1].replace("}", "");
		String[] macroArgs = temp1.split(",");
		return runMacro(macroName, macroArgs, location);
	}
	
	private static boolean runMacro (String macroName, String[] macroArgs, Location location) {
		
		// Determine which macro to run
		if (macroName.equalsIgnoreCase("erode")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Running erode macro"); // ----
			return ErodeMacro.createMacro(macroArgs, location).performMacro();
		} else if (macroName.equalsIgnoreCase("tree")) {
			if (Main.isDebug) Bukkit.getServer().broadcastMessage("§c[DEBUG] Running tree macro"); // ----
			return BasicTreeMacro.createMacro(macroArgs, location).performMacro();
		} else {
			return false;
		}
	}
}
