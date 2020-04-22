package com.fourteener.worldeditor.brush.shapes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.GlobalVars;

public class RandomSphere extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
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

	@Override
	public double GetArgCount() {
		return 3;
	}

}
