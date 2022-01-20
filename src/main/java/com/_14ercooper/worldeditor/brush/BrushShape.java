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

package com._14ercooper.worldeditor.brush;

import com._14ercooper.worldeditor.async.AsyncManager;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class BrushShape {

    public final BlockIterator GetBlocks(double x, double y, double z, World world, CommandSender sender) {
        BlockIterator iter = this.GetBlocks_impl(x, y, z, world, sender);
        iter.setOrigin((int) x, (int) y, (int) z);
        return iter;
    }
    
    public abstract BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender);

    public abstract void addNewArgument(String argument);

    public abstract boolean lastInputProcessed();

    public abstract boolean gotEnoughArgs();

    public abstract int minArgCount();

    public int operatorCount() {
        return 1;
    }

    public void runBrush(List<Operator> operators, double x, double y, double z, Player currentPlayer) {
        // Build an array of all blocks to operate on
        BlockIterator blockArray = GetBlocks(x, y, z, currentPlayer.getWorld(), currentPlayer);

        if (blockArray.getTotalBlocks() == 0) {
            return;
        }
        Main.logDebug("Block array size is " + blockArray.getTotalBlocks()); // -----

        AsyncManager.scheduleEdit(operators.get(0), currentPlayer, blockArray);
    }
}
