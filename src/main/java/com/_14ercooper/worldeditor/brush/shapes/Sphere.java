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
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends BrushShape {

    String radius = "";
    String correction = "0.5";
    int numArgsProcessed = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(radius);
        argList.add(Integer.toString(0));
        argList.add(correction);
        return IteratorManager.INSTANCE.getIterator("sphere").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (numArgsProcessed == 0) {
            radius = argument;
        } else if (numArgsProcessed == 1) {
            try {
                Double.parseDouble(argument);
                correction = argument;
            } catch (NumberFormatException e) {
                // This isn't a number, so start the operation parser
                numArgsProcessed++;
            }
        }
        numArgsProcessed++;
    }

    @Override
    public boolean lastInputProcessed() {
        return numArgsProcessed < 3;
    }

    @Override
    public boolean gotEnoughArgs() {
        return numArgsProcessed <= 0;
    }

    @Override
    public int minArgCount() {
        return 1;
    }

}
