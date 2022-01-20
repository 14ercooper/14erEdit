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

public class RandomCube extends BrushShape {

    int sideMin, sideMax;
    int argsSeen = 0;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
//	int cubeRad = (int) (rand.nextInt(sideMax - sideMin) + sideMin / 2);
        int cubeRad = Main.randRange(sideMin, sideMax) / 2;
        argList.add(Integer.toString((int) x - cubeRad));
        argList.add(Integer.toString((int) y - cubeRad));
        argList.add(Integer.toString((int) z - cubeRad));
        argList.add(Integer.toString((int) x + cubeRad));
        argList.add(Integer.toString((int) y + cubeRad));
        argList.add(Integer.toString((int) z + cubeRad));
        return IteratorManager.INSTANCE.getIterator("cube").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (argsSeen == 0) {
            sideMin = Integer.parseInt(argument);
        } else if (argsSeen == 1) {
            sideMax = Integer.parseInt(argument);
        }
        argsSeen++;
    }

    @Override
    public boolean lastInputProcessed() {
        return argsSeen < 3;
    }

    @Override
    public boolean gotEnoughArgs() {
        return argsSeen <= 1;
    }

    @Override
    public int minArgCount() {
        return 2;
    }

}
