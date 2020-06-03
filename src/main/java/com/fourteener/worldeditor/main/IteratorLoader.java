package com.fourteener.worldeditor.main;

import com.fourteener.worldeditor.blockiterator.iterators.*;

public class IteratorLoader {
	protected static void LoadIterators () {
		GlobalVars.iteratorManager.addIterator("cube", new CubeIterator());
		GlobalVars.iteratorManager.addIterator("sphere", new SphereIterator());
		GlobalVars.iteratorManager.addIterator("ellipse", new EllipseIterator());
		GlobalVars.iteratorManager.addIterator("diamond", new DiamondIterator());
	}
}
