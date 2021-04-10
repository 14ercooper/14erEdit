package com._14ercooper.worldeditor.scripts;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.undo.UndoManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CraftscriptManager {

    // Stores registered scripts
    final Map<String, Craftscript> registeredScripts = new HashMap<>();

    // Create a new manager
    public CraftscriptManager() {
        // This has no function right now
    }

    // Register a new Craftscript, called by label label and handled by handler
    public void registerCraftscript(String label, Craftscript handler) {
        registeredScripts.put(label, handler);
    }

    // Run the Craftscript label, with arguments args, and player player
    public boolean runCraftscript(String label, LinkedList<String> args, Player player) {
        Main.logDebug("Calling Craftsript: " + label);

        GlobalVars.currentUndo = UndoManager.getUndo(player);
        GlobalVars.currentUndo.startUndo(GlobalVars.undoLimit);

        try {
            registeredScripts.get(label).perform(args, player, label);
        } catch (Exception e) {
            Main.logError("Error performing CraftScript. Check your syntax?", player, e);
            e.printStackTrace();
            GlobalVars.currentUndo.finishUndo();
            GlobalVars.currentUndo = null;
        }

        if (!(GlobalVars.currentUndo == null)) {
            GlobalVars.currentUndo.finishUndo();
        }
        GlobalVars.currentUndo = null;

        return true;
    }
}
