package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SphereIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    int radMin, radMax;
    double radCorr;

    @Override
    public SphereIterator newIterator(List<String> args, World world, CommandSender player) {
        try {
            SphereIterator iterator = new SphereIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0));
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.radMax = Integer.parseInt(args.get(3));
            iterator.radMin = Integer.parseInt(args.get(4));
            iterator.radCorr = Double.parseDouble(args.get(5));
            iterator.totalBlocks = (2L * iterator.radMax + 1) * (2L * iterator.radMax + 1) * (2L * iterator.radMax + 1);
            iterator.x = -iterator.radMax - 1;
            iterator.y = -iterator.radMax;
            iterator.z = -iterator.radMax;
            return iterator;
        } catch (Exception e) {
            Main.logError("Error creating sphere iterator. Please check your brush parameters.",
                    player, e);
            return null;
        }
    }

    @Override
    public Block getNextBlock(CommandSender player) {
        while (true) {
            if (incrXYZ(radMax, radMax, radMax, xC, yC, zC, player)) {
                return null;
            }

//	    if (y > radMax || y + yC > 255) {
//		return null;
//	    }

            // Max radius check
            if (x * x + y * y + z * z >= (radMax + radCorr) * (radMax + radCorr)) {
                continue;
            }

            // Min radius check
            if (radMin > 1 && x * x + y * y + z * z <= (radMin - radCorr) * (radMin - radCorr)) {
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
