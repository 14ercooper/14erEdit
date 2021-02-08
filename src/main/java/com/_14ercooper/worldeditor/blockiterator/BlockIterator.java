package com._14ercooper.worldeditor.blockiterator;

import java.util.List;

import org.bukkit.block.Block;

public abstract class BlockIterator {
    // Returns a new instance of the block iterator based on the passed arguments
    // First 3 are the origin of the iterator, the rest vary
    public abstract BlockIterator newIterator(List<String> args);

    // Gets the next block in this block iterator
    public abstract Block getNext();

    // Gets the number of total blocks in this iterator
    // This cannot underestimate, but may slightly overestimate
    public abstract long getTotalBlocks();

    // Gets the number of remaining blocks in this iterator
    public abstract long getRemainingBlocks();
    
    // Increment cubic XYZ. Returns true when time to end
    public int x, y, z;
    public long doneBlocks = 0;
    public boolean incrXYZ(int radX, int radY, int radZ, int xOff, int yOff, int zOff) {
	x++;
	doneBlocks++;
	if (x > radX) {
	    z++;
	    x = -radX;
	}
	if (z > radZ) {
	    y++;
	    z = -radZ;
	}
	
	if (y > radY || y + yOff > 255) {
		return true;
	}
	
	return false;
    }
}
