package com._14ercooper.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class CylinderIterator extends BlockIterator {

    long totalBlocks;
//    long doneBlocks = 0;
//    int x, y, z;
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
	    iterator.totalBlocks = (2 * iterator.radMax + 1) * (2 * iterator.radMax + 1) * (2 * iterator.radMax + 1);
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
//	    if (y > radMax || y + yC > 255) {
//		return null;
//	    }
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
//	try {
//	    return Operator.currentPlayer.getWorld().getBlockAt(x + xC, y + yC, z + zC);
//	}
//	catch (NullPointerException e) {
//	    return Bukkit.getWorlds().get(0).getBlockAt(x + xC, y + yC, z + zC);
//	}
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
