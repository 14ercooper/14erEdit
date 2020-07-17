package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class RandomCylinder extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			List<String> argList = new ArrayList<String>();
			Random rand = new Random();
			int radiusMin = (int) (double) args.get(0);
			int radiusMax = (int) (double) args.get(1);
			argList.add(Integer.toString((int) x));
			argList.add(Integer.toString((int) y));
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString(rand.nextInt(radiusMax - radiusMin) + radiusMin));
			argList.add(args.get(2).toString());
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
		} catch (Exception e) {
			Main.logError("Could not parse random cylinder brush. Did you provide all of radius min&max, correction, axis?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 4;
	}

}
