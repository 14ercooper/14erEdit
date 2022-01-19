// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.macros.macros.nature;

import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VinesMacro extends Macro {

    double radius = 0, length = 0, variance = 0, density = 0;
    String block;
    Location pos;

    // Create a new macro
    private void SetupMacro(String[] args, Location loc, CommandSender player) {
        try {
            radius = Double.parseDouble(args[0]);
            length = Double.parseDouble(args[1]);
            variance = Double.parseDouble(args[2]);
            density = Double.parseDouble(args[3]);
            try {
                block = args[4];
            } catch (Exception e) {
                block = "vine";
            }
        } catch (Exception e) {
            Main.logError(
                    "Error parsing vine macro. Did you pass in radius, length, variance, density, and optionally block material?",
                    player, e);
        }
        try {
            Material m = Material.matchMaterial(block);
            if (m == null)
                throw new Exception();
        } catch (Exception e) {
            Main.logError("Error parsing vine macro. " + block + " is not a valid block.", player, e);
        }
        pos = loc;
    }

    // Run this macro
    @Override
    public boolean performMacro(String[] args, Location loc, OperatorState state) {
        SetupMacro(args, loc, state.getCurrentPlayer());

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
                                state.getCurrentWorld().getBlockAt((int) x + rx, (int) y + ry, (int) z + rz));
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
        Main.logDebug(snapshotArray.size() + " blocks in snapshot array");
        LinkedList<BlockState> operatedBlocks = new LinkedList<>();

        // OPERATE
        List<Material> nonsolidBlocks = new ArrayList<>();
        nonsolidBlocks.add(Material.AIR);
        nonsolidBlocks.add(Material.VINE);
        for (BlockState bs : snapshotArray) {
            Block b = state.getCurrentWorld().getBlockAt(bs.getLocation());
            // Make sure this block is air
            if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                continue;
            }

            // Check density
            if (Main.getRand().nextDouble() >= density) {
                continue;
            }

            String blockStateTop = "[";
            String blockState = "";
            if (block.equalsIgnoreCase("vine")) {
                boolean firstState = true;
                List<String> dirs = new ArrayList<>();
                // And next to a solid block
                if (!nonsolidBlocks.contains(b.getRelative(BlockFace.NORTH).getType())) {
                    if (firstState) {
                        firstState = false;
                    } else {
                        blockStateTop = blockStateTop.concat(",");
                    }
                    blockStateTop = blockStateTop.concat("north=true");
                    dirs.add("[north=true]");
                }
                if (!nonsolidBlocks.contains(b.getRelative(BlockFace.EAST).getType())) {
                    if (firstState) {
                        firstState = false;
                    } else {
                        blockStateTop = blockStateTop.concat(",");
                    }
                    blockStateTop = blockStateTop.concat("east=true");
                    dirs.add("[east=true]");
                }
                if (!nonsolidBlocks.contains(b.getRelative(BlockFace.SOUTH).getType())) {
                    if (firstState) {
                        firstState = false;
                    } else {
                        blockStateTop = blockStateTop.concat(",");
                    }
                    blockStateTop = blockStateTop.concat("south=true");
                    dirs.add("[south=true]");
                }
                if (!nonsolidBlocks.contains(b.getRelative(BlockFace.WEST).getType())) {
                    if (firstState) {
                        firstState = false;
                    } else {
                        blockStateTop = blockStateTop.concat(",");
                    }
                    blockStateTop = blockStateTop.concat("west=true");
                    dirs.add("[west=true]");
                }

                // Is this also a top vine?
                if (!blockStateTop.equals("[")) {
                    if (!nonsolidBlocks.contains(b.getRelative(BlockFace.UP).getType())) {
                        blockStateTop = blockStateTop.concat(",up=true");
                    }
                } else {
                    continue;
                }

                // Pick the side for the vines that will be below this one
                if (dirs.size() > 1) {
                    blockState = dirs.get(Main.getRand().nextInt(dirs.size() - 1));
                } else {
                    blockState = dirs.get(0);
                }

                // Close off the directional state; and move on if there was no solid block
                if (!blockStateTop.equals("[")) {
                    blockStateTop = blockStateTop.concat("]");
                }
            }

            // Determine the length of this vine
            double actVariance = ((Main.getRand().nextDouble() * 2.0) - 1.0) * variance;
            int vineLength = (int) Math.round(length + actVariance);

            // Grow the vine (checking to make sure only air gets replaced and registering
            // operated blocks)
            // Grow the top vine
            BlockState stateBS = b.getState();
            stateBS.setType(Material.matchMaterial(block));
            if (block.equalsIgnoreCase("vine"))
                stateBS.setBlockData(Bukkit.getServer().createBlockData("minecraft:vine" + blockState));
            operatedBlocks.add(stateBS);
            // Grow all the other vines
            for (int i = 1; i <= vineLength; i++) {
                stateBS = b.getRelative(BlockFace.DOWN, i).getState();
                if (stateBS.getType() == Material.AIR) {
                    stateBS.setType(Material.matchMaterial(block));
                    if (block.equalsIgnoreCase("vine"))
                        stateBS.setBlockData(Bukkit.getServer().createBlockData("minecraft:vine" + blockState));
                    operatedBlocks.add(stateBS);
                } else {
                    break; // Don't grow through solid blocks
                }
            }
        }

        Main.logDebug("Operated on and now placing " + operatedBlocks.size() + " blocks");
        // Apply the changes to the world
        for (BlockState bs : operatedBlocks) {
            Block b = state.getCurrentWorld().getBlockAt(bs.getLocation());
            SetBlock.setMaterial(b, bs.getType(), bs.getBlockData(), state.getCurrentUndo(), state.getCurrentPlayer());
        }

        return true;
    }
}
