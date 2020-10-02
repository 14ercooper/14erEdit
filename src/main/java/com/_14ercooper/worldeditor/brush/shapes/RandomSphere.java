package com._14ercooper.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class RandomSphere extends BrushShape {

    int radiusMin, radiusMax;
    String correction = "0.5";
    int gotArgs;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	    List<String> argList = new LinkedList<String>();
	    Random rand = new Random();
	    int radius = rand.nextInt(radiusMax - radiusMin) + radiusMin;
	    argList.add(Integer.toString((int) x));
	    argList.add(Integer.toString((int) y));
	    argList.add(Integer.toString((int) z));
	    argList.add(Integer.toString(radius));
	    argList.add(Integer.toString(0));
	    argList.add(correction);
	    return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
    }

    @Override
    public void addNewArgument(String argument) {
	if (gotArgs == 0) {
	    radiusMin = Integer.parseInt(argument);
	}
	else if (gotArgs == 1) {
	    radiusMax = Integer.parseInt(argument);
	}
	else if (gotArgs == 2) {
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
	return gotArgs < 4;
    }

    @Override
    public boolean gotEnoughArgs() {
	return gotArgs > 1;
    }

}
