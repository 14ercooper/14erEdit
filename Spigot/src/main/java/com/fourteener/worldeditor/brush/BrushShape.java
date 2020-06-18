package com.fourteener.worldeditor.brush;

import java.util.List;

import com.fourteener.worldeditor.blockiterator.BlockIterator;

public abstract class BrushShape {
	public abstract BlockIterator GetBlocks (List<Double> args, double x, double y, double z);
	
	public abstract double GetArgCount();
}
