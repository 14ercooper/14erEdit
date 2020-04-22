package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;

public class Diamond extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		// This uses the Manhattan distance
		List<String> argList = new ArrayList<String>();
		int radius = (int) (double) args.get(0);
		argList.add(Double.toString(x));
		argList.add(Double.toString(y));
		argList.add(Double.toString(z));
		argList.add(Integer.toString(radius));
		return GlobalVars.iteratorManager.getIterator("diamond").newIterator(argList);
	}

	@Override
	public double GetArgCount() {
		return 1;
	}

}
