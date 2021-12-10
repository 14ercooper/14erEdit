package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.IteratorManager;
import com._14ercooper.worldeditor.brush.BrushShape;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ScaledSphere extends BrushShape {

    final List<String> args = new ArrayList<>();
    boolean lastProcessed = true;

    @Override
    public BlockIterator GetBlocks_impl(double x, double y, double z, World world, CommandSender sender) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(args.get(0));
        argList.add(args.get(1));
        argList.add(args.get(2));
        argList.add(args.get(3));
        argList.add(args.get(4));
        return IteratorManager.INSTANCE.getIterator("cylinder").newIterator(argList, world, sender);
    }

    @Override
    public void addNewArgument(String argument) {
        lastProcessed = false;
        if (args.size() < 5) {
            args.add(argument);
            lastProcessed = true;
        }
    }

    @Override
    public boolean lastInputProcessed() {
        return lastProcessed;
    }

    @Override
    public boolean gotEnoughArgs() {
        return args.size() <= 4;
    }

    @Override
    public int minArgCount() {
        return 5;
    }

}
