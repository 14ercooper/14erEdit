package com.fourteener.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fourteener.worldeditor.blockiterator.BlockIterator;
import com.fourteener.worldeditor.brush.BrushShape;
import com.fourteener.worldeditor.main.*;
import com.fourteener.worldeditor.operations.Operator;

public class RandomCube extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			List<String> argList = new ArrayList<String>();
			Random rand = new Random();
			int sideMax = (int) (double) args.get(0);
			int sideMin = (int) (double) args.get(1);
			int cubeRad = (int) (rand.nextInt(sideMax - sideMin) + sideMin / 2);
			argList.add(Integer.toString((int) x - cubeRad));
			argList.add(Integer.toString((int) y - cubeRad));
			argList.add(Integer.toString((int) z - cubeRad));
			argList.add(Integer.toString((int) x + cubeRad));
			argList.add(Integer.toString((int) y + cubeRad));
			argList.add(Integer.toString((int) z + cubeRad));
			return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse random square brush. Did you provide a side length min&max?", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 2;
	}

}
