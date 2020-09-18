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
import com._14ercooper.worldeditor.main.Main;
import com._14ercooper.worldeditor.operations.Operator;

public class Splatter extends BrushShape {

    @Override
    public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
	try {
	    int splatterRadius = (int) (double) args.get(0);
	    int sphereCount = (int) (double) args.get(1);
	    int sphereRadius = (int) (double) args.get(2);
	    double radiusCorrection = args.get(3);
	    int spheresGenerated = 0;
	    Set<BlockIterator> spheres = new HashSet<BlockIterator>();
	    Random rand = new Random();
	    while (spheresGenerated < sphereCount) {
		double xOff = rand.nextInt((2 * splatterRadius) + 1) - splatterRadius;
		double yOff = rand.nextInt((2 * splatterRadius) + 1) - splatterRadius;
		double zOff = rand.nextInt((2 * splatterRadius) + 1) - splatterRadius;
		if (xOff * xOff + yOff * yOff + zOff * zOff < splatterRadius * splatterRadius + 0.5) {
		    List<String> argList = new ArrayList<String>();
		    argList.add(Integer.toString((int) (x + xOff)));
		    argList.add(Integer.toString((int) (y + yOff)));
		    argList.add(Integer.toString((int) (z + zOff)));
		    argList.add(Integer.toString(sphereRadius));
		    argList.add(Integer.toString(0));
		    argList.add(Double.toString(radiusCorrection));
		    spheres.add(GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList));
		    spheresGenerated++;
		}

	    }
	    return ((MultiIterator) GlobalVars.iteratorManager.getIterator("multi")).newIterator(spheres);
	}
	catch (Exception e) {
	    Main.logError(
		    "Could not parse splatter brush. Did you provide splatter radius, sphere count, sphere radius, and radius correction?",
		    Operator.currentPlayer);
	    return null;
	}
    }

    @Override
    public double GetArgCount() {
	// Splatter radius, sphere count, sphere radius, radius correction
	return 4;
    }

}
