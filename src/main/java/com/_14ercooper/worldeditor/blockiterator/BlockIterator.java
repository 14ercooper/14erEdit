package com._14ercooper.worldeditor.blockiterator;

import com._14ercooper.worldeditor.main.GlobalVars;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

public abstract class BlockIterator {
    // Returns a new instance of the block iterator based on the passed arguments
    // First 3 are the origin of the iterator, the rest vary
    public abstract BlockIterator newIterator(List<String> args, World world);

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
    protected World iterWorld;

    public boolean incrXYZ(int radX, int radY, int radZ, int xOff, int yOff, int zOff) {

	x++;
	doneBlocks++;
	if (x > radX || x + xOff > GlobalVars.maxEditX) {
	    y++;
	    x = -radX;

	    if (x + xOff < GlobalVars.minEditX) {
		x = (int) GlobalVars.minEditX - xOff;
	    }
	}
	if (y > radY || y + yOff > GlobalVars.maxEditY) {
	    z++;

	    if (z + zOff < GlobalVars.minEditZ) {
		z = (int) GlobalVars.minEditZ - zOff;
	    }

	    y = -radY;
	    while (y + yOff < GlobalVars.minEditY) {
		y++;
		doneBlocks++;
	    }
	}

        return z > radZ || z + zOff > GlobalVars.maxEditZ;
    }
}
