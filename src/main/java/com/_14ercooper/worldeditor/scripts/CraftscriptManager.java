package com._14ercooper.worldeditor.scripts;

import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.command.CommandSender;
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
    public boolean runCraftscript(String label, LinkedList<String> args, CommandSender player) {
        Main.logDebug("Calling Craftsript: " + label);

        try {
            registeredScripts.get(label).perform(args, (Player) player, label);
        } catch (Exception e) {
            Main.logError("Error performing CraftScript. Check your syntax?", player, e);
            e.printStackTrace();
        }

        return true;
    }
}
