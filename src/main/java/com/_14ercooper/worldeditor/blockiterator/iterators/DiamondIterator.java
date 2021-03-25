package com._14ercooper.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class DiamondIterator extends BlockIterator {

    long totalBlocks;
//    long doneBlocks = 0;
//    int x, y, z;
    int xC, yC, zC;
    int radius;

    @Override
    public DiamondIterator newIterator(List<String> args, World world) {
	try {
	    DiamondIterator iterator = new DiamondIterator();
	    iterator.iterWorld = world;
	    iterator.xC = Integer.parseInt(args.get(0));
	    iterator.yC = Integer.parseInt(args.get(1));
	    iterator.zC = Integer.parseInt(args.get(2));
	    iterator.radius = Integer.parseInt(args.get(3));
	    iterator.totalBlocks = (2 * iterator.radius + 1) * (2 * iterator.radius + 1) * (2 * iterator.radius + 1);
	    iterator.x = -iterator.radius - 1;
	    iterator.y = -iterator.radius;
	    iterator.z = -iterator.radius;
	    return iterator;
	}
	catch (Exception e) {
	    Main.logError("Error creating diamond iterator. Please check your brush parameters.",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public Block getNext() {
	while (true) {
//	    x++;
//	    doneBlocks++;
//	    if (x > radius) {
//		z++;
//		x = -radius;
//	    }
//	    if (z > radius) {
//		y++;
//		z = -radius;
//	    }
//	    if (y > radius) {
//		return null;
//	    }
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
