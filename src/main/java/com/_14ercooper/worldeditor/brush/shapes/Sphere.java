package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class Sphere extends BrushShape {

    @Override
    public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
	try {
	    List<String> argList = new ArrayList<String>();
	    int radius = (int) (double) args.get(0);
	    argList.add(Integer.toString((int) x));
	    argList.add(Integer.toString((int) y));
	    argList.add(Integer.toString((int) z));
	    argList.add(Integer.toString(radius));
	    argList.add(Integer.toString(0));
	    argList.add(args.get(1).toString());
	    return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
	}
	catch (Exception e) {
	    Main.logError("Could not parse sphere brush. Did you provide both a radius and correction?",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public double GetArgCount() {
	return 2;
    }

}
