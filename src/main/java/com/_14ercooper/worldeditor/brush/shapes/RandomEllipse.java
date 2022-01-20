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

package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RandomEllipse extends BrushShape {

    int xMin, xMax, yMin, yMax, zMin, zMax;
    String correction;
    int argsGot = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        // Generate the ellipse
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(Integer.toString(Main.randRange(xMin, xMax)));
        argList.add(Integer.toString(Main.randRange(yMin, yMax)));
        argList.add(Integer.toString(Main.randRange(zMin, zMax)));
        argList.add(correction);
        return IteratorManager.INSTANCE.getIterator("ellipse").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (argsGot == 0) {
            xMin = Integer.parseInt(argument);
        } else if (argsGot == 1) {
            xMax = Integer.parseInt(argument);
        } else if (argsGot == 2) {
            yMin = Integer.parseInt(argument);
        } else if (argsGot == 3) {
            yMax = Integer.parseInt(argument);
        } else if (argsGot == 4) {
            zMin = Integer.parseInt(argument);
        } else if (argsGot == 5) {
            zMax = Integer.parseInt(argument);
        } else if (argsGot == 6) {
            correction = argument;
        }
        argsGot++;
    }

    @Override
    public boolean lastInputProcessed() {
        return argsGot < 8;
    }

    @Override
    public boolean gotEnoughArgs() {
        return argsGot <= 6;
    }

    @Override
    public int minArgCount() {
        return 7;
    }

}
