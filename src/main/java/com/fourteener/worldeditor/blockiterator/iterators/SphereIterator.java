package com.fourteener.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.GlobalVars;

public class SphereIterator extends BlockIterator {

	long totalBlocks;
	long doneBlocks = 0;
	int x, y, z;
	int xC, yC, zC;
	int radMin, radMax;
	double radCorr;
	
	@Override
	public SphereIterator newIterator(List<String> args) {
		SphereIterator iterator = new SphereIterator();
		iterator.xC = Integer.parseInt(args.get(0));
		iterator.yC = Integer.parseInt(args.get(1));
		iterator.zC = Integer.parseInt(args.get(2));
		iterator.radMax = Integer.parseInt(args.get(3));
		iterator.radMin = Integer.parseInt(args.get(4));
		iterator.radCorr = Double.parseDouble(args.get(5));
		iterator.totalBlocks = (2 * iterator.radMax + 1) *(2 * iterator.radMax + 1) *(2 * iterator.radMax + 1);
		iterator.x = -iterator.radMax - 1;
		iterator.y = -iterator.radMax;
		iterator.z = -iterator.radMax;
		return null;
	}

	@Override
	public Block getNext() {
		while (true) {
			x++;
			doneBlocks++;
			if (x > radMax) {
				z++;
				x = -radMax;
			}
			if (z > radMax) {
				y++;
				z = -radMax;
			}
			if (y > radMax) {
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

		return GlobalVars.world.getBlockAt(x, y, z);
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
