package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.GlobalVars;

public class Dome extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		List<String> argList = new ArrayList<String>();
		int dir = (int) (double) args.get(0);
		int radius = (int) (double) args.get(1);
		double radiusCorrection = args.get(2);
		int xs, xe, ys, ye, zs, ze;
		xs = xe = ys = ye = zs = ze = radius;
		switch (dir) {
		case 0:
			ys = 0;
			break;
		case 1:
			ye = 0;
			break;
		case 2:
			xs = 0;
			break;
		case 3:
			xe = 0;
			break;
		case 4:
			zs = 0;
			break;
		case 5:
			ze = 0;
			break;
		}
		argList.add(Double.toString(xs));
		argList.add(Double.toString(ys));
		argList.add(Double.toString(zs));
		argList.add(Double.toString(xe));
		argList.add(Double.toString(ye));
		argList.add(Double.toString(ze));
		argList.add(Integer.toString(radius));
		argList.add("0");
		argList.add(Double.toString(radiusCorrection));
		return GlobalVars.iteratorManager.getIterator("dome").newIterator(argList);
	}

	@Override
	public double GetArgCount() {
		return 3;
	}

}
