package com._14ercooper.worldeditor.macros.macros.technical;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;

public class LineBrushMacro extends Macro {

    @Override
    public boolean performMacro(String[] args, Location loc) {
        // Parse args
        Player p = (Player) Operator.currentPlayer;
        Material m = Material.matchMaterial(args[0]);
        if (m == null) {
            Main.logError("Could not parse line brush macro. " + args[0] + " does not match a known block.", p, null);
            return false;
        }
        int blockCount = 1000;
        if (args.length > 1) {
            try {
                blockCount = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Main.logError("Could not parse line brush macro. " + args[1] + " is not a valid number.", p, e);
                return false;
            }
        }
        float startDist = 1.2f;
        if (args.length > 1) {
            try {
                startDist = Float.parseFloat(args[2]);
            } catch (Exception e) {
                Main.logError("Could not parse line brush macro. " + args[2] + " is not a valid number.", p, e);
                return false;
            }
        }
        Location start = p.getLocation().add(p.getLocation().getDirection().multiply(startDist));
        float xStart = (float) start.getX();
        float yStart = (float) (start.getY() + 1);
        float zStart = (float) start.getZ();
        float xStep = (xStart - loc.getBlockX()) / blockCount;
        float yStep = (yStart - loc.getBlockY()) / blockCount;
        float zStep = (zStart - loc.getBlockZ()) / blockCount;

        // Place blocks
        for (int t = 0; t < blockCount; t++) {
            int xPos = (int) (xStart - (xStep * t));
            int yPos = (int) (yStart - (yStep * t));
            int zPos = (int) (zStart - (zStep * t));
            Block b = Operator.currentWorld.getBlockAt(xPos, yPos, zPos);
            SetBlock.setMaterial(b, m);
        }

        // Return success
        return true;
    }

}
