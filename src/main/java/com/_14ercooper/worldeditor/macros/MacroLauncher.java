package com._14ercooper.worldeditor.macros;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class MacroLauncher {

    Map<String, Macro> macros = new HashMap<String, Macro>();

    // This allows for macros to be launched and executed
    public boolean launchMacro(String macro, Location location) {
	Main.logDebug("Launching macro " + macro); // ----
	// First off, parse the macro for the macro name and arguments
	String[] split1 = macro.split("\\{");
	String macroName = split1[0];
	String temp1 = "";
	try {
	    temp1 = split1[1].replace("}", "");
	}
	catch (IndexOutOfBoundsException e) {
	    Main.logError("Could not parse the macro. Did you provide arguments in {}?", Operator.currentPlayer);
	    return false;
	}
	String[] macroArgs = temp1.split(";");
	GlobalVars.countEdits = true;
	boolean returnVal = macros.get(macroName).performMacro(macroArgs, location);
	GlobalVars.countEdits = false;
	return returnVal;
    }

    public boolean addMacro(String name, Macro macro) {
	if (macros.containsKey(name)) {
	    return false;
	}
	macros.put(name, macro);
	return true;
    }
}
