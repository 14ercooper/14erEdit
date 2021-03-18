package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.Brush;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class Spike extends BrushShape {

    String height, baseSizeMax, baseSizeMin = "0";
    int gotArgs = 0;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	List<String> args = new ArrayList<String>();
	args.add(Integer.toString((int) x));
	args.add(Integer.toString((int) y));
	args.add(Integer.toString((int) z));
	args.add(baseSizeMax);
	args.add(baseSizeMin);
	args.add(height);
	args.add(Double.toString(Brush.currentPlayer.getLocation().getX()));
	args.add(Double.toString(Brush.currentPlayer.getLocation().getY()));
	args.add(Double.toString(Brush.currentPlayer.getLocation().getZ()));
	return GlobalVars.iteratorManager.getIterator("spike").newIterator(args);
    }

    @Override
    public void addNewArgument(String argument) {
	if (gotArgs == 0) {
	    height = argument;
	}
	else if (gotArgs == 1) {
	    baseSizeMax = argument;
	}
	else if (gotArgs == 2) {
	    try {
		Double.parseDouble(argument);
		baseSizeMin = argument;
	    }
	    catch (NumberFormatException e) {
		// This is fine *fire everywhere*
		gotArgs++;
	    }
	}
	gotArgs++;
    }

    @Override
    public boolean lastInputProcessed() {
	return gotArgs < 4;
    }

    @Override
    public boolean gotEnoughArgs() {
	return gotArgs > 1;
    }

}
