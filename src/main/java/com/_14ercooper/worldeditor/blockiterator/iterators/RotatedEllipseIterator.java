package com._14ercooper.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;

import com._14ercooper.math.Point3;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class RotatedEllipseIterator extends BlockIterator {

    long totalBlocks;
//    long doneBlocks = 0;
//    int x, y, z;
    int xC, yC, zC;
    double hFD, strL, dX, dY, dZ;
    int radMax;
    int maxDist;

    @Override
    public RotatedEllipseIterator newIterator(List<String> args, World world) {
	try {
	    RotatedEllipseIterator iterator = new RotatedEllipseIterator();
	    iterator.iterWorld = world;
	    iterator.xC = Integer.parseInt(args.get(0)); // Center
	    iterator.yC = Integer.parseInt(args.get(1));
	    iterator.zC = Integer.parseInt(args.get(2));
	    iterator.hFD = Double.parseDouble(args.get(3)); // Half the distance between focal points
	    iterator.strL = Double.parseDouble(args.get(4)); // "String length" of ellipse
	    iterator.dX = Double.parseDouble(args.get(5)); // Direction from center to a focal point
	    iterator.dY = Double.parseDouble(args.get(6));
	    iterator.dZ = Double.parseDouble(args.get(7));
	    iterator.maxDist = (int) (iterator.strL) + 1;
	    iterator.totalBlocks = (2 * iterator.maxDist + 1) * (2 * iterator.maxDist + 1) * (2 * iterator.maxDist + 1);
	    iterator.x = -iterator.maxDist - 1;
	    iterator.y = -iterator.maxDist;
	    iterator.z = -iterator.maxDist;
	    iterator.radMax = iterator.maxDist;
	    while (y + yC < 0) {
		y++;
	    }
	    iterator.setup();
	    return iterator;
	}
	catch (Exception e) {
	    Main.logError("Error creating rotated ellipse iterator. Please check your brush parameters.",
		    Operator.currentPlayer);
	    return null;
	}
    }

    Point3 focus1, focus2, negCenter;

    private void setup() {
	Point3 dir = new Point3(dX, dY, dZ);
	Point3 center = new Point3(xC, yC, zC);
	dir.normalize();
	dir = dir.mult(hFD);
	focus1 = center.add(dir);
	focus2 = center.add(dir.mult(-1));
	negCenter = center;
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
//	    if (y > radMax || y > 255) {
//		return null;
//	    }
	    if (incrXYZ(radMax, radMax, radMax, xC, yC, zC)) {
		return null;
	    }

	    // Check that it's within the ellipse
	    // Get what would be the needed string length
	    Point3 blockPoint = new Point3(x, y, z);
	    blockPoint = blockPoint.add(negCenter);
	    double dist = blockPoint.distance(focus1) + blockPoint.distance(focus2);

	    // Make sure it's small enough
	    if (dist > strL)
		continue;

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
