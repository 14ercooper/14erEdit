package com._14ercooper.worldeditor.brush.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.brush.BrushShape;
import com._14ercooper.worldeditor.main.GlobalVars;

public class ScaledSphere extends BrushShape {

    List<String> args = new ArrayList<String>();
    boolean lastProcessed = true;

    @Override
    public BlockIterator GetBlocks(double x, double y, double z, World world) {
	List<String> argList = new ArrayList<String>();
	argList.add(Integer.toString((int) x));
	argList.add(Integer.toString((int) y));
	argList.add(Integer.toString((int) z));
	argList.add(args.get(0));
	argList.add(args.get(1));
	argList.add(args.get(2));
	argList.add(args.get(3));
	argList.add(args.get(4));
	return GlobalVars.iteratorManager.getIterator("cylinder").newIterator(argList, world);
    }

    @Override
    public void addNewArgument(String argument) {
	lastProcessed = false;
	if (args.size() < 5) {
	    args.add(argument);
	    lastProcessed = true;
	}
    }

    @Override
    public boolean lastInputProcessed() {
	return lastProcessed;
    }

    @Override
    public boolean gotEnoughArgs() {
	return args.size() > 4;
    }

}
