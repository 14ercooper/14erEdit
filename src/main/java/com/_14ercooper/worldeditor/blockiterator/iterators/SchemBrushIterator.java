package com._14ercooper.worldeditor.blockiterator.iterators;

import java.io.IOException;
import java.util.List;

import org.bukkit.block.Block;

import com._14ercooper.schematics.SchemLite;
import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class SchemBrushIterator extends BlockIterator {

    SchemLite schem;
    BlockIterator schemIter;

    // Statics
    public static String blockType = "";
    public static String blockData = "";
    public static String nbt = "";

    @Override
    public BlockIterator newIterator(List<String> args) {
	try {
	    SchemBrushIterator iter = new SchemBrushIterator();
	    int x = Integer.parseInt(args.get(0));
	    int y = Integer.parseInt(args.get(1));
	    int z = Integer.parseInt(args.get(2));
	    iter.schem = new SchemLite(args.get(3), true, 0);
	    iter.schem.openRead();
	    iter.schemIter = iter.schem.getIterator(x - (iter.schem.getXSize() / 2), y - (iter.schem.getYSize() / 2),
		    z - (iter.schem.getZSize() / 2));
	    return iter;
	}
	catch (Exception e) {
	    Main.logError("Could not create schem brush iterator", Operator.currentPlayer);
	    return null;
	}
    }

    public void cleanup() {
	try {
	    schem.closeRead();
	}
	catch (Exception e) {
	    // This isn't a problem
	}
    }

    @Override
    public Block getNext() {
	// Update the schem block
	try {
	    String[] data = schem.readNext();
	    blockType = data[0];
	    blockData = data[1];
	    nbt = data[2];
	}
	catch (IOException e) {
	    Main.logError("Could not read next block from schematic.", Operator.currentPlayer);
	    blockType = blockData = nbt = "";
	}

	// Return the next world block
	return schemIter.getNext();
    }

    @Override
    public long getTotalBlocks() {
	// How big is the schematic?
	return schemIter.getTotalBlocks();
    }

    @Override
    public long getRemainingBlocks() {
	// About how much longer to go?
	return schemIter.getRemainingBlocks();
    }

}
