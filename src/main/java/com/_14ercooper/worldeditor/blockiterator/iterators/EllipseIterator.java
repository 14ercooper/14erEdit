package com._14ercooper.worldeditor.blockiterator.iterators;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

public class EllipseIterator extends BlockIterator {

    long totalBlocks;
    int xC, yC, zC;
    double rx, ry, rz;
    double radCorr;

    @Override
    public EllipseIterator newIterator(List<String> args, World world) {
        try {
            EllipseIterator iterator = new EllipseIterator();
            iterator.iterWorld = world;
            iterator.xC = Integer.parseInt(args.get(0));
            iterator.yC = Integer.parseInt(args.get(1));
            iterator.zC = Integer.parseInt(args.get(2));
            iterator.rx = Integer.parseInt(args.get(3));
            iterator.ry = Integer.parseInt(args.get(4));
            iterator.rz = Integer.parseInt(args.get(5));
            iterator.radCorr = Double.parseDouble(args.get(6));
            iterator.totalBlocks = (2L * (int) iterator.rx + 1) * (2L * (int) iterator.ry + 1)
                    * (2L * (int) iterator.rz + 1);
            iterator.x = (int) (-iterator.rx - 1);
            iterator.y = (int) -iterator.ry;
            iterator.z = (int) -iterator.rz;
            return iterator;
        }
	catch (Exception e) {
	    Main.logError("Error creating ellipse iterator. Please check your brush parameters.",
		    Operator.currentPlayer, e);
	    return null;
	}
    }

    @Override
    public Block getNext() {
	while (true) {
        if (incrXYZ((int) rx, (int) ry, (int) rz, xC, yC, zC)) {
            return null;
        }

	    // Radius check
	    if ((x * x / (rx * rx)) + (y * y / (ry * ry)) + (z * z / (rz * rz)) > 1 + radCorr) {
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
