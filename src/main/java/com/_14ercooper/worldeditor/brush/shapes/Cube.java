package com._14ercooper.worldeditor.brush.shapes;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Cube extends BrushShape {

    int cubeDiameter;
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
        List<String> argList = new ArrayList<>();
        int cubeRad = cubeDiameter / 2;
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
	if (gotArgs == 0) {
	    cubeDiameter = Integer.parseInt(argument);
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
}
