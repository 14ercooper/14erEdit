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

public class CylinderIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    double xS, yS, zS;
    int radMax;
    double radCorr;

    @Override
    public CylinderIterator newIterator(List<String> arg, World world, CommandSender player) {
        try {
            List<String> args = new ArrayList<>();
            for (Object s : arg) {
                args.add((String) s);
            }
            CylinderIterator iterator = new CylinderIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0));
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.radMax = Integer.parseInt(args.get(3));
            iterator.radCorr = Double.parseDouble(args.get(4));
            iterator.xS = Double.parseDouble(args.get(5));
            iterator.yS = Double.parseDouble(args.get(6));
            iterator.zS = Double.parseDouble(args.get(7));
            iterator.totalBlocks = (2L * iterator.radMax + 1) * (2L * iterator.radMax + 1) * (2L * iterator.radMax + 1);
            iterator.x = -iterator.radMax - 1;
            iterator.y = -iterator.radMax;
            iterator.z = -iterator.radMax;
            while (iterator.y + iterator.yC < 0) {
                iterator.y++;
            }
            return iterator;
        } catch (Exception e) {
            Main.logError("Error creating cylinder iterator. Please check your brush parameters.",
                    player, e);
            return null;
        }
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {
        while (true) {
            if (incrXYZ(radMax, radMax, radMax, xC, yC, zC, player)) {
                return null;
            }

            // Max radius check
            if ((x * x) * xS + (y * y) * yS + (z * z) * zS >= (radMax + radCorr) * (radMax + radCorr)) {
                continue;
            }

            break;
        }

        if (getBlock) {
            return new BlockWrapper(iterWorld.getBlockAt(x + xC, y + yC, z + zC), x + xC, y + yC, z + zC);
        } else {
            return new BlockWrapper(null, x + xC, y + yC, z + zC);
        }
    }

    @Override
    public long getTotalBlocks() {
        return totalBlocks;
    }

    @Override
    public long getRemainingBlocks() {
        return totalBlocks - doneBlocks;
    }

}
