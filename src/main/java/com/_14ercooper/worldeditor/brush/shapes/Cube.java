package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class Cube extends BrushShape {

    @Override
    public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
	try {
	    List<String> argList = new ArrayList<String>();
	    int cubeRad = (int) (args.get(0) / 2);
	    argList.add(Integer.toString((int) x - cubeRad));
	    argList.add(Integer.toString((int) y - cubeRad));
	    argList.add(Integer.toString((int) z - cubeRad));
	    argList.add(Integer.toString((int) x + cubeRad));
	    argList.add(Integer.toString((int) y + cubeRad));
	    argList.add(Integer.toString((int) z + cubeRad));
	    return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList);
	}
	catch (Exception e) {
	    Main.logError("Could not parse square brush. Did you provide a side length?", Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public double GetArgCount() {
	return 1;
    }

}
