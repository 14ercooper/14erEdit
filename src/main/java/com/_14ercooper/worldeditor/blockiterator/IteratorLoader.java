/**
 * This file is part of 14erEdit.
 * 
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.blockiterator;

import com._14ercooper.worldeditor.blockiterator.iterators.*;

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
        IteratorManager.INSTANCE.addIterator("floodfill", new FloodfillIterator());
        IteratorManager.INSTANCE.addIterator("blob", new BlobIterator());
        IteratorManager.INSTANCE.addIterator("spline", new SplineIterator());
    }
}
