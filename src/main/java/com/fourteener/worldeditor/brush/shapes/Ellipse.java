package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;

public class Ellipse extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		// Generate the ellipse
		List<String> argList = new ArrayList<String>();
		argList.add(Double.toString(x));
		argList.add(Double.toString(y));
		argList.add(Double.toString(z));
		argList.add(Double.toString(args.get(0)));
		argList.add(Double.toString(args.get(1)));
		argList.add(Double.toString(args.get(2)));
		argList.add(Double.toString(args.get(3)));
		return GlobalVars.iteratorManager.getIterator("ellipse").newIterator(argList);
	}

	@Override
	public double GetArgCount() {
		return 4;
	}

}
