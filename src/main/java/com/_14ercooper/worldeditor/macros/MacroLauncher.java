package com._14ercooper.worldeditor.macros;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.undo.UndoElement;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class MacroLauncher {

    final Map<String, Macro> macros = new HashMap<>();

    public static UndoElement undoElement;

    // This allows for macros to be launched and executed
    public boolean launchMacro(String macro, Location location, UndoElement undo, OperatorState state) {
        Main.logDebug("Launching macro " + macro); // ----
        undoElement = undo;
        // First off, parse the macro for the macro name and arguments
        String[] split1 = macro.split("\\{");
        String macroName = split1[0];
        String temp1;
        try {
            temp1 = split1[1].replace("}", "");
        } catch (IndexOutOfBoundsException e) {
            Main.logError("Could not parse the macro. Did you provide arguments in {}?", state.getCurrentPlayer(), e);
            return false;
        }
        String[] macroArgs = temp1.split(";");
        GlobalVars.countEdits = true;
        boolean returnVal = macros.get(macroName).performMacro(macroArgs, location, state);
        GlobalVars.countEdits = false;
        return returnVal;
    }

    public void addMacro(String name, Macro macro) {
        if (macros.containsKey(name)) {
            return;
        }
        macros.put(name, macro);
    }
}
