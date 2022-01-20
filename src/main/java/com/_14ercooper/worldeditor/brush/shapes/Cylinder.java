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

public class Cylinder extends BrushShape {

    String radius;
    String correction;
    String dimension;
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(radius);
        argList.add(correction);
        // Axis X
        if (dimension.equalsIgnoreCase("x")) {
            argList.add("0");
            argList.add("1");
            argList.add("1");
        }
        // Axis Y
        if (dimension.equalsIgnoreCase("y")) {
            argList.add("1");
            argList.add("0");
            argList.add("1");
        }
        // Axis Z
        if (dimension.equalsIgnoreCase("z")) {
            argList.add("1");
            argList.add("1");
            argList.add("0");
        }
        return IteratorManager.INSTANCE.getIterator("cylinder").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (gotArgs == 0) {
            radius = argument;
        } else if (gotArgs == 1) {
            dimension = argument;
        } else if (gotArgs == 2) {
            try {
                Double.parseDouble(argument);
                correction = argument;
            } catch (NumberFormatException e) {
                gotArgs++;
            }
        }
        gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return gotArgs < 4;
    }

    @Override
    public boolean gotEnoughArgs() {
        return gotArgs <= 1;
    }

    @Override
    public int minArgCount() {
        return 2;
    }

}
