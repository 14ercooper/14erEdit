package com._14ercooper.worldeditor.brush;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public abstract class BrushShape {
    public abstract BlockIterator GetBlocks(double x, double y, double z, World world, CommandSender sender);

    public abstract void addNewArgument(String argument);

    public abstract boolean lastInputProcessed();

    public abstract boolean gotEnoughArgs();

    public abstract int minArgCount();
}
