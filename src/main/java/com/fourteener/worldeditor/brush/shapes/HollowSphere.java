package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;

public class HollowSphere extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		List<String> argList = new ArrayList<String>();
		argList.add(Integer.toString((int) x));
		argList.add(Integer.toString((int) y));
		argList.add(Integer.toString((int) z));
		argList.add(Integer.toString((int) (double) args.get(0)));
		argList.add(Integer.toString((int) (double) args.get(1)));
		argList.add(args.get(2).toString());
		return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
	}

	@Override
	public double GetArgCount() {
		return 3;
	}

}
