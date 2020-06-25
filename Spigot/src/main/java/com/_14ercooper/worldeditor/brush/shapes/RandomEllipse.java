package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class RandomEllipse extends BrushShape {
	// rand.nextInt(Max - Min) + Min
	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			// Generate the ellipse
			Random rand = new Random();
			int xMin = (int) (double) args.get(0);
			int xMax = (int) (double) args.get(1);
			int yMin = (int) (double) args.get(2);
			int yMax = (int) (double) args.get(3);
			int zMin = (int) (double) args.get(4);
			int zMax = (int) (double) args.get(5);
			double radiusCorrection = args.get(6);
			List<String> argList = new ArrayList<String>();
			argList.add(Integer.toString((int) x));
			argList.add(Integer.toString((int) y));
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString(rand.nextInt(xMax - xMin) + xMin));
			argList.add(Integer.toString(rand.nextInt(yMax - yMin) + yMin));
			argList.add(Integer.toString(rand.nextInt(zMax - zMin) + zMin));
			argList.add(Double.toString(radiusCorrection / 5.0));
			return GlobalVars.iteratorManager.getIterator("ellipse").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse random ellipse brush. Did you provide 3 dimensions with both a min and max and a correction?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 7;
	}

}
