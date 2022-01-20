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

import com._14ercooper.math.Line;
import com._14ercooper.math.Point3;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SpikeIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    double bSize, h, dX, dY, dZ;
    double rMin;
    int radMax;

    @Override
    public SpikeIterator newIterator(List<String> arg, World world, CommandSender player) {
        try {
            List<String> args = new ArrayList<>();
            for (Object s : arg) {
                args.add((String) s);
            }
            SpikeIterator iterator = new SpikeIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0)); // Base center
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.bSize = Double.parseDouble(args.get(3)); // Base size
            iterator.rMin = Double.parseDouble(args.get(4)); // Base size min
            iterator.h = Double.parseDouble(args.get(5)); // Height
            iterator.dX = Double.parseDouble(args.get(6)); // Second point in spike
            iterator.dY = Double.parseDouble(args.get(7));
            iterator.dZ = Double.parseDouble(args.get(8));
            iterator.radMax = (int) Math.max(iterator.h, iterator.bSize) + 1;
            iterator.totalBlocks = (2L * iterator.radMax + 1) * (2L * iterator.radMax + 1) * (2L * iterator.radMax + 1);
            iterator.x = -iterator.radMax - 1;
            iterator.y = -iterator.radMax;
            iterator.z = -iterator.radMax;
            while (y + yC < 0) {
                y++;
            }
            iterator.setup();
            return iterator;
        } catch (Exception e) {
            Main.logError("Error creating spike iterator. Please check your brush parameters.", player, e);
            return null;
        }
    }

    Point3 basePos;
    Line spikeLine;

    private void setup() {
        basePos = new Point3(xC, yC, zC);
        Point3 secondPoint = new Point3(dX, dY, dZ);
        spikeLine = new Line(basePos, secondPoint);
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {
        while (true) {
            if (incrXYZ(radMax, radMax, radMax, xC, yC, zC, player)) {
                return null;
            }

            // Is in spike logic
            // Figure out the block's distances
            Point3 blockPos = new Point3(x, y, z);
            blockPos = blockPos.add(basePos);
            double distToLine = spikeLine.distanceTo(blockPos);
            double h0 = spikeLine.distanceFromFirst(spikeLine.closestPoint(blockPos));
            double radMi = rMin - ((rMin * h0) / (h + 0.0001));
            double radMa = bSize - ((bSize * h0) / (h + 0.0001));
//	    Main.logDebug("dists " + distToLine + " " + h0 + " " + radMi + " " + radMa);

            // Check they are in compliance
            if (distToLine > radMa || (distToLine < radMi && radMi > 0.05))
                continue;

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
