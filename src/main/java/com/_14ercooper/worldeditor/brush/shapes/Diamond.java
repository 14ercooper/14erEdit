package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Diamond extends BrushShape {

    int radius;
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
        // This uses the Manhattan distance
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(Integer.toString(radius));
        return GlobalVars.iteratorManager.getIterator("diamond").newIterator(argList, world);
    }

    @Override
    public void addNewArgument(String argument) {
        if (gotArgs == 0) {
            radius = Integer.parseInt(argument);
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
