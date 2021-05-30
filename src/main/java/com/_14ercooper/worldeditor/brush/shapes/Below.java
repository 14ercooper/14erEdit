package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Below extends BrushShape {

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add("0");
        argList.add(Integer.toString((int) z));
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList, world);
    }

    @Override
    public void addNewArgument(String argument) {
        // No argument brush
    }

    @Override
    public boolean lastInputProcessed() {
        return false;
    }

    @Override
    public boolean gotEnoughArgs() {
        return false;
    }

    @Override
    public int minArgCount() {
        return 0;
    }

}
