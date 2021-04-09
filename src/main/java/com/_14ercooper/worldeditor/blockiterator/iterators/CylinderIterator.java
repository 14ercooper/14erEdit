package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

public class CylinderIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    double xS, yS, zS;
    int radMin, radMax;
    double radCorr;

    @Override
    public CylinderIterator newIterator(List<String> args, World world) {
        try {
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
        }
	catch (Exception e) {
	    Main.logError("Error creating cylinder iterator. Please check your brush parameters.",
		    Operator.currentPlayer, e);
	    return null;
	}
    }

    @Override
    public Block getNext() {
	while (true) {
        if (incrXYZ(radMax, radMax, radMax, xC, yC, zC)) {
            return null;
        }

	    // Max radius check
	    if ((x * x) * xS + (y * y) * yS + (z * z) * zS >= (radMax + radCorr) * (radMax + radCorr)) {
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
