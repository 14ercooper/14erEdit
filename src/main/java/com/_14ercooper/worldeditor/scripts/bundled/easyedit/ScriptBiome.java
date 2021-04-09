package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptBiome extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, Player player, String label) {
        try {
            String radius = args.get(1);
            String biome = args.get(0);
            player.performCommand("fx br s " + radius + " 0.5 biome " + biome);
        } catch (Exception e) {
            Main.logError("Error parsing biome script. Did you pass in the correct arguments?", Operator.currentPlayer, e);
        }
    }

}
