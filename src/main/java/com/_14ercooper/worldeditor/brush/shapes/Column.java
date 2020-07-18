package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;
import com._14ercooper.worldeditor.operations.Operator;

public class Column extends BrushShape {

	@Override
	public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
		try {
			List<String> argList = new ArrayList<String>();
			argList.add(Integer.toString((int) x));
			argList.add("0");
			argList.add(Integer.toString((int) z));
			argList.add(Integer.toString((int) x));
			argList.add("255");
			argList.add(Integer.toString((int) z));
			return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList);
		} catch (Exception e) {
			Main.logError("Could not parse column brush. This should never show up.", Operator.currentPlayer);
			return null;
		}
	}

	@Override
	public double GetArgCount() {
		return 0;
	}

}
