package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class Cylinder extends BrushShape {

    @Override
    public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
	try {
	    List<String> argList = new ArrayList<String>();
	    int radius = (int) (double) args.get(0);
	    argList.add(Integer.toString((int) x));
	    argList.add(Integer.toString((int) y));
	    argList.add(Integer.toString((int) z));
	    argList.add(Integer.toString(radius));
	    argList.add(args.get(1).toString());
	    int dimension = (int) (double) args.get(2);
	    // Axis X
	    if (dimension == 0) {
		argList.add("0");
		argList.add("1");
		argList.add("1");
	    }
	    // Axis Y
	    if (dimension == 1) {
		argList.add("1");
		argList.add("0");
		argList.add("1");
	    }
	    // Axis Z
	    if (dimension == 2) {
		argList.add("1");
		argList.add("1");
		argList.add("0");
	    }
	    return GlobalVars.iteratorManager.getIterator("cylinder").newIterator(argList);
	}
	catch (Exception e) {
	    Main.logError("Could not parse cylinder brush. Did you provide all of radius, correction, axis?",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public double GetArgCount() {
	return 3;
    }

}
