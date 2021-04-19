package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

public class NewCylinderIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    double xS, yS, zS;
    int radMax;
    double radCorr;
    int height;
    int dirMaxX, dirMaxY, dirMaxZ;

    @Override
    public NewCylinderIterator newIterator(List<String> args, World world) {
        try {
            NewCylinderIterator iterator = new NewCylinderIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0)); // Center
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.radMax = Integer.parseInt(args.get(3)); // Radius
            iterator.radCorr = Double.parseDouble(args.get(4));
            iterator.height = Integer.parseInt(args.get(5)); // Height
            iterator.xS = Double.parseDouble(args.get(6)); // Scaling stuff
            iterator.yS = Double.parseDouble(args.get(7));
            iterator.zS = Double.parseDouble(args.get(8));
//	    iterator.dirMax = Math.max(iterator.height, iterator.radMax) + 2;
            iterator.dirMaxX = iterator.xS == 0 ? iterator.height : iterator.radMax;
            iterator.dirMaxY = iterator.yS == 0 ? iterator.height : iterator.radMax;
            iterator.dirMaxZ = iterator.zS == 0 ? iterator.height : iterator.radMax;
            iterator.totalBlocks = (2L * iterator.dirMaxX + 1) * (2L * iterator.dirMaxY + 1) * (2L * iterator.dirMaxZ + 1);
            iterator.x = -iterator.dirMaxX - 1;
            iterator.y = -iterator.dirMaxY;
            iterator.z = -iterator.dirMaxZ;
            while (iterator.y + iterator.yC < 0) {
                iterator.y++;
            }
            return iterator;
        } catch (Exception e) {
            Main.logError("Error creating new cylinder iterator. Please check your brush parameters.",
                    Operator.currentPlayer, e);
            return null;
        }
    }

    @Override
    public Block getNext() {
        while (true) {
            if (incrXYZ(dirMaxX, dirMaxY, dirMaxZ, xC, yC, zC)) {
                return null;
            }

            // Max radius check
            if ((x * x) * xS + (y * y) * yS + (z * z) * zS >= (radMax + radCorr) * (radMax + radCorr)) {
                continue;
            }

            // Height check

            break;
        }

        return iterWorld.getBlockAt(x + xC, y + yC, z + zC);
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
