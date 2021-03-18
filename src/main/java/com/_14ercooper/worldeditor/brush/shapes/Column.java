package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class Column extends BrushShape {

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	List<String> argList = new ArrayList<String>();
	argList.add(Integer.toString((int) x));
	argList.add("0");
	argList.add(Integer.toString((int) z));
	argList.add(Integer.toString((int) x));
	argList.add("255");
	argList.add(Integer.toString((int) z));
	return GlobalVars.iteratorManager.getIterator("cube").newIterator(argList);
    }

    @Override
    public void addNewArgument(String argument) {
	// Zero argument brush, do nothing

    }

    @Override
    public boolean lastInputProcessed() {
	return false;
    }

    @Override
    public boolean gotEnoughArgs() {
	return true;
    }

}
