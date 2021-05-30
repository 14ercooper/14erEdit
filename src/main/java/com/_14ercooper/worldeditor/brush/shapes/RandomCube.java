package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class RandomCube extends BrushShape {

    int sideMin, sideMax;
    int argsSeen = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
        List<String> argList = new ArrayList<>();
//	int cubeRad = (int) (rand.nextInt(sideMax - sideMin) + sideMin / 2);
        int cubeRad = Main.randRange(sideMin, sideMax) / 2;
        argList.add(Integer.toString((int) x - cubeRad));
        argList.add(Integer.toString((int) y - cubeRad));
        argList.add(Integer.toString((int) z - cubeRad));
        argList.add(Integer.toString((int) x + cubeRad));
        argList.add(Integer.toString((int) y + cubeRad));
        argList.add(Integer.toString((int) z + cubeRad));
        return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList, world);
    }

    @Override
    public void addNewArgument(String argument) {
        if (argsSeen == 0) {
            sideMin = Integer.parseInt(argument);
        } else if (argsSeen == 1) {
            sideMax = Integer.parseInt(argument);
        }
        argsSeen++;
    }

    @Override
    public boolean lastInputProcessed() {
        return argsSeen < 3;
    }

    @Override
    public boolean gotEnoughArgs() {
        return argsSeen <= 1;
    }

    @Override
    public int minArgCount() {
        return 2;
    }

}
