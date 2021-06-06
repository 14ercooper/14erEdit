package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptBiome extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String radius = args.get(1);
            String biome = args.get(0);
            Bukkit.getServer().dispatchCommand(player, "fx br s " + radius + " 0.5 biome " + biome);
        } catch (Exception e) {
            Main.logError("Error parsing biome script. Did you pass in the correct arguments?", Operator.currentPlayer, e);
        }
    }

}
