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

package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiIterator extends BlockIterator {

    public final List<BlockIterator> childIterators = new ArrayList<>();

    @Override
    public BlockIterator newIterator(List<String> args, World world, CommandSender player) {
        Main.logError("MultiIterator does not support the standard constructor", player, null);
        return null;
    }

    public BlockIterator newIterator(Set<BlockIterator> children) {
        MultiIterator iter = new MultiIterator();
        iter.childIterators.addAll(children);
        return iter;
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {
        BlockWrapper next = null;
        while (next == null) {
            if (childIterators.isEmpty())
                return null;
            next = childIterators.get(0).getNextBlock(player, getBlock);
            if (next == null) {
                if (childIterators.isEmpty())
                    return null;
                childIterators.remove(0);
            }
        }
        return next;
    }

    @Override
    public long getTotalBlocks() {
        long total = 0;
        for (BlockIterator b : childIterators) {
            total += b.getTotalBlocks();
        }
        return total;
    }

    @Override
    public long getRemainingBlocks() {
        long total = 0;
        for (BlockIterator b : childIterators) {
            total += b.getRemainingBlocks();
        }
        return total;
    }

}
