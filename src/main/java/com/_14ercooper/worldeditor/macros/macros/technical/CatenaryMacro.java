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

public class CatenaryMacro extends Macro {

    @Override
    public boolean performMacro(String[] args, Location loc, OperatorState state) {

        try {
            double x0 = Double.parseDouble(args[0]), y0 = Double.parseDouble(args[1]), z0 = Double.parseDouble(args[2]),
                    dx = Double.parseDouble(args[3]), dy = Double.parseDouble(args[4]),
                    dy2 = Double.parseDouble(args[5]), dz = Double.parseDouble(args[6]),
                    step = Double.parseDouble(args[7]);
            String block = args[8];

            Main.logDebug("Performing catenary with parameters: " + x0 + "," + x0 + "," + y0 + "," + z0 + "," + dx + ","
                    + dy + "," + dy2 + "," + dz + "," + step + "," + block);

            double t = 0f;
            Main.logDebug("" + (1f + (step / 2f)));
            for (; t < 1f + (step / 2f); t += step) {
                int x = (int) ((int) (x0 + (t * dx)) + 0.5);
                int y = (int) ((int) (y0 + (t * dy) + (t * t * dy2)) + 0.5);
                int z = (int) ((int) (z0 + (t * dz)) + 0.5);
                Block b = state.getCurrentWorld().getBlockAt(x, y, z);
                SetBlock.setMaterial(b, Material.matchMaterial(block), state.getCurrentUndo(), state.getCurrentPlayer());
            }
        } catch (Exception e) {
            Main.logError(
                    "Could not parse catenary macro. Did you pass in the correct 8 numerical arguments and material?",
                    state.getCurrentPlayer(), e);
            return false;
        }

        return true;
    }

}
