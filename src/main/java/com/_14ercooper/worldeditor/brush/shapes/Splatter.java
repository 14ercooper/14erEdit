package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.MultiIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class Splatter extends BrushShape {

    int splatterRadius, sphereCount, sphereRadius;
    String correction = "0.5";
    int argsSeen = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	int spheresGenerated = 0;
	Set<BlockIterator> spheres = new HashSet<BlockIterator>();
	while (spheresGenerated < sphereCount) {
	    double xOff = GlobalVars.rand.nextInt((2 * splatterRadius) + 1) - splatterRadius;
	    double yOff = GlobalVars.rand.nextInt((2 * splatterRadius) + 1) - splatterRadius;
	    double zOff = GlobalVars.rand.nextInt((2 * splatterRadius) + 1) - splatterRadius;
	    if (xOff * xOff + yOff * yOff + zOff * zOff < splatterRadius * splatterRadius + 0.5) {
		List<String> argList = new ArrayList<String>();
		argList.add(Integer.toString((int) (x + xOff)));
		argList.add(Integer.toString((int) (y + yOff)));
		argList.add(Integer.toString((int) (z + zOff)));
		argList.add(Integer.toString(sphereRadius));
		argList.add(Integer.toString(0));
		argList.add(correction);
		spheres.add(GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList));
		spheresGenerated++;
	    }

	}
	return ((MultiIterator) GlobalVars.iteratorManager.getIterator("multi")).newIterator(spheres);
    }

    @Override
    public void addNewArgument(String argument) {
	if (argsSeen == 0) {
	    splatterRadius = Integer.parseInt(argument);
	}
	if (argsSeen == 1) {
	    sphereCount = Integer.parseInt(argument);
	}
	if (argsSeen == 2) {
	    sphereRadius = Integer.parseInt(argument);
	}
	if (argsSeen == 3) {
	    try {
		Double.parseDouble(argument);
		correction = argument;
	    }
	    catch (NumberFormatException e) {
		argsSeen++;
	    }
	}
	argsSeen++;
    }

    @Override
    public boolean lastInputProcessed() {
	return argsSeen < 5;
    }

    @Override
    public boolean gotEnoughArgs() {
	return argsSeen > 2;
    }

}
