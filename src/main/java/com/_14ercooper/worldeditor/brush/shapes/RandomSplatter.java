package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.MultiIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class RandomSplatter extends BrushShape {

    int splatterRadiusMin, splatterRadiusMax, sphereCountMin, sphereCountMax, sphereRadiusMin, sphereRadiusMax;
    String correction = "0.5";
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	int spheresGenerated = 0;
	Set<BlockIterator> spheres = new HashSet<BlockIterator>();
	Random rand = new Random();
	int sphereCount = rand.nextInt(sphereCountMax - sphereCountMin) + sphereCountMin;
	while (spheresGenerated < sphereCount) {
	    int splatterRadius = rand.nextInt(splatterRadiusMax - splatterRadiusMin) + splatterRadiusMin;
	    double xOff = rand.nextInt(splatterRadius) * (rand.nextInt(2) == 0 ? -1 : 1);
	    double yOff = rand.nextInt(splatterRadius) * (rand.nextInt(2) == 0 ? -1 : 1);
	    double zOff = rand.nextInt(splatterRadius) * (rand.nextInt(2) == 0 ? -1 : 1);
	    if (xOff * xOff + yOff * yOff + zOff * zOff < splatterRadius * splatterRadius + 0.5) {
		int sphereRadius = rand.nextInt(sphereRadiusMax - sphereRadiusMin) + sphereRadiusMin;
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
	if (gotArgs == 0) {
	    splatterRadiusMin = Integer.parseInt(argument);
	}
	else if (gotArgs == 1) {
	    splatterRadiusMax = Integer.parseInt(argument);
	}
	else if (gotArgs == 2) {
	    sphereCountMin = Integer.parseInt(argument);
	}
	else if (gotArgs == 3) {
	    sphereCountMax = Integer.parseInt(argument);
	}
	else if (gotArgs == 4) {
	    sphereRadiusMin = Integer.parseInt(argument);
	}
	else if (gotArgs == 5) {
	    sphereRadiusMax = Integer.parseInt(argument);
	}
	else if (gotArgs == 6) {
	    try {
		Double.parseDouble(argument);
		correction = argument;
	    }
	    catch (NumberFormatException e) {
		gotArgs++;
	    }
	}
	gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
	return gotArgs < 8;
    }

    @Override
    public boolean gotEnoughArgs() {
	return gotArgs > 5;
    }

}
