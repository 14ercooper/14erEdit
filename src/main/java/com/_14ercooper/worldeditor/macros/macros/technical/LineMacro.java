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

import com._14ercooper.worldeditor.macros.macros.Macro;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.main.SetBlock;
import com._14ercooper.worldeditor.operations.OperatorState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class LineMacro extends Macro {

    @Override
    public boolean performMacro(String[] args, Location loc, OperatorState state) {
        int x1, x2, y1, y2, z1, z2;
        double dx, dy, dz;
        Material mat;
        try {
            x1 = Integer.parseInt(args[0]);
            y1 = Integer.parseInt(args[1]);
            z1 = Integer.parseInt(args[2]);
            x2 = Integer.parseInt(args[3]);
            y2 = Integer.parseInt(args[4]);
            z2 = Integer.parseInt(args[5]);
            mat = Material.matchMaterial(args[6]);
        } catch (Exception e) {
            Main.logError("Error parsing line macro. Did you provide start and end coordinates and a block material?",
                    state.getCurrentPlayer(), e);
            return false;
        }
        dx = (x1 - x2) / 1000f;
        dy = (y1 - y2) / 1000f;
        dz = (z1 - z2) / 1000f;

        Main.logDebug("Placing line from " + x1 + "," + y1 + "," + z1 + " in direction " + dx + "," + dy + "," + dz);

        double x = x1, y = y1, z = z1;
        for (int i = 0; i < 1000; i++) {
            Block b = state.getCurrentWorld().getBlockAt((int) (x + 0.5), (int) (y + 0.5), (int) (z + 0.5));
            SetBlock.setMaterial(b, mat, state.getCurrentUndo(), state.getCurrentPlayer());
            x += dx;
            y += dy;
            z += dz;
        }
        return true;
    }

}
