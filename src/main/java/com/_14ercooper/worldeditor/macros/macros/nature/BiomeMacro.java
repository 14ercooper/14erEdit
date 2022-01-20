/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

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
