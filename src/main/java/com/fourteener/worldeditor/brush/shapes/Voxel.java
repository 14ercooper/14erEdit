package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.GlobalVars;

public class Voxel extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		List<String> argList = new ArrayList<String>();
		argList.add(Double.toString(x));
		argList.add(Double.toString(y));
		argList.add(Double.toString(z));
		return GlobalVars.iteratorManager.getIterator("voxel").newIterator(argList);
	}

	@Override
	public double GetArgCount() {
		return 0;
	}

}
