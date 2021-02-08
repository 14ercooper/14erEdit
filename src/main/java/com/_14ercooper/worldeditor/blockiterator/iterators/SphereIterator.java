package com._14ercooper.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class SphereIterator extends BlockIterator {

    long totalBlocks;
//    long doneBlocks = 0;
//    int x, y, z;
    int xC, yC, zC;
    int radMin, radMax;
    double radCorr;

    @Override
    public SphereIterator newIterator(List<String> args) {
	try {
	    SphereIterator iterator = new SphereIterator();
	    iterator.xC = Integer.parseInt(args.get(0));
	    iterator.yC = Integer.parseInt(args.get(1));
	    iterator.zC = Integer.parseInt(args.get(2));
	    iterator.radMax = Integer.parseInt(args.get(3));
	    iterator.radMin = Integer.parseInt(args.get(4));
	    iterator.radCorr = Double.parseDouble(args.get(5));
	    iterator.totalBlocks = (2 * iterator.radMax + 1) * (2 * iterator.radMax + 1) * (2 * iterator.radMax + 1);
	    iterator.x = -iterator.radMax - 1;
	    iterator.y = -iterator.radMax;
	    iterator.z = -iterator.radMax;
	    while (y + yC < 0) {
		y++;
	    }
	    return iterator;
	}
	catch (Exception e) {
	    Main.logError("Error creating sphere iterator. Please check your brush parameters.",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public Block getNext() {
	while (true) {
//	    x++;
//	    doneBlocks++;
//	    if (x > radMax) {
//		z++;
//		x = -radMax;
//	    }
//	    if (z > radMax) {
//		y++;
//		z = -radMax;
//	    }
	    if (incrXYZ(radMax, radMax, radMax, xC, yC, zC)) {
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
