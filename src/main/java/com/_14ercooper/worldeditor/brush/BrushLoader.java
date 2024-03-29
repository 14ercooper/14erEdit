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

package com._14ercooper.worldeditor.brush;

import com._14ercooper.worldeditor.brush.shapes.*;

public class BrushLoader {
    public static void LoadBrushes() {
        Cube cube = new Cube();
        Diamond diamond = new Diamond();
        Ellipse ellipse = new Ellipse();
        HollowSphere hollowSphere = new HollowSphere();
        RadiusSphere radiusSphere = new RadiusSphere();
        RandomSphere randomSphere = new RandomSphere();
        Sphere sphere = new Sphere();
//	ScaledSphere scaledSphere = new ScaledSphere();
        Cylinder cylinder = new Cylinder();
        Voxel voxel = new Voxel();
        Splatter splatter = new Splatter();
        RandomSplatter randomSplatter = new RandomSplatter();
        RandomEllipse randomEllipse = new RandomEllipse();
        RandomCylinder randomCylinder = new RandomCylinder();
        RandomHollowSphere randomHollowSphere = new RandomHollowSphere();
        RandomCube randomCube = new RandomCube();
        RandomDiamond randomDiamond = new RandomDiamond();
        Column column = new Column();
        Above above = new Above();
        Below below = new Below();
        SchemShape schem = new SchemShape();
        NewCylinder cyl = new NewCylinder();
        RotatedEllipse rotatedEllipse = new RotatedEllipse();
        Spike spike = new Spike();
        Multi multi = new Multi();
        Floodfill floodfill = new Floodfill();
        Blob blob = new Blob();
        RandomBlob randomBlob = new RandomBlob();
        Brush.AddBrushShape("cube", cube);
        Brush.AddBrushShape("square", cube);
        Brush.AddBrushShape("diamond", diamond);
        Brush.AddBrushShape("d", diamond);
        Brush.AddBrushShape("ellipse", ellipse);
        Brush.AddBrushShape("e", ellipse);
        Brush.AddBrushShape("hollowsphere", hollowSphere);
        Brush.AddBrushShape("hsphere", hollowSphere);
        Brush.AddBrushShape("hs", hollowSphere);
        Brush.AddBrushShape("radiussphere", radiusSphere);
        Brush.AddBrushShape("randomsphere", randomSphere);
        Brush.AddBrushShape("rs", randomSphere);
        Brush.AddBrushShape("sphere", sphere);
        Brush.AddBrushShape("s", sphere);
        Brush.AddBrushShape("voxel", voxel);
        Brush.AddBrushShape("v", voxel);
        Brush.AddBrushShape("oc", cylinder);
        Brush.AddBrushShape("oldcylinder", cylinder);
        Brush.AddBrushShape("splatter", splatter);
        Brush.AddBrushShape("splat", splatter);
        Brush.AddBrushShape("randomsplatter", randomSplatter);
        Brush.AddBrushShape("rsplat", randomSplatter);
        Brush.AddBrushShape("randomellipse", randomEllipse);
        Brush.AddBrushShape("re", randomEllipse);
        Brush.AddBrushShape("randomoldcylinder", randomCylinder);
        Brush.AddBrushShape("roc", randomCylinder);
        Brush.AddBrushShape("randomhollowsphere", randomHollowSphere);
        Brush.AddBrushShape("randomhsphere", randomHollowSphere);
        Brush.AddBrushShape("rhs", randomHollowSphere);
        Brush.AddBrushShape("randomcube", randomCube);
        Brush.AddBrushShape("randomsquare", randomCube);
        Brush.AddBrushShape("rcube", randomCube);
        Brush.AddBrushShape("rsquare", randomCube);
        Brush.AddBrushShape("randomdiamond", randomDiamond);
        Brush.AddBrushShape("rd", randomDiamond);
        Brush.AddBrushShape("column", column);
        Brush.AddBrushShape("col", column);
        Brush.AddBrushShape("above", above);
        Brush.AddBrushShape("up", above);
        Brush.AddBrushShape("below", below);
        Brush.AddBrushShape("down", below);
        Brush.AddBrushShape("schem", schem);
        Brush.AddBrushShape("schematic", schem);
        Brush.AddBrushShape("cylinder", cyl);
        Brush.AddBrushShape("cyl", cyl);
        Brush.AddBrushShape("rotatedellipse", rotatedEllipse);
        Brush.AddBrushShape("rote", rotatedEllipse);
        Brush.AddBrushShape("spike", spike);
        Brush.AddBrushShape("sp", spike);
        Brush.AddBrushShape("multi", multi);
        Brush.AddBrushShape("floodfill", floodfill);
        Brush.AddBrushShape("flood", floodfill);
        Brush.AddBrushShape("fill", floodfill);
        Brush.AddBrushShape("blob", blob);
        Brush.AddBrushShape("b", blob);
        Brush.AddBrushShape("randomblob", randomBlob);
        Brush.AddBrushShape("randblob", randomBlob);
        Brush.AddBrushShape("rblob", randomBlob);
        Brush.AddBrushShape("rb", randomBlob);
    }
}
