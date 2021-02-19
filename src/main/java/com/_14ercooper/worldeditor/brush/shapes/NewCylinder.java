package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class NewCylinder extends BrushShape {

    String radius, height, dimension, correction = "0.5";
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	List<String> args = new ArrayList<String>();
	args.add(Integer.toString((int) x));
	args.add(Integer.toString((int) y));
	args.add(Integer.toString((int) z));
	args.add(radius);
	args.add(correction);
	args.add(height);
	// Axis X
	if (dimension.equalsIgnoreCase("x")) {
	    args.add("0");
	    args.add("1");
	    args.add("1");
	}
	// Axis Y
	if (dimension.equalsIgnoreCase("y")) {
	    args.add("1");
	    args.add("0");
	    args.add("1");
	}
	// Axis Z
	if (dimension.equalsIgnoreCase("z")) {
	    args.add("1");
	    args.add("1");
	    args.add("0");
	}
	return GlobalVars.iteratorManager.getIterator("newcylinder").newIterator(args);
    }

    @Override
    public void addNewArgument(String argument) {
	if (gotArgs == 0) {
	    radius = argument;
	}
	else if (gotArgs == 1) {
	    height = argument;
	}
	else if (gotArgs == 2) {
	    dimension = argument;
	}
	else if (gotArgs == 3) {
	    try {
		Double.parseDouble(argument);
		correction = argument;
	    }
	    catch (NumberFormatException e) {
		// This is okay
		gotArgs++;
	    }
	}
	gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
	return gotArgs < 5;
    }

    @Override
    public boolean gotEnoughArgs() {
	return gotArgs > 2;
    }

}
