package com._14ercooper.worldeditor.scripts.bundled.easyedit;

import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;
import com._14ercooper.worldeditor.scripts.Craftscript;
import com._14ercooper.worldeditor.selection.SelectionManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ScriptFlatten extends Craftscript {

    @Override
    public void perform(LinkedList<String> args, Player player, String label) {
        try {
            boolean useSelection = true;
            double height = Double.parseDouble(args.get(0));
            Material block = Material.matchMaterial(args.get(1));
            if (args.size() > 2) {
                useSelection = false;
            }
            if (label.equalsIgnoreCase("flatten")) {
                if (useSelection) {
                    selectionFlatten(player, height, block);
                } else {
                    player.performCommand("fx br s 0 0.5 $ flatten{" + args.get(2) + ";" + "false" + ";"
                            + args.get(0) + ";" + args.get(1) + "}");
                }
            } else if (label.equalsIgnoreCase("absflatten")) {
                if (useSelection) {
                    absoluteSelectionFlatten(player, height, block);
                } else {
                    player.performCommand("fx br s 0 0.5 $ flatten{" + args.get(2) + ";" + "true" + ";"
                            + args.get(0) + ";" + args.get(1) + "}");
                }
            }
        } catch (Exception e) {
            Main.logError("Could not parse flatten script. Did you pass in the correct arguments?",
                    Operator.currentPlayer, e);
        }
    }

    private boolean selectionFlatten(Player player, double height, Material block) {
        SelectionManager sm = SelectionManager.getSelectionManager(player);
        if (sm != null) {
            double[] negCorner = sm.getMostNegativeCorner();
            double[] posCorner = sm.getMostPositiveCorner();

            // Generate the box
            for (int rx = (int) negCorner[0]; rx <= posCorner[0]; rx++) {
                for (int rz = (int) negCorner[2]; rz <= posCorner[2]; rz++) {
                    for (int ry = (int) negCorner[1]; ry <= posCorner[1]; ry++) {
                        if (ry <= Math.round(height)) {
                            Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
                            SetBlock.setMaterial(b, block);
                        } else {
                            Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
                            SetBlock.setMaterial(b, Material.AIR);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean absoluteSelectionFlatten(Player player, double height, Material block) {
        SelectionManager sm = SelectionManager.getSelectionManager(player);
        if (sm != null) {
            double[] negCorner = sm.getMostNegativeCorner();
            double[] posCorner = sm.getMostPositiveCorner();

            // Generate the box
            for (int rx = (int) negCorner[0]; rx <= posCorner[0]; rx++) {
                for (int rz = (int) negCorner[2]; rz <= posCorner[2]; rz++) {
                    for (int ry = 0; ry <= 255; ry++) {
                        if (ry <= Math.round(height)) {
                            Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
                            SetBlock.setMaterial(b, block);
                        } else {
                            Block b = Operator.currentPlayer.getWorld().getBlockAt(rx, ry, rz);
                            SetBlock.setMaterial(b, Material.AIR);
                        }
                    }
                }
            }
        }

        return true;
    }
}
