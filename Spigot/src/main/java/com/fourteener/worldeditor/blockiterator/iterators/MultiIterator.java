package com.fourteener.worldeditor.blockiterator.iterators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.block.Block;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.main.Main;
import com.fourteener.worldeditor.operations.Operator;

public class MultiIterator extends BlockIterator {

	public List<BlockIterator> childIterators = new ArrayList<BlockIterator>();
	
	@Override
	public BlockIterator newIterator(List<String> args) {
		Main.logError("MultiIterator does not support the standard constructor", Operator.currentPlayer);
		return null;
	}
	
	public BlockIterator newIterator (Set<BlockIterator> children) {
		MultiIterator iter = new MultiIterator();
		iter.childIterators.addAll(children);
		return iter;
	}

	@Override
	public Block getNext() {
		Block next = null;
		while (next == null) {
			if (childIterators.isEmpty()) return null;
			next = childIterators.get(0).getNext();
			if (next == null) {
				if (childIterators.isEmpty()) return null;
				childIterators.remove(0);
			}
		}
		return next;
	}

	@Override
	public long getTotalBlocks() {
		long total = 0;
		for (BlockIterator b : childIterators) {
			total += b.getTotalBlocks();
		}
		return total;
	}

	@Override
	public long getRemainingBlocks() {
		long total = 0;
		for (BlockIterator b : childIterators) {
			total += b.getRemainingBlocks();
		}
		return total;
	}

}
