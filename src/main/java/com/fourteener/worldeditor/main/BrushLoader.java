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
		RandomSphere randomSphere = new RandomSphere();
		Sphere sphere = new Sphere();
		Voxel voxel = new Voxel();
		Dome dome = new Dome();
		Brush.AddBrushShape("cube", cube);
		Brush.AddBrushShape("square", cube);
		Brush.AddBrushShape("diamond", diamond);
		Brush.AddBrushShape("d", diamond);
		Brush.AddBrushShape("ellipse", ellipse);
		Brush.AddBrushShape("e", ellipse);
		Brush.AddBrushShape("hsphere", hollowSphere);
		Brush.AddBrushShape("hs", hollowSphere);
		Brush.AddBrushShape("radiussphere", radiusSphere);
		Brush.AddBrushShape("randomsphere", randomSphere);
		Brush.AddBrushShape("rs", randomSphere);
		Brush.AddBrushShape("sphere", sphere);
		Brush.AddBrushShape("s", sphere);
		Brush.AddBrushShape("voxel", voxel);
		Brush.AddBrushShape("v", voxel);
		Brush.AddBrushShape("dome", dome);
	}
}
