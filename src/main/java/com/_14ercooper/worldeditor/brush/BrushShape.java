package com._14ercooper.worldeditor.brush;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;

public abstract class BrushShape {
    public abstract BlockIterator GetBlocks(double x, double y, double z);

    public abstract void addNewArgument(String argument);

    public abstract boolean lastInputProcessed();

    public abstract boolean gotEnoughArgs();
}
