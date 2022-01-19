// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public class ScriptBiome extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String radius = args.get(1);
            String biome = args.get(0);
            Bukkit.getServer().dispatchCommand(player, "fx br s " + radius + " 0.5 biome " + biome);
        } catch (Exception e) {
            Main.logError("Error parsing biome script. Did you pass in the correct arguments?", player, e);
        }
    }

}
