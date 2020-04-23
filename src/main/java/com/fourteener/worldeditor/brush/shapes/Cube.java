package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;

public class Cube extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		List<String> argList = new ArrayList<String>();
		int cubeRad = (int) (args.get(0) / 2);
		argList.add(Integer.toString((int) x - cubeRad));
		argList.add(Integer.toString((int) y - cubeRad));
		argList.add(Integer.toString((int) z - cubeRad));
		argList.add(Integer.toString((int) x + cubeRad));
		argList.add(Integer.toString((int) y + cubeRad));
		argList.add(Integer.toString((int) z + cubeRad));
		return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList);
	}

	@Override
	public double GetArgCount() {
		return 1;
	}

}
