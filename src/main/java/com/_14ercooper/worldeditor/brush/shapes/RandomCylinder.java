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

public class RandomCylinder extends BrushShape {

    int radiusMin, radiusMax;
    String correction, dimension;
    int argsGot = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
//	argList.add(Integer.toString(rand.nextInt(radiusMax - radiusMin) + radiusMin));
        argList.add(Integer.toString(Main.randRange(radiusMin, radiusMax)));
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
        if (argsGot == 0) {
            radiusMax = Integer.parseInt(argument);
        } else if (argsGot == 1) {
            radiusMax = Integer.parseInt(argument);
        } else if (argsGot == 2) {
            correction = argument;
        } else if (argsGot == 3) {
            dimension = argument;
        }

        argsGot++;
    }

    @Override
    public boolean lastInputProcessed() {
        return argsGot < 5;
    }

    @Override
    public boolean gotEnoughArgs() {
        return argsGot <= 3;
    }

    @Override
    public int minArgCount() {
        return 4;
    }

}
