package com._14ercooper.worldeditor.macros.macros.technical;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlattenMacro extends Macro {

    double radius;
    boolean isAbsolute;
    double height;
    Material block;
    Location pos;

    // Create a new macro
    private void SetupMacro(String[] args, Location loc) {
        try {
            radius = Double.parseDouble(args[0]);
            isAbsolute = Boolean.parseBoolean(args[1]);
            height = Double.parseDouble(args[2]);
            block = Material.matchMaterial(args[3]);
        } catch (Exception e) {
            Main.logError(
                    "Could not parse flatten macro. Did you provide radius, absolute flatten, height setting, and a block material?",
                    Operator.currentPlayer, e);
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
        LinkedList<BlockState> operatedBlocks = new LinkedList<>();

        // OPERATE
        if (isAbsolute) {
            Main.logDebug("Absolute flatten");
            absoluteFlatten(x, y, z, operatedBlocks);
        } else {
            Main.logDebug("Brush flatten");
            notAbsoluteFlatten(x, y, z, operatedBlocks);
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

    private void absoluteFlatten(double x, double y, double z, LinkedList<BlockState> operatedBlocks) {
        // Generate the cylinder
        int radiusInt = (int) Math.round(radius);
        List<Block> blockArray = new ArrayList<>();
        for (int rx = -radiusInt; rx <= radiusInt; rx++) {
            for (int rz = -radiusInt; rz <= radiusInt; rz++) {
                for (int ry = 0; ry <= 255; ry++) {
                    if (rx * rx + rz * rz <= (radius + 0.5) * (radius + 0.5)) {
                        blockArray.add(Operator.currentWorld.getBlockAt((int) x + rx, ry, (int) z + rz));
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

        for (BlockState bs : snapshotArray) {
            Block b = Operator.currentWorld.getBlockAt(bs.getLocation());
            int yB = bs.getY();

            if (yB <= Math.round(height)) {
                BlockState state = b.getState();
                state.setType(block);
                operatedBlocks.add(state);
            } else {
                BlockState state = b.getState();
                state.setType(Material.AIR);
                operatedBlocks.add(state);
            }
        }
    }

    private void notAbsoluteFlatten(double x, double y, double z, LinkedList<BlockState> operatedBlocks) {
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
        blockArray.add(Operator.currentWorld.getBlockAt((int) x, (int) y, (int) z));
        Main.logDebug("Block array size: " + blockArray.size()); // ----

        // Create a snapshot array
        List<BlockState> snapshotArray = new ArrayList<>();
        for (Block b : blockArray) {
            snapshotArray.add(b.getState());
        }
        blockArray = null;
        Main.logDebug(snapshotArray.size() + " blocks in snapshot array");

        for (BlockState bs : snapshotArray) {
            Block b = Operator.currentWorld.getBlockAt(bs.getLocation());
            int yB = bs.getY();

            if (yB <= Math.round(height)) {
                BlockState state = b.getState();
                state.setType(block);
                operatedBlocks.add(state);
            } else {
                BlockState state = b.getState();
                state.setType(Material.AIR);
                operatedBlocks.add(state);
            }
        }
    }
}
