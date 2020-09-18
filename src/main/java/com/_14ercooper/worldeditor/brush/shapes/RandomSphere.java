package com._14ercooper.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class RandomSphere extends BrushShape {

    @Override
    public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
	try {
	    List<String> argList = new LinkedList<String>();
	    int radiusMin = (int) (double) args.get(0);
	    int radiusMax = (int) (double) args.get(1);
	    Random rand = new Random();
	    int radius = rand.nextInt(radiusMax - radiusMin) + radiusMin;
	    argList.add(Integer.toString((int) x));
	    argList.add(Integer.toString((int) y));
	    argList.add(Integer.toString((int) z));
	    argList.add(Integer.toString(radius));
	    argList.add(Integer.toString(0));
	    argList.add(args.get(2).toString());
	    return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
	}
	catch (Exception e) {
	    Main.logError(
		    "Could not parse random sphere. Did you provide a minimum radius, maximum radius, and radius correction?",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public double GetArgCount() {
	return 3;
    }

}
