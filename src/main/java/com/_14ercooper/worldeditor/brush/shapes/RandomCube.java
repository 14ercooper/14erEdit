package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;

public class RandomCube extends BrushShape {

    int sideMin, sideMax;
    int argsSeen = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	    List<String> argList = new ArrayList<String>();
	    Random rand = new Random();
	    int cubeRad = (int) (rand.nextInt(sideMax - sideMin) + sideMin / 2);
	    argList.add(Integer.toString((int) x - cubeRad));
	    argList.add(Integer.toString((int) y - cubeRad));
	    argList.add(Integer.toString((int) z - cubeRad));
	    argList.add(Integer.toString((int) x + cubeRad));
	    argList.add(Integer.toString((int) y + cubeRad));
	    argList.add(Integer.toString((int) z + cubeRad));
	    return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList);
    }

    @Override
    public void addNewArgument(String argument) {
	if (argsSeen == 0) {
	    sideMin = Integer.parseInt(argument);
	}
	else if (argsSeen == 1) {
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
	return argsSeen > 1;
    }

}
