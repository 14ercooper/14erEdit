package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;

public class ScaledSphere extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			List<String> argList = new ArrayList<String>();
			int radius = (int) (double) args.get(0);
			argList.add(Integer.toString((int) x));
			argList.add(Integer.toString((int) y));
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString(radius));
			argList.add(args.get(1).toString());
			argList.add(args.get(2).toString());
			argList.add(args.get(3).toString());
			argList.add(args.get(4).toString());
			return GlobalVars.iteratorManager.getIterator("cylinder").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse scaled sphere brush. Did you provide all of radius, correction, and three scale factors?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 5;
	}

}
