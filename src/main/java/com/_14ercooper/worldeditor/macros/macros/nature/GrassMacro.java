package com._14ercooper.worldeditor.macros.macros.nature;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GrassMacro extends Macro {

    double radius = 0;
    String blockMix = "";
    double airSpaces = 0;
    double density = 0;
    Location pos;

    // Create a new macro
    private void SetupMacro(String[] args, Location loc) {
        try {
            radius = Double.parseDouble(args[0]);
            blockMix = args[1];
            airSpaces = Double.parseDouble(args[2]);
            density = Double.parseDouble(args[3]);
        } catch (Exception e) {
            Main.logError("Could not parse grass macro. Did you provide all 4 arguments?", Operator.currentPlayer, e);
        }
        pos = loc;
    }

    // Run this macro
    @Override
    public boolean performMacro(String[] args, Location loc) {
        SetupMacro(args, loc);

        // Location of the brush
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        // Generate the sphere
        int radiusInt = (int) Math.round(radius);
        List<Block> blockArray = new ArrayList<>();
        for (int rx = -radiusInt; rx <= radiusInt; rx++) {
            for (int rz = -radiusInt; rz <= radiusInt; rz++) {
                for (int ry = -radiusInt; ry <= radiusInt; ry++) {
                    if (rx * rx + ry * ry + rz * rz <= (radius + 0.5) * (radius + 0.5)) {
                        blockArray.add(
                                Operator.currentWorld.getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
                    }
                }
            }
        }
        Main.logDebug("Block array size: " + blockArray.size()); // ----

        // Create a snapshot array
        List<BlockState> snapshotArray = new ArrayList<>();
        for (Block b : blockArray) {
            snapshotArray.add(b.getState());
        }
        blockArray = null;
        Main.logDebug(snapshotArray.size() + " blocks in snapshot array");

        // Parse the block mixture
        LinkedList<Double> odds = new LinkedList<>();
        LinkedList<String> blocks = new LinkedList<>();
        for (String s : blockMix.split(",")) {
            String[] split = s.split("%");
            odds.add(Double.parseDouble(split[0]));
            blocks.add(split[1]);
        }
        Main.logDebug("Found " + blocks.size() + " blocks");

        // Operate on the sphere
        List<BlockState> operatedBlocks = new ArrayList<>();
        for (BlockState bs : snapshotArray) {
            // Check if this block is air
            if (bs.getType() != Material.AIR) {
                continue;
            }

            Block b = Operator.currentWorld.getBlockAt(bs.getLocation());
            // Check if it's on a solid block
            if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                Main.logDebug("Skip block, is floating");
                continue;
            }

            // Check density
            if (GlobalVars.rand.nextDouble() >= density) {
                Main.logDebug("Skip block, density not met");
                continue;
            }

            // Check air spaces
            boolean doCont = false;
            for (int i = 1; i <= airSpaces; i++) {
                if (b.getRelative(BlockFace.UP, i).getType() != Material.AIR) {
                    doCont = true;
                }
            }
            if (doCont) {
                Main.logDebug("Skip block, too little air above");
                continue;
            }

            // Figure out which block to place
            Main.logDebug("Placing grass block");
            double randNum = GlobalVars.rand.nextDouble() * 100.0;
            double oddsChance = 0.0;
            int i = -1;
            do {
                i++;
                oddsChance += odds.get(i);
            }
            while (oddsChance <= randNum);
            String blockToPlace = blocks.get(i);

            // Place the block
            BlockState operated = b.getState();
            if (blockToPlace.contains("[")) {
                String[] split = blockToPlace.split("\\[");
                operated.setType(Material.matchMaterial(split[0]));
                operated.setBlockData(Bukkit.getServer().createBlockData(blockToPlace));
            } else {
                operated.setType(Material.matchMaterial(blockToPlace));
            }
            operatedBlocks.add(operated);
        }

        Main.logDebug("Operated on and now placing " + operatedBlocks.size() + " blocks");
        // Apply the changes to the world
        for (BlockState bs : operatedBlocks) {
            Block b = Operator.currentWorld.getBlockAt(bs.getLocation());
            SetBlock.setMaterial(b, bs.getType());
            b.setBlockData(bs.getBlockData());
        }

        return true;
    }
}
