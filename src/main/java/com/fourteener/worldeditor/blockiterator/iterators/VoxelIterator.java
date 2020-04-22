package com.fourteener.worldeditor.blockiterator.iterators;

import java.util.List;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.GlobalVars;

public class VoxelIterator extends BlockIterator {

	int x, y, z;
	boolean done = false;
	
	@Override
	public VoxelIterator newIterator(List<String> args) {
		VoxelIterator iterator = new VoxelIterator();
		iterator.x = Integer.parseInt(args.get(0));
		iterator.y = Integer.parseInt(args.get(1));
		iterator.z = Integer.parseInt(args.get(2));
		return iterator;
	}

	@Override
	public Block getNext() {
		if (!done) {
			done = true;
			return GlobalVars.world.getBlockAt(x, y, z);
		}
		return null;
	}

	@Override
	public long getTotalBlocks() {
		return 1;
	}

	@Override
	public long getRemainingBlocks() {
		return done ? 0 : 1;
	}

}
