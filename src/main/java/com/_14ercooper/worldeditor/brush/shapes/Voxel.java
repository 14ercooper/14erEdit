package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class Voxel extends BrushShape {

    @Override
    public BlockIterator GetBlocks(List<Double> args, double x, double y, double z) {
	List<String> argList = new ArrayList<String>();
	argList.add(Integer.toString((int) x));
	argList.add(Integer.toString((int) y));
	argList.add(Integer.toString((int) z));
	argList.add("0");
	argList.add("0");
	argList.add("0.5");
	return GlobalVars.iteratorManager.getIterator("sphere").newIterator(argList);
    }

    @Override
    public double GetArgCount() {
	return 0;
    }

}
