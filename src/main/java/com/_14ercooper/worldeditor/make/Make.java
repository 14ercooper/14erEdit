package com._14ercooper.worldeditor.make;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.main.Main;

public class Make {
    
    static Map<String, MakeGenerator> generators = new HashMap<String, MakeGenerator>();
    
    public static boolean make(Player player, String type, Map<String, String> tags) {
	if (!generators.containsKey(type)) {
	    Main.logError("Make generator not found: " + type, player);
	    return false;
	}
	try {
	    return generators.get(type).make(player, tags);
	}
	catch (Exception e) {
	    Main.logError("Error performing make. Please check your syntax.", player);
	    return false;
	}
    }
}
