package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class RandomHollowSphere extends BrushShape {

    int radiusMin, radiusMax, centerMin, centerMax;
    String correction = "0.5";
    int argsGot = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
        List<String> argList = new ArrayList<>();
        argList.add(Integer.toString((int) x));
        argList.add(Integer.toString((int) y));
        argList.add(Integer.toString((int) z));
        argList.add(Integer.toString(Main.randRange(radiusMin, radiusMax)));
        argList.add(Integer.toString(Main.randRange(centerMin, centerMax)));
        argList.add(correction);
        return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList, world);
    }

    @Override
    public void addNewArgument(String argument) {
        if (argsGot == 0) {
            radiusMin = Integer.parseInt(argument);
        } else if (argsGot == 1) {
            radiusMax = Integer.parseInt(argument);
        } else if (argsGot == 2) {
            centerMin = Integer.parseInt(argument);
        } else if (argsGot == 3) {
            centerMax = Integer.parseInt(argument);
        } else if (argsGot == 4) {
            try {
                correction = argument;
            } catch (NumberFormatException e) {
                argsGot++;
            }
        }
        argsGot++;
    }

    @Override
    public boolean lastInputProcessed() {
        return argsGot < 6;
    }

    @Override
    public boolean gotEnoughArgs() {
        return argsGot <= 3;
    }

}
