package com.fourteener.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.GlobalVars;

public class DomeIterator extends BlockIterator {

	long totalBlocks;
	long doneBlocks = 0;
	int x, y, z;
	int xC, yC, zC;
	int xMin, xMax, yMin, yMax, zMin, zMax;
	int radMin, radMax;
	double radCorr;
	
	@Override
	public DomeIterator newIterator(List<String> args) {
		DomeIterator iterator = new DomeIterator();
		iterator.xMin = Integer.parseInt(args.get(0));
		iterator.yMin = Integer.parseInt(args.get(1));
		iterator.zMin = Integer.parseInt(args.get(2));
		iterator.xMax = Integer.parseInt(args.get(3));
		iterator.yMax = Integer.parseInt(args.get(4));
		iterator.zMax = Integer.parseInt(args.get(5));
		iterator.radMax = Integer.parseInt(args.get(6));
		iterator.radMin = Integer.parseInt(args.get(7));
		iterator.radCorr = Double.parseDouble(args.get(8));
		iterator.radCorr = Double.parseDouble(args.get(9));
		iterator.radCorr = Double.parseDouble(args.get(10));
		iterator.radCorr = Double.parseDouble(args.get(11));
		iterator.totalBlocks = (2 * iterator.radMax + 1) *(2 * iterator.radMax + 1) *(2 * iterator.radMax + 1);
		iterator.x = iterator.xMin - 1;
		iterator.y = iterator.yMin;
		iterator.z = iterator.zMin;
		return iterator;
	}

	@Override
	public Block getNext() {
		while (true) {
			x++;
			doneBlocks++;
			if (x > xMax) {
				z++;
				x = xMin;
			}
			if (z > zMax) {
				y++;
				z = zMin;
			}
			if (y > yMax) {
				return null;
			}
			
			// Max radius check
			if (x*x + y*y + z*z >= (radMax + radCorr)*(radMax + radCorr)) {
				continue;
			}
			
			// Min radius check
			else if (x*x + y*y + z*z <= (radMin - radCorr)*(radMin - radCorr)) {
				continue;
			}
			
			break;
		}

		return GlobalVars.world.getBlockAt(x + xC, y + yC, z + zC);
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
