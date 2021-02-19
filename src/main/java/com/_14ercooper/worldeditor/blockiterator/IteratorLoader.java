package com._14ercooper.worldeditor.blockiterator;

import com._14ercooper.worldeditor.blockiterator.iterators.CubeIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.CylinderIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.DiamondIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.EllipseIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.MultiIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.NewCylinderIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.RotatedEllipseIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.SchemBrushIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.SphereIterator;
import com._14ercooper.worldeditor.blockiterator.iterators.SpikeIterator;
import com._14ercooper.worldeditor.main.GlobalVars;

public class IteratorLoader {
    public static void LoadIterators() {
	GlobalVars.iteratorManager.addIterator("cube", new CubeIterator());
	GlobalVars.iteratorManager.addIterator("sphere", new SphereIterator());
	GlobalVars.iteratorManager.addIterator("cylinder", new CylinderIterator());
	GlobalVars.iteratorManager.addIterator("ellipse", new EllipseIterator());
	GlobalVars.iteratorManager.addIterator("diamond", new DiamondIterator());
	GlobalVars.iteratorManager.addIterator("multi", new MultiIterator());
	GlobalVars.iteratorManager.addIterator("schem", new SchemBrushIterator());
	GlobalVars.iteratorManager.addIterator("newcylinder", new NewCylinderIterator());
	GlobalVars.iteratorManager.addIterator("rotatedellipse", new RotatedEllipseIterator());
	GlobalVars.iteratorManager.addIterator("spike", new SpikeIterator());
    }
}
