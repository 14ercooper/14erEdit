package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.*;

public class Ellipse extends BrushShape {

    List<String> args = new ArrayList<String>();
    int argsGot = 0;

    public Ellipse() {
	args.add("");
	args.add("");
	args.add("");
	args.add("0.1");
    }
    
    @Override
    public BlockIterator GetBlocks(double x, double y, double z) {
	    // Generate the ellipse
	    List<String> argList = new ArrayList<String>();
	    argList.add(Integer.toString((int) x));
	    argList.add(Integer.toString((int) y));
	    argList.add(Integer.toString((int) z));
	    argList.add(args.get(0));
	    argList.add(args.get(1));
	    argList.add(args.get(2));
	    argList.add(args.get(3));
	    return GlobalVars.iteratorManager.getIterator("ellipse").newIterator(argList);
    }

    @Override
    public void addNewArgument(String argument) {
	if (argsGot < 3) {
	    args.set(argsGot, argument);
	}
	else if (argsGot == 3) {
	    try {
		Double.parseDouble(argument);
		args.set(argsGot, argument);
	    }
	    catch (NumberFormatException e) {
		argsGot++;
	    }
	}
	argsGot++;
    }

    @Override
    public boolean lastInputProcessed() {
	return argsGot < 5;
    }

    @Override
    public boolean gotEnoughArgs() {
	return !args.get(2).isBlank();
    }

}
