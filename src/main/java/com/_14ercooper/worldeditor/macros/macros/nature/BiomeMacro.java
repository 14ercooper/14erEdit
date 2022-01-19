// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.macros.macros.nature;

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BiomeMacro extends Macro {

    double radius;
    Biome biome;
    Location pos;

    // Create a new macro
    private void SetupMacro(String[] args, Location loc, OperatorState state) {
        try {
            radius = Double.parseDouble(args[0]);
        } catch (Exception e) {
            Main.logError("Could not parse biome macro. " + args[0] + " is not a number.", state.getCurrentPlayer(), e);
        }
        try {
            biome = Biome.valueOf(args[1].toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            Main.logError("Could not parse biome macro. " + args[1] + " is not a known biome.", state.getCurrentPlayer(), e);
        }
        pos = loc;
    }

    // Run this macro
    @Override
    public boolean performMacro(String[] args, Location loc, OperatorState state) {
        SetupMacro(args, loc, state);

        // Location of the brush
        double x = pos.getX();
        double z = pos.getZ();
        double y = pos.getY();

        // Generate the sphere
        int radiusInt = (int) Math.round(radius);
        List<Block> blockArray = new ArrayList<>();
        for (int rx = -radiusInt; rx <= radiusInt; rx++) {
            for (int rz = -radiusInt; rz <= radiusInt; rz++) {
                for (int ry = -radiusInt; ry < radiusInt; ry++) {
                    if (rx * rx + rz * rz + ry * ry <= (radius + 0.5) * (radius + 0.5)) {
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

        // OPERATE
        for (BlockState bs : snapshotArray) {
            state.getCurrentWorld().setBiome(bs.getX(), bs.getY(), bs.getZ(), biome);
        }

        return true;
    }
}
