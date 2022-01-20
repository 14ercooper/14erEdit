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
import com._14ercooper.worldeditor.player.PlayerManager;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Column extends BrushShape {

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) PlayerManager.getPlayerWrapper(sender).getMinEditY()));
        argList.add(Integer.toString((int) z));
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) PlayerManager.getPlayerWrapper(sender).getMaxEditY()));
        argList.add(Integer.toString((int) z));
        return IteratorManager.INSTANCE.getIterator("cube").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        // Zero argument brush, do nothing

    }

    @Override
    public boolean lastInputProcessed() {
        return false;
    }

    @Override
    public boolean gotEnoughArgs() {
        return false;
    }

    @Override
    public int minArgCount() {
        return 0;
    }

}
