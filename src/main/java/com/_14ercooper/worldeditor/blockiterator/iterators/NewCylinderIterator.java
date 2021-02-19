package com._14ercooper.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class NewCylinderIterator extends BlockIterator {

    long totalBlocks;
//    long doneBlocks = 0;
//    int x, y, z;
    int xC, yC, zC;
    double xS, yS, zS;
    int radMax;
    double radCorr;
    int height;
    int dirMax;

    @Override
    public NewCylinderIterator newIterator(List<String> args) {
	try {
	    NewCylinderIterator iterator = new NewCylinderIterator();
	    iterator.xC = Integer.parseInt(args.get(0)); // Center
	    iterator.yC = Integer.parseInt(args.get(1));
	    iterator.zC = Integer.parseInt(args.get(2));
	    iterator.radMax = Integer.parseInt(args.get(3)); // Radius
	    iterator.radCorr = Double.parseDouble(args.get(4));
	    iterator.height = Integer.parseInt(args.get(5)); // Height
	    iterator.xS = Double.parseDouble(args.get(6)); // Scaling stuff
	    iterator.yS = Double.parseDouble(args.get(7));
	    iterator.zS = Double.parseDouble(args.get(8));
	    iterator.dirMax = Math.max(iterator.height, iterator.radMax) + 2;
	    System.out.println(iterator.dirMax);
	    iterator.totalBlocks = (2 * iterator.dirMax + 1) * (2 * iterator.dirMax + 1) * (2 * iterator.dirMax + 1);
	    iterator.x = -iterator.dirMax - 1;
	    iterator.y = -iterator.dirMax;
	    iterator.z = -iterator.dirMax;
	    while (iterator.y + iterator.yC < 0) {
		iterator.y++;
	    }
	    return iterator;
	}
	catch (Exception e) {
	    Main.logError("Error creating new cylinder iterator. Please check your brush parameters.",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public Block getNext() {
	while (true) {
//	    x++;
//	    doneBlocks++;
//	    if (x > dirMax) {
//		z++;
//		x = -dirMax;
//	    }
//	    if (z > dirMax) {
//		y++;
//		z = -dirMax;
//	    }
//	    if (y > dirMax || y + yC > 255) {
//		return null;
//	    }
	    if (incrXYZ(dirMax, dirMax, dirMax, xC, yC, zC)) {
		return null;
	    }

	    // Max radius check
	    if ((x * x) * xS + (y * y) * yS + (z * z) * zS >= (radMax + radCorr) * (radMax + radCorr)) {
		continue;
	    }

	    break;
	}

	try {
	    return Operator.currentPlayer.getWorld().getBlockAt(x + xC, y + yC, z + zC);
	}
	catch (NullPointerException e) {
	    return Bukkit.getWorlds().get(0).getBlockAt(x + xC, y + yC, z + zC);
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
