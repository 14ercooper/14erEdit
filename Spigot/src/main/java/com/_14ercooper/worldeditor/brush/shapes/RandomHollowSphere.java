package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class RandomHollowSphere extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			List<String> argList = new ArrayList<String>();
			Random rand = new Random();
			int radiusMin = (int) (double) args.get(0);
			int radiusMax = (int) (double) args.get(1);
			int centerMin = (int) (double) args.get(2);
			int centerMax = (int) (double) args.get(3);
			argList.add(Integer.toString((int) x));
			argList.add(Integer.toString((int) y));
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString(rand.nextInt(radiusMax - radiusMin) + radiusMin));
			argList.add(Integer.toString(rand.nextInt(centerMax - centerMin) + centerMin));
			argList.add(args.get(4).toString());
			return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse random hollow sphere. Did you provide a radius min&max, thickness min&max, and correction?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 5;
	}

}
