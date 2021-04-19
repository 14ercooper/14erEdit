package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptTree extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, Player player, String label) {
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
                            Operator.currentPlayer, e);
                    return;
                }
            }
            player.performCommand("fx br s 0 0.5 $ tree{" + treeType + ";" + treeLeaves + ";" + treeWood + ";"
                    + treeSize + ";" + treeSizeVariance + "}");
        } catch (Exception e) {
            Main.logError("Error parsing tree script. Did you provide the correct arguments?", Operator.currentPlayer, e);
        }
    }
}
