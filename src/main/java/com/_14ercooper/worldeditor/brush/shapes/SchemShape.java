package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class SchemShape extends BrushShape {

    String fileName;
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
        List<String> args = new ArrayList<>();
        args.add(Integer.toString((int) x));
        args.add(Integer.toString((int) y));
        args.add(Integer.toString((int) z));
        Main.logDebug("Creating schematic iterator, file " + fileName);
        args.add(fileName);
        return GlobalVars.iteratorManager.getIterator("schem").newIterator(args, world);
    }

    @Override
    public void addNewArgument(String argument) {
        if (gotArgs == 0) {
            fileName = argument;
        }
        gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
        return gotArgs < 2;
    }

    @Override
    public boolean gotEnoughArgs() {
        return gotArgs <= 0;
    }

    @Override
    public int minArgCount() {
        return 1;
    }
}
