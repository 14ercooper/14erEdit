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

public class IteratorLoader {
    public static void LoadIterators() {
        IteratorManager.INSTANCE.addIterator("cube", new CubeIterator());
        IteratorManager.INSTANCE.addIterator("sphere", new SphereIterator());
        IteratorManager.INSTANCE.addIterator("cylinder", new CylinderIterator());
        IteratorManager.INSTANCE.addIterator("ellipse", new EllipseIterator());
        IteratorManager.INSTANCE.addIterator("diamond", new DiamondIterator());
        IteratorManager.INSTANCE.addIterator("multi", new MultiIterator());
        IteratorManager.INSTANCE.addIterator("schem", new SchemBrushIterator());
        IteratorManager.INSTANCE.addIterator("newcylinder", new NewCylinderIterator());
        IteratorManager.INSTANCE.addIterator("rotatedellipse", new RotatedEllipseIterator());
        IteratorManager.INSTANCE.addIterator("spike", new SpikeIterator());
    }
}
