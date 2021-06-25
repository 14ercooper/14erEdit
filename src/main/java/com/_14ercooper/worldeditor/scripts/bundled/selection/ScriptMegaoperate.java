package com._14ercooper.worldeditor.scripts.bundled.selection;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import com._14ercooper.worldeditor.scripts.CraftscriptManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptMegaoperate extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            int xMin = Integer.parseInt(args.get(0));
            int xMax = Integer.parseInt(args.get(1));
            int yMin = Integer.parseInt(args.get(2));
            int yMax = Integer.parseInt(args.get(3));
            int zMin = Integer.parseInt(args.get(4));
            int zMax = Integer.parseInt(args.get(5));
            String op = args.get(6);
            Operator operator = new Operator(op, Operator.currentPlayer);
            Bukkit.getServer().broadcastMessage("Starting operation...");
            for (int x = xMin; x <= xMax; x++) {
                for (int y = yMin; y <= yMax; y++) {
                    for (int z = zMin; z <= zMax; z++) {
                        operator.operateOnBlock(Operator.currentWorld.getBlockAt(x, y, z), CraftscriptManager.getScriptUndo());
                    }
                }
            }
            Bukkit.getServer().broadcastMessage("Operation finished...");
        } catch (Exception e) {
            Main.logError("Error performing megaoperate script. Did you provide the correct arguments?",
                    Operator.currentPlayer, e);
        }
    }
}
