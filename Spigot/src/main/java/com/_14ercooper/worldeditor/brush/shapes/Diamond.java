package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class Diamond extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			// This uses the Manhattan distance
			List<String> argList = new ArrayList<String>();
			int radius = (int) (double) args.get(0);
			argList.add(Integer.toString((int) x));
			argList.add(Integer.toString((int) y));
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString(radius));
			return GlobalVars.iteratorManager.getIterator("diamond").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse diamond brush. Did you provide a radius?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 1;
	}

}
