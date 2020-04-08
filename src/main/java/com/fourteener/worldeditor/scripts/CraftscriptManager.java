package com.fourteener.worldeditor.scripts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.entity.Player;

import com.fourteener.worldeditor.main.*;

public class CraftscriptManager {
	
	// Stores registered scripts
	Map<String,Craftscript> registeredScripts = new HashMap<String,Craftscript>();
	
	// Create a new manager
	public CraftscriptManager () {
		// This has no function right now
	}
	
	// Register a new Craftscript, called by label label and handled by handler
	public boolean registerCraftscript (String label, Craftscript handler) {
		registeredScripts.put(label, handler);
		return true;
	}
	
	// Run the Craftscript label, with arguments args, and player player
	public boolean runCraftscript (String label, LinkedList<String> args, Player player) {
		Main.logDebug("Calling Craftsript: " + label);
		
		try {
			registeredScripts.get(label).perform(args, player, label);
		}
		catch (Exception e) {}
		
		return true;
	}
}
