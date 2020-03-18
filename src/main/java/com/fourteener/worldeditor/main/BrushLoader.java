package com.fourteener.worldeditor.main;

import com.fourteener.worldeditor.brush.Brush;
import com.fourteener.worldeditor.brush.shapes.*;

public class BrushLoader {
	public static void LoadBrushes() {
		Cube cube = new Cube();
		Diamond diamond = new Diamond();
		Ellipse ellipse = new Ellipse();
		HollowSphere hollowSphere = new HollowSphere();
		RadiusSphere radiusSphere = new RadiusSphere();
		Sphere sphere = new Sphere();
		Brush.AddBrushShape("cube", cube);
		Brush.AddBrushShape("square", cube);
		Brush.AddBrushShape("diamond", diamond);
		Brush.AddBrushShape("d", diamond);
		Brush.AddBrushShape("ellipse", ellipse);
		Brush.AddBrushShape("e", ellipse);
		Brush.AddBrushShape("hsphere", hollowSphere);
		Brush.AddBrushShape("hs", hollowSphere);
		Brush.AddBrushShape("radiussphere", radiusSphere);
		Brush.AddBrushShape("rs", radiusSphere);
		Brush.AddBrushShape("sphere", sphere);
		Brush.AddBrushShape("s", sphere);
	}
}
