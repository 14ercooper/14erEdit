package fourteener.worldeditor.worldeditor.macros;

import org.bukkit.Location;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.macros.macros.nature.*;
import fourteener.worldeditor.worldeditor.macros.macros.technical.*;

public class MacroLauncher {
	
	// This allows for macros to be launched and executed
	public static boolean launchMacro (String macro, Location location) {
		Main.logDebug("Launching macro " + macro); // ----
		// First off, parse the macro for the macro name and arguments
		String[] split1 = macro.split("\\{");
		String macroName = split1[0];
		String temp1 = split1[1].replace("}", "");
		String[] macroArgs = temp1.split(";");
		return runMacro(macroName, macroArgs, location);
	}
	
	private static boolean runMacro (String macroName, String[] macroArgs, Location location) {
		
		// Determine which macro to run
		if (macroName.equalsIgnoreCase("erode")) {
			Main.logDebug("Running erode macro"); // ----
			return new ErodeMacro(macroArgs, location).performMacro();
		}
		else if (macroName.equalsIgnoreCase("tree")) {
			Main.logDebug("Running tree macro"); // ----
			return new BasicTreeMacro(macroArgs, location).performMacro();
		}
		else if (macroName.equalsIgnoreCase("schem")) {
			Main.logDebug("Running schematic macro"); // ----
			return new SchematicMacro(macroArgs, location).performMacro();
		}
		else if (macroName.equalsIgnoreCase("grass")) {
			Main.logDebug("Running grass macro"); // ----
			return new GrassMacro(macroArgs, location).performMacro();
		}
		else if (macroName.equalsIgnoreCase("vines")) {
			Main.logDebug("Running vines macro"); // ----
			return new VinesMacro(macroArgs, location).performMacro();
		}
		else if (macroName.equalsIgnoreCase("biome")) {
			Main.logDebug("Running biome macro"); // ----
			return new BiomeMacro(macroArgs, location).performMacro();
		}
		else if (macroName.equalsIgnoreCase("flatten")) {
			Main.logDebug("Running flatten macro"); // ----
			return new FlattenMacro(macroArgs, location).performMacro();
		}
		else {
			return false;
		}
	}
}
