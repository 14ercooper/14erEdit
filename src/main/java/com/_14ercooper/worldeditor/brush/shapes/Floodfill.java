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

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Floodfill extends BrushShape {

    private boolean gotArg = false;
    private int seenArgs = 0;
    private String depth;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(depth);
        return IteratorManager.INSTANCE.getIterator("floodfill").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        if (!gotArg) {
            depth = argument;
        }
        gotArg = true;
        seenArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return seenArgs <= 1;
    }

    @Override
    public boolean gotEnoughArgs() {
        return !gotArg;
    }

    @Override
    public int minArgCount() {
        return 1;
    }

    @Override
    public int operatorCount() {
        return 2;
    }

    @Override
    public void runBrush(List<Operator> operators, double x, double y, double z, Player currentPlayer) {
        // Build an array of all blocks to operate on
        BlockIterator blockArray = GetBlocks(x, y, z, currentPlayer.getWorld(), currentPlayer);
        blockArray.setObjectArgs("FloodfillCondition", operators.get(0));

        if (blockArray.getTotalBlocks() == 0) {
            return;
        }
        Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

        AsyncManager.scheduleEdit(operators.get(1), currentPlayer, blockArray);
    }
}
