package com._14ercooper.worldeditor.scripts;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.undo.UndoElement;
import com._14ercooper.worldeditor.undo.UndoSystem;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CraftscriptManager {

    // Stores registered scripts
    public final Map<String, Craftscript> registeredScripts = new HashMap<>();

    // Undo
    private static CommandSender currentPlayer;
    private static UndoElement undoElement;
    private static boolean usedUndo = false;
    public static UndoElement getScriptUndo() {
        if (!usedUndo) {
            undoElement = UndoSystem.findUserUndo(currentPlayer).getNewUndoElement();
            usedUndo = true;
        }
        return undoElement;
    }

    // Create a new manager
    public CraftscriptManager() {
        // This has no function right now
    }

    // Register a new Craftscript, called by label label and handled by handler
    public void registerCraftscript(String label, Craftscript handler) {
        registeredScripts.put(label, handler);
    }

    // Run the Craftscript label, with arguments args, and player player
    public boolean runCraftscript(String label, LinkedList<String> args, CommandSender player) {
        Main.logDebug("Calling Craftsript: " + label);

        try {
            usedUndo = false;
            currentPlayer = player;
            registeredScripts.get(label).perform(args, player, label);
            if (usedUndo) {
                undoElement.finalizeUndo();
            }
        } catch (Exception e) {
            Main.logError("Error performing CraftScript. Check your syntax?", player, e);
            e.printStackTrace();
        }

        return true;
    }
}
