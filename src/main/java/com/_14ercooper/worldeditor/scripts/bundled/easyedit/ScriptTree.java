// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public class ScriptTree extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, CommandSender player, String label) {
        try {
            String treeType = args.get(0);
            String treeSize = args.get(1);
            String treeLeaves;
            String treeWood;
            double treeSizeVariance = Double.parseDouble(treeSize) * 0.25;
            if (args.size() == 2) {
                treeLeaves = "lime_wool";
                treeWood = "brown_wool";
            } else {
                try {
                    treeLeaves = args.get(2);
                    treeWood = args.get(3);
                } catch (Exception e) {
                    Main.logError(
                            "Error parsing tree script. If you provide leaves, you must provide wood block material as well.",
                            player, e);
                    return;
                }
            }
            Bukkit.getServer().dispatchCommand(player, "fx br s 0 0.5 $ tree{" + treeType + ";" + treeLeaves + ";" + treeWood + ";"
                    + treeSize + ";" + treeSizeVariance + "}");
        } catch (Exception e) {
            Main.logError("Error parsing tree script. Did you provide the correct arguments?", player, e);
        }
    }
}
