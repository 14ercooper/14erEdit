package com.fourteener.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.GlobalVars;

public class EllipseIterator extends BlockIterator {

	long totalBlocks;
	long doneBlocks = 0;
	int x, y, z;
	int xC, yC, zC;
	int rx, ry, rz;
	double radCorr;
	
	@Override
	public EllipseIterator newIterator(List<String> args) {
		EllipseIterator iterator = new EllipseIterator();
		iterator.xC = Integer.parseInt(args.get(0));
		iterator.yC = Integer.parseInt(args.get(1));
		iterator.zC = Integer.parseInt(args.get(2));
		iterator.rx = Integer.parseInt(args.get(3));
		iterator.ry = Integer.parseInt(args.get(4));
		iterator.rz = Integer.parseInt(args.get(5));
		iterator.radCorr = Double.parseDouble(args.get(6));
		iterator.totalBlocks = (2 * iterator.rx + 1) *(2 * iterator.ry + 1) *(2 * iterator.rz + 1);
		iterator.x = -iterator.rx - 1;
		iterator.y = -iterator.ry;
		iterator.z = -iterator.rz;
		return iterator;
	}

	@Override
	public Block getNext() {
		while (true) {
			x++;
			doneBlocks++;
			if (x > rx) {
				z++;
				x = -rx;
			}
			if (z > rz) {
				y++;
				z = -rz;
			}
			if (y > ry) {
				return null;
			}
			
			// Radius check
			if ((x*x) / rx + (y*y) / ry + (z*z) / rz >= (1 + radCorr)) {
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
