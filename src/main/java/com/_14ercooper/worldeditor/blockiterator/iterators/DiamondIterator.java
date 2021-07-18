package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DiamondIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    int radius;

    @Override
    public DiamondIterator newIterator(List<String> args, World world, CommandSender player) {
        try {
            DiamondIterator iterator = new DiamondIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0));
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.radius = Integer.parseInt(args.get(3));
            iterator.totalBlocks = (2L * iterator.radius + 1) * (2L * iterator.radius + 1) * (2L * iterator.radius + 1);
            iterator.x = -iterator.radius - 1;
            iterator.y = -iterator.radius;
            iterator.z = -iterator.radius;
            return iterator;
        } catch (Exception e) {
            Main.logError("Error creating diamond iterator. Please check your brush parameters.",
                    player, e);
            return null;
        }
    }

    @Override
    public Block getNextBlock() {
        while (true) {
            if (incrXYZ(radius, radius, radius, xC, yC, zC)) {
                return null;
            }

            // Distance check
            if (Math.abs(x) + Math.abs(y) + Math.abs(z) > radius) {
                continue;
            }

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
