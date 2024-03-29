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

import com._14ercooper.math.Point3;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.BlockWrapper;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlobIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    int radius;
    int vectorCount;
    double vectorAmplitude;
    double radiusBoost, radiusCorrection;

    int boostedRadius;
    List<Point3> vectors;
    List<Double> amplitudes;

    @Override
    public BlobIterator newIterator(List<String> args, World world, CommandSender player) {
        try {
            BlobIterator iterator = new BlobIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0));
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.radius = Integer.parseInt(args.get(3));
            iterator.vectorCount = Integer.parseInt(args.get(4));
            iterator.vectorAmplitude = Double.parseDouble(args.get(5));
            iterator.radiusBoost = Double.parseDouble(args.get(6));
            iterator.radiusCorrection = Double.parseDouble(args.get(7));

            iterator.boostedRadius = (int) (iterator.radius * (iterator.vectorAmplitude + iterator.radiusBoost)) + 1;
            if (iterator.vectorCount < 1) {
                iterator.vectorCount = 1;
            }
            Random random = new Random();
            iterator.vectors = new ArrayList<>(iterator.vectorCount);
            iterator.amplitudes = new ArrayList<>(iterator.vectorCount);
            for (int i = 0; i < iterator.vectorCount; i++) {
                Point3 point = new Point3(randomDouble(random), randomDouble(random), randomDouble(random));
                point.normalize();
                iterator.vectors.add(point);
                iterator.amplitudes.add(iterator.vectorAmplitude * (1.0 / (i + 1)));
            }

            iterator.totalBlocks = (2L * iterator.boostedRadius + 1) * (2L * iterator.boostedRadius + 1) * (2L * iterator.boostedRadius + 1);
            iterator.x = -iterator.boostedRadius - 1;
            iterator.y = -iterator.boostedRadius;
            iterator.z = -iterator.boostedRadius;

            return iterator;
        }
        catch (Exception e) {
            Main.logError("Error creating blob iterator. Please check your brush parameters.", player, e);
            return null;
        }
    }

    @Override
    public BlockWrapper getNextBlock(CommandSender player, boolean getBlock) {
        while (true) {
            if (incrXYZ(boostedRadius, boostedRadius, boostedRadius, xC, yC, zC, player)) {
                return null;
            }

            // Bias the radius
            Point3 point = new Point3(x, y, z);
            Point3 pointNorm = new Point3(x,y,z);
            pointNorm.normalize();
            double modAmp = 0;
            for (int i = 0; i < vectorCount; i++) {
                double dot = pointNorm.dot(vectors.get(i));
                if (dot < 0) {
                    continue;
                }
                modAmp += dot * amplitudes.get(i) * radius;
            }
            double radius = point.magnitude() + modAmp;

            // Check the radius
            if (radius * radius >= (this.radius + radiusCorrection) * (this.radius + radiusCorrection)) {
                continue;
            }

            break;
        }

        if (getBlock) {
            return new BlockWrapper(iterWorld.getBlockAt(x + xC, y + yC, z + zC), x + xC, y + yC, z + zC);
        }
        else {
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

    private double randomDouble(Random random) {
        return -1 + (2 * random.nextDouble());
    }
}
