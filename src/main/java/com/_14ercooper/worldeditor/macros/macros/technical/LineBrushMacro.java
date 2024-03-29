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

package com._14ercooper.worldeditor.macros.macros.technical;

import com._14ercooper.worldeditor.macros.MacroLauncher;
import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LineBrushMacro extends Macro {

    @Override
    public boolean performMacro(String[] args, Location loc, OperatorState state) {
        // Parse args
        Player p = (Player) state.getCurrentPlayer();
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
            int xPos = (int) ((int) (xStart - (xStep * t)) + 0.5);
            int yPos = (int) ((int) (yStart - (yStep * t)) + 0.5);
            int zPos = (int) ((int) (zStart - (zStep * t)) + 0.5);
            Block b = state.getCurrentWorld().getBlockAt(xPos, yPos, zPos);
            SetBlock.setMaterial(b, m, state.getCurrentUndo(), state.getCurrentPlayer());
        }

        // Return success
        return true;
    }

}
