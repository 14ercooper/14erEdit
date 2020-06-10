package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;

public class RandomDiamond extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			// This uses the Manhattan distance
			List<String> argList = new ArrayList<String>();
			Random rand = new Random();
			int radiusMin = (int) (double) args.get(0);
			int radiusMax = (int) (double) args.get(1);
			argList.add(Integer.toString((int) x));
			argList.add(Integer.toString((int) y));
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString(rand.nextInt(radiusMax - radiusMin) + radiusMin));
			return GlobalVars.iteratorManager.getIterator("diamond").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse random diamond brush. Did you provide a radius min&max?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 2;
	}

}
