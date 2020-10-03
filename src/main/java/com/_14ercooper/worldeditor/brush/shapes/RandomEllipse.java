package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;

public class RandomEllipse extends BrushShape {

    int xMin, xMax, yMin, yMax, zMin, zMax;
    String correction;
    int argsGot = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	// Generate the ellipse
	Random rand = new Random();
	List<String> argList = new ArrayList<String>();
	argList.add(Integer.toString((int) x));
	argList.add(Integer.toString((int) y));
	argList.add(Integer.toString((int) z));
	argList.add(Integer.toString(rand.nextInt(xMax - xMin) + xMin));
	argList.add(Integer.toString(rand.nextInt(yMax - yMin) + yMin));
	argList.add(Integer.toString(rand.nextInt(zMax - zMin) + zMin));
	argList.add(correction);
	return GlobalVars.iteratorManager.getIterator("ellipse").newIterator(argList);
    }

    @Override
    public void addNewArgument(String argument) {
	if (argsGot == 0) {
	    xMin = Integer.parseInt(argument);
	}
	else if (argsGot == 1) {
	    xMax = Integer.parseInt(argument);
	}
	else if (argsGot == 2) {
	    yMin = Integer.parseInt(argument);
	}
	else if (argsGot == 3) {
	    yMax = Integer.parseInt(argument);
	}
	else if (argsGot == 4) {
	    zMin = Integer.parseInt(argument);
	}
	else if (argsGot == 5) {
	    zMax = Integer.parseInt(argument);
	}
	else if (argsGot == 6) {
	    correction = argument;
	}
	argsGot++;
    }

    @Override
    public boolean lastInputProcessed() {
	return argsGot < 8;
    }

    @Override
    public boolean gotEnoughArgs() {
	return argsGot > 6;
    }

}
