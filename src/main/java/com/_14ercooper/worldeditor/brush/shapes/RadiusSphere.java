package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class RadiusSphere extends BrushShape {

    String radius;
    boolean gotArg = false;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	List<String> argList = new ArrayList<String>();
	argList.add(Integer.toString((int) x));
	argList.add(Integer.toString((int) y));
	argList.add(Integer.toString((int) z));
	argList.add(radius);
	argList.add(Integer.toString(0));
	argList.add("0.5");
	return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
    }

    @Override
    public void addNewArgument(String argument) {
	if (!gotArg) {
	    radius = argument;
	    gotArg = true;
	}
    }

    @Override
    public boolean lastInputProcessed() {
	return !gotArg;
    }

    @Override
    public boolean gotEnoughArgs() {
	return gotArg;
    }
}
